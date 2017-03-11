package com.samsung.android.visasdk.paywave.model;

public class PaymentData {
    private String IccPrivKExpo;
    private String api;
    private int atc;
    private String enrollmentId;
    private int lukUseCount;
    private String token;
    private TokenInfo tokenInfo;

    public String getToken() {
        return this.token;
    }

    public void setToken(String str) {
        this.token = str;
    }

    public void setEnrollmentId(String str) {
        this.enrollmentId = str;
    }

    public String getEnrollmentId() {
        return this.enrollmentId;
    }

    public String getApi() {
        return this.api;
    }

    public void setApi(String str) {
        this.api = str;
    }

    public String getIccPrivKExpo() {
        return this.IccPrivKExpo;
    }

    public void setIccPrivKExpo(String str) {
        this.IccPrivKExpo = str;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public TokenInfo getTokenInfo() {
        return this.tokenInfo;
    }

    public int getAtc() {
        return this.atc;
    }

    public void setAtc(int i) {
        this.atc = i;
    }

    public int getLukUseCount() {
        return this.lukUseCount;
    }

    public void setLukUseCount(int i) {
        this.lukUseCount = i;
    }
}
