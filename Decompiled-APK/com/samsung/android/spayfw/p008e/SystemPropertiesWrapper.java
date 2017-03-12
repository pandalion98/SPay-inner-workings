package com.samsung.android.spayfw.p008e;

import android.util.Log;
import com.samsung.android.spayfw.p003c.SdlSystemProperties;
import com.samsung.android.spayfw.p006d.SeSystemProperties;
import com.samsung.android.spayfw.p008e.p010b.Platformutils;

/* renamed from: com.samsung.android.spayfw.e.d */
public class SystemPropertiesWrapper {
    public static String get(String str) {
        if (Platformutils.fT()) {
            Log.d("SystemPropertiesWrapper", "it's se device");
            return SeSystemProperties.get(str);
        }
        Log.d("SystemPropertiesWrapper", "it's non se device");
        return SdlSystemProperties.get(str);
    }

    public static String get(String str, String str2) {
        if (Platformutils.fT()) {
            Log.d("SystemPropertiesWrapper", "it's se device");
            return SeSystemProperties.get(str, str2);
        }
        Log.d("SystemPropertiesWrapper", "it's non se device");
        return SdlSystemProperties.get(str, str2);
    }

    public static boolean getBoolean(String str, boolean z) {
        if (Platformutils.fT()) {
            Log.d("SystemPropertiesWrapper", "it's se device");
            return SeSystemProperties.getBoolean(str, z);
        }
        Log.d("SystemPropertiesWrapper", "it's non se device");
        return SdlSystemProperties.getBoolean(str, z);
    }
}
