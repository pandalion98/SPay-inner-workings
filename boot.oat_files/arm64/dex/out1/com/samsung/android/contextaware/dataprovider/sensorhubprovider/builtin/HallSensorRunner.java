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

public class HallSensorRunner extends LibTypeProvider {
    public HallSensorRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_HALL_SENSOR.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 50;
    }

    public final String[] getContextValueNames() {
        return new String[]{"Angle", "Type", "Intensity"};
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 2 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        String[] names = getContextValueNames();
        int tmpNext2 = tmpNext + 1;
        tmpNext = tmpNext2 + 1;
        short angle = (short) ((packet[tmpNext] & 255) + ((packet[tmpNext2] & 255) << 8));
        tmpNext2 = tmpNext + 1;
        tmpNext = tmpNext2 + 1;
        short type = (short) ((packet[tmpNext] & 255) + ((packet[tmpNext2] & 255) << 8));
        tmpNext2 = tmpNext + 1;
        tmpNext = tmpNext2 + 1;
        short intensity = (short) ((packet[tmpNext] & 255) + ((packet[tmpNext2] & 255) << 8));
        super.getContextBean().putContext(names[0], angle);
        super.getContextBean().putContext(names[1], type);
        super.getContextBean().putContext(names[2], intensity);
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
