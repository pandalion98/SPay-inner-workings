package android.os;

import android.app.IAlarmManager;
import android.app.IAlarmManager.Stub;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.util.Slog;

public final class SystemClock {
    private static final String TAG = "SystemClock";

    public static native long currentThreadTimeMicro();

    public static native long currentThreadTimeMillis();

    public static native long currentTimeMicro();

    public static native long elapsedRealtime();

    public static native long elapsedRealtimeNanos();

    public static native long uptimeMillis();

    private SystemClock() {
    }

    public static void sleep(long ms) {
        long start = uptimeMillis();
        long duration = ms;
        boolean interrupted = false;
        do {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                interrupted = true;
            }
            duration = (start + ms) - uptimeMillis();
        } while (duration > 0);
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    private static boolean isEDMDateTimePolicyEnabled() {
        if (Process.myUid() < 10000 || Process.myUid() > Process.LAST_APPLICATION_UID) {
            return true;
        }
        return EnterpriseDeviceManager.getInstance().getDateTimePolicy().isDateTimeChangeEnabled();
    }

    public static boolean setCurrentTimeMillis(long millis) {
        boolean z = false;
        if (isEDMDateTimePolicyEnabled()) {
            IAlarmManager mgr = Stub.asInterface(ServiceManager.getService("alarm"));
            if (mgr != null) {
                try {
                    z = mgr.setTime(millis);
                } catch (RemoteException e) {
                    Slog.e(TAG, "Unable to set RTC", e);
                } catch (SecurityException e2) {
                    Slog.e(TAG, "Unable to set RTC", e2);
                }
            }
        }
        return z;
    }
}
