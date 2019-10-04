/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.commerce;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.a;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentRequestData;

public class b
extends a {
    private static b AQ;

    private b(Context context) {
        super(context, "/ps/v1");
    }

    public static b J(Context context) {
        Class<b> class_ = b.class;
        synchronized (b.class) {
            if (AQ == null) {
                AQ = new b(context);
            }
            b b2 = AQ;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return b2;
        }
    }

    public com.samsung.android.spayfw.remoteservice.commerce.a a(PaymentRequestData paymentRequestData) {
        com.samsung.android.spayfw.remoteservice.commerce.a a2 = new com.samsung.android.spayfw.remoteservice.commerce.a(this, paymentRequestData);
        this.a(a2);
        return a2;
    }
}

