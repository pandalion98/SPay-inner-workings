package com.samsung.android.spayfw.payprovider.plcc.domain;

public class PlccCard {
    private String defaultSequenceConfig;
    private String merchantId;
    private String providerKey;
    private String timestamp;
    private String tokenStatus;
    private String trTokenId;
    private String tzEncCard;

    public PlccCard(String str, String str2, String str3, String str4) {
        this.providerKey = str;
        this.tzEncCard = str2;
        this.merchantId = str3;
        this.tokenStatus = str4;
    }

    public PlccCard(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.providerKey = str;
        this.tzEncCard = str2;
        this.merchantId = str3;
        this.tokenStatus = str4;
        this.defaultSequenceConfig = str5;
        this.trTokenId = str6;
        this.timestamp = str7;
    }

    public String getTrTokenId() {
        return this.trTokenId;
    }

    public void setTrTokenId(String str) {
        this.trTokenId = str;
    }

    public String getTzEncCard() {
        return this.tzEncCard;
    }

    public void setTzEncCard(String str) {
        this.tzEncCard = str;
    }

    public String getProviderKey() {
        return this.providerKey;
    }

    public void setProviderKey(String str) {
        this.providerKey = str;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(String str) {
        this.merchantId = str;
    }

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public void setTokenStatus(String str) {
        this.tokenStatus = str;
    }

    public String getDefaultSequenceConfig() {
        return this.defaultSequenceConfig;
    }

    public void setDefaultSequenceConfig(String str) {
        this.defaultSequenceConfig = str;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String str) {
        this.timestamp = str;
    }
}
