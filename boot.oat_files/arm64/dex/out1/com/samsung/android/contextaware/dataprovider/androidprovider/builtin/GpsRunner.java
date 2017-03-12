package com.samsung.android.contextaware.dataprovider.androidprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.androidprovider.RawGpsProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class GpsRunner extends RawGpsProvider {
    public GpsRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
    }

    protected final String getLocationProvider() {
        return "gps";
    }

    public final String[] getContextValueNames() {
        return new String[]{"Latitude", "Longitude", "Altitude", "Bearing", "Speed", "Accuracy", "Valid", "SVCount"};
    }

    public final String getContextType() {
        return ContextType.ANDROID_RUNNER_RAW_GPS.getCode();
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
