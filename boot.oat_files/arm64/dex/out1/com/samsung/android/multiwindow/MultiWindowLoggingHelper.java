package com.samsung.android.multiwindow;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.PersonaManager;
import android.os.UserHandle;
import android.provider.SyncStateContract.Columns;

public class MultiWindowLoggingHelper {
    private static final String APP_ID = "com.samsung.android.multiwindow";
    public static final String CHANGE_POPUPWINDOW_TYPE = "CHANGE-POPUP";
    public static final String CHANGE_SPLITWINDOW_TYPE = "CHANGE-SPLIT";
    public static final String CLOSE_TYPE = "CLOSE";
    public static final boolean DEBUG = false;
    public static final String DRAGCONTENT_TYPE = "DRAGCONTENT";
    public static final String GESTURE_LOGGING_FEATURE = "GEST";
    public static final String MAX_TYPE = "MAXIMIZE";
    public static final String MIN_TYPE = "MINIMIZE";
    public static final String POPUPWINDOW_LOGGING_FEATURE = "POPW";
    public static final String RECENT_LONGPRESS_FROM_HOME_LOGGING_FEATURE = "LPHM";
    public static final String RECENT_LONGPRESS_FROM_NORMAL_APP_LOGGING_FEATURE = "LPNA";
    public static final String RECENT_LONGPRESS_FROM_SPLIT_LOGGING_FEATURE = "LPSP";
    public static final String RECENT_LONGPRESS_LOGGING_FEATURE = "RCLP";
    public static final String RECENT_POPUP_LOGGING_FEATURE = "RCPO";
    public static final String RECENT_SPLIT_LOGGING_FEATURE = "RCSP";
    public static final String RECENT_STACKVIEW_LOGGING_FEATURE = "RCSV";
    public static final String RECENT_VIEWPAGER_LOGGING_FEATURE = "RCVP";
    public static final String REMOVE_MINIMIZE_ICON_FEATURE = "RMMI";
    public static final String SPLITWINDOW_LOGGING_FEATURE = "SPLW";
    public static final String SPLITWINDOW_PAIR_LOGGING_FEATURE = "PAIR";
    public static final String SWITCH_TYPE = "SWITCH";
    public static final String TAG = "MultiWindowLoggingHelper";
    public static final String TRAY_POPUP_LOGGING_FEATURE = "TRPO";
    public static final String TRAY_SPLIT_LOGGING_FEATURE = "TRSP";

    public enum LP_STATE {
        SPLIT_LAUNCH,
        NORMAL_APP_LAUNCH,
        NOT_SUPPORT,
        RETURN_TO_HOME,
        RETURN_TO_SPLIT
    }

    public static void insertLog(Context context, String feature, String extra) {
        if (!PersonaManager.isKnoxId(UserHandle.getCallingUserId())) {
            ContentValues cv = new ContentValues();
            cv.put("app_id", APP_ID);
            cv.put("feature", feature);
            if (extra != null) {
                cv.put("extra", extra);
            }
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY");
            broadcastIntent.putExtra(Columns.DATA, cv);
            broadcastIntent.setPackage("com.samsung.android.providers.context");
            context.sendBroadcast(broadcastIntent);
        }
    }

    public static void insertMultipleStatusLog(Context context, String[] features, String[] extras, long[] values) {
        if (!PersonaManager.isKnoxId(UserHandle.getCallingUserId())) {
            long origId = Binder.clearCallingIdentity();
            int j = 0;
            while (j < features.length && features[j] != null) {
                try {
                    j++;
                } finally {
                    Binder.restoreCallingIdentity(origId);
                }
            }
            ContentValues[] cvs = new ContentValues[j];
            for (int i = 0; i < j; i++) {
                cvs[i] = new ContentValues();
                cvs[i].put("app_id", APP_ID);
                cvs[i].put("feature", features[i]);
                cvs[i].put("extra", extras[i]);
                if (values != null) {
                    cvs[i].put("value", Long.valueOf(values[i]));
                }
            }
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.samsung.android.providers.context.log.action.USE_MULTI_APP_FEATURE_SURVEY");
            broadcastIntent.putExtra(Columns.DATA, cvs);
            broadcastIntent.setPackage("com.samsung.android.providers.context");
            context.sendBroadcast(broadcastIntent);
        }
    }
}
