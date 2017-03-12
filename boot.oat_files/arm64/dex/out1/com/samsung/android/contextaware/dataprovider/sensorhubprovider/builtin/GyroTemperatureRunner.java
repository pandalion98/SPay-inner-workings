package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class GyroTemperatureRunner extends LibTypeProvider {
    public GyroTemperatureRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_GYRO_TEMPERATURE.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 15;
    }

    public final String[] getContextValueNames() {
        return new String[]{"GyroTemperature"};
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        super.getContextBean().putContext(getContextValueNames()[0], (double) packet[tmpNext]);
        super.notifyObserver();
        tmpNext = tmpNext2;
        return tmpNext2;
    }

    protected final Bundle getInitContextBundle() {
        String name = getContextValueNames()[0];
        Bundle contextBundle = new Bundle();
        contextBundle.putDouble(name, 0.0d);
        return contextBundle;
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
