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

    public void setPinVerField(String str) {
        this.pinVerField = str;
    }

    public void setSvcCode(String str) {
        this.svcCode = str;
    }

    public void setTrack2DiscData(String str) {
        this.track2DiscData = str;
    }

    public String toString() {
        return (("Track2DataNotDec: \n pinVerField=" + this.pinVerField) + "\n svcCode=" + this.svcCode) + "\n track2DiscData=" + this.track2DiscData;
    }
}
