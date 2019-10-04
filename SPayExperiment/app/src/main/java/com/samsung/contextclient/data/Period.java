/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.ClassLoader
 *  java.lang.Object
 */
package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.contextclient.data.Hours;

public class Period
implements Parcelable {
    public static final Parcelable.Creator<Period> CREATOR = new Parcelable.Creator<Period>(){

        public Period createFromParcel(Parcel parcel) {
            return new Period(parcel);
        }

        public Period[] newArray(int n2) {
            return new Period[n2];
        }
    };
    private int day;
    private Hours hours;

    protected Period(Parcel parcel) {
        this.day = parcel.readInt();
        this.hours = (Hours)parcel.readParcelable(Hours.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public int getDay() {
        return this.day;
    }

    public Hours getHours() {
        return this.hours;
    }

    public void setDay(int n2) {
        this.day = n2;
    }

    public void setHours(Hours hours) {
        this.hours = hours;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt(this.day);
        parcel.writeParcelable((Parcelable)this.hours, n2);
    }

}

