package com.samsung.contextservice.p029b;

import android.os.Build;
import android.os.Environment;
import com.samsung.android.spayfw.p008e.SystemPropertiesWrapper;

/* renamed from: com.samsung.contextservice.b.c */
public final class Config {
    public static final String Dg;
    public static final boolean HK;
    public static final double[] HL;

    static {
        HK = !"eng".equals(Build.TYPE);
        Dg = Environment.getExternalStorageDirectory().getAbsolutePath();
        HL = new double[]{0.0d, 5003530.0d, 625441.0d, 123264.0d, 19545.0d, 3803.0d, 610.0d, 118.0d, 19.0d, 3.71d, 0.6d, 0.074d, 0.005d};
    }

    public static final boolean fL() {
        return SystemPropertiesWrapper.getBoolean("ro.product_ship", true);
    }
}
