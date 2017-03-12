package com.samsung.android.visasdk.paywave.model;

public class TokenInfo {
    private String appPrgrmID;
    private String encTokenInfo;
    private ExpirationDate expirationDate;
    private HceData hceData;
    private String lang;
    private String last4;
    private Mst mst;
    private String tokenReferenceID;
    private String tokenRequestorID;
    private String tokenStatus;

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public void setTokenStatus(String str) {
        this.tokenStatus = str;
    }

    public String getTokenRequestorID() {
        return this.tokenRequestorID;
    }

    public void setTokenRequestorID(String str) {
        this.tokenRequestorID = str;
    }

    public String getTokenReferenceID() {
        return this.tokenReferenceID;
    }

    public void setTokenReferenceID(String str) {
        this.tokenReferenceID = str;
    }

    public String getEncTokenInfo() {
        return this.encTokenInfo;
    }

    public void setEncTokenInfo(String str) {
        this.encTokenInfo = str;
    }

    public String getLast4() {
        return this.last4;
    }

    public void setLast4(String str) {
        this.last4 = str;
    }

    public ExpirationDate getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(ExpirationDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getAppPrgrmID() {
        return this.appPrgrmID;
    }

    public void setAppPrgrmID(String str) {
        this.appPrgrmID = str;
    }

    public Mst getMst() {
        return this.mst;
    }

    public void setMst(Mst mst) {
        this.mst = mst;
    }

    public HceData getHceData() {
        return this.hceData;
    }

    public void setHceData(HceData hceData) {
        this.hceData = hceData;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String str) {
        this.lang = str;
    }
}
