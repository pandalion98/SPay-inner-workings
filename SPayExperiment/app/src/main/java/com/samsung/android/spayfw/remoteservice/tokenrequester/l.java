/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.google.gson.JsonObject
 *  java.lang.Class
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import android.content.Context;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.a;
import com.samsung.android.spayfw.remoteservice.tokenrequester.b;
import com.samsung.android.spayfw.remoteservice.tokenrequester.c;
import com.samsung.android.spayfw.remoteservice.tokenrequester.d;
import com.samsung.android.spayfw.remoteservice.tokenrequester.e;
import com.samsung.android.spayfw.remoteservice.tokenrequester.g;
import com.samsung.android.spayfw.remoteservice.tokenrequester.h;
import com.samsung.android.spayfw.remoteservice.tokenrequester.i;
import com.samsung.android.spayfw.remoteservice.tokenrequester.j;
import com.samsung.android.spayfw.remoteservice.tokenrequester.k;
import com.samsung.android.spayfw.remoteservice.tokenrequester.m;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EnrollmentRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ErrorReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.EventReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Instruction;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.MstConfigurationRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReplenishTokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.n;
import com.samsung.android.spayfw.remoteservice.tokenrequester.o;
import java.util.List;

public class l
extends a {
    private static l Bj;

    protected l(Context context) {
        super(context, "/pf/v1");
    }

    public static l Q(Context context) {
        Class<l> class_ = l.class;
        synchronized (l.class) {
            if (Bj == null) {
                Bj = new l(context);
            }
            l l2 = Bj;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return l2;
        }
    }

    public b a(String string, String string2, Data data) {
        b b2 = new b(this, string2, data);
        this.a(b2, string);
        return b2;
    }

    public c a(String string, EnrollmentRequestData enrollmentRequestData) {
        c c2 = new c(this, enrollmentRequestData);
        this.a(c2, string);
        return c2;
    }

    public d a(String string, MstConfigurationRequestData mstConfigurationRequestData) {
        d d2 = new d(this, mstConfigurationRequestData);
        this.a(d2, string);
        return d2;
    }

    public e a(String string, TokenRequestData tokenRequestData) {
        e e2 = new e(this, tokenRequestData);
        this.a(e2, string);
        return e2;
    }

    public i a(String string, String string2, ReplenishTokenRequestData replenishTokenRequestData) {
        i i2 = new i(this, string2, replenishTokenRequestData);
        this.a(i2, string);
        return i2;
    }

    public j a(String string, ErrorReport errorReport) {
        j j2 = new j(this, new ReportData(errorReport));
        this.a(j2, string);
        return j2;
    }

    public j a(String string, EventReport eventReport) {
        j j2 = new j(this, new ReportData(eventReport));
        this.a(j2, string);
        return j2;
    }

    public j a(String string, ReportData reportData) {
        j j2 = new j(this, reportData);
        this.a(j2, string);
        return j2;
    }

    public j a(String string, TokenReport tokenReport) {
        j j2 = new j(this, new ReportData(tokenReport, tokenReport.getNotificationId()));
        this.a(j2, string);
        return j2;
    }

    public k a(String string, TokenRequestData tokenRequestData, IdvMethod idvMethod) {
        k k2 = new k(this, tokenRequestData, idvMethod);
        this.a(k2, string);
        return k2;
    }

    public o a(String string, TokenRequestData tokenRequestData, String string2) {
        o o2 = new o(this, tokenRequestData, string2);
        this.a(o2, string);
        return o2;
    }

    public void a(m m2, String string) {
        super.a(m2);
        if (string != null) {
            m2.setCardBrand(string);
        }
    }

    public j b(String string, JsonObject jsonObject) {
        j j2 = new j(this, new ReportData(jsonObject));
        this.a(j2, string);
        return j2;
    }

    public n b(String string, List<Instruction> list) {
        n n2 = new n(this, string, list);
        this.a(n2, "loyalty/*");
        return n2;
    }

    public g x(String string, String string2) {
        g g2 = new g(this, string2);
        this.a(g2, string);
        return g2;
    }

    public h y(String string, String string2) {
        h h2 = new h(this, string2);
        this.a(h2, string);
        return h2;
    }
}

