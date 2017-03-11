package com.samsung.android.visasdk.paywave.model;

public class QVSDCWithODA {
    private String afl;
    private String aip;
    private String appExpDate;
    private String capki;
    private String cardAuthData;
    private String iPubkCert;
    private ExpirationDate iPubkExpirationDate;
    private String iPubkExpo;
    private String iPubkRem;
    private ICC icc;
    private String sdad;

    public String getAfl() {
        return this.afl;
    }

    public void setAfl(String str) {
        this.afl = str;
    }

    public String getAip() {
        return this.aip;
    }

    public void setAip(String str) {
        this.aip = str;
    }

    public String getCapki() {
        return this.capki;
    }

    public void setCapki(String str) {
        this.capki = str;
    }

    public String getIPubkCert() {
        return this.iPubkCert;
    }

    public void setIPubkCert(String str) {
        this.iPubkCert = str;
    }

    public String getIPubkExpo() {
        return this.iPubkExpo;
    }

    public void setIPubkExpo(String str) {
        this.iPubkExpo = str;
    }

    public String getIPubkRem() {
        return this.iPubkRem;
    }

    public void setIPubkRem(String str) {
        this.iPubkRem = str;
    }

    public String getAppExpDate() {
        return this.appExpDate;
    }

    public void setAppExpDate(String str) {
        this.appExpDate = str;
    }

    public String getCardAuthData() {
        return this.cardAuthData;
    }

    public void setCardAuthData(String str) {
        this.cardAuthData = str;
    }

    public String getSdad() {
        return this.sdad;
    }

    public void setSdad(String str) {
        this.sdad = str;
    }

    public ICC getIcc() {
        return this.icc;
    }

    public void setIcc(ICC icc) {
        this.icc = icc;
    }

    public ExpirationDate getIPubkExpirationDate() {
        return this.iPubkExpirationDate;
    }

    public void setIPubkExpirationDate(ExpirationDate expirationDate) {
        this.iPubkExpirationDate = expirationDate;
    }
}
