package com.americanexpress.mobilepayments.hceclient.model;

public class TokenChannelInitializeResponse extends TokenOperationStatus {
    private String ephemeralData;

    public String getEphemeralData() {
        return this.ephemeralData;
    }

    public void setEphemeralData(String str) {
        this.ephemeralData = str;
    }
}
