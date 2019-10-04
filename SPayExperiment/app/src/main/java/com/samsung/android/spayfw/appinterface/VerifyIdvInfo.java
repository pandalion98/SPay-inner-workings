/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

public class VerifyIdvInfo
implements Parcelable {
    public static final Parcelable.Creator<VerifyIdvInfo> CREATOR = new Parcelable.Creator<VerifyIdvInfo>(){

        public VerifyIdvInfo createFromParcel(Parcel parcel) {
            return new VerifyIdvInfo(parcel);
        }

        public VerifyIdvInfo[] newArray(int n2) {
            return new VerifyIdvInfo[n2];
        }
    };
    private String id;
    private Intent intent;
    private String type;
    private String value;

    public VerifyIdvInfo() {
    }

    public VerifyIdvInfo(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.id = parcel.readString();
        this.type = parcel.readString();
        this.value = parcel.readString();
        this.intent = (Intent)parcel.readParcelable(ClassLoader.getSystemClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public Intent getIntent() {
        return this.intent;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public void setId(String string) {
        this.id = string;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public void setType(String string) {
        this.type = string;
    }

    public void setValue(String string) {
        this.value = string;
    }

    public String toString() {
        return "VerifyIdvInfo: id: " + this.id + " type: " + this.type + " value: " + this.value;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.id);
        parcel.writeString(this.type);
        parcel.writeString(this.value);
        parcel.writeParcelable((Parcelable)this.intent, n2);
    }

}

