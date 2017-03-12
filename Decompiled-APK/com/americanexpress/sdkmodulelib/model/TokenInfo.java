package com.americanexpress.sdkmodulelib.model;

public class TokenInfo {
    private String metaDataBlob;
    private String tokenDataBlob;
    private TokenDataStatus tokenDataStatus;
    private String tokenDataValidUntil;
    private String tokenDataVersion;
    private String transactionId;

    public String getTokenDataValidUntil() {
        return this.tokenDataValidUntil;
    }

    public void setTokenDataValidUntil(String str) {
        this.tokenDataValidUntil = str;
    }

    public String getTokenDataBlob() {
        return this.tokenDataBlob;
    }

    public void setTokenDataBlob(String str) {
        this.tokenDataBlob = str;
    }

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public void setTokenDataStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(String str) {
        this.transactionId = str;
    }

    public String getTokenDataVersion() {
        return this.tokenDataVersion;
    }

    public void setTokenDataVersion(String str) {
        this.tokenDataVersion = str;
    }

    public String getMetaDataBlob() {
        return this.metaDataBlob;
    }

    public void setMetaDataBlob(String str) {
        this.metaDataBlob = str;
    }
}
