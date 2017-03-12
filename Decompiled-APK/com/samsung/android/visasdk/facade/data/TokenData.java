package com.samsung.android.visasdk.facade.data;

import com.google.android.gms.location.LocationStatusCodes;
import com.google.gson.Gson;
import com.samsung.android.visasdk.p023a.JsonWraper;

public class TokenData extends JsonWraper {
    private static final transient String TAG = "TokenData";
    private int SECOND_MILLIS;
    private String api;
    private int atc;
    private int keyExpTS;
    private int lukUseCount;
    private int maxPmts;
    private int sequenceCounter;

    public TokenData() {
        this.SECOND_MILLIS = LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
        this.atc = 0;
        this.keyExpTS = 0;
        this.lukUseCount = 0;
        this.maxPmts = 0;
        this.sequenceCounter = 0;
    }

    public TokenData(String str, String str2, int i, int i2, int i3, int i4, int i5, int i6) {
        this.SECOND_MILLIS = LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
        this.atc = 0;
        this.keyExpTS = 0;
        this.lukUseCount = 0;
        this.maxPmts = 0;
        this.sequenceCounter = 0;
        this.api = str2;
        this.keyExpTS = i;
        this.atc = i2;
        this.maxPmts = i3;
        this.sequenceCounter = i4;
        this.lukUseCount = i5;
        this.SECOND_MILLIS = i6;
    }

    public String getApi() {
        return this.api;
    }

    public void setApi(String str) {
        this.api = str;
    }

    public int getKeyExpTS() {
        return this.keyExpTS;
    }

    public void setKeyExpTS(int i) {
        this.keyExpTS = i;
    }

    public int getAtc() {
        return this.atc;
    }

    public void setAtc(int i) {
        this.atc = i;
    }

    public int getMaxPmts() {
        return this.maxPmts;
    }

    public void setMaxPmts(int i) {
        this.maxPmts = i;
    }

    public int getSequenceCounter() {
        return this.sequenceCounter;
    }

    public void setSequenceCounter(int i) {
        this.sequenceCounter = i;
    }

    public int getLukUseCount() {
        return this.lukUseCount;
    }

    public void setLukUseCount(int i) {
        this.lukUseCount = i;
    }

    public int getSECOND_MILLIS() {
        return this.SECOND_MILLIS;
    }

    public void setSECOND_MILLIS(int i) {
        this.SECOND_MILLIS = i;
    }

    public static TokenData fromJson(String str) {
        return (TokenData) new Gson().fromJson(str, TokenData.class);
    }
}
