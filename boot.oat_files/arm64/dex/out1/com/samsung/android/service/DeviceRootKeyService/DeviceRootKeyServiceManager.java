package com.samsung.android.service.DeviceRootKeyService;

import android.content.Context;
import android.os.ServiceManager;
import android.util.Log;
import com.samsung.android.service.DeviceRootKeyService.IDeviceRootKeyService.Stub;

public final class DeviceRootKeyServiceManager {
    public static final int ERR_SERVICE_ERROR = -10000;
    public static final int KEY_TYPE_EC = 4;
    public static final int KEY_TYPE_RSA = 1;
    public static final int KEY_TYPE_SYMM = 2;
    public static final int NO_ERROR = 0;
    private static final String TAG = "DeviceRootKeyServiceManager";
    private final Context mContext;
    private IDeviceRootKeyService mService = Stub.asInterface(ServiceManager.getService("DeviceRootKeyService"));

    public DeviceRootKeyServiceManager(Context context) {
        this.mContext = context;
        Log.i(TAG, this.mContext.getPackageName() + " connects to DeviceRootKeyService.");
    }

    public boolean isAliveDeviceRootKeyService() {
        if (this.mService != null) {
            return true;
        }
        return false;
    }

    public boolean isExistDeviceRootKey(int drkType) {
        boolean z = false;
        Log.i(TAG, "isExistDeviceRootKey() is called.");
        try {
            z = this.mService.isExistDeviceRootKey(drkType);
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return z;
    }

    public String getDeviceRootKeyUID(int drkType) {
        String str = null;
        Log.i(TAG, "getDeviceRootKeyUID() is called.");
        try {
            str = this.mService.getDeviceRootKeyUID(drkType);
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public byte[] createServiceKeySession(String serviceName, int keyType, Tlv tlv) {
        byte[] bArr = null;
        Log.i(TAG, "createServiceKeySession() is called.");
        try {
            bArr = this.mService.createServiceKeySession(serviceName, keyType, tlv);
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bArr;
    }

    public int releaseServiceKeySession() {
        int i = ERR_SERVICE_ERROR;
        Log.i(TAG, "releaseServiceKeySession() is called.");
        try {
            i = this.mService.releaseServiceKeySession();
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public int setDeviceRootKey(byte[] keyBlob) {
        int i = ERR_SERVICE_ERROR;
        Log.i(TAG, "setDeviceRootKey() is called.");
        try {
            i = this.mService.setDeviceRootKey(keyBlob);
        } catch (NullPointerException npe) {
            Log.e(TAG, "Failed to connect service.");
            npe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }
}
