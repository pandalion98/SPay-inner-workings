package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.VoiceLibProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class WakeUpVoiceRunner extends VoiceLibProvider {
    private static final int DEFAULT_WAKE_UP_VOICE_MODE = 1;
    private int mMode = 1;

    public WakeUpVoiceRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_WAKE_UP_VOICE.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 1;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        byte[] packet = new byte[2];
        packet[0] = (byte) this.mMode;
        return packet;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        boolean result = true;
        String str;
        byte[] dataPacket;
        if (property == 16) {
            str = (String) ((ContextAwarePropertyBundle) value).getValue();
            if (str == null || str.isEmpty()) {
                CaLogger.error("value is null");
                return false;
            }
            dataPacket = CaConvertUtil.stringToByteArray(str);
            if (dataPacket == null || dataPacket.length <= 0) {
                CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PACKET_LENGTH_ZERO.getCode()));
                return false;
            }
            CaLogger.info("AM = " + str);
            result = sendPropertyValueToSensorHub((byte) 1, (byte) 1, dataPacket);
        } else if (property == 18) {
            str = (String) ((ContextAwarePropertyBundle) value).getValue();
            if (str == null || str.isEmpty()) {
                CaLogger.error("value is null");
                return false;
            }
            dataPacket = CaConvertUtil.stringToByteArray(str);
            if (dataPacket == null || dataPacket.length <= 0) {
                CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PACKET_LENGTH_ZERO.getCode()));
                return false;
            }
            CaLogger.info("GRAMMER = " + str);
            result = sendPropertyValueToSensorHub((byte) 1, (byte) 2, dataPacket);
        } else if (property == 53) {
            int mode = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Mode = " + Integer.toString(this.mMode));
            this.mMode = mode;
        } else {
            result = false;
        }
        return result;
    }

    public final String[] getContextValueNames() {
        return new String[]{"Action"};
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        String[] names = getContextValueNames();
        int tmpNext2 = tmpNext + 1;
        super.getContextBean().putContext(names[0], packet[tmpNext]);
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
