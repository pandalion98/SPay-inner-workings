/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

import com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload.McCardInfo;

public class McCardInfoWrapper {
    private String certificateFingerprint;
    private String encryptedData;
    private String encryptedKey;
    private String iv;
    private String oaepHashingAlgorithm;
    private String panUniqueReference;
    private String publicKeyFingerprint;
    private McCardInfo unencryptedData;

    public String getCertificateFingerprint() {
        return this.certificateFingerprint;
    }

    public String getEncryptedData() {
        return this.encryptedData;
    }

    public String getEncryptedKey() {
        return this.encryptedKey;
    }

    public String getIv() {
        return this.iv;
    }

    public String getOaepHashingAlgorithm() {
        return this.oaepHashingAlgorithm;
    }

    public String getPanUniqueReference() {
        return this.panUniqueReference;
    }

    public String getPublicKeyFingerprint() {
        return this.publicKeyFingerprint;
    }

    public McCardInfo getUnenryptedData() {
        return this.unencryptedData;
    }

    public void setCardInfo(McCardInfo mcCardInfo) {
        this.unencryptedData = mcCardInfo;
    }

    public void setCertificateFingerprint(String string) {
        this.certificateFingerprint = string;
    }

    public void setEncryptedData(String string) {
        this.encryptedData = string;
    }

    public void setEncryptedKey(String string) {
        this.encryptedKey = string;
    }

    public void setIv(String string) {
        this.iv = string;
    }

    public void setOaepHashingAlgorithm(String string) {
        this.oaepHashingAlgorithm = string;
    }

    public void setPanUniqueReference(String string) {
        this.panUniqueReference = string;
    }

    public void setPublicKeyFingerprint(String string) {
        this.publicKeyFingerprint = string;
    }
}

