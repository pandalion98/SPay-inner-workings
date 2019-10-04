/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.model;

import com.samsung.android.visasdk.paywave.model.ICCPubKeyCert;
import com.samsung.android.visasdk.paywave.model.TokenBinPubKeyCert;

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

    public String getAppProfile() {
        return this.appProfile;
    }

    public String getCaPubKeyIndex() {
        return this.caPubKeyIndex;
    }

    public String getEnciccPrivateKey() {
        return this.enciccPrivateKey;
    }

    public ICCPubKeyCert getIccPubKeyCert() {
        return this.iccPubKeyCert;
    }

    public TokenBinPubKeyCert getTokenBinPubKeyCert() {
        return this.tokenBinPubKeyCert;
    }

    public void setAppFileLocator(String string) {
        this.appFileLocator = string;
    }

    public void setAppProfile(String string) {
        this.appProfile = string;
    }

    public void setCaPubKeyIndex(String string) {
        this.caPubKeyIndex = string;
    }

    public void setEnciccPrivateKey(String string) {
        this.enciccPrivateKey = string;
    }

    public void setIccPubKeyCert(ICCPubKeyCert iCCPubKeyCert) {
        this.iccPubKeyCert = iCCPubKeyCert;
    }

    public void setTokenBinPubKeyCert(TokenBinPubKeyCert tokenBinPubKeyCert) {
        this.tokenBinPubKeyCert = tokenBinPubKeyCert;
    }
}

