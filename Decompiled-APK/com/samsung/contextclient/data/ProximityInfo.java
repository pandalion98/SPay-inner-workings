package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ProximityInfo implements Parcelable {
    public static final Creator<ProximityInfo> CREATOR;
    public double distance;
    String sensorUsed;

    /* renamed from: com.samsung.contextclient.data.ProximityInfo.1 */
    static class C06111 implements Creator<ProximityInfo> {
        C06111() {
        }

        public ProximityInfo createFromParcel(Parcel parcel) {
            return new ProximityInfo(parcel);
        }

        public ProximityInfo[] newArray(int i) {
            return new ProximityInfo[i];
        }
    }

    protected ProximityInfo(Parcel parcel) {
        this.distance = parcel.readDouble();
        this.sensorUsed = parcel.readString();
    }

    static {
        CREATOR = new C06111();
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double d) {
        this.distance = d;
    }

    public String getSensorUsed() {
        return this.sensorUsed;
    }

    public void setSensorUsed(String str) {
        this.sensorUsed = str;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.distance);
        parcel.writeString(this.sensorUsed);
    }
}
