package com.samsung.android.contextaware.dataprovider.androidprovider;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus.Listener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.smartclip.SmartClipMetaTagType;
import java.util.Iterator;

public abstract class RawGpsProvider extends AndroidProvider {
    private static final float GPS_MIN_DISTANCE = 0.0f;
    private static final long GPS_MIN_TIME = 1000;
    private LocationManager mGpsManager;
    private final Listener mGpsStatusListener = new Listener() {
        private final float[] mmAz = new float[32];
        private final float[] mmEl = new float[32];
        private final int[] mmMask = new int[3];
        private final int[] mmPrn = new int[32];
        private final float[] mmSnr = new float[32];

        public final void onGpsStatusChanged(int event) {
            if (event == 4) {
                Iterator<GpsSatellite> gpsSatellites = RawGpsProvider.this.getGpsSatellites();
                if (gpsSatellites == null) {
                    CaLogger.error("gpsSatellites is null");
                    return;
                }
                int nNumSat = 0;
                int nNumSatUsedInFix = 0;
                while (gpsSatellites.hasNext()) {
                    GpsSatellite satellite = (GpsSatellite) gpsSatellites.next();
                    this.mmPrn[nNumSat] = satellite.getPrn();
                    this.mmSnr[nNumSat] = satellite.getSnr();
                    this.mmEl[nNumSat] = satellite.getElevation();
                    this.mmAz[nNumSat] = satellite.getAzimuth();
                    if (satellite.usedInFix()) {
                        nNumSatUsedInFix++;
                    }
                    nNumSat++;
                }
                this.mmMask[2] = nNumSatUsedInFix;
                String[] names = RawGpsProvider.this.getContextValueNames();
                RawGpsProvider.this.getContextBean().putContext(names[0], nNumSat);
                RawGpsProvider.this.getContextBean().putContext(names[1], this.mmPrn);
                RawGpsProvider.this.getContextBean().putContext(names[2], this.mmSnr);
                RawGpsProvider.this.getContextBean().putContext(names[3], this.mmEl);
                RawGpsProvider.this.getContextBean().putContext(names[4], this.mmAz);
                RawGpsProvider.this.getContextBean().putContext(names[5], this.mmMask);
                RawGpsProvider.this.notifyObserver();
            }
        }
    };
    private final LocationListener mLocationListener = new LocationListener() {
        public final void onProviderEnabled(String provider) {
            if (RawGpsProvider.this.getContextType() != null && !RawGpsProvider.this.getContextType().isEmpty()) {
                CaLogger.info("[" + RawGpsProvider.this.getContextType() + "] : " + "Location service is enabled");
            }
        }

        public final void onLocationChanged(Location location) {
            if (location.getAccuracy() > 0.0f) {
                String[] names = RawGpsProvider.this.getContextValueNames();
                RawGpsProvider.this.getContextBean().putContext("SystemTime", System.currentTimeMillis());
                RawGpsProvider.this.getContextBean().putContext("TimeStamp", location.getTime());
                RawGpsProvider.this.getContextBean().putContext(names[0], location.getLatitude());
                RawGpsProvider.this.getContextBean().putContext(names[1], location.getLongitude());
                RawGpsProvider.this.getContextBean().putContext(names[2], location.getAltitude());
                if (names.length > 3) {
                    RawGpsProvider.this.getContextBean().putContext(names[3], location.getBearing());
                    RawGpsProvider.this.getContextBean().putContext(names[4], (float) (((double) location.getSpeed()) * 3.6d));
                    RawGpsProvider.this.getContextBean().putContext(names[5], location.getAccuracy());
                    Iterator<GpsSatellite> gpsSatellites = RawGpsProvider.this.getGpsSatellites();
                    int iSvCount = 0;
                    if (gpsSatellites != null) {
                        while (gpsSatellites.hasNext()) {
                            GpsSatellite satellite = (GpsSatellite) gpsSatellites.next();
                            if (satellite != null && satellite.usedInFix()) {
                                iSvCount++;
                            }
                        }
                    }
                    RawGpsProvider.this.getContextBean().putContext("Valid", 1);
                    RawGpsProvider.this.getContextBean().putContext("SVCount", iSvCount);
                }
                RawGpsProvider.this.notifyObserver();
            }
        }

        public final void onProviderDisabled(String provider) {
            if (RawGpsProvider.this.getContextType() != null && !RawGpsProvider.this.getContextType().isEmpty()) {
                CaLogger.info("[" + RawGpsProvider.this.getContextType() + "] : " + "Location service is disabled");
            }
        }

        public final void onStatusChanged(String provider, int status, Bundle extras) {
            if (RawGpsProvider.this.getContextType() == null || RawGpsProvider.this.getContextType().isEmpty()) {
                CaLogger.error("getContextType() is null");
                return;
            }
            switch (status) {
                case 0:
                    CaLogger.info("[" + RawGpsProvider.this.getContextType() + "] : " + "out of service");
                    return;
                case 1:
                    CaLogger.info("[" + RawGpsProvider.this.getContextType() + "] : " + "temporarily unavailable");
                    return;
                case 2:
                    CaLogger.info("[" + RawGpsProvider.this.getContextType() + "] : " + "available");
                    return;
                default:
                    return;
            }
        }
    };

    protected abstract String getLocationProvider();

    protected RawGpsProvider(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
    }

    public void enable() {
        new Handler(super.getLooper()).postDelayed(new Runnable() {
            public void run() {
                RawGpsProvider.this.registerGpsListener();
            }
        }, 0);
    }

    public void disable() {
        new Handler(super.getLooper()).postDelayed(new Runnable() {
            public void run() {
                RawGpsProvider.this.unregisterGpsListener();
            }
        }, 0);
    }

    protected void registerGpsListener() {
        String provider = getLocationProvider();
        if (this.mGpsManager == null || super.getLooper() == null || provider == null || provider.isEmpty()) {
            CaLogger.error("cannot register the gps listener");
        } else {
            this.mGpsManager.requestLocationUpdates(provider, 1000, 0.0f, this.mLocationListener, super.getLooper());
        }
    }

    protected void unregisterGpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("cannot unregister the gps listener");
        } else {
            this.mGpsManager.removeUpdates(this.mLocationListener);
        }
    }

    protected void initializeManager() {
        if (super.getContext() == null) {
            CaLogger.error("mContext is null");
            return;
        }
        this.mGpsManager = (LocationManager) super.getContext().getSystemService(SmartClipMetaTagType.LOCATION);
        if (this.mGpsManager == null) {
            CaLogger.error("cannot create the GpsManager object");
        }
    }

    protected final void terminateManager() {
    }

    private Iterator<GpsSatellite> getGpsSatellites() {
        if (this.mGpsManager == null) {
            return null;
        }
        return this.mGpsManager.getGpsStatus(null).getSatellites().iterator();
    }

    protected final LocationManager getGpsManager() {
        return this.mGpsManager;
    }

    protected final Listener getGpsStatusListener() {
        return this.mGpsStatusListener;
    }
}
