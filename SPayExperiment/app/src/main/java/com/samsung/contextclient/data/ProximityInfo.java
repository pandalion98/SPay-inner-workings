/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ProximityInfo
implements Parcelable {
    public static final Parcelable.Creator<ProximityInfo> CREATOR = new Parcelable.Creator<ProximityInfo>(){

        public ProximityInfo createFromParcel(Parcel parcel) {
            return new ProximityInfo(parcel);
        }

        public ProximityInfo[] newArray(int n2) {
            return new ProximityInfo[n2];
        }
    };
    public double distance;
    String sensorUsed;

    protected ProximityInfo(Parcel parcel) {
        this.distance = parcel.readDouble();
        this.sensorUsed = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public double getDistance() {
        return this.distance;
    }

    public String getSensorUsed() {
        return this.sensorUsed;
    }

    public void setDistance(double d2) {
        this.distance = d2;
    }

    public void setSensorUsed(String string) {
        this.sensorUsed = string;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeDouble(this.distance);
        parcel.writeString(this.sensorUsed);
    }

}

