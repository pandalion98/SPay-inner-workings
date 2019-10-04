/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.m;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;

public class o
extends m<TokenRequestData, TokenResponseData, c<TokenResponseData>, o> {
    private String Bk;

    protected o(l l2, TokenRequestData tokenRequestData, String string) {
        super(l2, Client.HttpRequest.RequestMethod.Ah, tokenRequestData);
        this.Bk = string;
    }

    @Override
    protected c<TokenResponseData> b(int n2, String string) {
        return new c<TokenResponseData>(null, this.Al.fromJson(string, TokenResponseData.class), n2);
    }

    @Override
    protected String cG() {
        return "/tokens";
    }

    @Override
    protected String getRequestType() {
        return "VerifyIdvRequest";
    }

    @Override
    protected void init() {
        this.addHeader("Proxy-Authorization", this.Bk);
    }
}

