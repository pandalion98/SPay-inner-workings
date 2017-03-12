package com.samsung.android.contextaware.utilbundle;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.contextaware.manager.ICurrrentPositionObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.smartclip.SmartClipMetaTagType;
import com.samsung.location.SLocationManager;
import java.util.Iterator;

public class CaCurrentPositionManager implements ICurrentPositionObservable, IUtilManager, ITimeOutCheckObserver {
    private static final float ACCURACY_BEST_THRESHOLE = 16.0f;
    private static final float ACCURACY_GOOD_THRESHOLE = 50.0f;
    private static final float GPS_MIN_DISTANCE = 0.0f;
    private static final long GPS_MIN_TIME = 1000;
    private static final int LOCATION_MODE_LOCATIONMANAGER = 1;
    private static final int LOCATION_MODE_SLOCATION = 2;
    private static final int LOCFROMLOCATIONMANAGER = 1;
    private static final int LOCFROMSLOCATION = 2;
    public static Context mContext;
    private int ACCURACY_CurrentLoc = 150;
    private final String CURLOC = "com.samsung.android.contextaware.SLOCATION";
    private int LocRequestSource = 0;
    private IntentFilter filter;
    private BroadcastReceiver mBrReceiver = null;
    private boolean mEnable = false;
    private PositionContextBean mGpsInfo;
    private final LocationListener mGpsListener = new LocationListener() {
        public final void onProviderEnabled(String provider) {
            CaLogger.info(provider + " is enabled");
        }

        public final void onLocationChanged(Location location) {
            int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            float speed = location.getSpeed();
            float accuracy = location.getAccuracy();
            double distance = PositionContextBean.calculationDistance(CaCurrentPositionManager.this.mPrePosition.getLatitude(), CaCurrentPositionManager.this.mPrePosition.getLongitude(), latitude, longitude);
            Iterator<GpsSatellite> gpsSatellites = CaCurrentPositionManager.this.getGpsSatellites();
            int satelliteCount = 0;
            if (gpsSatellites != null) {
                while (gpsSatellites.hasNext()) {
                    GpsSatellite satellite = (GpsSatellite) gpsSatellites.next();
                    if (satellite != null && satellite.usedInFix()) {
                        satelliteCount++;
                    }
                }
            }
            if (CaCurrentPositionManager.this.mGpsInfo.getAccuracy() >= accuracy) {
                CaCurrentPositionManager.this.mGpsInfo.setPosition(1, utcTime, latitude, longitude, altitude, distance, speed, accuracy, satelliteCount);
            }
            if (accuracy <= CaCurrentPositionManager.ACCURACY_BEST_THRESHOLE) {
                CaCurrentPositionManager.this.notifyCurrentPositionObserver();
            }
        }

        public final void onProviderDisabled(String provider) {
            CaLogger.info(provider + " is disabled");
        }

        public final void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private LocationManager mGpsManager;
    private ICurrrentPositionObserver mListener;
    private final Looper mLooper;
    private PositionContextBean mPrePosition;
    private CaTimeOutCheckManager mTimeOutCheck;
    private Thread mTimeOutCheckThreadHandler;
    private PositionContextBean mWpsInfo;
    private final LocationListener mWpsListener = new LocationListener() {
        public final void onProviderEnabled(String provider) {
            CaLogger.info(provider + " is enabled");
        }

        public final void onLocationChanged(Location location) {
            if (location.getAccuracy() <= 0.0f) {
                CaLogger.warning("Accuracy is low");
                return;
            }
            int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            CaCurrentPositionManager.this.mWpsInfo.setPosition(2, utcTime, latitude, longitude, location.getAltitude(), PositionContextBean.calculationDistance(CaCurrentPositionManager.this.mPrePosition.getLatitude(), CaCurrentPositionManager.this.mPrePosition.getLongitude(), latitude, longitude), 0.0f, 0.0f, 0);
        }

        public final void onProviderDisabled(String provider) {
            CaLogger.info(provider + " is disabled");
        }

        public final void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private SLocationManager sLm;

    static /* synthetic */ int access$676(CaCurrentPositionManager x0, int x1) {
        int i = x0.LocRequestSource | x1;
        x0.LocRequestSource = i;
        return i;
    }

    public CaCurrentPositionManager(Context context, Looper looper, ICurrrentPositionObserver observer) {
        this.mLooper = looper;
        initializeManager(context);
        registerCurrentPositionObserver(observer);
        mContext = context;
    }

    public final void initializeManager(Context context) {
        this.mGpsManager = (LocationManager) context.getSystemService(SmartClipMetaTagType.LOCATION);
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        }
        this.mGpsInfo = new PositionContextBean(10000.0f);
        this.mWpsInfo = new PositionContextBean(10000.0f);
        this.mPrePosition = new PositionContextBean();
        this.mEnable = false;
        this.sLm = (SLocationManager) context.getSystemService("sec_location");
        this.filter = new IntentFilter();
        this.filter.addAction("com.samsung.android.contextaware.SLOCATION");
        this.mBrReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("com.samsung.android.contextaware.SLOCATION")) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null && bundle.containsKey("currentlocation")) {
                        Location loc = (Location) bundle.get("currentlocation");
                        if (loc != null) {
                            CaLogger.debug("RSL is OK.");
                            CaCurrentPositionManager.this.CurrentLocUpdate(new Location(loc));
                            return;
                        }
                        CaLogger.debug("Loc is null");
                    }
                }
            }
        };
    }

    protected void CurrentLocUpdate(Location location) {
        if (this.mEnable) {
            CaLogger.debug("CurrentLocUpdate : provider " + location.getProvider());
            int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            float speed = location.getSpeed();
            float accuracy = location.getAccuracy();
            this.mWpsInfo.setPosition(4, utcTime, latitude, longitude, altitude, PositionContextBean.calculationDistance(this.mPrePosition.getLatitude(), this.mPrePosition.getLongitude(), latitude, longitude), speed, accuracy, 0);
            CaLogger.debug("CurrentLUpda : SLO update! ");
            notifyCurrentPositionObserver();
        }
    }

    public final void terminateManager() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            this.mGpsManager.removeUpdates(this.mGpsListener);
        }
    }

    public final void enable(int duration) {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mLooper == null) {
            CaLogger.error("Looper is null");
        } else {
            if (this.mEnable) {
                CaLogger.warning("mEnable value is true.");
                disable();
            }
            CaLogger.trace();
            this.mEnable = true;
            this.mGpsInfo = new PositionContextBean(10000.0f);
            this.mWpsInfo = new PositionContextBean(10000.0f);
            this.mPrePosition = new PositionContextBean();
            clearTimeOutCheckService();
            this.mTimeOutCheck = new CaTimeOutCheckManager(this, duration);
            this.mTimeOutCheckThreadHandler = new Thread(this.mTimeOutCheck);
            this.mTimeOutCheckThreadHandler.start();
            new Handler(this.mLooper).postDelayed(new Runnable() {
                public void run() {
                    CaCurrentPositionManager.this.registerGpsListener();
                    CaCurrentPositionManager.this.registerWpsListener();
                    CaCurrentPositionManager.access$676(CaCurrentPositionManager.this, 1);
                }
            }, 0);
        }
    }

    public final void enable(int mode, int param) {
        switch (mode) {
            case 1:
                enable(10);
                return;
            case 2:
                this.ACCURACY_CurrentLoc = param;
                if (this.mLooper == null) {
                    CaLogger.error("Looper is null");
                    return;
                }
                if (this.mEnable) {
                    CaLogger.warning("mEnable value is true.");
                    disable();
                }
                CaLogger.trace();
                this.mEnable = true;
                this.mGpsInfo = new PositionContextBean(10000.0f);
                this.mWpsInfo = new PositionContextBean(10000.0f);
                this.mPrePosition = new PositionContextBean();
                clearTimeOutCheckService();
                this.mTimeOutCheck = new CaTimeOutCheckManager(this, 11);
                this.mTimeOutCheckThreadHandler = new Thread(this.mTimeOutCheck);
                this.mTimeOutCheckThreadHandler.start();
                new Handler(this.mLooper).postDelayed(new Runnable() {
                    public void run() {
                        CaCurrentPositionManager.this.requestCurrentLoc(10);
                    }
                }, 0);
                return;
            default:
                return;
        }
    }

    protected void requestCurrentLoc(int duration) {
        boolean flag_CurrLocOK = false;
        if (this.sLm != null) {
            int result = this.sLm.requestSingleLocation(this.ACCURACY_CurrentLoc, duration, PendingIntent.getBroadcast(mContext, 0, new Intent("com.samsung.android.contextaware.SLOCATION"), 0));
            CaLogger.debug("result of SLM req : " + result);
            if (result > -1) {
                flag_CurrLocOK = true;
                this.LocRequestSource |= 2;
                mContext.registerReceiver(this.mBrReceiver, this.filter);
                CaLogger.debug("Request CurL");
            }
        } else {
            CaLogger.error("requestSingleL err - sLm is null ");
        }
        if (!flag_CurrLocOK) {
            registerGpsListener();
            registerWpsListener();
            this.LocRequestSource |= 1;
        }
    }

    protected void removeCurrentLoc() {
        if (this.sLm != null) {
            this.sLm.removeSingleLocation(PendingIntent.getBroadcast(mContext, 0, new Intent("com.samsung.android.contextaware.SLOCATION"), 0));
            CaLogger.debug("Remove CurL");
        }
    }

    public final void disable() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mEnable) {
            CaLogger.trace();
            clearTimeOutCheckService();
            new Handler(this.mLooper).postDelayed(new Runnable() {
                public void run() {
                    if ((CaCurrentPositionManager.this.LocRequestSource & 1) > 0) {
                        CaCurrentPositionManager.this.unregisterGpsListener();
                        CaCurrentPositionManager.this.unregisterWpsListener();
                    }
                    if ((CaCurrentPositionManager.this.LocRequestSource & 2) > 0) {
                        CaCurrentPositionManager.this.removeCurrentLoc();
                        if (CaCurrentPositionManager.this.mBrReceiver != null) {
                            CaCurrentPositionManager.mContext.unregisterReceiver(CaCurrentPositionManager.this.mBrReceiver);
                        }
                    }
                    CaCurrentPositionManager.this.LocRequestSource = 0;
                }
            }, 0);
            this.mEnable = false;
        }
    }

    private Iterator<GpsSatellite> getGpsSatellites() {
        if (this.mGpsManager == null) {
            return null;
        }
        return this.mGpsManager.getGpsStatus(null).getSatellites().iterator();
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public final void registerCurrentPositionObserver(ICurrrentPositionObserver observer) {
        this.mListener = observer;
    }

    public final void unregisterCurrentPositionObserver() {
        this.mListener = null;
    }

    public final void notifyCurrentPositionObserver() {
        if (this.mListener != null) {
            disable();
            if (this.mGpsInfo.getType() != 0) {
                if (this.mGpsInfo.getAccuracy() <= ACCURACY_GOOD_THRESHOLE) {
                    updateGpsPosition();
                } else if (this.mWpsInfo.getType() != 0) {
                    updateWpsPosition();
                } else {
                    updateEmptyPosition();
                }
            } else if (this.mWpsInfo.getType() != 0) {
                updateWpsPosition();
            } else {
                updateEmptyPosition();
            }
        }
    }

    private void updateGpsPosition() {
        if (this.mListener != null) {
            this.mPrePosition = this.mGpsInfo;
            this.mListener.updateCurrentPosition(this.mGpsInfo.getType(), this.mGpsInfo.getUtcTime(), this.mGpsInfo.getLatitude(), this.mGpsInfo.getLongitude(), this.mGpsInfo.getAltitude(), this.mGpsInfo.getDistance(), this.mGpsInfo.getSpeed(), this.mGpsInfo.getAccuracy(), this.mGpsInfo.getSatelliteCount());
        }
    }

    private void updateWpsPosition() {
        if (this.mListener != null) {
            this.mPrePosition = this.mWpsInfo;
            this.mListener.updateCurrentPosition(this.mWpsInfo.getType(), this.mWpsInfo.getUtcTime(), this.mWpsInfo.getLatitude(), this.mWpsInfo.getLongitude(), this.mWpsInfo.getAltitude(), this.mWpsInfo.getDistance(), this.mWpsInfo.getSpeed(), this.mWpsInfo.getAccuracy(), this.mWpsInfo.getSatelliteCount());
        }
    }

    private void updateEmptyPosition() {
        if (this.mListener != null) {
            PositionContextBean emptyPosition = new PositionContextBean();
            this.mListener.updateCurrentPosition(emptyPosition.getType(), emptyPosition.getUtcTime(), emptyPosition.getLatitude(), emptyPosition.getLongitude(), emptyPosition.getAltitude(), emptyPosition.getDistance(), emptyPosition.getSpeed(), emptyPosition.getAccuracy(), emptyPosition.getSatelliteCount());
        }
    }

    private void clearTimeOutCheckService() {
        if (this.mTimeOutCheckThreadHandler != null) {
            this.mTimeOutCheckThreadHandler.interrupt();
            this.mTimeOutCheck = null;
            this.mTimeOutCheckThreadHandler = null;
        }
    }

    private void registerGpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            this.mGpsManager.requestLocationUpdates("gps", 1000, 0.0f, this.mGpsListener, this.mLooper);
        }
    }

    private void registerWpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mGpsManager.isProviderEnabled("network")) {
            this.mGpsManager.requestLocationUpdates("network", 1000, 0.0f, this.mWpsListener, this.mLooper);
        }
    }

    private void unregisterGpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            this.mGpsManager.removeUpdates(this.mGpsListener);
        }
    }

    private void unregisterWpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            this.mGpsManager.removeUpdates(this.mWpsListener);
        }
    }

    public final void occurTimeOut() {
        notifyCurrentPositionObserver();
    }
}
