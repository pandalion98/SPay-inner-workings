package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class SleepMonitorRunner extends LibTypeProvider {
    private static final int DEFAULT_SAMPLING_INTERVAL = 100;
    private static final int DEFAULT_SENSIBILITY = 80;
    private int mSamplingInterval = 100;
    private int mSensibility = 80;

    public SleepMonitorRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_SLEEP_MONITOR.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 37;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        packet = new byte[8];
        byte[] sensibility = CaConvertUtil.intToByteArr(this.mSensibility, 4);
        packet[0] = sensibility[0];
        packet[1] = sensibility[1];
        packet[2] = sensibility[2];
        packet[3] = sensibility[3];
        byte[] samplingInterval = CaConvertUtil.intToByteArr(this.mSamplingInterval, 4);
        packet[4] = samplingInterval[0];
        packet[5] = samplingInterval[1];
        packet[6] = samplingInterval[2];
        packet[7] = samplingInterval[3];
        return packet;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        boolean result = true;
        if (property == 42) {
            int sensibility = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            if (this.mSensibility <= 0) {
                CaLogger.warning("sensibility must be above 0.");
                return false;
            }
            this.mSensibility = sensibility;
            CaLogger.info("Sensibility = " + Integer.toString(this.mSensibility));
        } else if (property == 43) {
            int samplingInterval = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            if (samplingInterval <= 0) {
                CaLogger.warning("sampling interval must be above 0.");
                return false;
            }
            this.mSamplingInterval = samplingInterval;
            CaLogger.info("Sampling interval = " + Integer.toString(this.mSamplingInterval));
        } else {
            result = false;
        }
        return result;
    }

    public final String[] getContextValueNames() {
        return new String[]{"SleepStatus", "PIM", "ZCM", "Stage", "Wrist", "Flag"};
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 2 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        byte b = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        int dataSize = b + ((packet[tmpNext2] & 255) << 8);
        if (dataSize <= 0 || dataSize % 17 != 0) {
            CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
            return -1;
        }
        int arraySize = dataSize / 17;
        String[] names = getContextValueNames();
        int[] status = new int[arraySize];
        float[] pim = new float[arraySize];
        int[] zcm = new int[arraySize];
        int[] stage = new int[arraySize];
        int[] wrist = new int[arraySize];
        int[] flag = new int[arraySize];
        int i = 0;
        tmpNext2 = tmpNext;
        while (i < arraySize) {
            tmpNext = tmpNext2 + 1;
            status[i] = packet[tmpNext2];
            tmpNext2 = tmpNext + 1;
            tmpNext = tmpNext2 + 1;
            tmpNext2 = tmpNext + 1;
            tmpNext = tmpNext2 + 1;
            pim[i] = ((float) ((((packet[tmpNext] & 255) + ((packet[tmpNext2] & 255) << 8)) + ((packet[tmpNext] & 255) << 16)) + ((packet[tmpNext2] & 255) << 24))) / 10.0f;
            tmpNext2 = tmpNext + 1;
            tmpNext = tmpNext2 + 1;
            tmpNext2 = tmpNext + 1;
            tmpNext = tmpNext2 + 1;
            zcm[i] = (((packet[tmpNext] & 255) + ((packet[tmpNext2] & 255) << 8)) + ((packet[tmpNext] & 255) << 16)) + ((packet[tmpNext2] & 255) << 24);
            tmpNext2 = tmpNext + 1;
            tmpNext = tmpNext2 + 1;
            tmpNext2 = tmpNext + 1;
            tmpNext = tmpNext2 + 1;
            stage[i] = (((packet[tmpNext] & 255) + ((packet[tmpNext2] & 255) << 8)) + ((packet[tmpNext] & 255) << 16)) + ((packet[tmpNext2] & 255) << 24);
            tmpNext2 = tmpNext + 1;
            tmpNext = tmpNext2 + 1;
            wrist[i] = (packet[tmpNext] & 255) + ((packet[tmpNext2] & 255) << 8);
            tmpNext2 = tmpNext + 1;
            tmpNext = tmpNext2 + 1;
            flag[i] = (packet[tmpNext] & 255) + ((packet[tmpNext2] & 255) << 8);
            CaLogger.info("status[" + i + "] = " + status[i] + ", pim[" + i + "] = " + pim[i] + ", zcm[" + i + " ] = " + zcm[i] + ", stage[" + i + " ] = " + stage[i] + ", wrist[" + i + " ] = " + wrist[i] + ", flag[" + i + " ] = " + flag[i]);
            i++;
            tmpNext2 = tmpNext;
        }
        super.getContextBean().putContext(names[0], status);
        super.getContextBean().putContext(names[1], pim);
        super.getContextBean().putContext(names[2], zcm);
        super.getContextBean().putContext(names[3], stage);
        super.getContextBean().putContext(names[4], wrist);
        super.getContextBean().putContext(names[5], flag);
        super.notifyObserver();
        tmpNext = tmpNext2;
        return tmpNext2;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }
}
