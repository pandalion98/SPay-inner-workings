package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Location {
    private String accuracy;
    private String altitude;
    private String latitude;
    private String longitude;
    private String provider;
    private String time;
    private String timezone;

    public Location(String str, String str2, String str3, String str4, String str5) {
        this.latitude = str;
        this.longitude = str2;
        this.timezone = str3;
        this.provider = str4;
        this.altitude = str5;
    }

    public void setAccuracy(String str) {
        this.accuracy = str;
    }

    public void setTime(String str) {
        this.time = str;
    }
}
