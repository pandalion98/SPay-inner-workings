package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.models.Art;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.g */
public class QueryTokenRequest extends TokenRequesterRequest<String, TokenResponseData, Response<TokenResponseData>, QueryTokenRequest> {
    private static final StringBuilder zX;
    private boolean Bg;
    private String tokenId;

    static {
        zX = new StringBuilder("/tokens");
    }

    protected QueryTokenRequest(TokenRequesterClient tokenRequesterClient, String str) {
        super(tokenRequesterClient, RequestMethod.GET, BuildConfig.FLAVOR);
        this.Bg = true;
        this.tokenId = str;
    }

    public void m1205h(boolean z) {
        this.Bg = z;
    }

    protected String cG() {
        Log.m285d("SPAYFW:QueryTokenRequest", this.tokenId);
        return zX + "/" + this.tokenId.toString();
    }

    protected String getRequestType() {
        return "SPAYFW:QueryTokenRequest";
    }

    protected Response<TokenResponseData> m1204b(int i, String str) {
        TokenResponseData tokenResponseData = (TokenResponseData) this.Al.fromJson(str, TokenResponseData.class);
        if (this.Bg) {
            if (!(tokenResponseData == null || tokenResponseData.getCard() == null)) {
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
                m840b(arrayList);
                if (!(tokenResponseData.getCard().getIssuer().getApp() == null || tokenResponseData.getCard().getIssuer().getApp().getIcon() == null)) {
                    m837a(new Art[]{tokenResponseData.getCard().getIssuer().getApp().getIcon()});
                }
            }
        } else {
            Log.m285d("SPAYFW:QueryTokenRequest", "Not downloading resources");
        }
        return new Response(null, tokenResponseData, i);
    }
}
