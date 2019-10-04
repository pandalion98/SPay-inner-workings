/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Process
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Thread$UncaughtExceptionHandler
 *  java.lang.Throwable
 */
package com.samsung.android.visasdk.facade.exception;

import android.os.Process;
import android.util.Log;

public class PaywaveUncaughtExceptionHandler
implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "PAYWAVE";

    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.d((String)TAG, (String)("uncaughtException: classs = " + (Object)throwable.getClass() + ", uncaughtException = " + (Object)((Object)throwable)));
        throwable.printStackTrace();
        Process.killProcess((int)Process.myPid());
        System.exit((int)1);
    }
}

