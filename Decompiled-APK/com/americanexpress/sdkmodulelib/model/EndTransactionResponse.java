package com.americanexpress.sdkmodulelib.model;

public class EndTransactionResponse {
    private String inAppTransactionId;
    private String lupcMetaDataBlob;
    private String nfcTransactionId;
    private String otherTransactionId;
    private TokenDataStatus tokenDataStatus;

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public void setTokenDataStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }

    public String getLupcMetaDataBlob() {
        return this.lupcMetaDataBlob;
    }

    public void setLupcMetaDataBlob(String str) {
        this.lupcMetaDataBlob = str;
    }

    public String getNfcTransactionId() {
        return this.nfcTransactionId;
    }

    public void setNfcTransactionId(String str) {
        this.nfcTransactionId = str;
    }

    public String getOtherTransactionId() {
        return this.otherTransactionId;
    }

    public void setOtherTransactionId(String str) {
        this.otherTransactionId = str;
    }

    public String getInAppTransactionId() {
        return this.inAppTransactionId;
    }

    public void setInAppTransactionId(String str) {
        this.inAppTransactionId = str;
    }
}
