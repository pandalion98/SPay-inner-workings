package android.app;

import android.content.Context;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.IStatusBarService.Stub;

public class StatusBarManager {
    public static final int CALL_BACKGROUND_ENDED = 2;
    public static final int CALL_BACKGROUND_IDLE = 0;
    public static final int CALL_BACKGROUND_NORMAL = 1;
    public static final int CALL_BACKGROUND_ONHOLD = 3;
    public static final int CAMERA_LAUNCH_SOURCE_POWER_DOUBLE_TAP = 1;
    public static final int CAMERA_LAUNCH_SOURCE_WIGGLE = 0;
    public static final int DISABLE2_MASK = 1;
    public static final int DISABLE2_NONE = 0;
    public static final int DISABLE2_QUICK_SETTINGS = 1;
    public static final int DISABLE_BACK = 4194304;
    public static final int DISABLE_CLOCK = 8388608;
    public static final int DISABLE_EXPAND = 65536;
    public static final int DISABLE_HOME = 2097152;
    public static final int DISABLE_MASK = 67043328;
    @Deprecated
    public static final int DISABLE_NAVIGATION = 18874368;
    public static final int DISABLE_NONE = 0;
    public static final int DISABLE_NOTIFICATION_ALERTS = 262144;
    public static final int DISABLE_NOTIFICATION_ICONS = 131072;
    @Deprecated
    public static final int DISABLE_NOTIFICATION_TICKER = 524288;
    public static final int DISABLE_RECENT = 16777216;
    public static final int DISABLE_SEARCH = 33554432;
    public static final int DISABLE_SYSTEM_INFO = 1048576;
    public static final int DISABLE_VISIBILITY = 555;
    public static final int ENABLE_VISIBILITY = 556;
    public static final int NAVIGATION_HINT_BACK_ALT = 1;
    public static final int NAVIGATION_HINT_IME_SHOWN = 2;
    public static final int SEALED_MODE_VISIBILITY = 557;
    private static final String TAG = "StatusBarManager";
    public static final int WINDOW_NAVIGATION_BAR = 2;
    public static final int WINDOW_STATE_HIDDEN = 2;
    public static final int WINDOW_STATE_HIDING = 1;
    public static final int WINDOW_STATE_SHOWING = 0;
    public static final int WINDOW_STATUS_BAR = 1;
    private Context mContext;
    private IStatusBarService mService;
    private IBinder mToken = new Binder();

    StatusBarManager(Context context) {
        this.mContext = context;
    }

    private synchronized IStatusBarService getService() {
        if (this.mService == null) {
            this.mService = Stub.asInterface(ServiceManager.getService(Context.STATUS_BAR_SERVICE));
            if (this.mService == null) {
                Slog.w(TAG, "warning: no STATUS_BAR_SERVICE");
            }
        }
        return this.mService;
    }

    public void disable(int what) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.disable(what, this.mToken, this.mContext.getPackageName());
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void disable2(int what) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.disable2(what, this.mToken, this.mContext.getPackageName());
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int getDisableFlags() {
        try {
            Slog.w(TAG, "getDisableFlags called");
            IStatusBarService svc = getService();
            if (svc != null) {
                return svc.getDisableFlags();
            }
            return 0;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void disableAsUser(int userId, int what) {
        if (Binder.getCallingUid() != 1000) {
            throw new SecurityException("Calling uid does not have permission to do this operation");
        }
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.disableAsUser(userId, what, this.mToken, this.mContext.getPackageName());
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void expandNotificationsPanel() {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.expandNotificationsPanel();
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void collapsePanels() {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.collapsePanels();
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void expandSettingsPanel() {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.expandSettingsPanel();
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setIcon(String slot, int iconId, int iconLevel, String contentDescription) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.setIcon(slot, this.mContext.getPackageName(), iconId, iconLevel, contentDescription);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void removeIcon(String slot) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.removeIcon(slot);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setIconVisibility(String slot, boolean visible) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.setIconVisibility(slot, visible);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String windowStateToString(int state) {
        if (state == 1) {
            return "WINDOW_STATE_HIDING";
        }
        if (state == 2) {
            return "WINDOW_STATE_HIDDEN";
        }
        if (state == 0) {
            return "WINDOW_STATE_SHOWING";
        }
        return "WINDOW_STATE_UNKNOWN";
    }

    public void toggleRecentApps() {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.toggleRecentApps();
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setPanelExpandState(boolean state) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.setPanelExpandState(state);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean getPanelExpandState() {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                return svc.getPanelExpandState();
            }
            return false;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void enableSignals(boolean enable) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.enableSignals(enable);
            }
        } catch (RemoteException ex) {
            Log.w(TAG, "RemoteExeption occurred: " + Log.getStackTraceString(ex));
        }
    }

    public void setCallBackground(int mode) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.setCallBackground(mode);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void notifyRecentPanelVisiblity(boolean mode) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.notifyRecentPanelVisiblity(mode);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setQuickSettingPanelExpandState(boolean state) {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.setQuickSettingPanelExpandState(state);
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean getQuickSettingPanelExpandState() {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                return svc.getQuickSettingPanelExpandState();
            }
            return false;
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void requestStatusBarOpen() {
        try {
            IStatusBarService svc = getService();
            if (svc != null) {
                svc.requestStatusBarOpen();
            }
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }
}