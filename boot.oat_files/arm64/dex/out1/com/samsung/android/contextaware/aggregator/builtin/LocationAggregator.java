package com.samsung.android.contextaware.aggregator.builtin;

import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.Aggregator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.ApdrRunner;
import com.samsung.android.contextaware.manager.CaUserInfo;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaAlarmManager;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaPowerManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocationAggregator extends Aggregator {
    private static final int ACCURACY_REQUIREMENT_DEFAULT = 100;
    private boolean mApdrNoti = false;
    private float mCurAccuracy;
    private double mCurAltitude;
    private double mCurLatitude;
    private double mCurLongitude;
    private long mCurSysTime;
    private long mCurTimeStamp;
    private int mPedestrianStatus;
    private int mUserWantedAccuracy;

    public LocationAggregator(int version, CopyOnWriteArrayList<ContextComponent> collectionList, ISensorHubResetObservable observable) {
        super(version, null, null, collectionList, observable);
    }

    protected final void initializeAggregator() {
    }

    protected final void terminateAggregator() {
    }

    public final void pause() {
    }

    public final void resume() {
    }

    public final void enable() {
        CaLogger.trace();
    }

    public final void disable() {
        CaLogger.trace();
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
        this.mCurSysTime = 0;
        this.mCurTimeStamp = 0;
        this.mCurLatitude = 0.0d;
        this.mCurLongitude = 0.0d;
        this.mCurAltitude = 0.0d;
        this.mCurAccuracy = 0.0f;
        this.mPedestrianStatus = -1;
        this.mApdrNoti = false;
        this.mUserWantedAccuracy = 100;
    }

    public final String[] getContextValueNames() {
        return new String[]{"SystemTime", "TimeStamp", "Latitude", "Longitude", "Altitude", "Heading", "Speed", "Accuracy", "Valid", "PedestrianStatus"};
    }

    private boolean isFilterInitialized() {
        return true;
    }

    private void sendSleepModeCmdToSensorHub() {
        ApdrRunner apdrRunner = (ApdrRunner) getSubCollectionObj(ContextType.SENSORHUB_RUNNER_APDR.getCode());
        if (apdrRunner != null) {
            this.mCurLatitude = 37.0d;
            this.mCurLongitude = 128.0d;
            this.mCurAltitude = 0.0d;
            this.mCurAccuracy = 10.0f;
            byte[] latBuf = CaConvertUtil.intToByteArr((int) (this.mCurLatitude * 1000000.0d), 4);
            byte[] longBuf = CaConvertUtil.intToByteArr((int) (this.mCurLongitude * 1000000.0d), 4);
            byte[] altiBuf = CaConvertUtil.intToByteArr((int) (this.mCurAltitude * 1000.0d), 3);
            byte[] accuracyBuf = CaConvertUtil.intToByteArr((int) this.mCurAccuracy, 1);
            byte[] userWantedAccuracyBuf = CaConvertUtil.intToByteArr(this.mUserWantedAccuracy, 1);
            byte[] dataPacket = new byte[((((latBuf.length + longBuf.length) + altiBuf.length) + accuracyBuf.length) + userWantedAccuracyBuf.length)];
            System.arraycopy(latBuf, 0, dataPacket, 0, latBuf.length);
            int size = 0 + latBuf.length;
            System.arraycopy(longBuf, 0, dataPacket, size, longBuf.length);
            size += longBuf.length;
            System.arraycopy(altiBuf, 0, dataPacket, size, altiBuf.length);
            size += altiBuf.length;
            System.arraycopy(accuracyBuf, 0, dataPacket, size, accuracyBuf.length);
            System.arraycopy(userWantedAccuracyBuf, 0, dataPacket, size + accuracyBuf.length, userWantedAccuracyBuf.length);
            apdrRunner.sendSleepModeCmdToSensorHub(dataPacket);
        }
    }

    private void updateRawGpsData(Bundle context) {
        CaLogger.trace();
        requestGpsData(context.getLong("SystemTime"), context.getLong("TimeStamp"));
    }

    private void updateRawSatelliteData(Bundle context) {
        CaLogger.trace();
        boolean isScreenOn = CaPowerManager.getInstance().isScreenOn();
        boolean isApSleep = super.getAPStatus() == -46;
        if (!isScreenOn && isFilterInitialized()) {
            if (isApSleep || this.mApdrNoti) {
                CaLogger.trace();
                CaAlarmManager.getInstance().vibrateAlarm(true);
                notifyApStatus();
                sendSleepModeCmdToSensorHub();
            }
        }
    }

    private void updateRawWpsData(Bundle context) {
        CaLogger.trace();
    }

    private void updateApdrData(Bundle context) {
        if (isFilterInitialized() && context != null) {
            this.mCurSysTime += (long) context.getDouble("DeltaTime");
            this.mCurTimeStamp += (long) context.getDouble("DeltaTime");
            CaLogger.trace();
            requestGpsData(this.mCurSysTime, this.mCurTimeStamp);
        }
    }

    private void receiveApdrNoti(Bundle context) {
        if (context != null) {
            int notiType = context.getInt("Alert");
            if (notiType == 1 || notiType == 2) {
                this.mApdrNoti = true;
                CaLogger.trace();
                super.resume();
            }
        }
    }

    private void requestGpsData(long sysTime, long timeStamp) {
        boolean locationHybridValid;
        double[] daLocationHybrid = new double[3];
        float[] faLocationExtHybrid = new float[3];
        if (new int[1][0] == 1) {
            locationHybridValid = true;
        } else {
            locationHybridValid = false;
        }
        if (locationHybridValid) {
            notifyLocationContext(sysTime, timeStamp, daLocationHybrid, faLocationExtHybrid, locationHybridValid, this.mPedestrianStatus);
            setCurLocationforHubApdr(sysTime, timeStamp, daLocationHybrid[0], daLocationHybrid[1], daLocationHybrid[2], faLocationExtHybrid[2]);
        }
    }

    private void notifyLocationContext(long sysTime, long timeStamp, double[] daLocationHybrid, float[] faLocationExtHybrid, boolean valid, int pedestrianStatus) {
        String[] names = getContextValueNames();
        super.getContextBean().putContext(names[0], sysTime);
        super.getContextBean().putContext(names[1], timeStamp);
        super.getContextBean().putContext(names[2], daLocationHybrid[0]);
        super.getContextBean().putContext(names[3], daLocationHybrid[1]);
        super.getContextBean().putContext(names[4], daLocationHybrid[2]);
        super.getContextBean().putContext(names[5], faLocationExtHybrid[0]);
        super.getContextBean().putContext(names[6], faLocationExtHybrid[1]);
        super.getContextBean().putContext(names[7], faLocationExtHybrid[2]);
        super.getContextBean().putContext(names[8], valid);
        super.getContextBean().putContext(names[9], pedestrianStatus);
        super.notifyObserver();
    }

    private void setCurLocationforHubApdr(long sysTime, long timeStamp, double latitude, double longitude, double altitude, float accuracy) {
        this.mCurSysTime = sysTime;
        this.mCurTimeStamp = timeStamp;
        this.mCurLatitude = latitude;
        this.mCurLongitude = longitude;
        this.mCurAltitude = altitude;
        this.mCurAccuracy = accuracy;
    }

    public final String getContextType() {
        return ContextType.AGGREGATOR_LOCATION.getCode();
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final void updateContext(String type, Bundle context) {
        if (context != null) {
            if (type.equals(ContextType.ANDROID_RUNNER_RAW_GPS.getCode())) {
                updateRawGpsData(context);
            } else if (type.equals(ContextType.ANDROID_RUNNER_RAW_SATELLITE.getCode())) {
                updateRawSatelliteData(context);
            } else if (type.equals(ContextType.ANDROID_RUNNER_RAW_WPS.getCode())) {
                updateRawWpsData(context);
            } else if (!type.equals(ContextType.SENSORHUB_RUNNER_APDR.getCode())) {
            } else {
                if (context.size() > 1) {
                    updateApdrData(context);
                } else {
                    receiveApdrNoti(context);
                }
            }
        }
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 1) {
            this.mUserWantedAccuracy = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("setProperty (User Wanted Accuracy) = " + Integer.toString(this.mUserWantedAccuracy));
        } else if (property == 3) {
            CaUserInfo.getInstance().setUserHeight(((Double) ((ContextAwarePropertyBundle) value).getValue()).doubleValue());
            CaLogger.info("setProperty (User Height) = " + Double.toString(CaUserInfo.getInstance().getUserHeight()));
        } else if (property == 2) {
            CaUserInfo.getInstance().setUserWeight(((Double) ((ContextAwarePropertyBundle) value).getValue()).doubleValue());
            CaLogger.info("setProperty (User Weight) = " + Double.toString(CaUserInfo.getInstance().getUserWeight()));
        }
        return true;
    }

    protected final void updateApSleep() {
        CaLogger.trace();
        this.mApdrNoti = false;
    }

    protected final void updateApWakeup() {
        CaLogger.trace();
        this.mApdrNoti = false;
        super.updateApWakeup();
    }
}
