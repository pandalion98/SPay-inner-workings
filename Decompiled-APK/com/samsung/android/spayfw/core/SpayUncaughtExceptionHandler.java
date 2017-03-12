package com.samsung.android.spayfw.core;

import android.content.Intent;
import android.os.Process;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.core.retry.RetryRequestData;
import com.samsung.android.spayfw.core.retry.RetryRequester;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;
import java.lang.Thread.UncaughtExceptionHandler;

/* renamed from: com.samsung.android.spayfw.core.n */
public class SpayUncaughtExceptionHandler implements UncaughtExceptionHandler {
    public void uncaughtException(Thread thread, Throwable th) {
        Log.m286e("SpayUncaughtExceptionHandler", "uncaughtException: classs = " + th.getClass() + ", uncaughtException = " + th);
        Log.m284c("SpayUncaughtExceptionHandler", "StackTrace : ", th);
        Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
        intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_PAY_FW_CRASHED);
        PaymentFrameworkApp.m315a(intent);
        m647a(th);
        Process.killProcess(Process.myPid());
        System.exit(1);
    }

    private void m647a(Throwable th) {
        ErrorReport errorReport = new ErrorReport();
        errorReport.setSeverity(ErrorReport.ERROR_SEVERITY_FATAL);
        errorReport.setCode(TokenReport.ERROR_TRUSTED_APP);
        errorReport.setDescription(Log.getStackTraceString(th));
        ReportData reportData = new ReportData(errorReport);
        RetryRequester.m670a(reportData, new RetryRequestData(reportData, "credit/vi"));
    }
}
