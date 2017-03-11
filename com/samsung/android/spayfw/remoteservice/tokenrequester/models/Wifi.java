package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Wifi {
    private String bssid;
    private String centerFreq0;
    private String centerFreq1;
    private String channelWidth;
    private String distance;
    private String frequency;
    private String operatorFriendlyName;
    private String rssi;
    private String ssid;
    private String time;
    private String venueName;

    public Wifi(String str, String str2, String str3, String str4, String str5, String str6) {
        this.bssid = str;
        this.ssid = str2;
        this.rssi = str3;
        this.time = str4;
        this.frequency = str5;
        this.distance = str6;
    }

    public String toString() {
        return "Wifi{bssid='" + this.bssid + '\'' + ", ssid='" + this.ssid + '\'' + ", rssi='" + this.rssi + '\'' + ", time='" + this.time + '\'' + ", distance='" + this.distance + '\'' + ", frequency='" + this.frequency + '\'' + ", centerFreq0='" + this.centerFreq0 + '\'' + ", centerFreq1='" + this.centerFreq1 + '\'' + ", channelWidth='" + this.channelWidth + '\'' + ", operatorFriendlyName='" + this.operatorFriendlyName + '\'' + ", venueName='" + this.venueName + '\'' + '}';
    }

    public String getDistance() {
        return this.distance;
    }

    public void setCenterFreq1(String str) {
        this.centerFreq1 = str;
    }

    public void setCenterFreq0(String str) {
        this.centerFreq0 = str;
    }

    public void setChannelWidth(String str) {
        this.channelWidth = str;
    }

    public void setOperatorFriendlyName(String str) {
        this.operatorFriendlyName = str;
    }

    public void setVenueName(String str) {
        this.venueName = str;
    }

    public String getBssid() {
        return this.bssid;
    }

    public String getSsid() {
        return this.ssid;
    }

    public String getRssi() {
        return this.rssi;
    }

    public String getFrequency() {
        return this.frequency;
    }
}
