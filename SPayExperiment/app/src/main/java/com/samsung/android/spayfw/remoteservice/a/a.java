/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.a;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;

public class a
extends com.samsung.android.spayfw.remoteservice.a {
    private static a AJ;

    private a(Context context) {
        super(context, "/af/v1");
    }

    public static a H(Context context) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (AJ == null) {
                AJ = new a(context);
            }
            a a2 = AJ;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return a2;
        }
    }

    public void a(com.samsung.android.spayfw.remoteservice.tokenrequester.a a2, String string) {
        super.a(a2);
        if (string != null) {
            a2.setCardBrand(string);
        }
        a2.bj("1.0.7");
    }

    public com.samsung.android.spayfw.remoteservice.tokenrequester.a t(String string, String string2) {
        com.samsung.android.spayfw.remoteservice.tokenrequester.a a2 = new com.samsung.android.spayfw.remoteservice.tokenrequester.a(this, string, string2);
        this.a(a2, string);
        return a2;
    }
}

