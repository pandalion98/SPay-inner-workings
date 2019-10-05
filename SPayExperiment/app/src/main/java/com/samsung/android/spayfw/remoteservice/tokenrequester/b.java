/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;

public class b
extends m<Data, String, c<String>, b> {
    private static final StringBuilder zX = new StringBuilder("/tokens");
    private String tokenId;

    protected b(l l2, String string, Data data) {
        super(l2, Client.HttpRequest.RequestMethod.Ai, data);
        this.tokenId = string;
    }

    @Override
    protected c<String> b(int n2, String string) {
        return null;
    }

    @Override
    protected String cG() {
        Log.i("DeleteTokenRequest", this.tokenId);
        return (Object)zX + "/" + this.tokenId.toString();
    }

    @Override
    protected String getRequestType() {
        return "DeleteTokenRequest";
    }
}

