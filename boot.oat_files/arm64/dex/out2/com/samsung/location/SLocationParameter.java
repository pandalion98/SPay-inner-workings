package com.samsung.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SLocationParameter implements Parcelable {
    public static final Creator<SLocationParameter> CREATOR = new Creator<SLocationParameter>() {
        public SLocationParameter createFromParcel(Parcel in) {
            return new SLocationParameter(in);
        }

        public SLocationParameter[] newArray(int size) {
            return new SLocationParameter[size];
        }
    };
    private String mBssid;
    private double mLatitude;
    private double mLongitude;
    private int mRadius;
    private int mType;

    public SLocationParameter(int type, double latitude, double longitude, int radius) {
        this.mType = type;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mRadius = radius;
        this.mBssid = null;
    }

    public SLocationParameter(int type, String bssid) {
        this.mType = type;
        this.mLatitude = 0.0d;
        this.mLongitude = 0.0d;
        this.mRadius = 0;
        this.mBssid = bssid;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.mType);
        parcel.writeDouble(this.mLatitude);
        parcel.writeDouble(this.mLongitude);
        parcel.writeInt(this.mRadius);
        parcel.writeString(this.mBssid);
    }

    private SLocationParameter(Parcel in) {
        this.mType = in.readInt();
        this.mLatitude = in.readDouble();
        this.mLongitude = in.readDouble();
        this.mRadius = in.readInt();
        this.mBssid = in.readString();
    }

    public int getType() {
        return this.mType;
    }

    public double getLatitude() {
        return this.mLatitude;
    }

    public double getLongitude() {
        return this.mLongitude;
    }

    public int getRadius() {
        return this.mRadius;
    }

    public String getBssid() {
        return this.mBssid;
    }
}
