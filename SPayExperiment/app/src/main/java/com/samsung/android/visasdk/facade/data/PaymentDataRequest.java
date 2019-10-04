/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

import com.samsung.android.visasdk.paywave.model.PaymentRequest;

public class PaymentDataRequest {
    private String atc;
    private String clientPaymentDataID;
    private PaymentRequest paymentRequest;
    private String vProvisionedTokenID;

    public String getAtc() {
        return this.atc;
    }

    public String getClientPaymentDataID() {
        return this.clientPaymentDataID;
    }

    public PaymentRequest getPaymentRequest() {
        return this.paymentRequest;
    }

    public String getvProvisionedTokenID() {
        return this.vProvisionedTokenID;
    }

    public void setAtc(String string) {
        this.atc = string;
    }

    public void setClientPaymentDataID(String string) {
        this.clientPaymentDataID = string;
    }

    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public void setvProvisionedTokenID(String string) {
        this.vProvisionedTokenID = string;
    }
}

