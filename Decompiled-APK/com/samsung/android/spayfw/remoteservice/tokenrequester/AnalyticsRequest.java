package com.samsung.android.spayfw.remoteservice.tokenrequester;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.Response;

/* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.a */
public class AnalyticsRequest extends Request<String, String, Response<String>, AnalyticsRequest> {
    private static volatile boolean Be;

    static {
        Be = false;
    }

    public AnalyticsRequest(Client client, String str, String str2) {
        super(client, RequestMethod.POST, str2);
    }

    public static boolean fc() {
        return Be;
    }

    protected String cG() {
        return "/reports";
    }

    protected String getRequestType() {
        return "AnalyticsRequest";
    }

    public void setCardBrand(String str) {
        addHeader("Payment-Type", str);
    }

    public void bj(String str) {
        addHeader("Spec-Version", str);
    }

    protected Response<String> m1195b(int i, String str) {
        return new Response(null, str, i);
    }

    protected String m1196j(Object obj) {
        if (obj != null) {
            return (String) obj;
        }
        Log.m286e("AnalyticsRequest", "given AnalyticData is null");
        return null;
    }
}
