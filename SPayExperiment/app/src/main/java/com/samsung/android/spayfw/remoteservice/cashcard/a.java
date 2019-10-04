/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Class
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.cashcard;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.cashcard.b;
import com.samsung.android.spayfw.remoteservice.cashcard.c;
import com.samsung.android.spayfw.remoteservice.cashcard.d;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.j;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.PatchData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;

public class a
extends com.samsung.android.spayfw.remoteservice.a {
    private static a AK;

    private a(Context context) {
        super(context, "/pf/cc/v1");
    }

    public static a I(Context context) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (AK == null) {
                AK = new a(context);
            }
            a a2 = AK;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return a2;
        }
    }

    public d a(String string, PatchData[] arrpatchData) {
        d d2 = new d(this, string, arrpatchData);
        this.a(d2);
        return d2;
    }

    public j a(String string, ErrorReport errorReport) {
        j j2 = new j(this, new CashCardReport(string, errorReport));
        this.a(j2);
        return j2;
    }

    public j a(String string, TokenReport tokenReport) {
        j j2 = new j(this, new CashCardReport(string, tokenReport));
        this.a(j2);
        return j2;
    }

    @Override
    public void a(Request request) {
        super.a(request);
        request.addHeader("Payment-Type", "cashcard/*");
        request.addHeader("Wallet-Id", request.bg("x-smps-dmid"));
        request.addHeader("User-Id", request.bg("x-smps-mid"));
        request.addHeader("Device-Id", request.bg("x-smps-did"));
        request.addHeader("Country-Code", request.bg("x-smps-cc2"));
    }

    public c b(String string, boolean bl) {
        c c2 = new c(this, string, bl);
        this.a(c2);
        return c2;
    }

    public b bi(String string) {
        b b2 = new b(this, string);
        this.a(b2);
        return b2;
    }
}

