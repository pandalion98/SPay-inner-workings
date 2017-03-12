package com.android.internal.app;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings$System;
import android.util.Log;
import com.android.internal.R;
import com.android.internal.telephony.SmsConstants;
import com.samsung.android.fingerprint.FingerprintManager;
import com.sec.android.app.CscFeature;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AppLockPolicy {
    public static final String FLOATING_MESSAGE_REQUEST = "FLOATING_MESSAGE_REQUEST";
    public static final String LAUNCHER_REQUEST = "LAUNCHER_REQUEST";
    public static final String LAUNCH_FROM_RESUME = "LAUNCH_FROM_RESUME";
    public static final String LAUNCH_FROM_SETTINGS = "APPLOCK_APPS_FROM_SETTINGS";
    public static final String LOCKED_PACKAGE_ICON = "LOCKED_PACKAGE_ICON";
    public static final String LOCKED_PACKAGE_INTENT = "LOCKED_PACKAGE_INTENT";
    public static final String LOCKED_PACKAGE_LABEL = "LOCKED_PACKAGE_LABEL";
    public static final String LOCKED_PACKAGE_MULTIWINDOWSTYLE = "LOCKED_PACKAGE_MULTIWINDOWSTYLE";
    public static final String LOCKED_PACKAGE_NAME = "LOCKED_PACKAGE_NAME";
    public static final String REQUEST_VERIFY_FROM = "REQUEST_VERIFY_FROM";
    private static String TAG = "AppLockPolicy";
    private static boolean isSupportAppLock = isSupportSmartManagerCHNEnhancedFeature("applock");
    private static AppLockPolicy mInstance;
    private final String LOCKED_CLASSES = "smartmanager_locked_apps_classes";
    private final String LOCKED_PACKAGE = "smartmanager_locked_apps_packages";
    private final String LOCKED_TYPE = Settings$System.DB_APPLOCK_LOCK_TYPE;
    private final String PASSWORD_TYPE = "password_type";
    private final String PATTERN_TYPE = "pattern_type";
    private ArrayList<String> mAppLockedClassList = new ArrayList();
    private ArrayList<String> mAppLockedHasUnLockedClassList = new ArrayList();
    private ArrayList<String> mAppLockedHasUnLockedPackageList = new ArrayList();
    private Object mAppLockedLock = new Object();
    private ArrayList<String> mAppLockedPackageList = new ArrayList();
    private HashMap<String, ArrayList<String>> mAppLockedRelatedClassMap = new HashMap();
    private HashMap<String, ArrayList<String>> mAppLockedRelatedPackageMap = new HashMap();
    private ArrayList<String> mAppLockedVerifyingList = new ArrayList();
    private Context mContext;
    private String mLockedType = null;
    private int mLockedTypeInt = 0;
    private SettingsObserver mSettingsObserver;

    class SettingsObserver extends ContentObserver {
        boolean isNotObserve = true;
        ContentResolver mSettingContentResolver;
        Context mSettingsObserverContext;

        public SettingsObserver(Context context, Handler handler) {
            super(handler);
            this.mSettingsObserverContext = context;
            this.mSettingContentResolver = this.mSettingsObserverContext.getContentResolver();
        }

        void observe() {
            this.isNotObserve = false;
            this.mSettingContentResolver.registerContentObserver(Settings$System.getUriFor("smartmanager_locked_apps_packages"), false, this, -1);
            this.mSettingContentResolver.registerContentObserver(Settings$System.getUriFor("smartmanager_locked_apps_classes"), false, this, -1);
            this.mSettingContentResolver.registerContentObserver(Settings$System.getUriFor(Settings$System.DB_APPLOCK_LOCK_TYPE), false, this, -1);
            getRelatedApps();
            getLockedApps();
        }

        void unObserve() {
            this.mSettingContentResolver.unregisterContentObserver(this);
        }

        public void onChange(boolean selfChange) {
            getLockedApps();
        }

        private void getRelatedApps() {
            int i;
            Resources res = this.mSettingsObserverContext.getResources();
            String[] relatedPackageArray = res.getStringArray(R.array.config_applock_locked_related_packages);
            if (relatedPackageArray != null && relatedPackageArray.length > 0) {
                for (String relatedPackages : relatedPackageArray) {
                    String[] packages = relatedPackages.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                    ArrayList<String> relatedPackageList = new ArrayList();
                    for (i = 0; i < packages.length; i++) {
                        relatedPackageList.add(packages[i]);
                        AppLockPolicy.this.mAppLockedRelatedPackageMap.put(packages[i], relatedPackageList);
                    }
                }
            }
            String[] relatedClassArray = res.getStringArray(R.array.config_applock_locked_related_classes);
            if (relatedClassArray != null && relatedClassArray.length > 0) {
                for (String relatedClasses : relatedClassArray) {
                    String[] classes = relatedClasses.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                    ArrayList<String> relatedClassList = new ArrayList();
                    for (i = 0; i < classes.length; i++) {
                        relatedClassList.add(classes[i]);
                        AppLockPolicy.this.mAppLockedRelatedClassMap.put(classes[i], relatedClassList);
                    }
                }
            }
        }

        private void getLockedApps() {
            String lockedPackages = Settings$System.getString(this.mSettingsObserverContext.getContentResolver(), "smartmanager_locked_apps_packages");
            String lockedClasses = Settings$System.getString(this.mSettingsObserverContext.getContentResolver(), "smartmanager_locked_apps_classes");
            AppLockPolicy.this.mLockedTypeInt = Settings$System.getInt(this.mSettingsObserverContext.getContentResolver(), Settings$System.DB_APPLOCK_LOCK_TYPE, 0);
            switch (AppLockPolicy.this.mLockedTypeInt) {
                case 1:
                    AppLockPolicy.this.mLockedType = "pattern_type";
                    break;
                case 2:
                    AppLockPolicy.this.mLockedType = "pincode_type";
                    break;
                case 3:
                    AppLockPolicy.this.mLockedType = "password_type";
                    break;
                case 4:
                    AppLockPolicy.this.mLockedType = "fingerprint_type";
                    break;
                case 5:
                    AppLockPolicy.this.mLockedType = "fingerprint_pattern_type";
                    break;
                case 6:
                    AppLockPolicy.this.mLockedType = "fingerprint_pincode_type";
                    break;
                case 7:
                    AppLockPolicy.this.mLockedType = "fingerprint_password_type";
                    break;
                default:
                    AppLockPolicy.this.mLockedType = null;
                    break;
            }
            synchronized (AppLockPolicy.this.mAppLockedLock) {
                ArrayList<String> list;
                if (lockedPackages != null) {
                    String[] lockedPackageArray = lockedPackages.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                    list = new ArrayList();
                    for (String packageName : lockedPackageArray) {
                        list.add(packageName);
                        if (AppLockPolicy.this.mAppLockedRelatedPackageMap.containsKey(packageName)) {
                            for (String relatedPackage : (List) AppLockPolicy.this.mAppLockedRelatedPackageMap.get(packageName)) {
                                if (!list.contains(relatedPackage)) {
                                    list.add(relatedPackage);
                                }
                            }
                        }
                    }
                    AppLockPolicy.this.mAppLockedPackageList = list;
                }
                if (lockedClasses != null) {
                    String[] lockedClassArray = lockedClasses.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                    list = new ArrayList();
                    for (String className : lockedClassArray) {
                        list.add(className);
                        if (AppLockPolicy.this.mAppLockedRelatedClassMap.containsKey(className)) {
                            for (String relatedClass : (List) AppLockPolicy.this.mAppLockedRelatedClassMap.get(className)) {
                                if (!list.contains(relatedClass)) {
                                    list.add(relatedClass);
                                }
                            }
                        }
                    }
                    AppLockPolicy.this.mAppLockedClassList = list;
                }
            }
        }
    }

    public static AppLockPolicy getInstance(Context context, Handler handler) {
        if (mInstance == null) {
            synchronized (AppLockPolicy.class) {
                if (mInstance == null) {
                    mInstance = new AppLockPolicy(context, handler);
                }
            }
        }
        return mInstance;
    }

    private AppLockPolicy(Context context, Handler handler) {
        this.mContext = context;
        this.mSettingsObserver = new SettingsObserver(this.mContext, handler);
        if (this.mSettingsObserver.isNotObserve) {
            this.mSettingsObserver.observe();
        }
    }

    public boolean isLockedActivity(Intent intent) {
        boolean isLocked = false;
        if (this.mSettingsObserver.isNotObserve) {
            this.mSettingsObserver.observe();
        }
        if (intent.getComponent() != null) {
            String className = intent.getComponent().getClassName();
            if (className != null) {
                synchronized (this.mAppLockedLock) {
                    Iterator i$ = this.mAppLockedClassList.iterator();
                    while (i$.hasNext()) {
                        if (((String) i$.next()).equals(className)) {
                            isLocked = true;
                            break;
                        }
                    }
                }
            }
        }
        return isLocked;
    }

    public boolean startCheckActivity(Intent blockedIntent, boolean isFromRecent) {
        if (this.mLockedType.equals(SmsConstants.FORMAT_UNKNOWN)) {
            Log.d(TAG, "startCheckActivity - unknown, skip!");
            return false;
        }
        Intent in = new Intent();
        if (this.mLockedType.equals("pattern_type")) {
            in.setAction("android.intent.action.CHECK_APPLOCK_PATTERN");
        } else if (this.mLockedType.equals("password_type")) {
            in.setAction("android.intent.action.CHECK_APPLOCK_PASSWORD");
        } else {
            in.setAction("android.intent.action.CHECK_APPLOCK_PASSWORD");
        }
        in.setFlags(1342177280);
        in.putExtra("APPLOCK_APPS_INTENT", blockedIntent);
        if (isFromRecent) {
            in.putExtra("APPLOCK_APPS_FROM_RECENTS", true);
        }
        try {
            this.mContext.startActivity(in);
            return true;
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, "CHECK_APPLOCK_PASSWORD", e);
            return true;
        }
    }

    public String getAppLockedLockType() {
        return this.mLockedType;
    }

    public String getAppLockedCheckAction() {
        String checkAction = null;
        boolean hasEnrolledFingers = false;
        if (this.mLockedTypeInt >= 5 && this.mLockedTypeInt <= 7) {
            hasEnrolledFingers = FingerprintManager.getInstance(this.mContext, 1).getEnrolledFingers() > 0;
        }
        if ("pattern_type".equals(this.mLockedType)) {
            checkAction = "android.intent.action.CHECK_APPLOCK_PATTERN";
        } else if ("password_type".equals(this.mLockedType)) {
            checkAction = "android.intent.action.CHECK_APPLOCK_PASSWORD";
        } else if ("pincode_type".equals(this.mLockedType)) {
            checkAction = "android.intent.action.CHECK_APPLOCK_PINCODE";
        } else if ("fingerprint_type".equals(this.mLockedType)) {
            checkAction = "android.intent.action.CHECK_APPLOCK_FINGERPRINT";
        } else if ("fingerprint_pattern_type".equals(this.mLockedType)) {
            if (hasEnrolledFingers) {
                checkAction = "android.intent.action.CHECK_APPLOCK_FINGERPRINT_PATTERN";
            } else {
                checkAction = "android.intent.action.CHECK_APPLOCK_PATTERN";
            }
        } else if ("fingerprint_pincode_type".equals(this.mLockedType)) {
            if (hasEnrolledFingers) {
                checkAction = "android.intent.action.CHECK_APPLOCK_FINGERPRINT_PINCODE";
            } else {
                checkAction = "android.intent.action.CHECK_APPLOCK_PINCODE";
            }
        } else if ("fingerprint_password_type".equals(this.mLockedType)) {
            if (hasEnrolledFingers) {
                checkAction = "android.intent.action.CHECK_APPLOCK_FINGERPRINT_PASSWORD";
            } else {
                checkAction = "android.intent.action.CHECK_APPLOCK_PASSWORD";
            }
        }
        Log.d(TAG, "getAppLockedCheckAction:" + checkAction);
        return checkAction;
    }

    public boolean isLockedPackage(Intent intent) {
        boolean isLocked = false;
        if (this.mSettingsObserver.isNotObserve) {
            this.mSettingsObserver.observe();
        }
        if (intent.getComponent() != null) {
            String packageName = intent.getComponent().getPackageName();
            synchronized (this.mAppLockedLock) {
                if (packageName != null) {
                    Iterator i$ = this.mAppLockedPackageList.iterator();
                    while (i$.hasNext()) {
                        if (((String) i$.next()).equals(packageName)) {
                            isLocked = true;
                            break;
                        }
                    }
                }
            }
        }
        return isLocked;
    }

    public ArrayList<String> getAppLockedPackageList() {
        if (this.mSettingsObserver.isNotObserve) {
            this.mSettingsObserver.observe();
        }
        return new ArrayList(this.mAppLockedPackageList);
    }

    public void setAppLockedUnLockPackage(String packageName) {
        synchronized (this.mAppLockedLock) {
            if (!this.mAppLockedHasUnLockedPackageList.contains(packageName)) {
                this.mAppLockedHasUnLockedPackageList.add(packageName);
                if (this.mAppLockedRelatedPackageMap.containsKey(packageName)) {
                    for (String relatedPackage : (List) this.mAppLockedRelatedPackageMap.get(packageName)) {
                        if (!this.mAppLockedHasUnLockedPackageList.contains(relatedPackage)) {
                            this.mAppLockedHasUnLockedPackageList.add(relatedPackage);
                        }
                    }
                }
            }
        }
    }

    public void clearAppLockedUnLockedApp() {
        synchronized (this.mAppLockedLock) {
            this.mAppLockedHasUnLockedPackageList.clear();
            this.mAppLockedHasUnLockedClassList.clear();
            this.mAppLockedVerifyingList.clear();
        }
    }

    public boolean isAppLockedPackage(String packageName) {
        boolean z = false;
        if (this.mSettingsObserver.isNotObserve) {
            this.mSettingsObserver.observe();
        }
        synchronized (this.mAppLockedLock) {
            if (this.mAppLockedHasUnLockedPackageList.contains(packageName)) {
            } else if (this.mAppLockedPackageList.contains(packageName)) {
                z = true;
            }
        }
        return z;
    }

    public ArrayList<String> getAppLockedClassList() {
        if (this.mSettingsObserver.isNotObserve) {
            this.mSettingsObserver.observe();
        }
        ArrayList<String> lockedist = new ArrayList(this.mAppLockedClassList);
        synchronized (this.mAppLockedLock) {
            Iterator i$ = this.mAppLockedHasUnLockedClassList.iterator();
            while (i$.hasNext()) {
                lockedist.remove((String) i$.next());
            }
        }
        return lockedist;
    }

    public void setAppLockedUnLockClass(String className) {
        synchronized (this.mAppLockedLock) {
            if (!this.mAppLockedHasUnLockedClassList.contains(className)) {
                this.mAppLockedHasUnLockedClassList.add(className);
                if (this.mAppLockedRelatedClassMap.containsKey(className)) {
                    for (String relatedClass : (List) this.mAppLockedRelatedClassMap.get(className)) {
                        if (!this.mAppLockedHasUnLockedClassList.contains(relatedClass)) {
                            this.mAppLockedHasUnLockedClassList.add(relatedClass);
                        }
                    }
                }
            }
        }
    }

    public boolean isAppLockedClass(String packageName) {
        boolean z = false;
        if (this.mSettingsObserver.isNotObserve) {
            this.mSettingsObserver.observe();
        }
        synchronized (this.mAppLockedLock) {
            if (this.mAppLockedHasUnLockedClassList.contains(packageName)) {
            } else if (this.mAppLockedClassList.contains(packageName)) {
                z = true;
            }
        }
        return z;
    }

    public void setAppLockedVerifying(String packageName, boolean verifying) {
        synchronized (this.mAppLockedLock) {
            if (verifying) {
                if (!this.mAppLockedVerifyingList.contains(packageName)) {
                    this.mAppLockedVerifyingList.add(packageName);
                }
            } else if (this.mAppLockedVerifyingList.contains(packageName)) {
                this.mAppLockedVerifyingList.remove(packageName);
            }
        }
    }

    public boolean isAppLockedVerifying(String packageName) {
        synchronized (this.mAppLockedLock) {
            if (this.mAppLockedVerifyingList.contains(packageName)) {
                return true;
            }
            return false;
        }
    }

    public boolean dumpAppLockPolicyLocked(FileDescriptor fd, PrintWriter pw) {
        pw.print("AppLockPolicy dump start");
        pw.println();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LockedPackage[");
        Iterator i$ = this.mAppLockedPackageList.iterator();
        while (i$.hasNext()) {
            stringBuilder.append((String) i$.next());
            stringBuilder.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
        }
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("]\n");
        stringBuilder.append("LockedClass[");
        i$ = this.mAppLockedClassList.iterator();
        while (i$.hasNext()) {
            stringBuilder.append((String) i$.next());
            stringBuilder.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
        }
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("]\n");
        stringBuilder.append("HasUnLockedPackage[");
        i$ = this.mAppLockedHasUnLockedPackageList.iterator();
        while (i$.hasNext()) {
            stringBuilder.append((String) i$.next());
            stringBuilder.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
        }
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("]\n");
        stringBuilder.append("HasUnLockedClass[");
        i$ = this.mAppLockedHasUnLockedClassList.iterator();
        while (i$.hasNext()) {
            stringBuilder.append((String) i$.next());
            stringBuilder.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
        }
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("]\n");
        stringBuilder.append("mAppLockedVerifyingList[");
        i$ = this.mAppLockedVerifyingList.iterator();
        while (i$.hasNext()) {
            stringBuilder.append((String) i$.next());
            stringBuilder.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
        }
        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        stringBuilder.append("]\n");
        pw.print(stringBuilder.toString());
        pw.print("AppLockPolicy dump end");
        pw.println();
        return true;
    }

    public static boolean isSupportAppLock() {
        return isSupportAppLock;
    }

    private static boolean isSupportSmartManagerCHNEnhancedFeature(String featureName) {
        String features = CscFeature.getInstance().getString("CscFeature_SmartManager_ConfigSubFeatures");
        return features != null && features.contains(featureName);
    }
}
