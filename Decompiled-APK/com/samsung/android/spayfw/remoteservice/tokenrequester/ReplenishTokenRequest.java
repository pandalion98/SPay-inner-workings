package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReplenishTokenRequestData;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.i */
public class ReplenishTokenRequest extends TokenRequesterRequest<ReplenishTokenRequestData, ReplenishTokenRequestData, Response<ReplenishTokenRequestData>, ReplenishTokenRequest> {
    private static final StringBuilder zX;
    private String TAG;
    private String tokenId;

    static {
        zX = new StringBuilder("/tokens");
    }

    protected ReplenishTokenRequest(TokenRequesterClient tokenRequesterClient, String str, ReplenishTokenRequestData replenishTokenRequestData) {
        super(tokenRequesterClient, RequestMethod.POST, replenishTokenRequestData);
        this.TAG = "ReplenishTokenRequest";
        this.tokenId = str;
    }

    protected String cG() {
        Log.m287i(this.TAG, this.tokenId);
        return zX + "/" + this.tokenId.toString();
    }

    protected String getRequestType() {
        return this.TAG;
    }

    protected Response<ReplenishTokenRequestData> m1207b(int i, String str) {
        return new Response(null, (ReplenishTokenRequestData) this.Al.fromJson(str, ReplenishTokenRequestData.class), i);
    }
}
