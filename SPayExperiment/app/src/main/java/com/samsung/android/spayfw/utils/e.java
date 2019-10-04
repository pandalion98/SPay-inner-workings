/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.android.volley.a
 *  com.android.volley.a.a
 *  com.android.volley.a.c
 *  com.android.volley.a.f
 *  com.android.volley.a.g
 *  com.android.volley.a.k
 *  com.android.volley.e
 *  com.android.volley.h
 *  java.io.File
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.utils;

import android.content.Context;
import com.android.volley.a;
import com.android.volley.a.c;
import com.android.volley.a.f;
import com.android.volley.a.g;
import com.android.volley.a.k;
import com.android.volley.h;
import java.io.File;

public class e {
    private static final String TAG = e.class.getSimpleName();
    private static h aD;

    private e() {
    }

    public static void a(Context context, Integer n2) {
        if (aD != null) {
            return;
        }
        aD = e.b(context, n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    static h b(Context context, Integer n2) {
        File file = context.getExternalCacheDir();
        File file2 = file != null ? new File(file, "volley") : new File(context.getCacheDir(), "volley");
        com.android.volley.a.a a2 = new com.android.volley.a.a((f)new g());
        c c2 = new c(file2, 314572800);
        c2.initialize();
        h h2 = n2 == null || n2 <= 0 ? new h((a)c2, (com.android.volley.e)a2) : new h((a)c2, (com.android.volley.e)a2, n2.intValue());
        h2.start();
        k.a((Context)context);
        return h2;
    }

    public static h fK() {
        if (aD != null) {
            return aD;
        }
        throw new IllegalStateException("Not initialized");
    }
}

