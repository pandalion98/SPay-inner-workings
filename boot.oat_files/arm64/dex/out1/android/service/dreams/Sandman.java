package android.service.dreams;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.provider.Settings.Secure;
import android.service.dreams.IDreamManager.Stub;
import android.util.Slog;
import com.android.internal.R;
import com.android.internal.os.SMProviderContract;

public final class Sandman {
    private static final ComponentName SOMNAMBULATOR_COMPONENT = new ComponentName("com.android.systemui", "com.android.systemui.Somnambulator");
    private static final String TAG = "Sandman";

    private Sandman() {
    }

    public static boolean shouldStartDockApp(Context context, Intent intent) {
        ComponentName name = intent.resolveActivity(context.getPackageManager());
        return (name == null || name.equals(SOMNAMBULATOR_COMPONENT)) ? false : true;
    }

    public static void startDreamByUserRequest(Context context) {
        startDream(context, false);
    }

    public static void startDreamWhenDockedIfAppropriate(Context context) {
        if (isScreenSaverEnabled(context) && isScreenSaverActivatedOnDock(context)) {
            startDream(context, true);
        } else {
            Slog.i(TAG, "Dreams currently disabled for docks.");
        }
    }

    private static void startDream(Context context, boolean docked) {
        try {
            IDreamManager dreamManagerService = Stub.asInterface(ServiceManager.getService(DreamService.DREAM_SERVICE));
            if (dreamManagerService != null && !dreamManagerService.isDreaming()) {
                if (docked) {
                    Slog.i(TAG, "Activating dream while docked.");
                    ((PowerManager) context.getSystemService(SMProviderContract.KEY_POWER)).wakeUp(SystemClock.uptimeMillis(), "android.service.dreams:DREAM");
                } else {
                    Slog.i(TAG, "Activating dream by user request.");
                }
                dreamManagerService.dream();
            }
        } catch (RemoteException ex) {
            Slog.e(TAG, "Could not start dream when docked.", ex);
        }
    }

    private static boolean isScreenSaverEnabled(Context context) {
        int def;
        if (context.getResources().getBoolean(R.bool.config_dreamsEnabledByDefault)) {
            def = 1;
        } else {
            def = 0;
        }
        return Secure.getIntForUser(context.getContentResolver(), "screensaver_enabled", def, -2) != 0;
    }

    private static boolean isScreenSaverActivatedOnDock(Context context) {
        int def;
        if (context.getResources().getBoolean(R.bool.config_dreamsActivatedOnDockByDefault)) {
            def = 1;
        } else {
            def = 0;
        }
        return Secure.getIntForUser(context.getContentResolver(), "screensaver_activate_on_dock", def, -2) != 0;
    }
}
