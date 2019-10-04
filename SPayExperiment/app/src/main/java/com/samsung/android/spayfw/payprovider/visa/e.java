/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.visa;

import android.content.Context;
import com.samsung.android.spayfw.payprovider.visa.inapp.a;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.m;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;

public class e
extends l {
    private static e zQ;

    private e(Context context) {
        super(context);
    }

    public static final e G(Context context) {
        Class<e> class_ = e.class;
        synchronized (e.class) {
            if (zQ == null) {
                zQ = new e(context);
            }
            e e2 = zQ;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return e2;
        }
    }

    public a a(String string, Data data) {
        a a2 = new a(this, string, data);
        this.a(a2, "credit/vi");
        return a2;
    }

    public com.samsung.android.spayfw.payprovider.visa.transaction.a r(String string, String string2) {
        com.samsung.android.spayfw.payprovider.visa.transaction.a a2 = new com.samsung.android.spayfw.payprovider.visa.transaction.a(this, string, string2);
        this.a(a2, "credit/vi");
        return a2;
    }
}

