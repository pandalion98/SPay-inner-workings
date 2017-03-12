package android.app.usage;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;

public abstract class UsageStatsManagerInternal {

    public static abstract class AppIdleStateChangeListener {
        public abstract void onAppIdleStateChanged(String str, int i, boolean z);

        public abstract void onParoleStateChanged(boolean z);
    }

    public abstract void addAppIdleStateChangeListener(AppIdleStateChangeListener appIdleStateChangeListener);

    public abstract int[] getIdleUidsForUser(int i);

    public abstract boolean isAppIdle(String str, int i, int i2);

    public abstract boolean isAppIdleParoleOn();

    public abstract void prepareShutdown();

    public abstract void removeAppIdleStateChangeListener(AppIdleStateChangeListener appIdleStateChangeListener);

    public abstract void reportConfigurationChange(Configuration configuration, int i);

    public abstract void reportContentProviderUsage(String str, String str2, int i);

    public abstract void reportEvent(ComponentName componentName, Intent intent, int i, int i2);

    public abstract void reportEvent(String str, int i, int i2);
}
