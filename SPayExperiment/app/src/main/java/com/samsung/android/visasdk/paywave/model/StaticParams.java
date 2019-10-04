/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.visasdk.paywave.model;

import com.google.gson.Gson;
import com.samsung.android.visasdk.a.a;
import com.samsung.android.visasdk.paywave.model.AidInfo;
import com.samsung.android.visasdk.paywave.model.MsdData;
import com.samsung.android.visasdk.paywave.model.Mst;
import com.samsung.android.visasdk.paywave.model.QVSDCData;
import com.samsung.android.visasdk.paywave.model.Track2DataDec;
import com.samsung.android.visasdk.paywave.model.Track2DataNotDec;
import java.util.ArrayList;
import java.util.List;

public class StaticParams
extends a {
    private List<AidInfo> aidInfo = new ArrayList();
    private String cardHolderNameVCPCS;
    private String countryCode5F55;
    private String issuerIdentificationNumber;
    private String kernelIdentifier;
    private MsdData msdData;
    private Mst mst;
    private String pdol;
    private QVSDCData qVSDCData;
    private Track2DataDec track2DataDec;
    private Track2DataNotDec track2DataNotDec;

    public static StaticParams fromJson(String string) {
        return (StaticParams)new Gson().fromJson(string, StaticParams.class);
    }

    public List<AidInfo> getAidInfo() {
        return this.aidInfo;
    }

    public String getCardHolderNameVCPCS() {
        return this.cardHolderNameVCPCS;
    }

    public String getCountryCode5F55() {
        return this.countryCode5F55;
    }

    public String getIssuerIdentificationNumber() {
        return this.issuerIdentificationNumber;
    }

    public String getKernelIdentifier() {
        return this.kernelIdentifier;
    }

    public MsdData getMsdData() {
        return this.msdData;
    }

    public Mst getMst() {
        return this.mst;
    }

    public String getPdol() {
        return this.pdol;
    }

    public QVSDCData getQVSDCData() {
        return this.qVSDCData;
    }

    public Track2DataDec getTrack2DataDec() {
        return this.track2DataDec;
    }

    public Track2DataNotDec getTrack2DataNotDec() {
        return this.track2DataNotDec;
    }

    public void setAidInfo(List<AidInfo> list) {
        this.aidInfo = list;
    }

    public void setCardHolderNameVCPCS(String string) {
        this.cardHolderNameVCPCS = string;
    }

    public void setCountryCode5F55(String string) {
        this.countryCode5F55 = string;
    }

    public void setIssuerIdentificationNumber(String string) {
        this.issuerIdentificationNumber = string;
    }

    public void setKernelIdentifier(String string) {
        this.kernelIdentifier = string;
    }

    public void setMsdData(MsdData msdData) {
        this.msdData = msdData;
    }

    public void setMst(Mst mst) {
        this.mst = mst;
    }

    public void setPdol(String string) {
        this.pdol = string;
    }

    public void setQVSDCData(QVSDCData qVSDCData) {
        this.qVSDCData = qVSDCData;
    }

    public void setTrack2DataDec(Track2DataDec track2DataDec) {
        this.track2DataDec = track2DataDec;
    }

    public void setTrack2DataNotDec(Track2DataNotDec track2DataNotDec) {
        this.track2DataNotDec = track2DataNotDec;
    }
}

