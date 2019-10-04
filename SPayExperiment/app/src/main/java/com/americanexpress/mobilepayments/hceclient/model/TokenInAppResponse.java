/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.model;

import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;

public class TokenInAppResponse
extends TokenOperationStatus {
    private String paymentPayload;

    public String getPaymentPayload() {
        return this.paymentPayload;
    }

    public void setPaymentPayload(String string) {
        this.paymentPayload = string;
    }
}

