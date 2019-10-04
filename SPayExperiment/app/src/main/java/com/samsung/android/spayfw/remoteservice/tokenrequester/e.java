/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.models.Art;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.models.Eula;
import com.samsung.android.spayfw.remoteservice.tokenrequester.f;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.m;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardIssuerAppInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.CardIssuerInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionsData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import java.util.ArrayList;

public class e
extends m<TokenRequestData, TokenResponseData, f, e> {
    protected e(l l2, TokenRequestData tokenRequestData) {
        super(l2, Client.HttpRequest.RequestMethod.Ah, tokenRequestData);
    }

    @Override
    protected /* synthetic */ c b(int n2, String string) {
        return this.g(n2, string);
    }

    @Override
    protected String cG() {
        return "/tokens";
    }

    @Override
    protected /* synthetic */ c e(int n2, String string) {
        return this.h(n2, string);
    }

    protected f g(int n2, String string) {
        if (n2 == 202) {
            IdvOptionsData idvOptionsData = this.Al.fromJson(string, IdvOptionsData.class);
            TokenResponseData tokenResponseData = this.Al.fromJson(string, TokenResponseData.class);
            if (tokenResponseData != null && tokenResponseData.getCard() != null && tokenResponseData.getCard().getArts() != null) {
                this.a(tokenResponseData.getCard().getArts());
            }
            if (tokenResponseData != null && tokenResponseData.getCard() != null && tokenResponseData.getCard().getIssuer() != null) {
                ArrayList arrayList = new ArrayList();
                if (tokenResponseData.getCard().getIssuer().getPrivacy() != null) {
                    arrayList.add((Object)tokenResponseData.getCard().getIssuer().getPrivacy());
                }
                if (tokenResponseData.getCard().getIssuer().getTnc() != null) {
                    arrayList.add((Object)tokenResponseData.getCard().getIssuer().getTnc());
                }
                if (tokenResponseData.getCard().getIssuer().getApp() != null && tokenResponseData.getCard().getIssuer().getApp().getIcon() != null) {
                    Art[] arrart = new Art[]{tokenResponseData.getCard().getIssuer().getApp().getIcon()};
                    this.a(arrart);
                }
            }
            return new f(idvOptionsData, tokenResponseData, n2);
        }
        TokenResponseData tokenResponseData = this.Al.fromJson(string, TokenResponseData.class);
        if (tokenResponseData != null && tokenResponseData.getCard() != null && tokenResponseData.getCard().getArts() != null) {
            this.a(tokenResponseData.getCard().getArts());
        }
        return new f(tokenResponseData, n2);
    }

    @Override
    protected String getRequestType() {
        return "ProvisionRequest";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected f h(int n2, String string) {
        ErrorResponseData errorResponseData;
        try {
            errorResponseData = this.Al.fromJson(string, ErrorResponseData.class);
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.c("ProvisionRequest", exception.getMessage(), exception);
            errorResponseData = null;
            return new f(errorResponseData, n2);
        }
        do {
            return new f(errorResponseData, n2);
            break;
        } while (true);
    }
}

