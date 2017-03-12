package com.samsung.android.visasdk.paywave.model;

public class AidInfo {
    private String CVMrequired;
    private String aid;
    private String applicationLabel;
    private String cap;
    private String priority;

    public String getApplicationLabel() {
        return this.applicationLabel;
    }

    public String getAid() {
        return this.aid;
    }

    public String getPriority() {
        return this.priority;
    }

    public String getCap() {
        return this.cap;
    }

    public void setApplicationLabel(String str) {
        this.applicationLabel = str;
    }

    public void setAid(String str) {
        this.aid = str;
    }

    public void setPriority(String str) {
        this.priority = str;
    }

    public void setCap(String str) {
        this.cap = str;
    }

    public String getCVMrequired() {
        return this.CVMrequired;
    }

    public void setCVMrequired(String str) {
        this.CVMrequired = str;
    }
}
