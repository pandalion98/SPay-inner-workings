/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.location.Location
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Location
implements Parcelable {
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>(){

        public Location createFromParcel(Parcel parcel) {
            return new Location(parcel);
        }

        public Location[] newArray(int n2) {
            return new Location[n2];
        }
    };
    private double accuracy;
    private String address;
    private double altitude;
    private double latitude;
    private double longitude;
    private double radius;

    public Location() {
    }

    public Location(double d2, double d3) {
        this.latitude = d2;
        this.longitude = d3;
    }

    public Location(android.location.Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.accuracy = location.getAccuracy();
        this.altitude = location.getAltitude();
        this.address = "";
    }

    protected Location(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.accuracy = parcel.readDouble();
        this.latitude = parcel.readDouble();
        this.longitude = parcel.readDouble();
        this.altitude = parcel.readDouble();
        this.radius = parcel.readDouble();
        this.address = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public double getAccuracy() {
        return this.accuracy;
    }

    public String getAddress() {
        return this.address;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setAccuracy(double d2) {
        this.accuracy = d2;
    }

    public void setAddress(String string) {
        this.address = string;
    }

    public void setAltitude(double d2) {
        this.altitude = d2;
    }

    public void setLatitude(double d2) {
        this.latitude = d2;
    }

    public void setLongitude(double d2) {
        this.longitude = d2;
    }

    public void setRadius(double d2) {
        this.radius = d2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(lat=");
        stringBuilder.append(this.latitude);
        stringBuilder.append(", lon=");
        stringBuilder.append(this.longitude);
        stringBuilder.append(", altitude=");
        stringBuilder.append(this.altitude);
        stringBuilder.append(", radius=");
        stringBuilder.append(this.radius);
        stringBuilder.append(", addr=");
        stringBuilder.append(this.address);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeDouble(this.accuracy);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
        parcel.writeDouble(this.altitude);
        parcel.writeDouble(this.radius);
        parcel.writeString(this.address);
    }

}

