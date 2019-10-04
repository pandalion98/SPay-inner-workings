/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ApduReasonCode
implements Parcelable {
    public static final Parcelable.Creator<ApduReasonCode> CREATOR = new Parcelable.Creator<ApduReasonCode>(){

        public ApduReasonCode createFromParcel(Parcel parcel) {
            return new ApduReasonCode(parcel);
        }

        public ApduReasonCode[] newArray(int n2) {
            return new ApduReasonCode[n2];
        }
    };
    private Bundle extra;
    private short reasonCode;
    private short reasonCommand;

    private ApduReasonCode(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public ApduReasonCode(short s2, short s3) {
        this.reasonCommand = s2;
        this.reasonCode = s3;
        this.extra = null;
    }

    public int describeContents() {
        return 0;
    }

    public int getCode() {
        return this.reasonCode;
    }

    public int getCommand() {
        return this.reasonCommand;
    }

    public Bundle getExtraData() {
        return this.extra;
    }

    public void readFromParcel(Parcel parcel) {
        this.reasonCommand = (short)parcel.readInt();
        this.reasonCode = (short)parcel.readInt();
        this.extra = parcel.readBundle();
    }

    public void reset() {
        this.reasonCommand = 1;
        this.reasonCode = (short)-28672;
        this.extra = null;
    }

    public void setCode(short s2) {
        this.reasonCode = s2;
    }

    public void setCommand(short s2) {
        this.reasonCommand = s2;
    }

    public void setExtraData(Bundle bundle) {
        this.extra = bundle;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt((int)this.reasonCommand);
        parcel.writeInt((int)this.reasonCode);
        parcel.writeBundle(this.extra);
    }

}

