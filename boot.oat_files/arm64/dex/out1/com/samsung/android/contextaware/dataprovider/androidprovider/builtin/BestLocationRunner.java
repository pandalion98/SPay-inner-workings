package com.samsung.android.contextaware.dataprovider.androidprovider.builtin;

import android.content.Context;
import android.location.Criteria;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.androidprovider.RawGpsProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class BestLocationRunner extends RawGpsProvider {
    private Criteria mCriteria;

    public BestLocationRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
    }

    protected final String getLocationProvider() {
        if (getGpsManager() == null) {
            CaLogger.error("getGpsManager() is null");
            return null;
        }
        String provider = getGpsManager().getBestProvider(this.mCriteria, true);
        CaLogger.info("BestProvider : " + provider);
        return provider;
    }

    protected final void initializeManager() {
        super.initializeManager();
        this.mCriteria = new Criteria();
        this.mCriteria.setAccuracy(1);
        this.mCriteria.setPowerRequirement(2);
        this.mCriteria.setAltitudeRequired(false);
        this.mCriteria.setBearingRequired(false);
        this.mCriteria.setSpeedRequired(false);
        this.mCriteria.setCostAllowed(true);
    }

    public final String[] getContextValueNames() {
        return new String[]{"Latitude", "Longitude", "Altitude"};
    }

    public final String getContextType() {
        return ContextType.ANDROID_RUNNER_BEST_LOCATION.getCode();
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
