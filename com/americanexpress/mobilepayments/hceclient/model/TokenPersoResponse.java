package com.americanexpress.mobilepayments.hceclient.model;

public class TokenPersoResponse extends TokenOperationStatus {
    private String tokenConfiguration;
    private String tokenDataSignature;

    public String getTokenDataSignature() {
        return this.tokenDataSignature;
    }

    public void setTokenDataSignature(String str) {
        this.tokenDataSignature = str;
    }

    public String getTokenConfiguration() {
        return this.tokenConfiguration;
    }

    public void setTokenConfiguration(String str) {
        this.tokenConfiguration = str;
    }
}
