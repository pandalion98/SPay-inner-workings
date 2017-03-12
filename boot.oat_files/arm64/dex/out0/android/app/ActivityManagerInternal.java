package android.app;

import android.content.ComponentName;

public abstract class ActivityManagerInternal {

    public static abstract class SleepToken {
        public abstract void release();
    }

    public abstract SleepToken acquireSleepToken(String str);

    public abstract ComponentName getHomeActivityForUser(int i);

    public abstract void handleSContextChanged(int i, int i2);

    public abstract void onMultipleScreenStateChanged(int i, int i2);

    public abstract void onScreenStateChanged(int i, int i2);

    public abstract void onWakefulnessChanged(int i);

    public abstract int startIsolatedProcess(String str, String[] strArr, String str2, String str3, int i, Runnable runnable);
}
