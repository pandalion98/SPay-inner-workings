package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.nio.ByteBuffer;

public class CareGiverRunner extends LibTypeProvider {
    private static final int DEFAULT_CARE_GIVER_DURATION = 200;
    private static final int DEFAULT_CARE_GIVER_STRENGTH = 3;
    private int mDuration = 200;
    private int mStrength = 3;

    public CareGiverRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_CARE_GIVER.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_CARE_GIVER_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        packet = new byte[3];
        byte[] data = CaConvertUtil.intToByteArr(this.mDuration, 2);
        packet[1] = data[0];
        packet[2] = data[1];
        return packet;
    }

    public final String[] getContextValueNames() {
        return new String[]{"UserStatus"};
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        boolean result = true;
        if (property == 40) {
            int strength = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Strength = " + Integer.toString(strength));
            if (strength <= 0 || strength > 5) {
                CaLogger.warning("range error of care giver strength (range : 1~5)");
                return false;
            }
            this.mStrength = strength;
        } else if (property == 41) {
            int duration = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Duration = " + Integer.toString(duration));
            if (duration <= 0 || duration > 5000) {
                CaLogger.warning("range error of care giver duration (range : 1~5000)");
                return false;
            }
            this.mDuration = duration;
        } else {
            result = false;
        }
        return result;
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        String name = getContextValueNames()[0];
        if ((packet.length - tmpNext) - 2 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        byte[] stat = new byte[]{(byte) 0, (byte) 0};
        int tmpNext2 = tmpNext + 1;
        stat[0] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        stat[1] = packet[tmpNext2];
        super.getContextBean().putContext(name, ByteBuffer.wrap(stat).getShort());
        super.notifyObserver();
        return tmpNext;
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
