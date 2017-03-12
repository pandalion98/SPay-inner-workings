package com.android.internal.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;

public class ActivityTrigger {
    private static final int FLAG_HARDWARE_ACCELERATED = 512;
    private static final int FLAG_OVERRIDE_RESOLUTION = 1;
    private static final String TAG = "ActivityTrigger";

    private native void native_at_deinit();

    private native void native_at_resumeActivity(String str);

    private native int native_at_startActivity(String str, int i);

    private native void native_at_startProcessActivity(String str, int i);

    protected void finalize() {
        native_at_deinit();
    }

    public void activityStartProcessTrigger(String process, int pid) {
        native_at_startProcessActivity(process, pid);
    }

    public void activityStartTrigger(Intent intent, ActivityInfo acInfo, ApplicationInfo appInfo) {
        ComponentName cn = intent.getComponent();
        String activity = null;
        if (cn != null) {
            activity = cn.flattenToString() + "/" + appInfo.versionCode;
        }
        int overrideFlags = native_at_startActivity(activity, 0);
        if ((overrideFlags & 512) != 0) {
            acInfo.flags |= 512;
        }
        if ((overrideFlags & 1) != 0) {
            appInfo.setOverrideRes(1);
        }
    }

    public void activityResumeTrigger(Intent intent, ActivityInfo acInfo, ApplicationInfo appInfo) {
        ComponentName cn = intent.getComponent();
        String activity = null;
        if (cn != null) {
            activity = cn.flattenToString() + "/" + appInfo.versionCode;
        }
        native_at_resumeActivity(activity);
    }
}
