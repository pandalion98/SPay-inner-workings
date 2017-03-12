package android.app;

import android.app.IUiModeManager.Stub;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

public class UiModeManager {
    public static String ACTION_ENTER_CAR_MODE = "android.app.action.ENTER_CAR_MODE";
    public static String ACTION_ENTER_DESK_MODE = "android.app.action.ENTER_DESK_MODE";
    public static String ACTION_EXIT_CAR_MODE = "android.app.action.EXIT_CAR_MODE";
    public static String ACTION_EXIT_DESK_MODE = "android.app.action.EXIT_DESK_MODE";
    public static final int DISABLE_CAR_MODE_GO_HOME = 1;
    public static final int ENABLE_CAR_MODE_ALLOW_SLEEP = 2;
    public static final int ENABLE_CAR_MODE_GO_CAR_HOME = 1;
    public static final int MODE_NIGHT_AUTO = 0;
    public static final int MODE_NIGHT_NO = 1;
    public static final int MODE_NIGHT_YES = 2;
    private static final String TAG = "UiModeManager";
    private IUiModeManager mService = Stub.asInterface(ServiceManager.getService(Context.UI_MODE_SERVICE));

    UiModeManager() {
    }

    public void enableCarMode(int flags) {
        if (this.mService != null) {
            try {
                this.mService.enableCarMode(flags);
            } catch (RemoteException e) {
                Log.e(TAG, "disableCarMode: RemoteException", e);
            }
        }
    }

    public void disableCarMode(int flags) {
        if (this.mService != null) {
            try {
                this.mService.disableCarMode(flags);
            } catch (RemoteException e) {
                Log.e(TAG, "disableCarMode: RemoteException", e);
            }
        }
    }

    public int getCurrentModeType() {
        if (this.mService != null) {
            try {
                return this.mService.getCurrentModeType();
            } catch (RemoteException e) {
                Log.e(TAG, "getCurrentModeType: RemoteException", e);
            }
        }
        return 1;
    }

    public void setNightMode(int mode) {
        if (this.mService != null) {
            try {
                this.mService.setNightMode(mode);
            } catch (RemoteException e) {
                Log.e(TAG, "setNightMode: RemoteException", e);
            }
        }
    }

    public int getNightMode() {
        if (this.mService != null) {
            try {
                return this.mService.getNightMode();
            } catch (RemoteException e) {
                Log.e(TAG, "getNightMode: RemoteException", e);
            }
        }
        return -1;
    }
}
