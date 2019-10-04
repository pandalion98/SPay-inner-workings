/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Environment
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.contextservice.b;

import android.os.Build;
import android.os.Environment;
import com.samsung.android.spayfw.e.d;

public final class c {
    public static final String Dg;
    public static final boolean HK;
    public static final double[] HL;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = !"eng".equals((Object)Build.TYPE);
        HK = bl;
        Dg = Environment.getExternalStorageDirectory().getAbsolutePath();
        HL = new double[]{0.0, 5003530.0, 625441.0, 123264.0, 19545.0, 3803.0, 610.0, 118.0, 19.0, 3.71, 0.6, 0.074, 0.005};
    }

    public static final boolean fL() {
        return d.getBoolean("ro.product_ship", true);
    }
}

