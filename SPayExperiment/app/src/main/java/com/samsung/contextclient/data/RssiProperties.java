/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 */
package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;

public class RssiProperties
implements Parcelable {
    public static final Parcelable.Creator<RssiProperties> CREATOR = new Parcelable.Creator<RssiProperties>(){

        public RssiProperties createFromParcel(Parcel parcel) {
            return new RssiProperties(parcel);
        }

        public RssiProperties[] newArray(int n2) {
            return new RssiProperties[n2];
        }
    };
    private double mean;
    private double median;
    private double stdDev;

    protected RssiProperties(Parcel parcel) {
        this.mean = parcel.readDouble();
        this.median = parcel.readDouble();
        this.stdDev = parcel.readDouble();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeDouble(this.mean);
        parcel.writeDouble(this.median);
        parcel.writeDouble(this.stdDev);
    }

}

