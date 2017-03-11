package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionsData;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.h */
public class RefreshIdvRequest extends TokenRequesterRequest<String, IdvOptionsData, Response<IdvOptionsData>, RefreshIdvRequest> {
    private String mTokenId;

    protected RefreshIdvRequest(TokenRequesterClient tokenRequesterClient, String str) {
        super(tokenRequesterClient, RequestMethod.GET, BuildConfig.FLAVOR);
        this.mTokenId = str;
    }

    protected String cG() {
        Log.m285d("RefreshIdvRequest", this.mTokenId);
        return "/tokens/" + this.mTokenId.toString();
    }

    protected String getRequestType() {
        return "RefreshIdvRequest";
    }

    protected Response<IdvOptionsData> m1206b(int i, String str) {
        return new Response(null, (IdvOptionsData) this.Al.fromJson(str, IdvOptionsData.class), i);
    }

    protected void init() {
        addHeader("Cache-Control", "no-cache");
    }
}
