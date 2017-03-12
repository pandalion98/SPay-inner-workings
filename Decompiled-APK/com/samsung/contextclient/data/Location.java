package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;

public class Location implements Parcelable {
    public static final Creator<Location> CREATOR;
    private double accuracy;
    private String address;
    private double altitude;
    private double latitude;
    private double longitude;
    private double radius;

    /* renamed from: com.samsung.contextclient.data.Location.1 */
    static class C06031 implements Creator<Location> {
        C06031() {
        }

        public Location createFromParcel(Parcel parcel) {
            return new Location(parcel);
        }

        public Location[] newArray(int i) {
            return new Location[i];
        }
    }

    public Location(double d, double d2) {
        this.latitude = d;
        this.longitude = d2;
    }

    public Location(android.location.Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.accuracy = (double) location.getAccuracy();
        this.altitude = location.getAltitude();
        this.address = BuildConfig.FLAVOR;
    }

    protected Location(Parcel parcel) {
        readFromParcel(parcel);
    }

    static {
        CREATOR = new C06031();
    }

    public double getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(double d) {
        this.accuracy = d;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double d) {
        this.latitude = d;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double d) {
        this.longitude = d;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(double d) {
        this.altitude = d;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double d) {
        this.radius = d;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String str) {
        this.address = str;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.accuracy);
        parcel.writeDouble(this.latitude);
        parcel.writeDouble(this.longitude);
        parcel.writeDouble(this.altitude);
        parcel.writeDouble(this.radius);
        parcel.writeString(this.address);
    }

    private void readFromParcel(Parcel parcel) {
        this.accuracy = parcel.readDouble();
        this.latitude = parcel.readDouble();
        this.longitude = parcel.readDouble();
        this.altitude = parcel.readDouble();
        this.radius = parcel.readDouble();
        this.address = parcel.readString();
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
}
