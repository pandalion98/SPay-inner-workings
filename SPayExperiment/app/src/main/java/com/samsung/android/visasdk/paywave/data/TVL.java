/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.data;

import com.samsung.android.visasdk.facade.data.TokenKey;

public class TVL {
    private String api;
    private int atc;
    private String cryptogram;
    private long timestamp;
    private TokenKey tokenKey;
    private String transactionType;
    private String unpredictableNumber;

    public String getApi() {
        return this.api;
    }

    public int getAtc() {
        return this.atc;
    }

    public String getCryptogram() {
        return this.cryptogram;
    }

    public long getTimeStamp() {
        return this.timestamp;
    }

    public TokenKey getTokenKey() {
        return this.tokenKey;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public String getUnpredictableNumber() {
        return this.unpredictableNumber;
    }

    public void setApi(String string) {
        this.api = string;
    }

    public void setAtc(int n2) {
        this.atc = n2;
    }

    public void setCryptogram(String string) {
        this.cryptogram = string;
    }

    public void setTimeStamp(long l2) {
        this.timestamp = l2;
    }

    public void setTokenKey(TokenKey tokenKey) {
        this.tokenKey = tokenKey;
    }

    public void setTransactionType(String string) {
        this.transactionType = string;
    }

    public void setUnpredictableNumber(String string) {
        this.unpredictableNumber = string;
    }

    public String toString() {
        return "TVL: \n timestamp: " + this.timestamp + " | unpredictableNumber: " + this.unpredictableNumber + " | atc: " + this.atc + " | api: " + this.api + " | transactionType: " + this.transactionType + " | cryptogram: " + this.cryptogram;
    }
}

