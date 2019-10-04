/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.model;

import com.samsung.android.visasdk.paywave.model.TokenInfo;

public class PaymentData {
    private String IccPrivKExpo;
    private String api;
    private int atc;
    private String enrollmentId;
    private int lukUseCount;
    private String token;
    private TokenInfo tokenInfo;

    public String getApi() {
        return this.api;
    }

    public int getAtc() {
        return this.atc;
    }

    public String getEnrollmentId() {
        return this.enrollmentId;
    }

    public String getIccPrivKExpo() {
        return this.IccPrivKExpo;
    }

    public int getLukUseCount() {
        return this.lukUseCount;
    }

    public String getToken() {
        return this.token;
    }

    public TokenInfo getTokenInfo() {
        return this.tokenInfo;
    }

    public void setApi(String string) {
        this.api = string;
    }

    public void setAtc(int n2) {
        this.atc = n2;
    }

    public void setEnrollmentId(String string) {
        this.enrollmentId = string;
    }

    public void setIccPrivKExpo(String string) {
        this.IccPrivKExpo = string;
    }

    public void setLukUseCount(int n2) {
        this.lukUseCount = n2;
    }

    public void setToken(String string) {
        this.token = string;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }
}

