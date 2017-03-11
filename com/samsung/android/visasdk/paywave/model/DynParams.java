package com.samsung.android.visasdk.paywave.model;

import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DynParams {
    private String api;
    private String dki;
    private String encKeyInfo;
    private Long keyExpTS;
    private Integer maxPmts;
    private int sc;
    private List<String> tvl;

    public DynParams() {
        this.keyExpTS = null;
        this.maxPmts = null;
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

    public void setEncKeyInfo(String str) {
        this.encKeyInfo = str;
    }

    public void setApi(String str) {
        this.api = str;
    }

    public void setDki(String str) {
        this.dki = str;
    }

    public void setKeyExpTS(Long l) {
        this.keyExpTS = l;
    }

    public void setMaxPmts(Integer num) {
        this.maxPmts = num;
    }

    public void setSc(int i) {
        this.sc = i;
    }

    public List<String> getTvl() {
        if (this.tvl == null) {
            this.tvl = new ArrayList();
        }
        return this.tvl;
    }

    public void setTvl(List<String> list) {
        this.tvl = list;
    }

    public static String constructReplenishAckRequest(DynParams dynParams) {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson((Object) dynParams, (Type) DynParams.class);
    }
}
