/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.model;

import com.americanexpress.mobilepayments.hceclient.model.TokenOperationStatus;

public class TokenPersoResponse
extends TokenOperationStatus {
    private String tokenConfiguration;
    private String tokenDataSignature;

    public String getTokenConfiguration() {
        return this.tokenConfiguration;
    }

    public String getTokenDataSignature() {
        return this.tokenDataSignature;
    }

    public void setTokenConfiguration(String string) {
        this.tokenConfiguration = string;
    }

    public void setTokenDataSignature(String string) {
        this.tokenDataSignature = string;
    }
}

