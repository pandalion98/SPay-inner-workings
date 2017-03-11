package com.americanexpress.mobilepayments.hceclient.model;

public class TokenUpdateResponse extends TokenOperationStatus {
    private String tokenDataSignature;

    public String getTokenDataSignature() {
        return this.tokenDataSignature;
    }

    public void setTokenDataSignature(String str) {
        this.tokenDataSignature = str;
    }
}
