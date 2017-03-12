package com.samsung.android.share;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SShareLogging {
    private static final boolean DEBUG = false;
    private static final String SURVERY_ACTION = "com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY";
    private static final String SURVERY_EXTRA_DATA = "data";
    private static final String SURVERY_PERMISSION = "com.samsung.android.providers.context.permission.WRITE_USE_APP_FEATURE_SURVEY";
    private static final String SURVEY_APP_NAME = "com.android.internal.app.resolveractivity";
    private static final String SURVEY_CONTENT_APPID = "app_id";
    private static final String SURVEY_CONTENT_EXTRA = "extra";
    private static final String SURVEY_CONTENT_FEATURE = "feature";
    public static final String SURVEY_DETAIL_FEATURE_CHANGEPLAYER = "Change Player";
    public static final String SURVEY_DETAIL_FEATURE_CONTACTGROUP = "group";
    public static final String SURVEY_DETAIL_FEATURE_CONTACTPRIV = "personal";
    public static final String SURVEY_DETAIL_FEATURE_MIRRORING = "Screen Mirroring";
    public static final String SURVEY_DETAIL_FEATURE_NEARBY_SHARING = "Nearby sharing";
    public static final String SURVEY_DETAIL_FEATURE_PRINT = "Print";
    public static final String SURVEY_DETAIL_FEATURE_SCREEN_SHARING = "Smart View";
    public static final String SURVEY_FEATURE_APPLIST = "APPP";
    public static final String SURVEY_FEATURE_EASYSHARE = "EASY";
    public static final String SURVEY_FEATURE_MOREACTION = "MORE";
    public static final String SURVEY_FEATURE_START = "STRT";
    private static final String SURVEY_TARGET_PACKAGE = "com.samsung.android.providers.context";
    private static final String TAG = "SShareLogging";
    private Context mContext;
    private Intent mIntent;

    public SShareLogging(Context context, Intent intent) {
        this.mContext = context;
        this.mIntent = intent;
    }

    private boolean checkSurveyCondition(Intent intent) {
        String action = intent.getAction();
        if ("android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action)) {
            return true;
        }
        return false;
    }

    public void insertLog(String feature, String extra) {
        if (!checkSurveyCondition(this.mIntent)) {
            return;
        }
        if (this.mContext.checkCallingOrSelfPermission(SURVERY_PERMISSION) == 0) {
            ContentValues cv = new ContentValues();
            cv.put(SURVEY_CONTENT_APPID, SURVEY_APP_NAME);
            cv.put(SURVEY_CONTENT_FEATURE, feature);
            cv.put(SURVEY_CONTENT_EXTRA, extra);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(SURVERY_ACTION);
            broadcastIntent.putExtra("data", cv);
            broadcastIntent.setPackage(SURVEY_TARGET_PACKAGE);
            this.mContext.sendBroadcast(broadcastIntent);
            return;
        }
        Log.w(TAG, "insertLog : no permission of survey");
    }
}
