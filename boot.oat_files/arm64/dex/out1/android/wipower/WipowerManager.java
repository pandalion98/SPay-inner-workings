package android.wipower;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.Log;
import android.wipower.IWipower.Stub;
import java.util.ArrayList;

public final class WipowerManager {
    private static final boolean DBG = true;
    private static final String TAG = "WipowerManager";
    private static final boolean VDBG = false;
    private static ArrayList<WipowerManagerCallback> mCallbacks;
    private static WipowerDynamicParam mPruData;
    private static IWipower mService;
    private static WipowerState mState;
    private static WipowerManager mWipowerManager;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            WipowerManager.mService = Stub.asInterface(service);
            Log.v(WipowerManager.TAG, "Proxy object connected: " + WipowerManager.mService);
            try {
                WipowerManager.mService.registerCallback(WipowerManager.this.mWiPowerMangerCallback);
            } catch (RemoteException e) {
                Log.e(WipowerManager.TAG, "not able to register as client");
            }
            Log.v(WipowerManager.TAG, "Calling onWipowerReady");
            WipowerManager.this.updateWipowerReady();
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.v(WipowerManager.TAG, "Proxy object disconnected");
            try {
                WipowerManager.mService.unregisterCallback(WipowerManager.this.mWiPowerMangerCallback);
            } catch (RemoteException e) {
                Log.e(WipowerManager.TAG, "not able to unregister as client");
            }
            WipowerManager.mService = null;
        }
    };
    private final IWipowerManagerCallback mWiPowerMangerCallback = new IWipowerManagerCallback.Stub() {
        public void onWipowerStateChange(int state) {
            WipowerState s;
            if (state == 1) {
                s = WipowerState.ON;
            } else {
                s = WipowerState.OFF;
            }
            Log.v(WipowerManager.TAG, "onWipowerStateChange: state" + state);
            WipowerManager.this.updateWipowerState(s);
        }

        public void onWipowerAlert(byte alert) {
            Log.v(WipowerManager.TAG, "onWipowerAlert: alert" + alert);
            WipowerManager.this.updateWipowerAlert(alert);
        }

        public void onPowerApply(byte alert) {
            PowerApplyEvent s;
            if (alert == (byte) 1) {
                s = PowerApplyEvent.ON;
            } else {
                s = PowerApplyEvent.OFF;
            }
            WipowerManager.this.updatePowerApplyAlert(s);
        }

        public void onWipowerData(byte[] value) {
            Log.v(WipowerManager.TAG, "onWipowerData: " + value);
            if (WipowerManager.mPruData != null) {
                WipowerManager.mPruData.setValue(value);
                WipowerManager.this.updateWipowerData(WipowerManager.mPruData);
                return;
            }
            Log.e(WipowerManager.TAG, "mPruData is null");
        }
    };

    public enum PowerApplyEvent {
        OFF,
        ON
    }

    public enum PowerLevel {
        POWER_LEVEL_MAXIMUM,
        POWER_LEVEL_MEDIUM,
        POWER_LEVEL_MINIMUM,
        POWER_LEVEL_UNKNOWN
    }

    public enum WipowerState {
        OFF,
        ON
    }

    void updateWipowerState(WipowerState state) {
        if (mCallbacks != null) {
            int n = mCallbacks.size();
            Log.v(TAG, "Broadcasting updateAdapterState() to " + n + " receivers.");
            for (int i = 0; i < n; i++) {
                ((WipowerManagerCallback) mCallbacks.get(i)).onWipowerStateChange(state);
            }
        }
    }

    void updateWipowerData(WipowerDynamicParam pruData) {
        if (mCallbacks != null) {
            int n = mCallbacks.size();
            Log.v(TAG, "Broadcasting updateWipowerData() to " + n + " receivers.");
            for (int i = 0; i < n; i++) {
                ((WipowerManagerCallback) mCallbacks.get(i)).onWipowerData(pruData);
            }
        }
    }

    void updateWipowerAlert(byte alert) {
        if (mCallbacks != null) {
            int n = mCallbacks.size();
            Log.v(TAG, "Broadcasting updateWipowerAlert() to " + n + " receivers.");
            for (int i = 0; i < n; i++) {
                ((WipowerManagerCallback) mCallbacks.get(i)).onWipowerAlert(alert);
            }
        }
    }

    void updatePowerApplyAlert(PowerApplyEvent alert) {
        if (mCallbacks != null) {
            int n = mCallbacks.size();
            for (int i = 0; i < n; i++) {
                ((WipowerManagerCallback) mCallbacks.get(i)).onPowerApply(alert);
            }
        }
    }

    void updateWipowerReady() {
        if (mCallbacks != null) {
            int n = mCallbacks.size();
            for (int i = 0; i < n; i++) {
                ((WipowerManagerCallback) mCallbacks.get(i)).onWipowerReady();
            }
        }
    }

    public static synchronized WipowerManager getWipowerManger(Context context, WipowerManagerCallback callback) {
        WipowerManager wipowerManager;
        synchronized (WipowerManager.class) {
            if (isWipowerSupported()) {
                if (mWipowerManager == null) {
                    Log.v(TAG, "Instantiate Singleton");
                    mWipowerManager = new WipowerManager(context.getApplicationContext(), callback);
                }
                wipowerManager = mWipowerManager;
            } else {
                Log.e(TAG, "Wipower not supported");
                wipowerManager = null;
            }
        }
        return wipowerManager;
    }

    private WipowerManager(Context context, WipowerManagerCallback callback) {
        if (mService == null) {
            try {
                Intent bindIntent = new Intent(IWipower.class.getName());
                ComponentName comp = bindIntent.resolveSystemService(context.getPackageManager(), 0);
                bindIntent.setComponent(comp);
                if (comp == null || !context.bindService(bindIntent, this.mConnection, 1)) {
                    Log.e(TAG, "Could not bind to Wipower Service");
                }
            } catch (SecurityException e) {
                Log.e(TAG, "Security Exception");
            }
        }
        Log.v(TAG, "Bound to Wipower Service");
        mPruData = new WipowerDynamicParam();
        mCallbacks = new ArrayList();
    }

    static boolean isWipowerSupported() {
        if (SystemProperties.getBoolean("ro.bluetooth.a4wp", false)) {
            Log.v(TAG, "System.getProperty is true");
            return true;
        }
        Log.v(TAG, "System.getProperty is false");
        return false;
    }

    public boolean startCharging() {
        boolean ret = false;
        if (isWipowerSupported()) {
            if (mService == null) {
                Log.e(TAG, "startCharging: Service  not available");
            } else {
                try {
                    ret = mService.startCharging();
                } catch (RemoteException e) {
                    Log.e(TAG, "Service  Exceptione");
                }
            }
            return ret;
        }
        Log.e(TAG, "Wipower not supported");
        return false;
    }

    public boolean stopCharging() {
        boolean ret = false;
        if (isWipowerSupported()) {
            if (mService == null) {
                Log.e(TAG, " Wipower Service not available");
            } else {
                try {
                    ret = mService.stopCharging();
                } catch (RemoteException e) {
                    Log.e(TAG, "Service  Exceptione");
                }
            }
            return ret;
        }
        Log.e(TAG, "Wipower not supported");
        return false;
    }

    public boolean setPowerLevel(PowerLevel powerlevel) {
        boolean ret = false;
        if (isWipowerSupported()) {
            if (mService == null) {
                Log.e(TAG, " Wipower Service not available");
            } else {
                byte level = (byte) 0;
                if (powerlevel == PowerLevel.POWER_LEVEL_MINIMUM) {
                    level = (byte) 2;
                } else if (powerlevel == PowerLevel.POWER_LEVEL_MEDIUM) {
                    level = (byte) 1;
                } else if (powerlevel == PowerLevel.POWER_LEVEL_MAXIMUM) {
                    level = (byte) 0;
                }
                try {
                    ret = mService.setCurrentLimit(level);
                } catch (RemoteException e) {
                    Log.e(TAG, "Service  Exceptione");
                }
            }
            return ret;
        }
        Log.e(TAG, "Wipower not supported");
        return false;
    }

    public PowerLevel getPowerLevel() {
        PowerLevel ret = PowerLevel.POWER_LEVEL_UNKNOWN;
        if (mService == null) {
            Log.e(TAG, " Wipower Service not available");
            return ret;
        }
        byte res = (byte) 0;
        try {
            res = mService.getCurrentLimit();
        } catch (RemoteException e) {
            Log.e(TAG, "Service  Exceptione");
        }
        if (res == (byte) 0) {
            return PowerLevel.POWER_LEVEL_MINIMUM;
        }
        if (res == (byte) 1) {
            return PowerLevel.POWER_LEVEL_MEDIUM;
        }
        if (res == (byte) 2) {
            return PowerLevel.POWER_LEVEL_MAXIMUM;
        }
        return ret;
    }

    public WipowerState getState() {
        WipowerState ret = WipowerState.OFF;
        if (mService == null) {
            Log.e(TAG, " Wipower Service not available");
            return ret;
        }
        int res = 0;
        try {
            res = mService.getState();
        } catch (RemoteException e) {
            Log.e(TAG, "Service  Exceptione");
        }
        if (res == 0) {
            return WipowerState.OFF;
        }
        return WipowerState.ON;
    }

    public boolean enableAlertNotification(boolean enable) {
        boolean ret = false;
        if (mService == null) {
            Log.e(TAG, "Service  not available");
        } else {
            try {
                ret = mService.enableAlert(enable);
            } catch (RemoteException e) {
                Log.e(TAG, "Service  Exception");
            }
        }
        return ret;
    }

    public boolean enableDataNotification(boolean enable) {
        boolean ret = false;
        if (mService == null) {
            Log.e(TAG, "Service  not available");
        } else {
            try {
                ret = mService.enableData(enable);
            } catch (RemoteException e) {
                Log.e(TAG, "Service  Exceptione");
            }
        }
        return ret;
    }

    public boolean enablePowerApply(boolean enable, boolean on, boolean time_flag) {
        boolean ret = false;
        Log.v(TAG, "enablePowerApply: enable: " + enable + " on: " + on + " time_flag:" + time_flag);
        if (mService == null) {
            Log.e(TAG, "Service  not available");
        } else {
            try {
                ret = mService.enablePowerApply(enable, on, time_flag);
            } catch (RemoteException e) {
                Log.e(TAG, "Service  Exceptione");
            }
        }
        return ret;
    }

    public void registerCallback(WipowerManagerCallback callback) {
        if (mService == null) {
            Log.e(TAG, "registerCallback:Service  not available");
        }
        mCallbacks.add(callback);
    }

    public void unregisterCallback(WipowerManagerCallback callback) {
        if (mService == null) {
            Log.e(TAG, "Service  not available");
        }
        mCallbacks.remove(callback);
    }
}
