/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Type
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.visasdk.paywave.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DynParams {
    private String api;
    private String dki;
    private String encKeyInfo;
    private Long keyExpTS = null;
    private Integer maxPmts = null;
    private int sc;
    private List<String> tvl;

    public static String constructReplenishAckRequest(DynParams dynParams) {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson((Object)dynParams, DynParams.class);
    }

    public String getApi() {
        return this.api;
    }

    public String getDki() {
        return this.dki;
    }

    public String getEncKeyInfo() {
        return this.encKeyInfo;
    }

    public Long getKeyExpTS() {
        return this.keyExpTS;
    }

    public Integer getMaxPmts() {
        return this.maxPmts;
    }

    public int getSc() {
        return this.sc;
    }

    public List<String> getTvl() {
        if (this.tvl == null) {
            this.tvl = new ArrayList();
        }
        return this.tvl;
    }

    public void setApi(String string) {
        this.api = string;
    }

    public void setDki(String string) {
        this.dki = string;
    }

    public void setEncKeyInfo(String string) {
        this.encKeyInfo = string;
    }

    public void setKeyExpTS(Long l2) {
        this.keyExpTS = l2;
    }

    public void setMaxPmts(Integer n2) {
        this.maxPmts = n2;
    }

    public void setSc(int n2) {
        this.sc = n2;
    }

    public void setTvl(List<String> list) {
        this.tvl = list;
    }
}

