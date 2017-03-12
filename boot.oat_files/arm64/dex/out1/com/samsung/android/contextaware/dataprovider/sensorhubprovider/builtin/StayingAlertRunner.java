package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ICurrrentLocationObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaCurrentLocationManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class StayingAlertRunner extends LibTypeProvider implements ICurrrentLocationObserver {
    private static final byte ASK_CURRENT_LOCATION = (byte) 2;
    private double mCurAltitude;
    private double mCurLatitude;
    private final CaCurrentLocationManager mCurLocationManager;
    private double mCurLongitude;
    private int mStopPeriod = 30;
    private int mWaitPeriod = 60;

    public StayingAlertRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
        this.mCurLocationManager = new CaCurrentLocationManager(context, looper, this);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_STAYING_ALERT.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 27;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) this.mStopPeriod, (byte) this.mWaitPeriod};
    }

    public final String[] getContextValueNames() {
        return new String[]{"Action", "CurLatitude", "CurLongitude", "CurAltitude"};
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        String[] names = getContextValueNames();
        CaLogger.info("parse start:" + tmpNext);
        if (names == null || names.length <= 0 || (packet.length - tmpNext) - 1 < 0) {
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        int data = packet[tmpNext];
        if (data == 2) {
            this.mCurLocationManager.enable();
        } else {
            super.getContextBean().putContext(names[0], data);
            super.getContextBean().putContext(names[1], this.mCurLatitude);
            super.getContextBean().putContext(names[2], this.mCurLongitude);
            super.getContextBean().putContext(names[3], this.mCurAltitude);
            super.notifyObserver();
        }
        CaLogger.info("parse end:" + tmpNext2);
        tmpNext = tmpNext2;
        return tmpNext2;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final void updateCurrentLocation(long sysTime, long timeStamp, double latitude, double longitude, double altitude) {
        this.mCurLatitude = latitude;
        this.mCurLongitude = longitude;
        this.mCurAltitude = altitude;
        byte[] latBuf = CaConvertUtil.intToByteArr((int) (1000000.0d * latitude), 4);
        byte[] longBuf = CaConvertUtil.intToByteArr((int) (1000000.0d * longitude), 4);
        byte[] dataPacket = new byte[8];
        System.arraycopy(latBuf, 0, dataPacket, 0, 4);
        System.arraycopy(longBuf, 0, dataPacket, 4, 4);
        sendPropertyValueToSensorHub((byte) 27, (byte) 3, dataPacket);
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 23) {
            int stopPeriod = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            this.mStopPeriod = stopPeriod;
            CaLogger.info("Stop Period = " + Integer.toString(stopPeriod));
            return sendPropertyValueToSensorHub((byte) 27, (byte) 1, CaConvertUtil.intToByteArr(stopPeriod, 1));
        } else if (property != 24) {
            return false;
        } else {
            int waitPeriod = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            this.mWaitPeriod = waitPeriod;
            CaLogger.info("Wait Period = " + Integer.toString(waitPeriod));
            return sendPropertyValueToSensorHub((byte) 27, (byte) 2, CaConvertUtil.intToByteArr(waitPeriod, 1));
        }
    }

    protected void display() {
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
        this.mCurLocationManager.registerCurrentLocationObserver(this);
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
        this.mCurLocationManager.unregisterCurrentLocationObserver();
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
