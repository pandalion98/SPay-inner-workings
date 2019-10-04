/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model;

import com.americanexpress.sdkmodulelib.model.TokenDataStatus;

public class EndTransactionResponse {
    private String inAppTransactionId;
    private String lupcMetaDataBlob;
    private String nfcTransactionId;
    private String otherTransactionId;
    private TokenDataStatus tokenDataStatus;

    public String getInAppTransactionId() {
        return this.inAppTransactionId;
    }

    public String getLupcMetaDataBlob() {
        return this.lupcMetaDataBlob;
    }

    public String getNfcTransactionId() {
        return this.nfcTransactionId;
    }

    public String getOtherTransactionId() {
        return this.otherTransactionId;
    }

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public void setInAppTransactionId(String string) {
        this.inAppTransactionId = string;
    }

    public void setLupcMetaDataBlob(String string) {
        this.lupcMetaDataBlob = string;
    }

    public void setNfcTransactionId(String string) {
        this.nfcTransactionId = string;
    }

    public void setOtherTransactionId(String string) {
        this.otherTransactionId = string;
    }

    public void setTokenDataStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }
}

