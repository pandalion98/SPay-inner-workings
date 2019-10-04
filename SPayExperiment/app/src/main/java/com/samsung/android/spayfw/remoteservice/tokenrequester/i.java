/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.m;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReplenishTokenRequestData;

public class i
extends m<ReplenishTokenRequestData, ReplenishTokenRequestData, c<ReplenishTokenRequestData>, i> {
    private static final StringBuilder zX = new StringBuilder("/tokens");
    private String TAG = "ReplenishTokenRequest";
    private String tokenId;

    protected i(l l2, String string, ReplenishTokenRequestData replenishTokenRequestData) {
        super(l2, Client.HttpRequest.RequestMethod.Ah, replenishTokenRequestData);
        this.tokenId = string;
    }

    @Override
    protected c<ReplenishTokenRequestData> b(int n2, String string) {
        return new c<ReplenishTokenRequestData>(null, this.Al.fromJson(string, ReplenishTokenRequestData.class), n2);
    }

    @Override
    protected String cG() {
        com.samsung.android.spayfw.b.c.i(this.TAG, this.tokenId);
        return (Object)zX + "/" + this.tokenId.toString();
    }

    @Override
    protected String getRequestType() {
        return this.TAG;
    }
}

