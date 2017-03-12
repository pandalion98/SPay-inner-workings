package com.samsung.android.multiwindow;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.service.notification.ZenModeConfig;
import android.util.Singleton;
import android.view.WindowManagerPolicy;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiWindowApplicationInfos extends Singleton<MultiWindowApplicationInfos> {
    private static final boolean SUPPORTEDSCALE_ALL_APPLICATIONS = false;
    private static final boolean SUPPORTED_ALL_APPLICATIONS = false;
    static MultiWindowApplicationInfos sInstance = new MultiWindowApplicationInfos();
    ArrayList<String> mAvoidInheritStackAppList = new ArrayList();
    ArrayList<String> mAvoidLaunchStyleAppList = new ArrayList();
    boolean mExtendScaleAppList = false;
    ArrayList<String> mHideAppList = new ArrayList();
    int mMaxPenWindowCount = -1;
    ArrayList<String> mNoTitleActivityList = new ArrayList();
    ArrayList<String> mNotSupportScaleAppList = new ArrayList();
    ArrayList<String> mShouldNotBeLaunchedInMultiWindowStyleActivityList = new ArrayList();
    ArrayList<String> mSupportAppList = new ArrayList();
    ArrayList<String> mSupportComponentList = new ArrayList();
    ArrayList<String> mSupportMultiInstanceAppList = new ArrayList();
    ArrayList<String> mSupportPackageList = new ArrayList();
    ArrayList<String> mSupportScaleAppList = new ArrayList();
    Resources mSystemResources = Resources.getSystem();

    protected MultiWindowApplicationInfos create() {
        return new MultiWindowApplicationInfos();
    }

    public static MultiWindowApplicationInfos getInstance() {
        return (MultiWindowApplicationInfos) sInstance.get();
    }

    private MultiWindowApplicationInfos() {
        if (this.mSystemResources == null) {
            throw new RuntimeException("System Resources is not ready.");
        }
        initConfig();
    }

    public void initConfig() {
        loadStringArray(R.array.config_multiWindowSupportAppList, this.mSupportAppList);
        loadStringArray(R.array.config_pen_window_applist, this.mSupportScaleAppList);
        loadStringArray(R.array.config_multiInstanceSupportAppList, this.mSupportMultiInstanceAppList);
        loadStringArray(R.array.config_multiWindowAvoidLaunchStyle, this.mAvoidLaunchStyleAppList);
        loadStringArray(R.array.config_multiWindowAvoidInheritStack, this.mAvoidInheritStackAppList);
        loadStringArray(R.array.config_multiWindowNotSupportScaleApp, this.mNotSupportScaleAppList);
        loadStringArray(R.array.config_multiWindowSupportPackageList, this.mSupportPackageList);
        loadStringArray(R.array.config_multiWindowSupportComponentList, this.mSupportComponentList);
        loadStringArray(R.array.config_multiWindowHideFlashBarAppList, this.mHideAppList);
        loadStringArray(R.array.config_penWindowNoTitleActivityList, this.mNoTitleActivityList);
        loadStringArray(R.array.config_shouldNotBeLaunchedInMultiWindowStyleActivityList, this.mShouldNotBeLaunchedInMultiWindowStyleActivityList);
        this.mExtendScaleAppList = this.mSystemResources.getBoolean(R.bool.config_multiWindowExtendScaleAppList);
        this.mMaxPenWindowCount = this.mSystemResources.getInteger(R.integer.config_maxPenWindowCount);
        loadCscAppList();
    }

    private void loadStringArray(int resId, ArrayList<String> outArray) {
        outArray.clear();
        String[] strings = this.mSystemResources.getStringArray(resId);
        if (strings != null) {
            for (String string : strings) {
                outArray.add(string);
            }
        }
    }

    public void loadCscAppList() {
    }

    public boolean isSupportApp(String packageName) {
        if (this.mSupportAppList.contains(packageName) || ZenModeConfig.SYSTEM_AUTHORITY.equals(packageName)) {
            return true;
        }
        return false;
    }

    public boolean isSupportPopup(String packageName) {
        if ("com.sec.android.app.popupuireceiver".equals(packageName) || "com.android.providers.media".equals(packageName) || "com.android.providers.downloads.ui".equals(packageName)) {
            return true;
        }
        return false;
    }

    public boolean isSupportFullScreenMinimizable(ActivityInfo activityInfo) {
        if (activityInfo == null || activityInfo.metaData == null) {
            return false;
        }
        return activityInfo.metaData.getBoolean("com.sec.android.multiwindow.fullscreen_minimizable");
    }

    public boolean isSupportScaleApp(ActivityInfo activityInfo) {
        return isSupportScaleApp(activityInfo, null);
    }

    public boolean isSupportScaleApp(ActivityInfo activityInfo, Context context) {
        if (activityInfo == null || activityInfo.applicationInfo == null || isHideAppList(activityInfo.packageName)) {
            return false;
        }
        if (activityInfo.applicationInfo.metaData != null && activityInfo.applicationInfo.metaData.getBoolean("com.samsung.android.sdk.multiwindowonly.enable")) {
            return false;
        }
        if (this.mExtendScaleAppList && isSupportMultiWindow(activityInfo) && (activityInfo.applicationInfo.flags & 1) != 0) {
            if (this.mNotSupportScaleAppList.contains(activityInfo.packageName)) {
                return false;
            }
            return true;
        } else if (ZenModeConfig.SYSTEM_AUTHORITY.equals(activityInfo.packageName)) {
            return true;
        } else {
            if (this.mSupportScaleAppList.contains(activityInfo.applicationInfo.packageName) || (activityInfo.applicationInfo.metaData != null && activityInfo.applicationInfo.metaData.getBoolean("com.samsung.android.sdk.multiwindow.penwindow.enable"))) {
                if (activityInfo.applicationInfo.metaData != null) {
                    String applicationStyle = activityInfo.applicationInfo.metaData.getString("com.sec.android.multiwindow.STYLE");
                    if (applicationStyle != null && new ArrayList(Arrays.asList(applicationStyle.split("\\|"))).contains(WindowManagerPolicy.WINDOW_STYLE_FREESTYLE_ONLY)) {
                        return false;
                    }
                }
                return true;
            } else if (MultiWindowFeatures.isSupportRecentUI(context) && isSupportMultiWindow(activityInfo)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isPenWindowOnly(ActivityInfo aInfo) {
        if (aInfo == null || isSupportApp(aInfo.packageName) || aInfo.metaData == null || !aInfo.metaData.getBoolean("com.samsung.android.sdk.multiwindow.penwindow.enable")) {
            return false;
        }
        return true;
    }

    public boolean isPenWindowOnlyApp(ApplicationInfo aInfo) {
        if (aInfo == null || isSupportApp(aInfo.packageName) || aInfo.metaData == null || !aInfo.metaData.getBoolean("com.samsung.android.sdk.multiwindow.penwindow.enable")) {
            return false;
        }
        return true;
    }

    public boolean isSupportMultiWindow(ActivityInfo aInfo) {
        if (aInfo == null || (!isSupportApp(aInfo.packageName) && (aInfo.applicationInfo == null || aInfo.applicationInfo.metaData == null || (!aInfo.applicationInfo.metaData.getBoolean("com.sec.android.support.multiwindow") && !aInfo.applicationInfo.metaData.getBoolean("com.samsung.android.sdk.multiwindow.enable") && !aInfo.applicationInfo.metaData.getBoolean("com.samsung.android.sdk.multiwindowonly.enable"))))) {
            return false;
        }
        return true;
    }

    public boolean isSupporMultiInstance(ActivityInfo activityInfo) {
        if (activityInfo == null || activityInfo.applicationInfo == null) {
            return false;
        }
        if (this.mSupportMultiInstanceAppList.contains(activityInfo.applicationInfo.packageName) || (activityInfo.applicationInfo.metaData != null && activityInfo.applicationInfo.metaData.getBoolean("com.samsung.android.sdk.multiwindow.multiinstance.enable"))) {
            return true;
        }
        return false;
    }

    public boolean isSupportPopupApp(ActivityInfo activityInfo) {
        if (activityInfo == null || activityInfo.applicationInfo == null || activityInfo.applicationInfo.metaData == null || !activityInfo.applicationInfo.metaData.getBoolean("com.samsung.android.sdk.multiwindow.popupwindow.enable")) {
            return false;
        }
        return true;
    }

    public boolean isPopupLaunchApp(ActivityInfo activityInfo) {
        if (activityInfo == null || activityInfo.applicationInfo == null || activityInfo.applicationInfo.metaData == null || !activityInfo.applicationInfo.metaData.getBoolean("com.sec.android.multiwindow.LAUNCH_POPUPWINDOW")) {
            return false;
        }
        return true;
    }

    public boolean isHideAppList(String packageName) {
        if (this.mHideAppList.contains(packageName)) {
            return true;
        }
        return false;
    }

    public boolean isSupportPackageList(String packageName) {
        if (this.mSupportPackageList.contains(packageName)) {
            return true;
        }
        return false;
    }

    public boolean isSupportComponentList(String packageName) {
        if (this.mSupportComponentList.contains(packageName)) {
            return true;
        }
        return false;
    }

    public boolean isNoTitleActivityList(String packageName) {
        if (this.mNoTitleActivityList.contains(packageName)) {
            return true;
        }
        return false;
    }

    public boolean isFixedRatioApp(String packageName) {
        return false;
    }

    public boolean isAvoidLaunchStyleApp(String packageName) {
        if (packageName != null && this.mAvoidLaunchStyleAppList.contains(packageName)) {
            return true;
        }
        return false;
    }

    public boolean isAvoidInheritStack(String packageName, String callerPackageName) {
        if (packageName == null || callerPackageName == null || callerPackageName.equals(packageName) || !this.mAvoidInheritStackAppList.contains(packageName)) {
            return false;
        }
        return true;
    }

    public boolean isAvoidScaleOption(String packageName) {
        if (packageName != null && this.mNotSupportScaleAppList.contains(packageName)) {
            return true;
        }
        return false;
    }

    public int getMaxPenWindow(Context context) {
        return this.mMaxPenWindowCount;
    }

    public boolean shouldNotBeLaunchedInMultiWindowStyle(String shortComponentName) {
        if (shortComponentName != null && this.mShouldNotBeLaunchedInMultiWindowStyleActivityList.contains(shortComponentName)) {
            return true;
        }
        return false;
    }
}
