package com.samsung.android.visasdk.paywave.model;

public class ODAData {
    private String appFileLocator;
    private String appProfile;
    private String caPubKeyIndex;
    private String enciccPrivateKey;
    private ICCPubKeyCert iccPubKeyCert;
    private TokenBinPubKeyCert tokenBinPubKeyCert;

    public String getAppFileLocator() {
        return this.appFileLocator;
    }

    public void setAppFileLocator(String str) {
        this.appFileLocator = str;
    }

    public String getAppProfile() {
        return this.appProfile;
    }

    public void setAppProfile(String str) {
        this.appProfile = str;
    }

    public String getCaPubKeyIndex() {
        return this.caPubKeyIndex;
    }

    public void setCaPubKeyIndex(String str) {
        this.caPubKeyIndex = str;
    }

    public ICCPubKeyCert getIccPubKeyCert() {
        return this.iccPubKeyCert;
    }

    public void setIccPubKeyCert(ICCPubKeyCert iCCPubKeyCert) {
        this.iccPubKeyCert = iCCPubKeyCert;
    }

    public TokenBinPubKeyCert getTokenBinPubKeyCert() {
        return this.tokenBinPubKeyCert;
    }

    public void setTokenBinPubKeyCert(TokenBinPubKeyCert tokenBinPubKeyCert) {
        this.tokenBinPubKeyCert = tokenBinPubKeyCert;
    }

    public String getEnciccPrivateKey() {
        return this.enciccPrivateKey;
    }

    public void setEnciccPrivateKey(String str) {
        this.enciccPrivateKey = str;
    }
}
