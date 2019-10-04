/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.model;

import com.samsung.android.visasdk.paywave.model.ExpirationDate;
import com.samsung.android.visasdk.paywave.model.ICC;

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

    public String getAip() {
        return this.aip;
    }

    public String getAppExpDate() {
        return this.appExpDate;
    }

    public String getCapki() {
        return this.capki;
    }

    public String getCardAuthData() {
        return this.cardAuthData;
    }

    public String getIPubkCert() {
        return this.iPubkCert;
    }

    public ExpirationDate getIPubkExpirationDate() {
        return this.iPubkExpirationDate;
    }

    public String getIPubkExpo() {
        return this.iPubkExpo;
    }

    public String getIPubkRem() {
        return this.iPubkRem;
    }

    public ICC getIcc() {
        return this.icc;
    }

    public String getSdad() {
        return this.sdad;
    }

    public void setAfl(String string) {
        this.afl = string;
    }

    public void setAip(String string) {
        this.aip = string;
    }

    public void setAppExpDate(String string) {
        this.appExpDate = string;
    }

    public void setCapki(String string) {
        this.capki = string;
    }

    public void setCardAuthData(String string) {
        this.cardAuthData = string;
    }

    public void setIPubkCert(String string) {
        this.iPubkCert = string;
    }

    public void setIPubkExpirationDate(ExpirationDate expirationDate) {
        this.iPubkExpirationDate = expirationDate;
    }

    public void setIPubkExpo(String string) {
        this.iPubkExpo = string;
    }

    public void setIPubkRem(String string) {
        this.iPubkRem = string;
    }

    public void setIcc(ICC iCC) {
        this.icc = iCC;
    }

    public void setSdad(String string) {
        this.sdad = string;
    }
}

