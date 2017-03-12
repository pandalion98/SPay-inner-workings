package com.samsung.android.spayfw.payprovider.visa.inapp;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;

/* renamed from: com.samsung.android.spayfw.payprovider.visa.inapp.a */
public class GenCryptogramRequest extends TokenRequesterRequest<Data, Data, Response<Data>, GenCryptogramRequest> {
    private static final StringBuilder zX;
    private static final StringBuilder zY;
    private String tokenId;

    static {
        zX = new StringBuilder("/tokens");
        zY = new StringBuilder("cryptograms");
    }

    public GenCryptogramRequest(Client client, String str, Data data) {
        super(client, RequestMethod.POST, data);
        this.tokenId = str;
    }

    protected String cG() {
        Log.m285d("GenCryptogramRequest", this.tokenId);
        return zX + "/" + this.tokenId.toString() + "/" + zY;
    }

    protected String getRequestType() {
        return "GenCryptogramRequest";
    }

    protected Response<Data> m1147b(int i, String str) {
        return new Response(null, (Data) this.Al.fromJson(str, Data.class), i);
    }
}
