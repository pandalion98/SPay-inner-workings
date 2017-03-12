package com.samsung.android.visasdk.paywave.model;

import com.samsung.android.visasdk.p023a.JsonWraper;

public class ICC extends JsonWraper {
    private String encIccCRTPrivKey;
    private String iccCRTCoeffDModP;
    private String iccCRTCoeffDModQ;
    private String iccCRTCoeffQModP;
    private String iccCRTprimep;
    private String iccCRTprimeq;
    private String iccKeymod;
    private String iccPrivKExpo;
    private String iccPubKCert;
    private ExpirationDate iccPubKExpirationDate;
    private String iccPubKExpo;
    private String iccPubKRem;

    public String getEncIccCRTPrivKey() {
        return this.encIccCRTPrivKey;
    }

    public void setEncIccCRTPrivKey(String str) {
        this.encIccCRTPrivKey = str;
    }

    public String getIccPubKCert() {
        return this.iccPubKCert;
    }

    public void setIccPubKCert(String str) {
        this.iccPubKCert = str;
    }

    public String getIccPubKExpo() {
        return this.iccPubKExpo;
    }

    public void setIccPubKExpo(String str) {
        this.iccPubKExpo = str;
    }

    public String getIccPubKRem() {
        return this.iccPubKRem;
    }

    public void setIccPubKRem(String str) {
        this.iccPubKRem = str;
    }

    public String getIccPrivKExpo() {
        return this.iccPrivKExpo;
    }

    public void setIccPrivKExpo(String str) {
        this.iccPrivKExpo = str;
    }

    public String getIccKeymod() {
        return this.iccKeymod;
    }

    public void setIccKeymod(String str) {
        this.iccKeymod = str;
    }

    public String getIccCRTCoeffQModP() {
        return this.iccCRTCoeffQModP;
    }

    public void setIccCRTCoeffQModP(String str) {
        this.iccCRTCoeffQModP = str;
    }

    public String getIccCRTCoeffDModQ() {
        return this.iccCRTCoeffDModQ;
    }

    public void setIccCRTCoeffDModQ(String str) {
        this.iccCRTCoeffDModQ = str;
    }

    public String getIccCRTCoeffDModP() {
        return this.iccCRTCoeffDModP;
    }

    public void setIccCRTCoeffDModP(String str) {
        this.iccCRTCoeffDModP = str;
    }

    public String getIccCRTprimep() {
        return this.iccCRTprimep;
    }

    public void setIccCRTprimep(String str) {
        this.iccCRTprimep = str;
    }

    public String getIccCRTprimeq() {
        return this.iccCRTprimeq;
    }

    public void setIccCRTprimeq(String str) {
        this.iccCRTprimeq = str;
    }

    public ExpirationDate getIccPubKExpirationDate() {
        return this.iccPubKExpirationDate;
    }

    public void setIccPubKExpirationDate(ExpirationDate expirationDate) {
        this.iccPubKExpirationDate = expirationDate;
    }
}
