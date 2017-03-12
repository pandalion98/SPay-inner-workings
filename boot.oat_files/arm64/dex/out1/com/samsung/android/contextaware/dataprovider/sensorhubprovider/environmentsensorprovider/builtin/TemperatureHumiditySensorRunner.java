package com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.environmentsensorprovider.EnvironmentSensorProvider;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class TemperatureHumiditySensorRunner extends EnvironmentSensorProvider {
    private double[] mHumidityData;
    private double[] mTemperatureData;

    public TemperatureHumiditySensorRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_RAW_TEMPERATURE_HUMIDITY_SENSOR.getCode();
    }

    protected final byte[] getDataPacketToRegisterLib() {
        packet = new byte[3];
        byte[] data = CaConvertUtil.intToByteArr(super.getInterval(), 2);
        packet[0] = (byte) 6;
        packet[1] = data[0];
        packet[2] = data[1];
        return packet;
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
        this.mTemperatureData = null;
        this.mHumidityData = null;
    }

    public final String[] getContextValueNames() {
        return new String[]{"LoggingStatus", "Temperature", "Humidity"};
    }

    public final int parse(byte[] packet, int next) {
        byte[] tmpPacket = (byte[]) packet.clone();
        int tmpNext = next;
        if ((tmpPacket.length - tmpNext) - 2 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        tmpNext = tmpNext2 + 1;
        int sensorCount = (tmpPacket[tmpNext] << 8) + (tmpPacket[tmpNext2] & 255);
        if (sensorCount <= 0) {
            CaLogger.error(SensorHubErrors.ERROR_ENVIRONMENT_SENSOR_COUNT.getMessage());
            return -1;
        }
        this.mTemperatureData = new double[sensorCount];
        this.mHumidityData = new double[sensorCount];
        tmpNext2 = tmpNext;
        for (int i = 0; i < sensorCount; i++) {
            if ((tmpPacket.length - tmpNext2) - 2 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                tmpNext = tmpNext2;
                return -1;
            }
            tmpNext = tmpNext2 + 1;
            tmpNext2 = tmpNext + 1;
            this.mTemperatureData[i] = ((double) ((tmpPacket[tmpNext2] << 8) + (tmpPacket[tmpNext] & 255))) / 100.0d;
            tmpNext = tmpNext2 + 1;
            tmpNext2 = tmpNext + 1;
            this.mHumidityData[i] = ((double) ((tmpPacket[tmpNext2] << 8) + (tmpPacket[tmpNext] & 255))) / 100.0d;
        }
        String[] names = getContextValueNames();
        super.getContextBean().putContext(names[0], super.getLoggingStatus());
        super.getContextBean().putContext(names[1], this.mTemperatureData);
        super.getContextBean().putContext(names[2], this.mHumidityData);
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
