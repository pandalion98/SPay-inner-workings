/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
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

    public Wifi(String string, String string2, String string3, String string4, String string5, String string6) {
        this.bssid = string;
        this.ssid = string2;
        this.rssi = string3;
        this.time = string4;
        this.frequency = string5;
        this.distance = string6;
    }

    public String getBssid() {
        return this.bssid;
    }

    public String getDistance() {
        return this.distance;
    }

    public String getFrequency() {
        return this.frequency;
    }

    public String getRssi() {
        return this.rssi;
    }

    public String getSsid() {
        return this.ssid;
    }

    public void setCenterFreq0(String string) {
        this.centerFreq0 = string;
    }

    public void setCenterFreq1(String string) {
        this.centerFreq1 = string;
    }

    public void setChannelWidth(String string) {
        this.channelWidth = string;
    }

    public void setOperatorFriendlyName(String string) {
        this.operatorFriendlyName = string;
    }

    public void setVenueName(String string) {
        this.venueName = string;
    }

    public String toString() {
        return "Wifi{bssid='" + this.bssid + '\'' + ", ssid='" + this.ssid + '\'' + ", rssi='" + this.rssi + '\'' + ", time='" + this.time + '\'' + ", distance='" + this.distance + '\'' + ", frequency='" + this.frequency + '\'' + ", centerFreq0='" + this.centerFreq0 + '\'' + ", centerFreq1='" + this.centerFreq1 + '\'' + ", channelWidth='" + this.channelWidth + '\'' + ", operatorFriendlyName='" + this.operatorFriendlyName + '\'' + ", venueName='" + this.venueName + '\'' + '}';
    }
}

