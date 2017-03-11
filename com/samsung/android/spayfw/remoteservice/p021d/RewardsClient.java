package com.samsung.android.spayfw.remoteservice.p021d;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.CommonClient;
import com.samsung.android.spayfw.remoteservice.Request;

/* renamed from: com.samsung.android.spayfw.remoteservice.d.a */
public class RewardsClient extends CommonClient {
    private static RewardsClient AY;

    public static synchronized RewardsClient m1185L(Context context) {
        RewardsClient rewardsClient;
        synchronized (RewardsClient.class) {
            if (AY == null) {
                AY = new RewardsClient(context);
            }
            rewardsClient = AY;
        }
        return rewardsClient;
    }

    private RewardsClient(Context context) {
        super(context, "/pf/rs/v1");
    }

    public void m1186a(Request request) {
        super.m1125a(request);
        request.addHeader("request-time", Long.toString(System.currentTimeMillis()));
        request.addHeader("Payment-Type", "promotions/*");
        request.addHeader("Wallet-Id", request.bg("x-smps-dmid"));
        request.addHeader("User-Id", request.bg("x-smps-mid"));
        request.addHeader("Device-Id", request.bg("x-smps-did"));
        request.addHeader("Country-Code", request.bg("x-smps-cc2"));
    }
}
