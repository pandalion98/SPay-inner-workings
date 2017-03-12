package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.o */
public class VerifyIdvRequest extends TokenRequesterRequest<TokenRequestData, TokenResponseData, Response<TokenResponseData>, VerifyIdvRequest> {
    private String Bk;

    protected VerifyIdvRequest(TokenRequesterClient tokenRequesterClient, TokenRequestData tokenRequestData, String str) {
        super(tokenRequesterClient, RequestMethod.POST, tokenRequestData);
        this.Bk = str;
    }

    protected String cG() {
        return "/tokens";
    }

    protected String getRequestType() {
        return "VerifyIdvRequest";
    }

    protected Response<TokenResponseData> m1217b(int i, String str) {
        return new Response(null, (TokenResponseData) this.Al.fromJson(str, TokenResponseData.class), i);
    }

    protected void init() {
        addHeader("Proxy-Authorization", this.Bk);
    }
}
