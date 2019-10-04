/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.SystemProperties
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.c;

import android.os.SystemProperties;

public class d {
    public static String get(String string) {
        return SystemProperties.get((String)string);
    }

    public static String get(String string, String string2) {
        return SystemProperties.get((String)string, (String)string2);
    }

    public static boolean getBoolean(String string, boolean bl) {
        return SystemProperties.getBoolean((String)string, (boolean)bl);
    }
}

