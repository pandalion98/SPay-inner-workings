package com.kddi.android.qualityinformation;

import android.net.IConnectivityManager;
import android.net.IConnectivityManager.Stub;
import android.net.LinkQualityInfo;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.telephony.SignalStrength;
import android.util.Log;

public final class RadioQualityinformation {
    private static final String TAG = "RadioQualityinformation";
    IConnectivityManager service;

    public RadioQualityinformation() {
        this.service = null;
        this.service = Stub.asInterface(ServiceManager.getService("connectivity"));
        Log.d(TAG, "RadioQualityinformation const()");
        if (this.service != null) {
            Log.d(TAG, "RadioQualityinformation service != null");
            try {
                SignalStrength mInfo = this.service.getKdiSignalStrength();
                if (mInfo != null) {
                    Log.d(TAG, "getRSSNR = " + mInfo.getLteRssnr());
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Exception at getLteRssnr " + Log.getStackTraceString(e));
            }
        }
    }

    public int getRSSNR() {
        Log.d(TAG, "RadioQualityinformation getRSSNR()");
        this.service = Stub.asInterface(ServiceManager.getService("connectivity"));
        if (this.service != null) {
            try {
                SignalStrength mInfo = this.service.getKdiSignalStrength();
                if (mInfo != null) {
                    Log.d(TAG, "getRSSNR = " + mInfo.getLteRssnr());
                    return mInfo.getLteRssnr();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Exception at getRSSNR " + Log.getStackTraceString(e));
            }
        }
        return Integer.MAX_VALUE;
    }

    public int getCQI() {
        Log.d(TAG, "RadioQualityinformation getCQI()");
        this.service = Stub.asInterface(ServiceManager.getService("connectivity"));
        if (this.service != null) {
            try {
                SignalStrength mInfo = this.service.getKdiSignalStrength();
                if (mInfo != null) {
                    Log.d(TAG, "getCQI = " + mInfo.getLteCqi());
                    return mInfo.getLteCqi();
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Exception at getCQI " + Log.getStackTraceString(e));
            }
        }
        return Integer.MAX_VALUE;
    }

    public long getPacketCount() {
        Log.d(TAG, "RadioQualityinformation getPacketCount()");
        this.service = Stub.asInterface(ServiceManager.getService("connectivity"));
        if (this.service != null) {
            try {
                LinkQualityInfo mInfo = this.service.getKdiLinkQualityInfo();
                Log.d(TAG, "getPacketCount = " + mInfo);
                if (mInfo != null) {
                    Log.d(TAG, "RadioQualityinformation getPacketCount() mInfo != null");
                    return mInfo.getPacketCount();
                }
                Log.d(TAG, "RadioQualityinformation getPacketCount() mInfo == null");
                return Long.MIN_VALUE;
            } catch (RemoteException e) {
                Log.e(TAG, "Exception at getPacketCount " + Log.getStackTraceString(e));
            }
        }
        return Long.MAX_VALUE;
    }

    public long getPacketErrorCount() {
        Log.d(TAG, "RadioQualityinformation getPacketErrorCount()");
        this.service = Stub.asInterface(ServiceManager.getService("connectivity"));
        if (this.service != null) {
            try {
                LinkQualityInfo mInfo = this.service.getKdiLinkQualityInfo();
                Log.d(TAG, "getPacketErrorCount = " + mInfo);
                if (mInfo != null) {
                    Log.d(TAG, "RadioQualityinformation getPacketCount() mInfo != null");
                    return mInfo.getPacketErrorCount();
                }
                Log.d(TAG, "RadioQualityinformation getPacketCount() mInfo == null");
                return Long.MIN_VALUE;
            } catch (RemoteException e) {
                Log.e(TAG, "Exception at getPacketErrorCount " + Log.getStackTraceString(e));
            }
        }
        return Long.MAX_VALUE;
    }
}
