/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.m;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvSelectionResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;

public class k
extends m<TokenRequestData, IdvSelectionResponseData, c<IdvSelectionResponseData>, k> {
    private IdvMethod Bi;

    protected k(l l2, TokenRequestData tokenRequestData, IdvMethod idvMethod) {
        super(l2, Client.HttpRequest.RequestMethod.Ah, tokenRequestData);
        this.Bi = idvMethod;
    }

    @Override
    protected c<IdvSelectionResponseData> b(int n2, String string) {
        return new c<IdvSelectionResponseData>(null, this.Al.fromJson(string, IdvSelectionResponseData.class), n2);
    }

    @Override
    protected String cG() {
        return "/tokens";
    }

    @Override
    protected String getRequestType() {
        return "SelectIdvRequest";
    }

    @Override
    protected void init() {
        this.addHeader("Proxy-Authorization", "IDV " + this.Bi.getId());
    }
}

