package com.samsung.android.visasdk.paywave.model;

import com.google.gson.Gson;
import com.samsung.android.visasdk.p023a.JsonWraper;
import java.util.ArrayList;
import java.util.List;

public class StaticParams extends JsonWraper {
    private List<AidInfo> aidInfo;
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

    public StaticParams() {
        this.aidInfo = new ArrayList();
    }

    public List<AidInfo> getAidInfo() {
        return this.aidInfo;
    }

    public void setAidInfo(List<AidInfo> list) {
        this.aidInfo = list;
    }

    public String getKernelIdentifier() {
        return this.kernelIdentifier;
    }

    public void setKernelIdentifier(String str) {
        this.kernelIdentifier = str;
    }

    public String getCardHolderNameVCPCS() {
        return this.cardHolderNameVCPCS;
    }

    public void setCardHolderNameVCPCS(String str) {
        this.cardHolderNameVCPCS = str;
    }

    public String getPdol() {
        return this.pdol;
    }

    public void setPdol(String str) {
        this.pdol = str;
    }

    public String getCountryCode5F55() {
        return this.countryCode5F55;
    }

    public void setCountryCode5F55(String str) {
        this.countryCode5F55 = str;
    }

    public String getIssuerIdentificationNumber() {
        return this.issuerIdentificationNumber;
    }

    public void setIssuerIdentificationNumber(String str) {
        this.issuerIdentificationNumber = str;
    }

    public MsdData getMsdData() {
        return this.msdData;
    }

    public void setMsdData(MsdData msdData) {
        this.msdData = msdData;
    }

    public Track2DataDec getTrack2DataDec() {
        return this.track2DataDec;
    }

    public void setTrack2DataDec(Track2DataDec track2DataDec) {
        this.track2DataDec = track2DataDec;
    }

    public Track2DataNotDec getTrack2DataNotDec() {
        return this.track2DataNotDec;
    }

    public void setTrack2DataNotDec(Track2DataNotDec track2DataNotDec) {
        this.track2DataNotDec = track2DataNotDec;
    }

    public QVSDCData getQVSDCData() {
        return this.qVSDCData;
    }

    public void setQVSDCData(QVSDCData qVSDCData) {
        this.qVSDCData = qVSDCData;
    }

    public Mst getMst() {
        return this.mst;
    }

    public void setMst(Mst mst) {
        this.mst = mst;
    }

    public static StaticParams fromJson(String str) {
        return (StaticParams) new Gson().fromJson(str, StaticParams.class);
    }
}
