package com.americanexpress.sdkmodulelib.model;

public class TokenDataRecord {
    private String apduBlob;
    private String inAppTID;
    private String lupcMetadataBlob;
    private String metaDataBlob;
    private String nfcAtc;
    private String nfcCryptogram;
    private String nfcLUPCBlob;
    private String nfcUnpredictableNumber;
    private String otherLUPCBlob;
    private String otherTID;
    private String paymentPayload;
    private String tokenRefId;
    private int transactionType;

    public String getTokenRefId() {
        return this.tokenRefId;
    }

    public void setTokenRefId(String str) {
        this.tokenRefId = str;
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

    public String getNfcAtc() {
        return this.nfcAtc;
    }

    public void setNfcAtc(String str) {
        this.nfcAtc = str;
    }

    public String getNfcUnpredictableNumber() {
        return this.nfcUnpredictableNumber;
    }

    public void setNfcUnpredictableNumber(String str) {
        this.nfcUnpredictableNumber = str;
    }

    public String getNfcCryptogram() {
        return this.nfcCryptogram;
    }

    public void setNfcCryptogram(String str) {
        this.nfcCryptogram = str;
    }

    public String getOtherTID() {
        return this.otherTID;
    }

    public void setOtherTID(String str) {
        this.otherTID = str;
    }

    public String getPaymentPayload() {
        return this.paymentPayload;
    }

    public void setPaymentPayload(String str) {
        this.paymentPayload = str;
    }

    public String getInAppTID() {
        return this.inAppTID;
    }

    public void setInAppTID(String str) {
        this.inAppTID = str;
    }
}
