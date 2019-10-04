/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.Long
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.android.spayfw.remoteservice.d;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.Request;

public class a
extends com.samsung.android.spayfw.remoteservice.a {
    private static a AY;

    private a(Context context) {
        super(context, "/pf/rs/v1");
    }

    public static a L(Context context) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (AY == null) {
                AY = new a(context);
            }
            a a2 = AY;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return a2;
        }
    }

    @Override
    public void a(Request request) {
        super.a(request);
        request.addHeader("request-time", Long.toString((long)System.currentTimeMillis()));
        request.addHeader("Payment-Type", "promotions/*");
        request.addHeader("Wallet-Id", request.bg("x-smps-dmid"));
        request.addHeader("User-Id", request.bg("x-smps-mid"));
        request.addHeader("Device-Id", request.bg("x-smps-did"));
        request.addHeader("Country-Code", request.bg("x-smps-cc2"));
    }
}

