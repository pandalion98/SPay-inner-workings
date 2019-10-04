/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.americanexpress.sdkmodulelib.model;

import com.americanexpress.sdkmodulelib.model.TokenDataStatus;

public class ProcessInAppPaymentResponse {
    String paymentPayload;
    TokenDataStatus tokenDataStatus;

    public String getPaymentPayload() {
        return this.paymentPayload;
    }

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public void setPaymentPayload(String string) {
        this.paymentPayload = string;
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

