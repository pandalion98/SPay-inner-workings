package com.samsung.android.spayfw.p006d;

import android.os.SemSystemProperties;

/* renamed from: com.samsung.android.spayfw.d.b */
public class SeSystemProperties {
    public static String get(String str) {
        return SemSystemProperties.get(str);
    }

    public static String get(String str, String str2) {
        return SemSystemProperties.get(str, str2);
    }

    public static boolean getBoolean(String str, boolean z) {
        return SemSystemProperties.getBoolean(str, z);
    }
}
