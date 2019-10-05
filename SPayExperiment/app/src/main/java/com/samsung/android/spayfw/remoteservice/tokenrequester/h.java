/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionsData;

public class h
extends m<String, IdvOptionsData, c<IdvOptionsData>, h> {
    private String mTokenId;

    protected h(l l2, String string) {
        super(l2, Client.HttpRequest.RequestMethod.Ag, "");
        this.mTokenId = string;
    }

    @Override
    protected c<IdvOptionsData> b(int n2, String string) {
        return new c<IdvOptionsData>(null, this.Al.fromJson(string, IdvOptionsData.class), n2);
    }

    @Override
    protected String cG() {
        Log.d("RefreshIdvRequest", this.mTokenId);
        return "/tokens/" + this.mTokenId.toString();
    }

    @Override
    protected String getRequestType() {
        return "RefreshIdvRequest";
    }

    @Override
    protected void init() {
        this.addHeader("Cache-Control", "no-cache");
    }
}

