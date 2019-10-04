/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.plcc.domain;

public class PlccCard {
    private String defaultSequenceConfig;
    private String merchantId;
    private String providerKey;
    private String timestamp;
    private String tokenStatus;
    private String trTokenId;
    private String tzEncCard;

    public PlccCard() {
    }

    public PlccCard(String string, String string2, String string3, String string4) {
        this.providerKey = string;
        this.tzEncCard = string2;
        this.merchantId = string3;
        this.tokenStatus = string4;
    }

    public PlccCard(String string, String string2, String string3, String string4, String string5, String string6, String string7) {
        this.providerKey = string;
        this.tzEncCard = string2;
        this.merchantId = string3;
        this.tokenStatus = string4;
        this.defaultSequenceConfig = string5;
        this.trTokenId = string6;
        this.timestamp = string7;
    }

    public String getDefaultSequenceConfig() {
        return this.defaultSequenceConfig;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public String getProviderKey() {
        return this.providerKey;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public String getTrTokenId() {
        return this.trTokenId;
    }

    public String getTzEncCard() {
        return this.tzEncCard;
    }

    public void setDefaultSequenceConfig(String string) {
        this.defaultSequenceConfig = string;
    }

    public void setMerchantId(String string) {
        this.merchantId = string;
    }

    public void setProviderKey(String string) {
        this.providerKey = string;
    }

    public void setTimestamp(String string) {
        this.timestamp = string;
    }

    public void setTokenStatus(String string) {
        this.tokenStatus = string;
    }

    public void setTrTokenId(String string) {
        this.trTokenId = string;
    }

    public void setTzEncCard(String string) {
        this.tzEncCard = string;
    }
}

