package com.americanexpress.sdkmodulelib.model;

public class ProcessInAppPaymentResponse {
    String paymentPayload;
    TokenDataStatus tokenDataStatus;

    public String getPaymentPayload() {
        return this.paymentPayload;
    }

    public void setPaymentPayload(String str) {
        this.paymentPayload = str;
    }

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public void setTokenDataStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.paymentPayload != null) {
            stringBuilder.append("\npaymentPayload=").append(this.paymentPayload);
        }
        return stringBuilder.toString();
    }
}
