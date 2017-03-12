package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.contextaware.manager.ICurrrentLocationObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.smartclip.SmartClipMetaTagType;

public class CaCurrentLocationManager implements ICurrentLocationObservable, IUtilManager {
    private static final float GPS_MIN_DISTANCE = 0.0f;
    private static final long GPS_MIN_TIME = 1000;
    private Criteria mCriteria;
    private boolean mEnable = false;
    private LocationManager mGpsManager;
    private ICurrrentLocationObserver mListener;
    private final LocationListener mLocationListener = new LocationListener() {
        public final void onProviderEnabled(String provider) {
            CaLogger.info("Location service is enabled");
        }

        public final void onLocationChanged(Location location) {
            if (location.getAccuracy() <= 0.0f) {
                CaLogger.warning("Accuracy is low");
                return;
            }
            CaCurrentLocationManager.this.notifyCurrentLocationObserver(System.currentTimeMillis(), location.getTime(), location.getLatitude(), location.getLongitude(), location.getAltitude());
        }

        public final void onProviderDisabled(String provider) {
            CaLogger.info("Location service is disabled");
        }

        public final void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case 0:
                    CaLogger.info("out of service");
                    return;
                case 1:
                    CaLogger.info("temporarily unavailable");
                    return;
                case 2:
                    CaLogger.info("available");
                    return;
                default:
                    return;
            }
        }
    };
    private final Looper mLooper;

    public CaCurrentLocationManager(Context context, Looper looper, ICurrrentLocationObserver observer) {
        this.mLooper = looper;
        initializeManager(context);
        registerCurrentLocationObserver(observer);
    }

    public final void initializeManager(Context context) {
        this.mCriteria = new Criteria();
        this.mCriteria.setAccuracy(1);
        this.mCriteria.setPowerRequirement(2);
        this.mCriteria.setAltitudeRequired(true);
        this.mCriteria.setBearingRequired(false);
        this.mCriteria.setSpeedRequired(false);
        this.mCriteria.setCostAllowed(true);
        this.mGpsManager = (LocationManager) context.getSystemService(SmartClipMetaTagType.LOCATION);
        if (this.mGpsManager == null) {
            CaLogger.error("cannot create the GpsManager object");
        }
        this.mEnable = false;
    }

    public final void terminateManager() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            this.mGpsManager.removeUpdates(this.mLocationListener);
        }
    }

    public final void enable() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mLooper == null) {
            CaLogger.error("Looper is null");
        } else {
            final String provider = this.mGpsManager.getBestProvider(this.mCriteria, true);
            if (provider == null || provider.isEmpty()) {
                CaLogger.error("cannot register the gps listener");
                return;
            }
            if (this.mEnable) {
                CaLogger.warning("mEnable value is true.");
                disable();
            }
            this.mEnable = true;
            CaLogger.info("BestProvider : " + provider);
            new Handler(this.mLooper).postDelayed(new Runnable() {
                public void run() {
                    CaCurrentLocationManager.this.mGpsManager.requestLocationUpdates(provider, 1000, 0.0f, CaCurrentLocationManager.this.mLocationListener, CaCurrentLocationManager.this.mLooper);
                }
            }, 0);
        }
    }

    public void disable() {
        if (this.mGpsManager == null) {
            CaLogger.error("cannot unregister the gps listener");
        } else if (this.mEnable) {
            CaLogger.trace();
            new Handler(this.mLooper).postDelayed(new Runnable() {
                public void run() {
                    CaCurrentLocationManager.this.mGpsManager.removeUpdates(CaCurrentLocationManager.this.mLocationListener);
                }
            }, 0);
            this.mEnable = false;
        }
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public final void registerCurrentLocationObserver(ICurrrentLocationObserver observer) {
        this.mListener = observer;
    }

    public final void unregisterCurrentLocationObserver() {
        this.mListener = null;
    }

    public final void notifyCurrentLocationObserver(long sysTime, long timeStamp, double latitude, double longitude, double altitude) {
        if (this.mListener != null) {
            this.mListener.updateCurrentLocation(sysTime, timeStamp, latitude, longitude, altitude);
        }
        disable();
    }
}
