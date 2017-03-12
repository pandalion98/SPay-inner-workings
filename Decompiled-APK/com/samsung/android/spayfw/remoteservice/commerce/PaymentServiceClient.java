package com.samsung.android.spayfw.remoteservice.commerce;

import android.content.Context;
import com.samsung.android.spayfw.remoteservice.CommonClient;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentRequestData;

/* renamed from: com.samsung.android.spayfw.remoteservice.commerce.b */
public class PaymentServiceClient extends CommonClient {
    private static PaymentServiceClient AQ;

    public static synchronized PaymentServiceClient m1183J(Context context) {
        PaymentServiceClient paymentServiceClient;
        synchronized (PaymentServiceClient.class) {
            if (AQ == null) {
                AQ = new PaymentServiceClient(context);
            }
            paymentServiceClient = AQ;
        }
        return paymentServiceClient;
    }

    private PaymentServiceClient(Context context) {
        super(context, "/ps/v1");
    }

    public PaymentRequest m1184a(PaymentRequestData paymentRequestData) {
        Request paymentRequest = new PaymentRequest(this, paymentRequestData);
        m1125a(paymentRequest);
        return paymentRequest;
    }
}
