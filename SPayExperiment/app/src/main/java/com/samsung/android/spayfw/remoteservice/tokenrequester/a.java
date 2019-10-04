/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;

public class a
extends Request<String, String, c<String>, a> {
    private static volatile boolean Be = false;

    public a(Client client, String string, String string2) {
        super(client, Client.HttpRequest.RequestMethod.Ah, string2);
    }

    public static boolean fc() {
        return Be;
    }

    @Override
    protected c<String> b(int n2, String string) {
        return new c<String>(null, string, n2);
    }

    public void bj(String string) {
        this.addHeader("Spec-Version", string);
    }

    @Override
    protected String cG() {
        return "/reports";
    }

    @Override
    protected String getRequestType() {
        return "AnalyticsRequest";
    }

    @Override
    protected String j(Object object) {
        if (object == null) {
            com.samsung.android.spayfw.b.c.e("AnalyticsRequest", "given AnalyticData is null");
            return null;
        }
        return (String)object;
    }

    public void setCardBrand(String string) {
        this.addHeader("Payment-Type", string);
    }
}

