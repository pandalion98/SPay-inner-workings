/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.model;

import com.samsung.android.visasdk.paywave.model.ExpirationDate;
import com.samsung.android.visasdk.paywave.model.HceData;
import com.samsung.android.visasdk.paywave.model.Mst;

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

    public String getAppPrgrmID() {
        return this.appPrgrmID;
    }

    public String getEncTokenInfo() {
        return this.encTokenInfo;
    }

    public ExpirationDate getExpirationDate() {
        return this.expirationDate;
    }

    public HceData getHceData() {
        return this.hceData;
    }

    public String getLang() {
        return this.lang;
    }

    public String getLast4() {
        return this.last4;
    }

    public Mst getMst() {
        return this.mst;
    }

    public String getTokenReferenceID() {
        return this.tokenReferenceID;
    }

    public String getTokenRequestorID() {
        return this.tokenRequestorID;
    }

    public String getTokenStatus() {
        return this.tokenStatus;
    }

    public void setAppPrgrmID(String string) {
        this.appPrgrmID = string;
    }

    public void setEncTokenInfo(String string) {
        this.encTokenInfo = string;
    }

    public void setExpirationDate(ExpirationDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setHceData(HceData hceData) {
        this.hceData = hceData;
    }

    public void setLang(String string) {
        this.lang = string;
    }

    public void setLast4(String string) {
        this.last4 = string;
    }

    public void setMst(Mst mst) {
        this.mst = mst;
    }

    public void setTokenReferenceID(String string) {
        this.tokenReferenceID = string;
    }

    public void setTokenRequestorID(String string) {
        this.tokenRequestorID = string;
    }

    public void setTokenStatus(String string) {
        this.tokenStatus = string;
    }
}

