package com.samsung.android.spayfw.p003c;

import android.os.SystemProperties;

/* renamed from: com.samsung.android.spayfw.c.d */
public class SdlSystemProperties {
    public static String get(String str) {
        return SystemProperties.get(str);
    }

    public static String get(String str, String str2) {
        return SystemProperties.get(str, str2);
    }

    public static boolean getBoolean(String str, boolean z) {
        return SystemProperties.getBoolean(str, z);
    }
}
