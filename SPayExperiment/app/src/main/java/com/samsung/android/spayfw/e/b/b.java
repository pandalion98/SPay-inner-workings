/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.NoSuchFieldException
 *  java.lang.Object
 *  java.lang.SecurityException
 *  java.lang.String
 *  java.lang.reflect.Field
 */
package com.samsung.android.spayfw.e.b;

import android.util.Log;
import java.lang.reflect.Field;

public class b {
    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static Field a(Class var0, String var1_1) {
        if (var0 == null) {
            return null;
        }
        try {
            return var0.getField(var1_1);
        }
        catch (NoSuchFieldException var2_3) {}
        ** GOTO lbl-1000
        catch (SecurityException var2_5) {}
lbl-1000: // 2 sources:
        {
            Log.d((String)"ReflectUtils", (String)("Cannot load field: " + var2_4.getMessage()));
            return null;
        }
    }
}

