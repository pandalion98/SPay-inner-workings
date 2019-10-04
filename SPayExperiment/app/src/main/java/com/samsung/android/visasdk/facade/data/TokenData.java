/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.facade.data;

import com.google.gson.Gson;
import com.samsung.android.visasdk.a.a;

public class TokenData
extends a {
    private static final transient String TAG = "TokenData";
    private int SECOND_MILLIS = 1000;
    private String api;
    private int atc = 0;
    private int keyExpTS = 0;
    private int lukUseCount = 0;
    private int maxPmts = 0;
    private int sequenceCounter = 0;

    public TokenData() {
    }

    public TokenData(String string, String string2, int n2, int n3, int n4, int n5, int n6, int n7) {
        this.api = string2;
        this.keyExpTS = n2;
        this.atc = n3;
        this.maxPmts = n4;
        this.sequenceCounter = n5;
        this.lukUseCount = n6;
        this.SECOND_MILLIS = n7;
    }

    public static TokenData fromJson(String string) {
        return (TokenData)new Gson().fromJson(string, TokenData.class);
    }

    public String getApi() {
        return this.api;
    }

    public int getAtc() {
        return this.atc;
    }

    public int getKeyExpTS() {
        return this.keyExpTS;
    }

    public int getLukUseCount() {
        return this.lukUseCount;
    }

    public int getMaxPmts() {
        return this.maxPmts;
    }

    public int getSECOND_MILLIS() {
        return this.SECOND_MILLIS;
    }

    public int getSequenceCounter() {
        return this.sequenceCounter;
    }

    public void setApi(String string) {
        this.api = string;
    }

    public void setAtc(int n2) {
        this.atc = n2;
    }

    public void setKeyExpTS(int n2) {
        this.keyExpTS = n2;
    }

    public void setLukUseCount(int n2) {
        this.lukUseCount = n2;
    }

    public void setMaxPmts(int n2) {
        this.maxPmts = n2;
    }

    public void setSECOND_MILLIS(int n2) {
        this.SECOND_MILLIS = n2;
    }

    public void setSequenceCounter(int n2) {
        this.sequenceCounter = n2;
    }
}

