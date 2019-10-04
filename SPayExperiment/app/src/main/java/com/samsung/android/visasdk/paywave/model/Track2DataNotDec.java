/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave.model;

public class Track2DataNotDec {
    private String pinVerField;
    private String svcCode;
    private String track2DiscData;

    public String getPinVerField() {
        return this.pinVerField;
    }

    public String getSvcCode() {
        return this.svcCode;
    }

    public String getTrack2DiscData() {
        return this.track2DiscData;
    }

    public void setPinVerField(String string) {
        this.pinVerField = string;
    }

    public void setSvcCode(String string) {
        this.svcCode = string;
    }

    public void setTrack2DiscData(String string) {
        this.track2DiscData = string;
    }

    public String toString() {
        String string = "Track2DataNotDec: \n pinVerField=" + this.pinVerField;
        String string2 = string + "\n svcCode=" + this.svcCode;
        return string2 + "\n track2DiscData=" + this.track2DiscData;
    }
}

