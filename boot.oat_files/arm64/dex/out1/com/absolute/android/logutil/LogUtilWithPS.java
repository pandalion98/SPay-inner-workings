package com.absolute.android.logutil;

import android.content.Context;
import com.absolute.android.persistence.ABTPersistenceManager;
import com.absolute.android.persistence.IABTPersistenceLog;
import com.absolute.android.utils.ExceptionUtil;

public class LogUtilWithPS extends LogUtilNoPS {
    static final /* synthetic */ boolean b = (!LogUtilWithPS.class.desiredAssertionStatus());
    private static final boolean d = false;
    private static final boolean e = true;
    private IABTPersistenceLog c;

    public void start(Context context) {
        try {
            ABTPersistenceManager aBTPersistenceManager = (ABTPersistenceManager) context.getSystemService(ABTPersistenceManager.ABT_PERSISTENCE_SERVICE);
            if (aBTPersistenceManager == null) {
                throw new Exception("LogUtilWithPS.ctor abtPersistMgr == null");
            }
            this.c = aBTPersistenceManager.getLog(ABTPersistenceManager.PERSISTENCE_SERVICE_LOG);
        } catch (Throwable e) {
            super.logMessage(6, "LogUtilwithPS.start: exception caught. Exception: " + ExceptionUtil.getExceptionMessage(e));
        }
    }

    public void logMessage(int i, String str) {
        Object obj = 1;
        super.logMessage(i, str);
        switch (i) {
            case 2:
            case 4:
            case 5:
            case 6:
                break;
            case 3:
                obj = null;
                break;
            default:
                obj = null;
                break;
        }
        if (obj != null) {
            try {
                String str2 = "Unknown";
                StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                if (b || stackTrace.length > 3) {
                    if (stackTrace.length > 3) {
                        str2 = stackTrace[3].getClassName() + "." + stackTrace[3].getMethodName();
                    }
                    this.c.logMessage(i, str2, str);
                    return;
                }
                throw new AssertionError();
            } catch (Throwable e) {
                super.logMessage(6, "LogUtilwithPS.logMessage: exception caught. Exception: " + ExceptionUtil.getExceptionMessage(e));
            }
        }
    }
}
