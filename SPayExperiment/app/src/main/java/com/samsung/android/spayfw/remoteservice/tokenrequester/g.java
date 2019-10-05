/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.models.Art;
import com.samsung.android.spayfw.remoteservice.models.Eula;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import java.util.ArrayList;
import java.util.List;

public class g
extends m<String, TokenResponseData, c<TokenResponseData>, g> {
    private static final StringBuilder zX = new StringBuilder("/tokens");
    private boolean Bg = true;
    private String tokenId;

    protected g(l l2, String string) {
        super(l2, Client.HttpRequest.RequestMethod.Ag, "");
        this.tokenId = string;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected c<TokenResponseData> b(int n2, String string) {
        TokenResponseData tokenResponseData = this.Al.fromJson(string, TokenResponseData.class);
        if (this.Bg) {
            if (tokenResponseData != null && tokenResponseData.getCard() != null) {
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
                this.b((List<Eula>)arrayList);
                if (tokenResponseData.getCard().getIssuer().getApp() != null && tokenResponseData.getCard().getIssuer().getApp().getIcon() != null) {
                    Art[] arrart = new Art[]{tokenResponseData.getCard().getIssuer().getApp().getIcon()};
                    this.a(arrart);
                }
            }
            do {
                return new c<TokenResponseData>(null, tokenResponseData, n2);
                break;
            } while (true);
        }
        Log.d("SPAYFW:QueryTokenRequest", "Not downloading resources");
        return new c<TokenResponseData>(null, tokenResponseData, n2);
    }

    @Override
    protected String cG() {
        Log.d("SPAYFW:QueryTokenRequest", this.tokenId);
        return (Object)zX + "/" + this.tokenId.toString();
    }

    @Override
    protected String getRequestType() {
        return "SPAYFW:QueryTokenRequest";
    }

    public void h(boolean bl) {
        this.Bg = bl;
    }
}

