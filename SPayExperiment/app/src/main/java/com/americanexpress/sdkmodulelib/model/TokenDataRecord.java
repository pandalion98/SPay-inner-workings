/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
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

    public String getApduBlob() {
        return this.apduBlob;
    }

    public String getInAppTID() {
        return this.inAppTID;
    }

    public String getLupcMetadataBlob() {
        return this.lupcMetadataBlob;
    }

    public String getMetaDataBlob() {
        return this.metaDataBlob;
    }

    public String getNfcAtc() {
        return this.nfcAtc;
    }

    public String getNfcCryptogram() {
        return this.nfcCryptogram;
    }

    public String getNfcLUPCBlob() {
        return this.nfcLUPCBlob;
    }

    public String getNfcUnpredictableNumber() {
        return this.nfcUnpredictableNumber;
    }

    public String getOtherLUPCBlob() {
        return this.otherLUPCBlob;
    }

    public String getOtherTID() {
        return this.otherTID;
    }

    public String getPaymentPayload() {
        return this.paymentPayload;
    }

    public String getTokenRefId() {
        return this.tokenRefId;
    }

    public void setApduBlob(String string) {
        this.apduBlob = string;
    }

    public void setInAppTID(String string) {
        this.inAppTID = string;
    }

    public void setLupcMetadataBlob(String string) {
        this.lupcMetadataBlob = string;
    }

    public void setMetaDataBlob(String string) {
        this.metaDataBlob = string;
    }

    public void setNfcAtc(String string) {
        this.nfcAtc = string;
    }

    public void setNfcCryptogram(String string) {
        this.nfcCryptogram = string;
    }

    public void setNfcLUPCBlob(String string) {
        this.nfcLUPCBlob = string;
    }

    public void setNfcUnpredictableNumber(String string) {
        this.nfcUnpredictableNumber = string;
    }

    public void setOtherLUPCBlob(String string) {
        this.otherLUPCBlob = string;
    }

    public void setOtherTID(String string) {
        this.otherTID = string;
    }

    public void setPaymentPayload(String string) {
        this.paymentPayload = string;
    }

    public void setTokenRefId(String string) {
        this.tokenRefId = string;
    }
}

