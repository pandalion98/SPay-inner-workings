package com.samsung.android.spayfw.p008e.p010b;

import android.os.Build.VERSION;

/* renamed from: com.samsung.android.spayfw.e.b.a */
public class Platformutils {
    public static final boolean fT() {
        if (ReflectUtils.m691a(VERSION.class, "SEM_INT") != null) {
            return true;
        }
        return false;
    }
}
