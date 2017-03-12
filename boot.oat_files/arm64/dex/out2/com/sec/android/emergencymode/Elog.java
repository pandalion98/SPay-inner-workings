package com.sec.android.emergencymode;

import android.util.Slog;

public final class Elog {
    private static final boolean DEBUG = true;
    private static final String M_TAG = "EmergencyMode";

    public static void d(String moduleTag, String log) {
        Slog.secD(M_TAG, "[" + moduleTag + "] " + log);
    }

    public static void v(String moduleTag, String log) {
        Slog.v(M_TAG, "[" + moduleTag + "] " + log);
    }
}
