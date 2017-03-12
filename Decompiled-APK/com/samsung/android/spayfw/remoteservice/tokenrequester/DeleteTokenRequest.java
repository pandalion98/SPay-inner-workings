package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.b */
public class DeleteTokenRequest extends TokenRequesterRequest<Data, String, Response<String>, DeleteTokenRequest> {
    private static final StringBuilder zX;
    private String tokenId;

    static {
        zX = new StringBuilder("/tokens");
    }

    protected DeleteTokenRequest(TokenRequesterClient tokenRequesterClient, String str, Data data) {
        super(tokenRequesterClient, RequestMethod.DELETE, data);
        this.tokenId = str;
    }

    protected String cG() {
        Log.m287i("DeleteTokenRequest", this.tokenId);
        return zX + "/" + this.tokenId.toString();
    }

    protected String getRequestType() {
        return "DeleteTokenRequest";
    }

    protected Response<String> m1197b(int i, String str) {
        return null;
    }
}
