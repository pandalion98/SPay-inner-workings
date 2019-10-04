/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.americanexpress.sdkmodulelib.model;

import com.americanexpress.sdkmodulelib.model.TokenDataStatus;

public class TokenDataResponse {
    private String apduBlob;
    private String lupcMetadataBlob;
    private String metaDataBlob;
    private String nfcLUPCBlob;
    private String otherLUPCBlob;
    private TokenDataStatus tokenDataStatus;

    public String getApduBlob() {
        return this.apduBlob;
    }

    public String getLupcMetadataBlob() {
        return this.lupcMetadataBlob;
    }

    public String getMetaDataBlob() {
        return this.metaDataBlob;
    }

    public String getNfcLUPCBlob() {
        return this.nfcLUPCBlob;
    }

    public String getOtherLUPCBlob() {
        return this.otherLUPCBlob;
    }

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public void setApduBlob(String string) {
        this.apduBlob = string;
    }

    public void setLupcMetadataBlob(String string) {
        this.lupcMetadataBlob = string;
    }

    public void setMetaDataBlob(String string) {
        this.metaDataBlob = string;
    }

    public void setNfcLUPCBlob(String string) {
        this.nfcLUPCBlob = string;
    }

    public void setOtherLUPCBlob(String string) {
        this.otherLUPCBlob = string;
    }

    public void setTokenDataStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }
}

