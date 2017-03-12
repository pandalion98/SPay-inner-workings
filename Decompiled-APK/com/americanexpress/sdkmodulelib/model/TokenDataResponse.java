package com.americanexpress.sdkmodulelib.model;

public class TokenDataResponse {
    private String apduBlob;
    private String lupcMetadataBlob;
    private String metaDataBlob;
    private String nfcLUPCBlob;
    private String otherLUPCBlob;
    private TokenDataStatus tokenDataStatus;

    public TokenDataStatus getTokenDataStatus() {
        return this.tokenDataStatus;
    }

    public void setTokenDataStatus(TokenDataStatus tokenDataStatus) {
        this.tokenDataStatus = tokenDataStatus;
    }

    public String getApduBlob() {
        return this.apduBlob;
    }

    public void setApduBlob(String str) {
        this.apduBlob = str;
    }

    public String getNfcLUPCBlob() {
        return this.nfcLUPCBlob;
    }

    public void setNfcLUPCBlob(String str) {
        this.nfcLUPCBlob = str;
    }

    public String getOtherLUPCBlob() {
        return this.otherLUPCBlob;
    }

    public void setOtherLUPCBlob(String str) {
        this.otherLUPCBlob = str;
    }

    public String getMetaDataBlob() {
        return this.metaDataBlob;
    }

    public void setMetaDataBlob(String str) {
        this.metaDataBlob = str;
    }

    public String getLupcMetadataBlob() {
        return this.lupcMetadataBlob;
    }

    public void setLupcMetadataBlob(String str) {
        this.lupcMetadataBlob = str;
    }
}
