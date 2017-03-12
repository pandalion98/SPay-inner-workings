package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.ContextBean;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.nio.ByteBuffer;

public class AutoBrightnessRunner extends LibTypeProvider {
    byte[] mConfigData = null;
    byte mDeviceMode = (byte) 0;
    byte[] mOffsetData = null;

    public AutoBrightnessRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_AUTO_BRIGHTNESS.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 48;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{this.mDeviceMode};
    }

    public final String[] getContextValueNames() {
        return new String[]{"Candela", "AmbientLux"};
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        boolean result = true;
        if (property == 64) {
            int mode = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Mode = " + Integer.toString(mode));
            if (mode == 0 || mode == 1 || mode == 2) {
                this.mDeviceMode = (byte) mode;
                sendPropertyValueToSensorHub((byte) 23, (byte) 48, (byte) 1, CaConvertUtil.intToByteArr(mode, 1));
            } else {
                CaLogger.warning("invalid value for mode");
                return false;
            }
        } else if (property == 65) {
            String str = (String) ((ContextAwarePropertyBundle) value).getValue();
            if (str == null || str.isEmpty()) {
                CaLogger.error("value is null");
                return false;
            }
            byte[] dataPacket = CaConvertUtil.stringToByteArray(str);
            if (dataPacket == null || dataPacket.length <= 0) {
                CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_PACKET_LENGTH_ZERO.getCode()));
                return false;
            }
            CaLogger.info("CFG = " + str);
            if (dataPacket[0] == (byte) -1) {
                this.mOffsetData = dataPacket;
                sendPropertyValueToSensorHub((byte) 23, (byte) 48, (byte) 2, dataPacket);
            } else {
                this.mConfigData = dataPacket;
            }
        } else {
            result = false;
        }
        return result;
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        String[] names = getContextValueNames();
        if ((packet.length - tmpNext) - 8 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        ContextBean contextBean = super.getContextBean();
        String str = names[0];
        r5 = new byte[4];
        int tmpNext2 = tmpNext + 1;
        r5[0] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[1] = packet[tmpNext2];
        tmpNext2 = tmpNext + 1;
        r5[2] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[3] = packet[tmpNext2];
        contextBean.putContext(str, ByteBuffer.wrap(r5).getInt());
        contextBean = super.getContextBean();
        str = names[1];
        r5 = new byte[4];
        tmpNext2 = tmpNext + 1;
        r5[0] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[1] = packet[tmpNext2];
        tmpNext2 = tmpNext + 1;
        r5[2] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r5[3] = packet[tmpNext2];
        contextBean.putContext(str, ByteBuffer.wrap(r5).getInt());
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
        if (this.mConfigData != null) {
            sendPropertyValueToSensorHub((byte) 23, (byte) 48, (byte) 2, this.mConfigData);
        }
        if (this.mOffsetData != null) {
            sendPropertyValueToSensorHub((byte) 23, (byte) 48, (byte) 2, this.mOffsetData);
        }
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
