/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.model;

import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;

public class TokenUpdateResponse
extends TokenOperationStatus {
    private String tokenDataSignature;

    public String getTokenDataSignature() {
        return this.tokenDataSignature;
    }

    public void setTokenDataSignature(String string) {
        this.tokenDataSignature = string;
    }
}

