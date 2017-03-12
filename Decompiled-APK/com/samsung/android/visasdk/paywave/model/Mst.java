package com.samsung.android.visasdk.paywave.model;

public class Mst {
    private String cvv;
    private String svcCode;

    public String getSvcCode() {
        return this.svcCode;
    }

    public String getCvv() {
        return this.cvv;
    }

    public void setSvcCode(String str) {
        this.svcCode = str;
    }

    public void setCvv(String str) {
        this.cvv = str;
    }
}
