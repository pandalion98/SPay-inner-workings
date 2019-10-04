/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Process
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Thread$UncaughtExceptionHandler
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.core;

import android.content.Intent;
import android.os.Process;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.retry.RetryRequestData;
import com.samsung.android.spayfw.core.retry.d;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;

public class n
implements Thread.UncaughtExceptionHandler {
    private void a(Throwable throwable) {
        ErrorReport errorReport = new ErrorReport();
        errorReport.setSeverity("FATAL");
        errorReport.setCode("ERROR-30000");
        errorReport.setDescription(c.getStackTraceString(throwable));
        ReportData reportData = new ReportData(errorReport);
        d.a(reportData, new RetryRequestData(reportData, "credit/vi"));
    }

    public void uncaughtException(Thread thread, Throwable throwable) {
        c.e("SpayUncaughtExceptionHandler", "uncaughtException: classs = " + (Object)throwable.getClass() + ", uncaughtException = " + (Object)((Object)throwable));
        c.c("SpayUncaughtExceptionHandler", "StackTrace : ", throwable);
        Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
        intent.putExtra("notiType", "payFwCrashed");
        PaymentFrameworkApp.a(intent);
        this.a(throwable);
        Process.killProcess((int)Process.myPid());
        System.exit((int)1);
    }
}

