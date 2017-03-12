package com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.EnvironmentSensorProvider;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class BarometerSensorRunner extends EnvironmentSensorProvider {
    private int[] mBarometerData;
    private int mBarometerInitData;

    public BarometerSensorRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_RAW_BAROMETER_SENSOR.getCode();
    }

    protected final byte[] getDataPacketToRegisterLib() {
        packet = new byte[3];
        byte[] data = CaConvertUtil.intToByteArr(super.getInterval(), 2);
        packet[0] = (byte) 3;
        packet[1] = data[0];
        packet[2] = data[1];
        return packet;
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
        this.mBarometerInitData = 0;
        this.mBarometerData = null;
    }

    public final String[] getContextValueNames() {
        return new String[]{"Barometer"};
    }

    public final int parse(byte[] packet, int next) {
        byte[] tmpPacket = (byte[]) packet.clone();
        int tmpNext = next;
        if ((tmpPacket.length - tmpNext) - 1 < 0) {
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        int sensorCount = tmpPacket[tmpNext];
        if (sensorCount <= 0) {
            sensorCount *= -1;
            tmpNext = tmpNext2 + 1;
            tmpNext2 = tmpNext + 1;
            this.mBarometerInitData = (tmpPacket[tmpNext2] << 8) + (tmpPacket[tmpNext] & 255);
        }
        tmpNext = tmpNext2;
        this.mBarometerData = new int[sensorCount];
        int i = 0;
        tmpNext2 = tmpNext;
        while (i < sensorCount) {
            if ((tmpPacket.length - tmpNext2) - 1 < 0) {
                tmpNext = tmpNext2;
                return -1;
            }
            if (i <= 0) {
                tmpNext = tmpNext2 + 1;
                this.mBarometerData[i] = this.mBarometerInitData + tmpPacket[tmpNext2];
            } else {
                tmpNext = tmpNext2 + 1;
                this.mBarometerData[i] = this.mBarometerData[i - 1] + tmpPacket[tmpNext2];
            }
            this.mBarometerInitData = this.mBarometerData[i];
            i++;
            tmpNext2 = tmpNext;
        }
        super.getContextBean().putContext(getContextValueNames()[0], this.mBarometerData);
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

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }
}
