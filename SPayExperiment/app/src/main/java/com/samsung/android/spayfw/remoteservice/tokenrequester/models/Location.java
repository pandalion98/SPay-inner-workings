/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

public class Location {
    private String accuracy;
    private String altitude;
    private String latitude;
    private String longitude;
    private String provider;
    private String time;
    private String timezone;

    public Location(String string, String string2, String string3, String string4, String string5) {
        this.latitude = string;
        this.longitude = string2;
        this.timezone = string3;
        this.provider = string4;
        this.altitude = string5;
    }

    public void setAccuracy(String string) {
        this.accuracy = string;
    }

    public void setTime(String string) {
        this.time = string;
    }
}

