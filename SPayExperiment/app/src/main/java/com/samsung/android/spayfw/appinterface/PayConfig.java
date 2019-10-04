/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.MstPayConfig;

public class PayConfig
implements Parcelable {
    public static final Parcelable.Creator<PayConfig> CREATOR = new Parcelable.Creator<PayConfig>(){

        public PayConfig createFromParcel(Parcel parcel) {
            return new PayConfig(parcel);
        }

        public PayConfig[] newArray(int n2) {
            return new PayConfig[n2];
        }
    };
    private MstPayConfig mstPayConfig;
    private int mstTransmitTime;
    private int payIdleTime = 1500;
    private int payType;

    public PayConfig() {
    }

    private PayConfig(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public MstPayConfig getMstPayConfig() {
        return this.mstPayConfig;
    }

    public int getMstTransmitTime() {
        return this.mstTransmitTime;
    }

    public int getPayIdleTime() {
        return this.payIdleTime;
    }

    public int getPayType() {
        return this.payType;
    }

    public void readFromParcel(Parcel parcel) {
        this.payType = parcel.readInt();
        this.payIdleTime = parcel.readInt();
        this.mstPayConfig = (MstPayConfig)parcel.readParcelable(this.getClass().getClassLoader());
        this.mstTransmitTime = parcel.readInt();
    }

    public void setMstPayConfig(MstPayConfig mstPayConfig) {
        this.mstPayConfig = mstPayConfig;
    }

    public void setMstTransmitTime(int n2) {
        this.mstTransmitTime = n2;
    }

    public void setPayIdleTime(int n2) {
        this.payIdleTime = n2;
    }

    public void setPayType(int n2) {
        this.payType = n2;
    }

    public String toString() {
        String string = "PayConfig:  payType: " + this.payType + " payIdleTime: " + this.payIdleTime + " mstTransmitTime: " + this.mstTransmitTime;
        if (this.mstPayConfig != null) {
            return string + this.mstPayConfig.toString();
        }
        return string + " = mstPayConfig: null";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt(this.payType);
        parcel.writeInt(this.payIdleTime);
        parcel.writeParcelable((Parcelable)this.mstPayConfig, n2);
        parcel.writeInt(this.mstTransmitTime);
    }

}

