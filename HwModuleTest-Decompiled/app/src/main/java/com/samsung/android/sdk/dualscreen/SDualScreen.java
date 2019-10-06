package com.samsung.android.sdk.dualscreen;

import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.os.Build;
import com.samsung.android.sdk.SsdkInterface;
import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.SsdkVendorCheck;
import com.samsung.android.sdk.dualscreen.SDualScreenConstantsReflector.PackageManager;

public final class SDualScreen implements SsdkInterface {
    public static final int DUALSCREEN_BASIC_FEATURE = 1;
    static final int DUALSCREEN_SDK_VERSION_CODE = 3;
    static final String DUALSCREEN_SDK_VERSION_NAME = "0.3";
    private static final String TAG = SDualScreen.class.getSimpleName();
    private static boolean enableQueried = false;
    private static boolean isDualScreenEnabled = false;

    public SDualScreen() {
        initDualScreenFeature();
    }

    public void initialize(Context arg0) throws SsdkUnsupportedException {
        if (!SsdkVendorCheck.isSamsungDevice()) {
            StringBuilder sb = new StringBuilder();
            sb.append(Build.BRAND);
            sb.append(" is not supported.");
            throw new SsdkUnsupportedException(sb.toString(), 0);
        } else if (!isDualScreenEnabled) {
            throw new SsdkUnsupportedException("The device is not supported.", 1);
        }
    }

    public boolean isFeatureEnabled(int feature) {
        if (feature != 1) {
            return false;
        }
        return isDualScreenEnabled;
    }

    public int getVersionCode() {
        return 3;
    }

    public String getVersionName() {
        return DUALSCREEN_SDK_VERSION_NAME;
    }

    private void initDualScreenFeature() {
        try {
            if (!enableQueried) {
                enableQueried = true;
                IPackageManager pm = ActivityThread.getPackageManager();
                if (pm != null) {
                    isDualScreenEnabled = pm.hasSystemFeature(PackageManager.FEATURE_DUALSCREEN);
                }
            }
        } catch (Exception e) {
        }
    }
}
