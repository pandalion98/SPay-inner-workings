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

    public TokenKey getTokenKey() {
        return this.tokenKey;
    }

    public void setTokenKey(TokenKey tokenKey) {
        this.tokenKey = tokenKey;
    }

    public long getTimeStamp() {
        return this.timestamp;
    }

    public void setTimeStamp(long j) {
        this.timestamp = j;
    }

    public String getUnpredictableNumber() {
        return this.unpredictableNumber;
    }

    public void setUnpredictableNumber(String str) {
        this.unpredictableNumber = str;
    }

    public int getAtc() {
        return this.atc;
    }

    public void setAtc(int i) {
        this.atc = i;
    }

    public String getApi() {
        return this.api;
    }

    public void setApi(String str) {
        this.api = str;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(String str) {
        this.transactionType = str;
    }

    public String getCryptogram() {
        return this.cryptogram;
    }

    public void setCryptogram(String str) {
        this.cryptogram = str;
    }

    public String toString() {
        return "TVL: \n timestamp: " + this.timestamp + " | unpredictableNumber: " + this.unpredictableNumber + " | atc: " + this.atc + " | api: " + this.api + " | transactionType: " + this.transactionType + " | cryptogram: " + this.cryptogram;
    }
}
