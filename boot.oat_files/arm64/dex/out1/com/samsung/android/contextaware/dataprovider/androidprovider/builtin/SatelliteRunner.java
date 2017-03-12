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

public class SatelliteRunner extends RawGpsProvider {
    public SatelliteRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
    }

    public final String getContextType() {
        return ContextType.ANDROID_RUNNER_RAW_SATELLITE.getCode();
    }

    public final String[] getContextValueNames() {
        return new String[]{"NumSat", "Prn", "Snr", "El", "Az", "Mask"};
    }

    protected final void registerGpsListener() {
        if (super.getGpsManager() != null && super.getLooper() != null) {
            super.getGpsManager().addGpsStatusListener(super.getGpsStatusListener());
        }
    }

    protected final void unregisterGpsListener() {
        if (super.getGpsManager() != null) {
            super.getGpsManager().removeGpsStatusListener(super.getGpsStatusListener());
        }
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final String getLocationProvider() {
        return null;
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
