/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model;

import com.americanexpress.sdkmodulelib.model.TokenDataStatus;

public class TokenInfo {
    private String metaDataBlob;
    private String tokenDataBlob;
    private TokenDataStatus tokenDataStatus;
    private String tokenDataValidUntil;
    private String tokenDataVersion;
    private String transactionId;

    public String getMetaDataBlob() {
        return this.metaDataBlob;
    }

    public String getTokenDataBlob() {
        return this.tokenDataBlob;
    }

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public String getTokenDataValidUntil() {
        return this.tokenDataValidUntil;
    }

    public String getTokenDataVersion() {
        return this.tokenDataVersion;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public void setMetaDataBlob(String string) {
        this.metaDataBlob = string;
    }

    public void setTokenDataBlob(String string) {
        this.tokenDataBlob = string;
    }

    public void setTokenDataStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }

    public void setTokenDataValidUntil(String string) {
        this.tokenDataValidUntil = string;
    }

    public void setTokenDataVersion(String string) {
        this.tokenDataVersion = string;
    }

    public void setTransactionId(String string) {
        this.transactionId = string;
    }
}

