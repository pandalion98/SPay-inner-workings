package com.sec.rll;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings.Secure;
import android.util.Log;
import com.sec.rll.IExtControlDeviceService.Stub;

public class ExtControlDeviceService extends Stub {
    private static final String ACTION_NFC_POLICY_CHANGED = "android.action.nfc.policychanged";
    private static final int DEVICE_GPS = 4097;
    private static final int DEVICE_NFC = 8193;
    private static final String PROPERTY_NFC_LOCKOUT = "persist.security.nfc.lockout";
    private static final int STATUS_DISABLED = 0;
    private static final int STATUS_ENABLED = 1;
    private static final String TAG = "SRIB-ExtControlDeviceService";
    private static Context mContext;
    private static PackageManager mPackageManager;
    private static int mUid;
    private static ExtControlDeviceService sService;

    public static void init(Context context) {
        mContext = context;
        mUid = Process.myUid();
        mPackageManager = mContext.getPackageManager();
    }

    public static synchronized ExtControlDeviceService getInstance() {
        ExtControlDeviceService extControlDeviceService;
        synchronized (ExtControlDeviceService.class) {
            if (sService == null) {
                sService = new ExtControlDeviceService();
            }
            extControlDeviceService = sService;
        }
        return extControlDeviceService;
    }

    public void setStatus(int deviceType, int status) throws RemoteException {
        Log.d(TAG, "setStatus called");
        if (!isAccessPermitted()) {
            return;
        }
        if (deviceType == DEVICE_GPS) {
            Log.e(TAG, "Set gps state called with state : " + status);
            Binder.clearCallingIdentity();
            int mode = 3;
            switch (Secure.getInt(mContext.getContentResolver(), "location_mode", 0)) {
                case 0:
                    if (status != 1) {
                        mode = 0;
                        break;
                    } else {
                        mode = 1;
                        break;
                    }
                case 1:
                    if (status != 1) {
                        mode = 0;
                        break;
                    } else {
                        mode = 1;
                        break;
                    }
                case 2:
                    if (status != 1) {
                        mode = 2;
                        break;
                    } else {
                        mode = 3;
                        break;
                    }
                case 3:
                    if (status != 1) {
                        mode = 2;
                        break;
                    } else {
                        mode = 3;
                        break;
                    }
            }
            Secure.putInt(mContext.getContentResolver(), "location_mode", mode);
        } else if (deviceType == DEVICE_NFC) {
            Log.e(TAG, "Set NFC/Felica state called with state : " + status);
            setNfcState(status);
        }
    }

    private static void setNfcState(int state) {
        SystemProperties.set(PROPERTY_NFC_LOCKOUT, Integer.toString(state));
        Intent nfcIntent = new Intent(ACTION_NFC_POLICY_CHANGED);
        nfcIntent.putExtra("NfcState", state);
        mContext.sendBroadcast(nfcIntent);
    }

    private static int getNfcState() {
        return SystemProperties.getInt(PROPERTY_NFC_LOCKOUT, 1);
    }

    private static boolean setLocationMode(int mode) {
        Binder.clearCallingIdentity();
        return Secure.putInt(mContext.getContentResolver(), "location_mode", mode);
    }

    private boolean isAccessPermitted() {
        int callerUid = Binder.getCallingUid();
        if (callerUid == mUid) {
            Log.d(TAG, "UID matches - access granted to uid:" + callerUid);
            return true;
        }
        for (String pkg : mPackageManager.getPackagesForUid(callerUid)) {
            if (pkg.startsWith("com.example.testrll") || pkg.startsWith("com.kddi.extcontroldevice")) {
                Log.d(TAG, "Allowing RLL access");
                return true;
            }
        }
        Log.w(TAG, "Access denied to UID:" + callerUid);
        return false;
    }

    public int getStatus(int deviceType) throws RemoteException {
        Log.d(TAG, "getStatus called");
        if (!isAccessPermitted()) {
            return -1;
        }
        if (deviceType == DEVICE_GPS) {
            Binder.clearCallingIdentity();
            Log.e(TAG, "get gps state called return value  : " + Secure.getInt(mContext.getContentResolver(), "location_mode", 0));
            int currentMode = Secure.getInt(mContext.getContentResolver(), "location_mode", 0);
            if (currentMode == 3 || currentMode == 1) {
                return 1;
            }
            return 0;
        } else if (deviceType != DEVICE_NFC) {
            return 0;
        } else {
            Log.e(TAG, "get nfc/felica state called return value : " + getNfcState());
            return getNfcState();
        }
    }
}
