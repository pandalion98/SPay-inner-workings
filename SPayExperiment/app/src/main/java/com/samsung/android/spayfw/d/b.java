/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.SemSystemProperties
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.d;

import android.os.SemSystemProperties;

public class b {
    public static String get(String string) {
        return SemSystemProperties.get((String)string);
    }

    public static String get(String string, String string2) {
        return SemSystemProperties.get((String)string, (String)string2);
    }

    public static boolean getBoolean(String string, boolean bl) {
        return SemSystemProperties.getBoolean((String)string, (boolean)bl);
    }
}

