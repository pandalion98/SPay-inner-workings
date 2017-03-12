package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.models.Art;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.IdvOptionsData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenRequestData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.e */
public class ProvisionRequest extends TokenRequesterRequest<TokenRequestData, TokenResponseData, ProvisionResponse, ProvisionRequest> {
    protected /* synthetic */ Response m1200b(int i, String str) {
        return m1202g(i, str);
    }

    protected /* synthetic */ Response m1201e(int i, String str) {
        return m1203h(i, str);
    }

    protected ProvisionRequest(TokenRequesterClient tokenRequesterClient, TokenRequestData tokenRequestData) {
        super(tokenRequesterClient, RequestMethod.POST, tokenRequestData);
    }

    protected String cG() {
        return "/tokens";
    }

    protected String getRequestType() {
        return "ProvisionRequest";
    }

    protected ProvisionResponse m1202g(int i, String str) {
        if (i == 202) {
            IdvOptionsData idvOptionsData = (IdvOptionsData) this.Al.fromJson(str, IdvOptionsData.class);
            TokenResponseData tokenResponseData = (TokenResponseData) this.Al.fromJson(str, TokenResponseData.class);
            if (!(tokenResponseData == null || tokenResponseData.getCard() == null || tokenResponseData.getCard().getArts() == null)) {
                m837a(tokenResponseData.getCard().getArts());
            }
            if (!(tokenResponseData == null || tokenResponseData.getCard() == null || tokenResponseData.getCard().getIssuer() == null)) {
                List arrayList = new ArrayList();
                if (tokenResponseData.getCard().getIssuer().getPrivacy() != null) {
                    arrayList.add(tokenResponseData.getCard().getIssuer().getPrivacy());
                }
                if (tokenResponseData.getCard().getIssuer().getTnc() != null) {
                    arrayList.add(tokenResponseData.getCard().getIssuer().getTnc());
                }
                if (!(tokenResponseData.getCard().getIssuer().getApp() == null || tokenResponseData.getCard().getIssuer().getApp().getIcon() == null)) {
                    m837a(new Art[]{tokenResponseData.getCard().getIssuer().getApp().getIcon()});
                }
            }
            return new ProvisionResponse(idvOptionsData, tokenResponseData, i);
        }
        TokenResponseData tokenResponseData2 = (TokenResponseData) this.Al.fromJson(str, TokenResponseData.class);
        if (!(tokenResponseData2 == null || tokenResponseData2.getCard() == null || tokenResponseData2.getCard().getArts() == null)) {
            m837a(tokenResponseData2.getCard().getArts());
        }
        return new ProvisionResponse(tokenResponseData2, i);
    }

    protected ProvisionResponse m1203h(int i, String str) {
        ErrorResponseData errorResponseData;
        try {
            errorResponseData = (ErrorResponseData) this.Al.fromJson(str, ErrorResponseData.class);
        } catch (Throwable e) {
            Log.m284c("ProvisionRequest", e.getMessage(), e);
            errorResponseData = null;
        }
        return new ProvisionResponse(errorResponseData, i);
    }
}
