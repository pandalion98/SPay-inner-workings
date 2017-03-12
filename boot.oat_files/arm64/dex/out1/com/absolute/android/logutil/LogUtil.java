package com.absolute.android.logutil;

import android.content.Context;
import android.util.Log;
import com.absolute.android.persistence.ABTPersistenceManager;

public abstract class LogUtil {
    public static final boolean LOG_DEBUG = false;
    public static final boolean LOG_INFO = true;
    public static final String LOG_TAG = "APS";
    static String a = LOG_TAG;
    private static LogUtil b = null;

    public abstract void logMessage(int i, String str);

    public abstract void start(Context context);

    public static void init(Context context, String str) {
        try {
            a = str;
            if (b == null) {
                Object obj;
                try {
                    if (context.getSystemService(ABTPersistenceManager.ABT_PERSISTENCE_SERVICE) == null) {
                        throw new Exception("LogUtil.init: abtPersistMgr == null");
                    }
                    String str2;
                    obj = 1;
                    if (obj != null) {
                        str2 = "com.absolute.android.logutil.LogUtilWithPS";
                    } else {
                        str2 = "com.absolute.android.logutil.LogUtilNoPS";
                    }
                    LogUtil logUtil = (LogUtil) Class.forName(str2).asSubclass(LogUtil.class).newInstance();
                    b = logUtil;
                    logUtil.start(context);
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Exception caught getting PSMgr: " + e.getMessage());
                    obj = null;
                }
            }
        } catch (Throwable e2) {
            throw new IllegalStateException(e2);
        } catch (Exception e3) {
        }
    }

    public static LogUtil get() {
        if (b == null) {
            try {
                b = (LogUtil) Class.forName("com.absolute.android.common.logutil.LogUtilNoPS").asSubclass(LogUtil.class).newInstance();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
        return b;
    }
}
