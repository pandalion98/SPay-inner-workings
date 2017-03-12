package com.samsung.android.spayfw.remoteservice.commerce;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Client.HttpRequest.RequestMethod;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentRequestData;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentResponseData;

/* renamed from: com.samsung.android.spayfw.remoteservice.commerce.a */
public class PaymentRequest extends Request<PaymentRequestData, PaymentResponseData, Response<PaymentResponseData>, PaymentRequest> {
    protected PaymentRequest(Client client, PaymentRequestData paymentRequestData) {
        super(client, RequestMethod.POST, paymentRequestData);
    }

    protected String cG() {
        return "/payments";
    }

    protected String getRequestType() {
        return "PaymentRequest";
    }

    protected Response<PaymentResponseData> m1182b(int i, String str) {
        return new Response(null, (PaymentResponseData) this.Al.fromJson(str, PaymentResponseData.class), i);
    }
}
