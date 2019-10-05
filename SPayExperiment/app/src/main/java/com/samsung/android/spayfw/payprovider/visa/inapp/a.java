/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.android.spayfw.payprovider.visa.inapp;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.tokenrequester.m;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;

public class a
extends m<Data, Data, c<Data>, a> {
    private static final StringBuilder zX = new StringBuilder("/tokens");
    private static final StringBuilder zY = new StringBuilder("cryptograms");
    private String tokenId;

    public a(Client client, String string, Data data) {
        super(client, Client.HttpRequest.RequestMethod.Ah, data);
        this.tokenId = string;
    }

    @Override
    protected c<Data> b(int n2, String string) {
        return new c<Data>(null, this.Al.fromJson(string, Data.class), n2);
    }

    @Override
    protected String cG() {
        Log.d("GenCryptogramRequest", this.tokenId);
        return (Object)zX + "/" + this.tokenId.toString() + "/" + (Object)zY;
    }

    @Override
    protected String getRequestType() {
        return "GenCryptogramRequest";
    }
}

