/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.c;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.Request;

public class a
extends com.samsung.android.spayfw.remoteservice.a {
    private static a AX;

    private a(Context context) {
        super(context, "/pf/ps/v1");
    }

    public static a K(Context context) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (AX == null) {
                AX = new a(context);
            }
            a a2 = AX;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return a2;
        }
    }

    @Override
    public void a(Request request) {
        super.a(request);
        request.addHeader("Payment-Type", "promotions/*");
        request.addHeader("Wallet-Id", request.bg("x-smps-dmid"));
        request.addHeader("User-Id", request.bg("x-smps-mid"));
        request.addHeader("Device-Id", request.bg("x-smps-did"));
        request.addHeader("Country-Code", request.bg("x-smps-cc2"));
    }
}

