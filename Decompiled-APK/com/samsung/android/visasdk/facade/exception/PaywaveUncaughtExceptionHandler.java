package com.samsung.android.visasdk.facade.exception;

import android.os.Process;
import android.util.Log;
import java.lang.Thread.UncaughtExceptionHandler;

public class PaywaveUncaughtExceptionHandler implements UncaughtExceptionHandler {
    private static final String TAG = "PAYWAVE";

    public void uncaughtException(Thread thread, Throwable th) {
        Log.d(TAG, "uncaughtException: classs = " + th.getClass() + ", uncaughtException = " + th);
        th.printStackTrace();
        Process.killProcess(Process.myPid());
        System.exit(1);
    }
}
