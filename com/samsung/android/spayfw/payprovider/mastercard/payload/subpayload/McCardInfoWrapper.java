package com.samsung.android.spayfw.payprovider.mastercard.payload.subpayload;

public class McCardInfoWrapper {
    private String certificateFingerprint;
    private String encryptedData;
    private String encryptedKey;
    private String iv;
    private String oaepHashingAlgorithm;
    private String panUniqueReference;
    private String publicKeyFingerprint;
    private McCardInfo unencryptedData;

    public void setEncryptedKey(String str) {
        this.encryptedKey = str;
    }

    public void setOaepHashingAlgorithm(String str) {
        this.oaepHashingAlgorithm = str;
    }

    public void setCertificateFingerprint(String str) {
        this.certificateFingerprint = str;
    }

    public void setEncryptedData(String str) {
        this.encryptedData = str;
    }

    public void setCardInfo(McCardInfo mcCardInfo) {
        this.unencryptedData = mcCardInfo;
    }

    public void setPanUniqueReference(String str) {
        this.panUniqueReference = str;
    }

    public void setIv(String str) {
        this.iv = str;
    }

    public String getEncryptedKey() {
        return this.encryptedKey;
    }

    public String getOaepHashingAlgorithm() {
        return this.oaepHashingAlgorithm;
    }

    public String getCertificateFingerprint() {
        return this.certificateFingerprint;
    }

    public String getEncryptedData() {
        return this.encryptedData;
    }

    public McCardInfo getUnenryptedData() {
        return this.unencryptedData;
    }

    public String getPanUniqueReference() {
        return this.panUniqueReference;
    }

    public String getIv() {
        return this.iv;
    }

    public String getPublicKeyFingerprint() {
        return this.publicKeyFingerprint;
    }

    public void setPublicKeyFingerprint(String str) {
        this.publicKeyFingerprint = str;
    }
}
