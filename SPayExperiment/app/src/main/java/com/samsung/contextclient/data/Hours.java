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

public class Hours
implements Parcelable {
    public static final Parcelable.Creator<Hours> CREATOR = new Parcelable.Creator<Hours>(){

        public Hours createFromParcel(Parcel parcel) {
            return new Hours(parcel);
        }

        public Hours[] newArray(int n2) {
            return new Hours[n2];
        }
    };
    private String end;
    private String start;

    protected Hours(Parcel parcel) {
        this.start = parcel.readString();
        this.end = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public String getEnd() {
        return this.end;
    }

    public String getStart() {
        return this.start;
    }

    public void setEnd(String string) {
        this.end = string;
    }

    public void setStart(String string) {
        this.start = string;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.start);
        parcel.writeString(this.end);
    }

}

