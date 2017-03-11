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

    public void setAtc(String str) {
        this.atc = str;
    }

    public String getClientPaymentDataID() {
        return this.clientPaymentDataID;
    }

    public void setClientPaymentDataID(String str) {
        this.clientPaymentDataID = str;
    }

    public String getvProvisionedTokenID() {
        return this.vProvisionedTokenID;
    }

    public void setvProvisionedTokenID(String str) {
        this.vProvisionedTokenID = str;
    }

    public PaymentRequest getPaymentRequest() {
        return this.paymentRequest;
    }

    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }
}
