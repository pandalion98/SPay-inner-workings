package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvSelectionResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.k */
public class SelectIdvRequest extends TokenRequesterRequest<TokenRequestData, IdvSelectionResponseData, Response<IdvSelectionResponseData>, SelectIdvRequest> {
    private IdvMethod Bi;

    protected SelectIdvRequest(TokenRequesterClient tokenRequesterClient, TokenRequestData tokenRequestData, IdvMethod idvMethod) {
        super(tokenRequesterClient, RequestMethod.POST, tokenRequestData);
        this.Bi = idvMethod;
    }

    protected String cG() {
        return "/tokens";
    }

    protected String getRequestType() {
        return "SelectIdvRequest";
    }

    protected Response<IdvSelectionResponseData> m1215b(int i, String str) {
        return new Response(null, (IdvSelectionResponseData) this.Al.fromJson(str, IdvSelectionResponseData.class), i);
    }

    protected void init() {
        addHeader("Proxy-Authorization", "IDV " + this.Bi.getId());
    }
}
