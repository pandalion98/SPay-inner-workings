package com.samsung.android.spayfw.remoteservice.p020c;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.CommonClient;
import com.samsung.android.spayfw.remoteservice.Request;

/* renamed from: com.samsung.android.spayfw.remoteservice.c.a */
public class PromotionsClient extends CommonClient {
    private static PromotionsClient AX;

    public static synchronized PromotionsClient m1170K(Context context) {
        PromotionsClient promotionsClient;
        synchronized (PromotionsClient.class) {
            if (AX == null) {
                AX = new PromotionsClient(context);
            }
            promotionsClient = AX;
        }
        return promotionsClient;
    }

    private PromotionsClient(Context context) {
        super(context, "/pf/ps/v1");
    }

    public void m1171a(Request request) {
        super.m1125a(request);
        request.addHeader("Payment-Type", "promotions/*");
        request.addHeader("Wallet-Id", request.bg("x-smps-dmid"));
        request.addHeader("User-Id", request.bg("x-smps-mid"));
        request.addHeader("Device-Id", request.bg("x-smps-did"));
        request.addHeader("Country-Code", request.bg("x-smps-cc2"));
    }
}
