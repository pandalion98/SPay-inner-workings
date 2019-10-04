/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.e;

import android.util.Log;
import com.samsung.android.spayfw.d.b;
import com.samsung.android.spayfw.e.b.a;

public class d {
    public static String get(String string) {
        if (a.fT()) {
            Log.d((String)"SystemPropertiesWrapper", (String)"it's se device");
            return b.get(string);
        }
        Log.d((String)"SystemPropertiesWrapper", (String)"it's non se device");
        return com.samsung.android.spayfw.c.d.get(string);
    }

    public static String get(String string, String string2) {
        if (a.fT()) {
            Log.d((String)"SystemPropertiesWrapper", (String)"it's se device");
            return b.get(string, string2);
        }
        Log.d((String)"SystemPropertiesWrapper", (String)"it's non se device");
        return com.samsung.android.spayfw.c.d.get(string, string2);
    }

    public static boolean getBoolean(String string, boolean bl) {
        if (a.fT()) {
            Log.d((String)"SystemPropertiesWrapper", (String)"it's se device");
            return b.getBoolean(string, bl);
        }
        Log.d((String)"SystemPropertiesWrapper", (String)"it's non se device");
        return com.samsung.android.spayfw.c.d.getBoolean(string, bl);
    }
}

