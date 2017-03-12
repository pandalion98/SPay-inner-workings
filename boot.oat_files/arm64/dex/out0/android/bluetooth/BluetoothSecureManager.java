package android.bluetooth;

import android.app.BluetoothSecureManagerService;
import android.bluetooth.IBluetoothSecureManagerService.Stub;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.lang.reflect.Method;

public final class BluetoothSecureManager {
    public static final String SECURE_SETTING_A2DP_ENABLE = "a2dp_enable";
    public static final String SECURE_SETTING_FTP_ENABLE = "ftp_enable";
    public static final String SECURE_SETTING_GATT_ENABLE = "gatt_enable";
    public static final String SECURE_SETTING_HDP_ENABLE = "hdp_enable";
    public static final String SECURE_SETTING_HFP_ENABLE = "hfp_enable";
    public static final String SECURE_SETTING_HID_ENABLE = "hid_enable";
    public static final String SECURE_SETTING_MAP_ENABLE = "map_enable";
    public static final String SECURE_SETTING_OPP_ENABLE = "opp_enable";
    public static final String SECURE_SETTING_PAIRING_MODE = "pairing_mode";
    public static final String SECURE_SETTING_PAN_ENABLE = "pan_enable";
    public static final String SECURE_SETTING_PBAP_ENABLE = "pbap_enable";
    public static final String SECURE_SETTING_SAP_ENABLE = "sap_enable";
    public static final String SECURE_SETTING_SCAN_MODE = "scan_mode";
    private static final String TAG = "BluetoothSecureManager";
    private static BluetoothSecureManager This = null;
    private IBluetoothSecureManagerService mService = loadSecureManager();

    public static BluetoothSecureManager getInstant() {
        if (This != null) {
            return This;
        }
        synchronized (BluetoothSecureManager.class) {
            if (This == null) {
                This = new BluetoothSecureManager();
            }
        }
        return This;
    }

    public boolean enableSecureMode(boolean enable) throws RemoteException {
        if (this.mService == null) {
            return false;
        }
        return this.mService.enableSecureMode(enable);
    }

    public boolean isSecureModeEnabled() throws RemoteException {
        if (this.mService == null) {
            return false;
        }
        return this.mService.isSecureModeEnabled();
    }

    public int getSecureModeSetting(String valueName) throws RemoteException {
        if (this.mService == null) {
            return 0;
        }
        return this.mService.getSecureModeSetting(valueName);
    }

    public boolean setSecureModeSetting(String valueName, int value) throws RemoteException {
        if (this.mService == null) {
            return false;
        }
        return this.mService.setSecureModeSetting(valueName, value);
    }

    public boolean enableWhiteList(boolean enable) throws RemoteException {
        if (this.mService == null) {
            return false;
        }
        return this.mService.enableWhiteList(enable);
    }

    public boolean isWhiteListEnabled() throws RemoteException {
        if (this.mService == null) {
            return false;
        }
        return this.mService.isWhiteListEnabled();
    }

    public boolean addWhiteList(String name, int cod, String[] uuids) throws RemoteException {
        if (this.mService == null) {
            return false;
        }
        return this.mService.addWhiteList(name, cod, uuids);
    }

    public boolean removeWhiteList(String name, int cod) throws RemoteException {
        if (this.mService == null) {
            return false;
        }
        return this.mService.removeWhiteList(name, cod);
    }

    public int getWhiteListFirstIndex() throws RemoteException {
        if (this.mService == null) {
            return -1;
        }
        return this.mService.getWhiteListFirstIndex();
    }

    public int getWhiteListNextIndex(int currentIndex) throws RemoteException {
        if (this.mService == null) {
            return -1;
        }
        return this.mService.getWhiteListNextIndex(currentIndex);
    }

    public String getWhiteListName(int index) throws RemoteException {
        if (this.mService == null) {
            return null;
        }
        return this.mService.getWhiteListName(index);
    }

    public int getWhiteListCod(int index) throws RemoteException {
        if (this.mService == null) {
            return 0;
        }
        return this.mService.getWhiteListCod(index);
    }

    public String[] getWhiteListUuids(int index) throws RemoteException {
        if (this.mService == null) {
            return null;
        }
        return this.mService.getWhiteListUuids(index);
    }

    private BluetoothSecureManager() {
    }

    private static IBluetoothSecureManagerService loadSecureManager() {
        IBluetoothSecureManagerService iBluetoothSecureManagerService = null;
        try {
            Class<?> ServiceManager = Class.forName("android.os.ServiceManager");
            Log.d(TAG, "calling getMethod for getService");
            Method getService = ServiceManager.getMethod("getService", new Class[]{String.class});
            Log.d(TAG, "calling getService");
            IBinder binder = (IBinder) getService.invoke(null, new Object[]{BluetoothSecureManagerService.Name});
            Log.d(TAG, "getService return binder: " + binder);
            iBluetoothSecureManagerService = Stub.asInterface(binder);
        } catch (Exception e) {
            Log.e(TAG, "load bt secure manager failed: " + e);
        }
        return iBluetoothSecureManagerService;
    }
}
