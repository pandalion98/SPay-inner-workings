package com.americanexpress.mobilepayments.hceclient.model;

public class TokenData {
    private String encryptedTokenData;
    private String encryptedTokenSignature;
    private String initializationVector;
    private String tokenRefId;
    private String tokenStatus;

    public String getEncryptedTokenSignature() {
        return this.encryptedTokenSignature;
    }

    public void setEncryptedTokenSignature(String str) {
        this.encryptedTokenSignature = str;
    }

    public String getEncryptedTokenData() {
        return this.encryptedTokenData;
    }

    public void setEncryptedTokenData(String str) {
        this.encryptedTokenData = str;
    }

    public String getInitializationVector() {
        return this.initializationVector;
    }

    public void setInitializationVector(String str) {
        this.initializationVector = str;
    }

    public String getTokenRefId() {
        return this.tokenRefId;
    }

    public void setTokenRefId(String str) {
        this.tokenRefId = str;
    }

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public void setTokenStatus(String str) {
        this.tokenStatus = str;
    }

    public String toString() {
        return "TokenData{encryptedTokenSignature='" + this.encryptedTokenSignature + '\'' + ", encryptedTokenData='" + this.encryptedTokenData + '\'' + ", initializationVector='" + this.initializationVector + '\'' + ", tokenRefId='" + this.tokenRefId + '\'' + ", tokenStatus='" + this.tokenStatus + '\'' + '}';
    }
}
