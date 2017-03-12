package com.samsung.android.spayfw.payprovider.plcc.util;

import android.util.Log;

public final class LogHelper {
    private static final String PLCC_SDK = "LOOP";

    public static final void m1079v(String str) {
        Log.v(PLCC_SDK, str);
    }

    public static final void m1080v(String str, String str2) {
        Log.v("LOOP " + str, str2);
    }

    public static final void m1077i(String str) {
        Log.i(PLCC_SDK, str);
    }

    public static final void m1078i(String str, String str2) {
        Log.i("LOOP " + str, str2);
    }

    public static final void m1072d(String str) {
        Log.d(PLCC_SDK, str);
    }

    public static final void m1073d(String str, String str2) {
        Log.d("LOOP " + str, str2);
    }

    public static final void m1081w(String str) {
        Log.w(PLCC_SDK, str);
    }

    public static final void m1082w(String str, String str2) {
        Log.w("LOOP " + str, str2);
    }

    public static final void m1075e(String str) {
        Log.e(PLCC_SDK, str);
    }

    public static final void m1074e(Exception exception) {
        Log.e(PLCC_SDK, exception.getMessage());
    }

    public static final void m1076e(String str, String str2) {
        Log.e("LOOP " + str, str2);
    }
}
