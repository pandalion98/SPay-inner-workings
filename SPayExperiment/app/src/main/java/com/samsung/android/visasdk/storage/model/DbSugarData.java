/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.storage.model;

import com.google.gson.Gson;
import com.samsung.android.visasdk.c.a;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.exception.TokenInvalidException;
import com.samsung.android.visasdk.paywave.model.DynParams;
import com.samsung.android.visasdk.paywave.model.HceData;
import com.samsung.android.visasdk.paywave.model.TokenInfo;

public class DbSugarData
extends com.samsung.android.visasdk.a.a {
    private static final transient String TAG = "DbSugarData";
    private String api;
    private int atc = 0;
    private String dki;
    private long keyExpTS = 0L;
    private int lukUseCount = 0;
    private int maxPmts = 0;
    private int sequenceCounter = 0;
    private transient TokenKey tokenKey;

    public DbSugarData() {
    }

    public DbSugarData(TokenInfo tokenInfo) {
        this.atc = 0;
        this.lukUseCount = 0;
        try {
            DynParams dynParams = tokenInfo.getHceData().getDynParams();
            this.api = dynParams.getApi();
            this.dki = dynParams.getDki();
            this.sequenceCounter = dynParams.getSc();
            this.keyExpTS = dynParams.getKeyExpTS();
            this.maxPmts = tokenInfo.getHceData().getDynParams().getMaxPmts();
            return;
        }
        catch (Exception exception) {
            throw new TokenInvalidException("cannot retrieve data from token");
        }
    }

    public static DbSugarData fromJson(String string) {
        return (DbSugarData)new Gson().fromJson(string, DbSugarData.class);
    }

    public String getApi() {
        return this.api;
    }

    public int getAtc() {
        return this.atc;
    }

    public String getDki() {
        return this.dki;
    }

    public long getKeyExpTS() {
        return this.keyExpTS;
    }

    public int getLukUseCount() {
        return this.lukUseCount;
    }

    public int getMaxPmts() {
        return this.maxPmts;
    }

    public int getSequenceCounter() {
        return this.sequenceCounter;
    }

    public TokenKey getTokenKey() {
        return this.tokenKey;
    }

    public void setApi(String string) {
        this.api = string;
    }

    public void setAtc(int n2) {
        this.atc = n2;
    }

    public void setDki(String string) {
        this.dki = string;
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

    public void setSequenceCounter(int n2) {
        this.sequenceCounter = n2;
    }

    public void setTokenKey(TokenKey tokenKey) {
        this.tokenKey = tokenKey;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void updateForReplenish(DynParams dynParams) {
        if (dynParams == null || dynParams.getApi() == null) {
            a.e(TAG, "dynParams is null");
            return;
        } else {
            if (dynParams.getApi().equals((Object)this.api)) return;
            {
                this.api = dynParams.getApi();
                this.dki = dynParams.getDki();
                this.maxPmts = dynParams.getMaxPmts();
                this.keyExpTS = dynParams.getKeyExpTS();
                this.lukUseCount = 0;
                this.sequenceCounter = 1 + this.sequenceCounter;
                return;
            }
        }
    }
}

