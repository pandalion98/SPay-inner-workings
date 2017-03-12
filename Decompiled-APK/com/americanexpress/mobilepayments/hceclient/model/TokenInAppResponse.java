package com.americanexpress.mobilepayments.hceclient.model;

public class TokenInAppResponse extends TokenOperationStatus {
    private String paymentPayload;

    public String getPaymentPayload() {
        return this.paymentPayload;
    }

    public void setPaymentPayload(String str) {
        this.paymentPayload = str;
    }
}
