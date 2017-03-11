package com.samsung.android.spayfw.remoteservice.p018a;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.CommonClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.AnalyticsRequest;

/* renamed from: com.samsung.android.spayfw.remoteservice.a.a */
public class AnalyticsRequesterClient extends CommonClient {
    private static AnalyticsRequesterClient AJ;

    public static synchronized AnalyticsRequesterClient m1159H(Context context) {
        AnalyticsRequesterClient analyticsRequesterClient;
        synchronized (AnalyticsRequesterClient.class) {
            if (AJ == null) {
                AJ = new AnalyticsRequesterClient(context);
            }
            analyticsRequesterClient = AJ;
        }
        return analyticsRequesterClient;
    }

    private AnalyticsRequesterClient(Context context) {
        super(context, "/af/v1");
    }

    public AnalyticsRequest m1161t(String str, String str2) {
        AnalyticsRequest analyticsRequest = new AnalyticsRequest(this, str, str2);
        m1160a(analyticsRequest, str);
        return analyticsRequest;
    }

    public void m1160a(AnalyticsRequest analyticsRequest, String str) {
        super.m1125a(analyticsRequest);
        if (str != null) {
            analyticsRequest.setCardBrand(str);
        }
        analyticsRequest.bj("1.0.7");
    }
}
