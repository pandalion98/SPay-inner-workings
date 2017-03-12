package com.samsung.android.service.EngineeringMode;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.service.EngineeringMode.IEngineeringModeService.Stub;

public final class EngineeringModeManager {
    public static final int ALLOWED = 1;
    public static final int DISABLE = 1;
    public static final int ENABLE = 0;
    public static final int ENG_KERNEL = 0;
    public static final byte[] ERRORBYTE_EM_SERVICE = new byte[]{(byte) -1};
    public static final int ERROR_EM_SERVICE = -1000;
    public static final int NOK = 0;
    public static final int NOT_ALLOWED = 0;
    public static final int OK = 1;
    private static final String TAG = "EngineeringModeManager";
    public static final int USB_DEBUG = 1;
    public static final int USB_DEBUG_ALLOWED = 1;
    public static final int USB_DEBUG_NOT_ALLOWED = 0;
    private final Context mContext;
    private IEngineeringModeService mService = Stub.asInterface(ServiceManager.getService("EngineeringModeService"));

    public EngineeringModeManager(Context context) {
        this.mContext = context;
        Log.i(TAG, this.mContext.getPackageName() + " connects to EngineeringModeService.");
    }

    public boolean isConnected() {
        if (this.mService != null) {
            return true;
        }
        return false;
    }

    public int getStatus(int mode) {
        int i = -1000;
        Log.i(TAG, "getStatus() is called.");
        try {
            i = this.mService.getStatus(mode);
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public byte[] getRequestMsg(String singleID, String OTP, byte[] modeSet) {
        Log.i(TAG, "getRequestMsg() is called.");
        try {
            return this.mService.getRequestMsg(singleID, OTP, modeSet, 0);
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        } catch (Exception e) {
            e.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        }
    }

    public byte[] getRequestMsg(String singleID, String OTP, byte[] modeSet, int validityCount) {
        Log.i(TAG, "getRequestMsg() is called.");
        try {
            return this.mService.getRequestMsg(singleID, OTP, modeSet, validityCount);
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        } catch (Exception e) {
            e.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        }
    }

    public int installToken(byte[] token) {
        int i = -1000;
        Log.i(TAG, "installToken() is called.");
        try {
            i = this.mService.installToken(token);
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public int isTokenInstalled() {
        int i = -1000;
        Log.i(TAG, "isTokenInstalled() is called.");
        try {
            i = this.mService.isTokenInstalled();
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public int removeToken() {
        int i = -1000;
        Log.i(TAG, "removeToken() is called.");
        try {
            i = this.mService.removeToken();
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public byte[] getID() {
        Log.i(TAG, "getID() is called.");
        try {
            return this.mService.getID();
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        } catch (Exception e) {
            e.printStackTrace();
            return ERRORBYTE_EM_SERVICE;
        }
    }

    public int getNumOfModes() {
        int i = -1000;
        Log.i(TAG, "getNumOfModes() is called.");
        try {
            i = this.mService.getNumOfModes();
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
