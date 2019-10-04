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

public class MstSequence
implements Parcelable {
    public static final Parcelable.Creator<MstSequence> CREATOR = new Parcelable.Creator<MstSequence>(){

        public MstSequence createFromParcel(Parcel parcel) {
            return new MstSequence(parcel);
        }

        public MstSequence[] newArray(int n2) {
            return new MstSequence[n2];
        }
    };
    private String config;
    private long idle;
    private String key;
    private long transmit;

    protected MstSequence(Parcel parcel) {
        this.key = parcel.readString();
        this.transmit = parcel.readLong();
        this.idle = parcel.readLong();
        this.config = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public String getConfig() {
        return this.config;
    }

    public long getIdle() {
        return this.idle;
    }

    public String getKey() {
        return this.key;
    }

    public long getTransmit() {
        return this.transmit;
    }

    public void setConfig(String string) {
        this.config = string;
    }

    public void setIdle(long l2) {
        this.idle = l2;
    }

    public void setKey(String string) {
        this.key = string;
    }

    public void setTransmit(long l2) {
        this.transmit = l2;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.key);
        parcel.writeLong(this.transmit);
        parcel.writeLong(this.idle);
        parcel.writeString(this.config);
    }

}

