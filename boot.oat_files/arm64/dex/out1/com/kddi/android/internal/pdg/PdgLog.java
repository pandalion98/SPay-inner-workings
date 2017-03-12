package com.kddi.android.internal.pdg;

import android.util.Log;

public final class PdgLog {
    private static final int LOG_LEVEL_D = 4;
    private static final int LOG_LEVEL_E = 1;
    private static final int LOG_LEVEL_I = 3;
    private static final int LOG_LEVEL_NONE = 0;
    private static final int LOG_LEVEL_V = 5;
    private static final int LOG_LEVEL_W = 2;
    private static final String TAG = "PDG";
    private static int mLevel = 0;

    private PdgLog() {
    }

    public static void e(String msg) {
        if (mLevel >= 1 && msg != null) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (msg != null) {
            e(msg + getStackTraceString(tr));
        }
    }

    public static void w(String msg) {
        if (mLevel >= 2 && msg != null) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String msg, Throwable tr) {
        if (msg != null) {
            w(msg + getStackTraceString(tr));
        }
    }

    public static void i(String msg) {
        if (mLevel >= 3 && msg != null) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String msg, Throwable tr) {
        if (msg != null) {
            i(msg + getStackTraceString(tr));
        }
    }

    public static void d(String msg) {
        if (mLevel >= 4 && msg != null) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String msg, Throwable tr) {
        if (msg != null) {
            d(msg + getStackTraceString(tr));
        }
    }

    public static void v(String msg) {
        if (mLevel >= 5 && msg != null) {
            Log.v(TAG, msg);
        }
    }

    public static void v(String msg, Throwable tr) {
        if (msg != null) {
            v(msg + getStackTraceString(tr));
        }
    }

    private static String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }
}
