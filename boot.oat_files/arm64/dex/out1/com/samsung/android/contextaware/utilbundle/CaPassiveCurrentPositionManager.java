package com.samsung.android.contextaware.utilbundle;

import android.content.Context;
import android.location.GpsStatus.NmeaListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.IPassiveCurrrentPositionObserver;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.smartclip.SmartClipMetaTagType;

public class CaPassiveCurrentPositionManager implements IUtilManager, IPassiveCurrentPositionObservable {
    private static final float ACCURACY_GOOD_THRESHOLE = 4800.0f;
    private static final float GPS_MIN_DISTANCE = 0.0f;
    private static final long GPS_MIN_TIME = 1000;
    private boolean mEnable = false;
    private PositionContextBean mGpsInfo;
    private final LocationListener mGpsListener = new LocationListener() {
        public final void onProviderEnabled(String provider) {
            CaLogger.info("Location service is enabled");
        }

        public final void onLocationChanged(Location location) {
            int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            float speed = location.getSpeed();
            float accuracy = location.getAccuracy();
            double distance = PositionContextBean.calculationDistance(CaPassiveCurrentPositionManager.this.mPrePosition.getLatitude(), CaPassiveCurrentPositionManager.this.mPrePosition.getLongitude(), latitude, longitude);
            int type = 1;
            if (location.getProvider().equals("network")) {
                type = 2;
            } else if (location.getProvider().equals("fused")) {
                type = 3;
            }
            CaPassiveCurrentPositionManager.this.mGpsInfo.setPosition(type, utcTime, latitude, longitude, altitude, distance, speed, accuracy, CaPassiveCurrentPositionManager.this.mSatelliteCount);
            if (accuracy <= CaPassiveCurrentPositionManager.ACCURACY_GOOD_THRESHOLE) {
                CaPassiveCurrentPositionManager.this.notifyPassiveCurrentPositionObserver(CaPassiveCurrentPositionManager.this.mGpsInfo);
            }
        }

        public final void onProviderDisabled(String provider) {
            CaLogger.info("Location service is disabled");
        }

        public final void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    private LocationManager mGpsManager;
    private IPassiveCurrrentPositionObserver mListener;
    private final Looper mLooper;
    private PositionContextBean mPrePosition;
    private int mSatelliteCount;
    private final NmeaListener m_nmea_listener = new NmeaListener() {
        public void onNmeaReceived(long timestamp, String nmea) {
            String[] str_temp = nmea.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            if (!str_temp[0].equals("$GPGGA")) {
                return;
            }
            if (str_temp[7].equals("")) {
                CaLogger.error("satelliteCount null");
                return;
            }
            CaPassiveCurrentPositionManager.this.mSatelliteCount = CaConvertUtil.strToInt(str_temp[7]);
            CaLogger.info("Satellite Count : " + str_temp[7]);
        }
    };

    public CaPassiveCurrentPositionManager(Context context, Looper looper, IPassiveCurrrentPositionObserver observer) {
        this.mLooper = looper;
        initializeManager(context);
        registerPassiveCurrentPositionObserver(observer);
    }

    public final void initializeManager(Context context) {
        if (context != null) {
            this.mGpsManager = (LocationManager) context.getSystemService(SmartClipMetaTagType.LOCATION);
            if (this.mGpsManager == null) {
                CaLogger.error("mGpsManager is null");
            }
            this.mGpsInfo = new PositionContextBean(1000.0f);
            this.mPrePosition = new PositionContextBean();
            this.mEnable = false;
        }
    }

    public final void terminateManager() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else {
            this.mGpsManager.removeUpdates(this.mGpsListener);
        }
    }

    public final void enable() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mLooper == null) {
            CaLogger.error("Looper is null");
        } else if (!this.mEnable) {
            CaLogger.trace();
            this.mEnable = true;
            this.mSatelliteCount = 0;
            this.mGpsInfo = new PositionContextBean(1000.0f);
            this.mPrePosition = new PositionContextBean();
            new Handler(this.mLooper).postDelayed(new Runnable() {
                public void run() {
                    CaPassiveCurrentPositionManager.this.registerGpsListener();
                }
            }, 0);
        }
    }

    public final void disable() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
        } else if (this.mEnable) {
            CaLogger.trace();
            new Handler(this.mLooper).postDelayed(new Runnable() {
                public void run() {
                    CaPassiveCurrentPositionManager.this.unregisterGpsListener();
                }
            }, 0);
            this.mEnable = false;
        }
    }

    public final void registerPassiveCurrentPositionObserver(IPassiveCurrrentPositionObserver observer) {
        this.mListener = observer;
    }

    public final void unregisterPassiveCurrentPositionObserver() {
        this.mListener = null;
    }

    public final void notifyPassiveCurrentPositionObserver(PositionContextBean position) {
        if (this.mListener != null) {
            this.mPrePosition = position;
            this.mListener.updatePassiveCurrentPosition(position.getType(), position.getUtcTime(), position.getLatitude(), position.getLongitude(), position.getAltitude(), position.getDistance(), position.getSpeed(), position.getAccuracy(), position.getSatelliteCount());
        }
    }

    private void registerGpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
            return;
        }
        this.mGpsManager.requestLocationUpdates("passive", 1000, 0.0f, this.mGpsListener, this.mLooper);
        this.mGpsManager.addNmeaListener(this.m_nmea_listener);
    }

    private void unregisterGpsListener() {
        if (this.mGpsManager == null) {
            CaLogger.error("mGpsManager is null");
            return;
        }
        this.mGpsManager.removeUpdates(this.mGpsListener);
        this.mGpsManager.removeNmeaListener(this.m_nmea_listener);
    }

    public static int sendPositionToSensorHub(int type, int[] utcTime, double latitude, double longitude, double altitude, double distance, float speed, float accuracy, int satelliteCount) {
        byte[] dataPacket = new byte[22];
        System.arraycopy(CaConvertUtil.intToByteArr((int) (1000000.0d * latitude), 4), 0, dataPacket, 0, 4);
        int size = 0 + 4;
        System.arraycopy(CaConvertUtil.intToByteArr((int) (1000000.0d * longitude), 4), 0, dataPacket, size, 4);
        size += 4;
        System.arraycopy(CaConvertUtil.intToByteArr((int) (1000.0d * altitude), 4), 0, dataPacket, size, 4);
        size += 4;
        System.arraycopy(CaConvertUtil.intToByteArr((int) accuracy, 1), 0, dataPacket, size, 1);
        size++;
        System.arraycopy(CaConvertUtil.intToByteArr(utcTime[0], 1), 0, dataPacket, size, 1);
        size++;
        System.arraycopy(CaConvertUtil.intToByteArr(utcTime[1], 1), 0, dataPacket, size, 1);
        size++;
        System.arraycopy(CaConvertUtil.intToByteArr(utcTime[2], 1), 0, dataPacket, size, 1);
        size++;
        System.arraycopy(CaConvertUtil.intToByteArr(satelliteCount, 1), 0, dataPacket, size, 1);
        size++;
        System.arraycopy(CaConvertUtil.intToByteArr((int) (100.0f * speed), 2), 0, dataPacket, size, 2);
        size += 2;
        System.arraycopy(CaConvertUtil.intToByteArr((int) (1000.0d * distance), 2), 0, dataPacket, size, 2);
        System.arraycopy(CaConvertUtil.intToByteArr(type, 1), 0, dataPacket, size + 2, 1);
        return SensorHubCommManager.getInstance().sendCmdToSensorHub(dataPacket, ISensorHubCmdProtocol.INST_LIB_PUTVALUE, (byte) 22);
    }
}
