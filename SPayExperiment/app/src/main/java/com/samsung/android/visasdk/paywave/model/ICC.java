/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.model;

import com.samsung.android.visasdk.a.a;
import com.samsung.android.visasdk.paywave.model.ExpirationDate;

public class ICC
extends a {
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

    public String getIccCRTCoeffDModP() {
        return this.iccCRTCoeffDModP;
    }

    public String getIccCRTCoeffDModQ() {
        return this.iccCRTCoeffDModQ;
    }

    public String getIccCRTCoeffQModP() {
        return this.iccCRTCoeffQModP;
    }

    public String getIccCRTprimep() {
        return this.iccCRTprimep;
    }

    public String getIccCRTprimeq() {
        return this.iccCRTprimeq;
    }

    public String getIccKeymod() {
        return this.iccKeymod;
    }

    public String getIccPrivKExpo() {
        return this.iccPrivKExpo;
    }

    public String getIccPubKCert() {
        return this.iccPubKCert;
    }

    public ExpirationDate getIccPubKExpirationDate() {
        return this.iccPubKExpirationDate;
    }

    public String getIccPubKExpo() {
        return this.iccPubKExpo;
    }

    public String getIccPubKRem() {
        return this.iccPubKRem;
    }

    public void setEncIccCRTPrivKey(String string) {
        this.encIccCRTPrivKey = string;
    }

    public void setIccCRTCoeffDModP(String string) {
        this.iccCRTCoeffDModP = string;
    }

    public void setIccCRTCoeffDModQ(String string) {
        this.iccCRTCoeffDModQ = string;
    }

    public void setIccCRTCoeffQModP(String string) {
        this.iccCRTCoeffQModP = string;
    }

    public void setIccCRTprimep(String string) {
        this.iccCRTprimep = string;
    }

    public void setIccCRTprimeq(String string) {
        this.iccCRTprimeq = string;
    }

    public void setIccKeymod(String string) {
        this.iccKeymod = string;
    }

    public void setIccPrivKExpo(String string) {
        this.iccPrivKExpo = string;
    }

    public void setIccPubKCert(String string) {
        this.iccPubKCert = string;
    }

    public void setIccPubKExpirationDate(ExpirationDate expirationDate) {
        this.iccPubKExpirationDate = expirationDate;
    }

    public void setIccPubKExpo(String string) {
        this.iccPubKExpo = string;
    }

    public void setIccPubKRem(String string) {
        this.iccPubKRem = string;
    }
}

