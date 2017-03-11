package com.samsung.android.visasdk.paywave.model;

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

    public String getCtq() {
        return this.ctq;
    }

    public void setCtq(String str) {
        this.ctq = str;
    }

    public String getFfi() {
        return this.ffi;
    }

    public void setFfi(String str) {
        this.ffi = str;
    }

    public String getAuc() {
        return this.auc;
    }

    public void setAuc(String str) {
        this.auc = str;
    }

    public String getPsn() {
        return this.psn;
    }

    public void setPsn(String str) {
        this.psn = str;
    }

    public String getCvn() {
        return this.cvn;
    }

    public void setCvn(String str) {
        this.cvn = str;
    }

    public String getDigitalWalletID() {
        return this.digitalWalletID;
    }

    public void setDigitalWalletID(String str) {
        this.digitalWalletID = str;
    }

    public String getCed() {
        return this.ced;
    }

    public void setCed(String str) {
        this.ced = str;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String str) {
        this.countryCode = str;
    }

    public String getCid() {
        return this.cid;
    }

    public void setCid(String str) {
        this.cid = str;
    }

    public QVSDCWithoutODA getQVSDCWithoutODA() {
        return this.qVSDCWithoutODA;
    }

    public void setQVSDCWithoutODA(QVSDCWithoutODA qVSDCWithoutODA) {
        this.qVSDCWithoutODA = qVSDCWithoutODA;
    }

    public QVSDCWithODA getqVSDCWithODA() {
        return this.qVSDCWithODA;
    }

    public void setqVSDCWithODA(QVSDCWithODA qVSDCWithODA) {
        this.qVSDCWithODA = qVSDCWithODA;
    }
}
