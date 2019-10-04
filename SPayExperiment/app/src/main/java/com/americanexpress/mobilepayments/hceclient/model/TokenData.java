/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.mobilepayments.hceclient.model;

public class TokenData {
    private String encryptedTokenData;
    private String encryptedTokenSignature;
    private String initializationVector;
    private String tokenRefId;
    private String tokenStatus;

    public String getEncryptedTokenData() {
        return this.encryptedTokenData;
    }

    public String getEncryptedTokenSignature() {
        return this.encryptedTokenSignature;
    }

    public String getInitializationVector() {
        return this.initializationVector;
    }

    public String getTokenRefId() {
        return this.tokenRefId;
    }

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public void setEncryptedTokenData(String string) {
        this.encryptedTokenData = string;
    }

    public void setEncryptedTokenSignature(String string) {
        this.encryptedTokenSignature = string;
    }

    public void setInitializationVector(String string) {
        this.initializationVector = string;
    }

    public void setTokenRefId(String string) {
        this.tokenRefId = string;
    }

    public void setTokenStatus(String string) {
        this.tokenStatus = string;
    }

    public String toString() {
        return "TokenData{encryptedTokenSignature='" + this.encryptedTokenSignature + '\'' + ", encryptedTokenData='" + this.encryptedTokenData + '\'' + ", initializationVector='" + this.initializationVector + '\'' + ", tokenRefId='" + this.tokenRefId + '\'' + ", tokenStatus='" + this.tokenStatus + '\'' + '}';
    }
}

