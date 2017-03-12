package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.contextaware.manager.ICurrrentPositionObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.smartclip.SmartClipMetaTagType;

public class CaGpsPositionManager implements ICurrentPositionObservable, IUtilManager {
    private Handler mDisabler = null;
    private boolean mEnable = false;
    private LocationManager mGpsManager;
    private long mGpsTime = 1000;
    private final LocationListener mLocationListener = new LocationListener() {
        public final void onProviderEnabled(String provider) {
            CaLogger.info("Location service is enabled");
        }

        public final void onLocationChanged(Location location) {
            CaGpsPositionManager.this.notifyListener(new Location(location));
            CaGpsPositionManager.this.preLatitude = location.getLatitude();
            CaGpsPositionManager.this.preLongitude = location.getLongitude();
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
    private ICurrrentPositionObserver mObserver;
    private double preLatitude = 0.0d;
    private double preLongitude = 0.0d;

    public CaGpsPositionManager(Context context, Looper looper, ICurrrentPositionObserver observer) {
        this.mLooper = looper;
        initializeManager(context);
        registerCurrentPositionObserver(observer);
    }

    public final void initializeManager(Context context) {
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
            new Handler(this.mLooper).postDelayed(new Runnable() {
                public void run() {
                    CaGpsPositionManager.this.mGpsManager.removeUpdates(CaGpsPositionManager.this.mLocationListener);
                }
            }, 0);
        }
    }

    private void notifyListener(Location loc) {
        if (this.mObserver != null) {
            int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
            Bundle bun = loc.getExtras();
            int numSat = 0;
            if (bun != null) {
                numSat = bun.getInt("satellites");
            }
            int[] iArr = utcTime;
            this.mObserver.updateCurrentPosition(1, iArr, loc.getLatitude(), loc.getLongitude(), loc.getAltitude(), PositionContextBean.calculationDistance(this.preLatitude, this.preLongitude, loc.getLatitude(), loc.getLongitude()), loc.getSpeed(), loc.getAccuracy(), numSat);
        }
    }

    public final void enable() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mLooper == null) {
            CaLogger.error("Looper is null");
        } else {
            if (this.mEnable) {
                CaLogger.warning("mEnable value is true.");
                disable();
            }
            this.mEnable = true;
            if (this.mDisabler != null) {
                this.mDisabler.removeCallbacksAndMessages(null);
            }
            new Handler(this.mLooper).postDelayed(new Runnable() {
                public void run() {
                    CaGpsPositionManager.this.mGpsManager.requestLocationUpdates("gps", 1000, 0.0f, CaGpsPositionManager.this.mLocationListener, CaGpsPositionManager.this.mLooper);
                }
            }, 0);
            this.mDisabler = new Handler(this.mLooper);
            this.mDisabler.postDelayed(new Runnable() {
                public void run() {
                    CaGpsPositionManager.this.disable();
                }
            }, this.mGpsTime);
        }
    }

    public void disable() {
        if (this.mGpsManager == null) {
            CaLogger.error("cannot unregister the gps listener");
        } else if (this.mEnable) {
            CaLogger.trace();
            new Handler(this.mLooper).postDelayed(new Runnable() {
                public void run() {
                    CaGpsPositionManager.this.mGpsManager.removeUpdates(CaGpsPositionManager.this.mLocationListener);
                }
            }, 0);
            this.mEnable = false;
            if (this.mDisabler != null) {
                this.mDisabler.removeCallbacksAndMessages(null);
            }
        }
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public void setGpsUpdateTime(long time) {
        this.mGpsTime = time;
    }

    public void registerCurrentPositionObserver(ICurrrentPositionObserver observer) {
        this.mObserver = observer;
    }

    public void unregisterCurrentPositionObserver() {
        this.mObserver = null;
    }

    public void notifyCurrentPositionObserver() {
    }
}
