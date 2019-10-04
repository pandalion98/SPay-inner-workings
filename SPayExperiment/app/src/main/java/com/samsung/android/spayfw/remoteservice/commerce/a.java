/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.commerce;

import com.samsung.android.spayfw.remoteservice.Client;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentRequestData;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentResponseData;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;

public class a
extends Request<PaymentRequestData, PaymentResponseData, c<PaymentResponseData>, a> {
    protected a(Client client, PaymentRequestData paymentRequestData) {
        super(client, Client.HttpRequest.RequestMethod.Ah, paymentRequestData);
    }

    @Override
    protected c<PaymentResponseData> b(int n2, String string) {
        return new c<PaymentResponseData>(null, this.Al.fromJson(string, PaymentResponseData.class), n2);
    }

    @Override
    protected String cG() {
        return "/payments";
    }

    @Override
    protected String getRequestType() {
        return "PaymentRequest";
    }
}

