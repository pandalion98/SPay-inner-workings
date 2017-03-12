package com.samsung.android.secretmode;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.util.Log;
import com.samsung.android.smartface.SmartFaceManager;

public class SecretModeManager {
    @Deprecated
    public static final String ACTION_CHANGE_PERSONAL_PAGE_MODE = "com.samsung.android.intent.action.CHANGE_PERSONAL_PAGE_MODE";
    @Deprecated
    public static final String ACTION_CHANGE_SECRET_MODE = "com.samsung.android.intent.action.CHANGE_SECRET_MODE";
    @Deprecated
    public static final String ACTION_PERSONAL_PAGE_OFF = "com.samsung.android.intent.action.PERSONAL_PAGE_OFF";
    @Deprecated
    public static final String ACTION_PERSONAL_PAGE_ON = "com.samsung.android.intent.action.PERSONAL_PAGE_ON";
    public static final String ACTION_PRIVATE_MODE_OFF = "com.samsung.android.intent.action.PRIVATE_MODE_OFF";
    public static final String ACTION_PRIVATE_MODE_ON = "com.samsung.android.intent.action.PRIVATE_MODE_ON";
    @Deprecated
    public static final String ACTION_SECRET_MODE_OFF = "com.samsung.android.intent.action.SECRET_MODE_OFF";
    @Deprecated
    public static final String ACTION_SECRET_MODE_ON = "com.samsung.android.intent.action.SECRET_MODE_ON";
    public static final String ACTION_SHOW_VISUAL_CUE = "com.samsung.android.personalpage.action.SHOW_VISUAL_CUE";
    private static final boolean DEBUG;
    public static final String FAIL_PENDING_INTENT = "FailPendingIntent";
    public static final String PENDING_INTENT_DATA = "PendingIntentData";
    private static final String PERSONAL_PAGE_PATH = "/storage/Private";
    public static final String PROPERTY_KEY_PERSONAL_PAGE_MODE = "sys.samsung.personalpage.mode";
    @Deprecated
    public static final String PROPERTY_KEY_SECRET_MODE = "sys.samsung.personalpage.mode";
    private static final ComponentName SECRETMODE_SERVICE = new ComponentName("com.samsung.android.personalpage.service", "com.samsung.android.personalpage.service.PersonalPageService");
    public static final String SUCCESS_PENDING_INTENT = "SuccessPendingIntent";
    private static final String TAG = "SecretModeManager";

    static {
        boolean z;
        if (Debug.isProductShip() == 0) {
            z = true;
        } else {
            z = false;
        }
        DEBUG = z;
    }

    @Deprecated
    public static String getSecretDir(Context context) {
        return PERSONAL_PAGE_PATH;
    }

    public static String getPersonalPageRoot(Context context) {
        return PERSONAL_PAGE_PATH;
    }

    @Deprecated
    public static boolean isSecretDirMounted(Context context) {
        return isPersonalPageMounted(context);
    }

    public static boolean isPersonalPageMounted(Context context) {
        boolean isMouted = false;
        if (context == null) {
            Log.i(TAG, "isPersonalPageMounted: context is null");
            return false;
        }
        StorageManager mStorageMgr = (StorageManager) context.getSystemService("storage");
        if (DEBUG) {
            Log.i(TAG, "isPersonalPageMounted: " + context.getPackageName());
        }
        if (context.getPackageName().startsWith("sec_container_") || SmartFaceManager.PAGE_MIDDLE.equals(SystemProperties.get("sys.samsung.personalpage.mode", SmartFaceManager.PAGE_MIDDLE))) {
            return false;
        }
        if (mStorageMgr != null) {
            try {
                if ("mounted".equals(mStorageMgr.getVolumeState(PERSONAL_PAGE_PATH))) {
                    isMouted = true;
                }
            } catch (Exception e) {
                isMouted = false;
            }
        }
        Log.i(TAG, "isPersonalPageMounted result: " + isMouted);
        return isMouted;
    }

    @Deprecated
    public static boolean isSecretMode() {
        return isPersonalPageMode();
    }

    public static boolean isPersonalPageMode() {
        return SmartFaceManager.PAGE_BOTTOM.equals(SystemProperties.get("sys.samsung.personalpage.mode", SmartFaceManager.PAGE_MIDDLE));
    }

    public static boolean showVisualCue(Context context, PendingIntent success, PendingIntent fail) {
        Intent intent = new Intent(ACTION_SHOW_VISUAL_CUE);
        intent.setComponent(SECRETMODE_SERVICE);
        Bundle data = new Bundle();
        data.putParcelable(SUCCESS_PENDING_INTENT, success);
        data.putParcelable(FAIL_PENDING_INTENT, fail);
        intent.putExtra(PENDING_INTENT_DATA, data);
        context.startService(intent);
        return true;
    }
}
