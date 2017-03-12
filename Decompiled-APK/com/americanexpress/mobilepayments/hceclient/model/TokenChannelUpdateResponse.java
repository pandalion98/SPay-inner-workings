package com.americanexpress.mobilepayments.hceclient.model;

public class TokenChannelUpdateResponse extends TokenOperationStatus {
    private String hmac;

    public String getHmac() {
        return this.hmac;
    }

    public void setHmac(String str) {
        this.hmac = str;
    }
}
