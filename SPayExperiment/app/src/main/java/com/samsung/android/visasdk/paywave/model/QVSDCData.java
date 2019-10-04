/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.model;

import com.samsung.android.visasdk.paywave.model.QVSDCWithODA;
import com.samsung.android.visasdk.paywave.model.QVSDCWithoutODA;

public class QVSDCData {
    private String auc;
    private String ced;
    private String cid;
    private String countryCode;
    private String ctq;
    private String cvn;
    private String digitalWalletID;
    private String ffi;
    private String psn;
    private QVSDCWithODA qVSDCWithODA;
    private QVSDCWithoutODA qVSDCWithoutODA;

    public String getAuc() {
        return this.auc;
    }

    public String getCed() {
        return this.ced;
    }

    public String getCid() {
        return this.cid;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public String getCtq() {
        return this.ctq;
    }

    public String getCvn() {
        return this.cvn;
    }

    public String getDigitalWalletID() {
        return this.digitalWalletID;
    }

    public String getFfi() {
        return this.ffi;
    }

    public String getPsn() {
        return this.psn;
    }

    public QVSDCWithoutODA getQVSDCWithoutODA() {
        return this.qVSDCWithoutODA;
    }

    public QVSDCWithODA getqVSDCWithODA() {
        return this.qVSDCWithODA;
    }

    public void setAuc(String string) {
        this.auc = string;
    }

    public void setCed(String string) {
        this.ced = string;
    }

    public void setCid(String string) {
        this.cid = string;
    }

    public void setCountryCode(String string) {
        this.countryCode = string;
    }

    public void setCtq(String string) {
        this.ctq = string;
    }

    public void setCvn(String string) {
        this.cvn = string;
    }

    public void setDigitalWalletID(String string) {
        this.digitalWalletID = string;
    }

    public void setFfi(String string) {
        this.ffi = string;
    }

    public void setPsn(String string) {
        this.psn = string;
    }

    public void setQVSDCWithoutODA(QVSDCWithoutODA qVSDCWithoutODA) {
        this.qVSDCWithoutODA = qVSDCWithoutODA;
    }

    public void setqVSDCWithODA(QVSDCWithODA qVSDCWithODA) {
        this.qVSDCWithODA = qVSDCWithODA;
    }
}

