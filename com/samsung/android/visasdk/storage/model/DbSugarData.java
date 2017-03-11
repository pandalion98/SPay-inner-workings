package com.samsung.android.visasdk.storage.model;

import com.google.gson.Gson;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.exception.TokenInvalidException;
import com.samsung.android.visasdk.p023a.JsonWraper;
import com.samsung.android.visasdk.p025c.Log;
import com.samsung.android.visasdk.paywave.model.DynParams;
import com.samsung.android.visasdk.paywave.model.TokenInfo;

public class DbSugarData extends JsonWraper {
    private static final transient String TAG = "DbSugarData";
    private String api;
    private int atc;
    private String dki;
    private long keyExpTS;
    private int lukUseCount;
    private int maxPmts;
    private int sequenceCounter;
    private transient TokenKey tokenKey;

    public DbSugarData() {
        this.sequenceCounter = 0;
        this.maxPmts = 0;
        this.keyExpTS = 0;
        this.atc = 0;
        this.lukUseCount = 0;
    }

    public DbSugarData(TokenInfo tokenInfo) {
        this.sequenceCounter = 0;
        this.maxPmts = 0;
        this.keyExpTS = 0;
        this.atc = 0;
        this.lukUseCount = 0;
        this.atc = 0;
        this.lukUseCount = 0;
        try {
            DynParams dynParams = tokenInfo.getHceData().getDynParams();
            this.api = dynParams.getApi();
            this.dki = dynParams.getDki();
            this.sequenceCounter = dynParams.getSc();
            this.keyExpTS = dynParams.getKeyExpTS().longValue();
            this.maxPmts = tokenInfo.getHceData().getDynParams().getMaxPmts().intValue();
        } catch (Exception e) {
            throw new TokenInvalidException("cannot retrieve data from token");
        }
    }

    public void setTokenKey(TokenKey tokenKey) {
        this.tokenKey = tokenKey;
    }

    public TokenKey getTokenKey() {
        return this.tokenKey;
    }

    public void setApi(String str) {
        this.api = str;
    }

    public String getApi() {
        return this.api;
    }

    public void setDki(String str) {
        this.dki = str;
    }

    public String getDki() {
        return this.dki;
    }

    public void setSequenceCounter(int i) {
        this.sequenceCounter = i;
    }

    public int getSequenceCounter() {
        return this.sequenceCounter;
    }

    public void setMaxPmts(int i) {
        this.maxPmts = i;
    }

    public int getMaxPmts() {
        return this.maxPmts;
    }

    public void setKeyExpTS(int i) {
        this.keyExpTS = (long) i;
    }

    public long getKeyExpTS() {
        return this.keyExpTS;
    }

    public void setAtc(int i) {
        this.atc = i;
    }

    public int getAtc() {
        return this.atc;
    }

    public void setLukUseCount(int i) {
        this.lukUseCount = i;
    }

    public int getLukUseCount() {
        return this.lukUseCount;
    }

    public void updateForReplenish(DynParams dynParams) {
        if (dynParams == null || dynParams.getApi() == null) {
            Log.m1301e(TAG, "dynParams is null");
        } else if (!dynParams.getApi().equals(this.api)) {
            this.api = dynParams.getApi();
            this.dki = dynParams.getDki();
            this.maxPmts = dynParams.getMaxPmts().intValue();
            this.keyExpTS = dynParams.getKeyExpTS().longValue();
            this.lukUseCount = 0;
            this.sequenceCounter++;
        }
    }

    public static DbSugarData fromJson(String str) {
        return (DbSugarData) new Gson().fromJson(str, DbSugarData.class);
    }
}
