/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.plcc.util;

import android.util.Log;

public final class LogHelper {
    private static final String PLCC_SDK = "LOOP";

    public static final void d(String string) {
        Log.d((String)PLCC_SDK, (String)string);
    }

    public static final void d(String string, String string2) {
        Log.d((String)("LOOP " + string), (String)string2);
    }

    public static final void e(Exception exception) {
        Log.e((String)PLCC_SDK, (String)exception.getMessage());
    }

    public static final void e(String string) {
        Log.e((String)PLCC_SDK, (String)string);
    }

    public static final void e(String string, String string2) {
        Log.e((String)("LOOP " + string), (String)string2);
    }

    public static final void i(String string) {
        Log.i((String)PLCC_SDK, (String)string);
    }

    public static final void i(String string, String string2) {
        Log.i((String)("LOOP " + string), (String)string2);
    }

    public static final void v(String string) {
        Log.v((String)PLCC_SDK, (String)string);
    }

    public static final void v(String string, String string2) {
        Log.v((String)("LOOP " + string), (String)string2);
    }

    public static final void w(String string) {
        Log.w((String)PLCC_SDK, (String)string);
    }

    public static final void w(String string, String string2) {
        Log.w((String)("LOOP " + string), (String)string2);
    }
}

