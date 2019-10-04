/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.model;

public class AidInfo {
    private String CVMrequired;
    private String aid;
    private String applicationLabel;
    private String cap;
    private String priority;

    public String getAid() {
        return this.aid;
    }

    public String getApplicationLabel() {
        return this.applicationLabel;
    }

    public String getCVMrequired() {
        return this.CVMrequired;
    }

    public String getCap() {
        return this.cap;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setAid(String string) {
        this.aid = string;
    }

    public void setApplicationLabel(String string) {
        this.applicationLabel = string;
    }

    public void setCVMrequired(String string) {
        this.CVMrequired = string;
    }

    public void setCap(String string) {
        this.cap = string;
    }

    public void setPriority(String string) {
        this.priority = string;
    }
}

