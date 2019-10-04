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

public class CommonSpayResponse
implements Parcelable {
    public static final Parcelable.Creator<CommonSpayResponse> CREATOR = new Parcelable.Creator<CommonSpayResponse>(){

        public CommonSpayResponse createFromParcel(Parcel parcel) {
            return new CommonSpayResponse(parcel);
        }

        public CommonSpayResponse[] newArray(int n2) {
            return new CommonSpayResponse[n2];
        }
    };
    private Bundle dataBundle;
    private byte[] dataByte;
    private int status;

    public CommonSpayResponse() {
    }

    private CommonSpayResponse(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.status = parcel.readInt();
        this.dataByte = parcel.createByteArray();
        this.dataBundle = parcel.readBundle();
    }

    public int describeContents() {
        return 0;
    }

    public Bundle getDataBundle() {
        return this.dataBundle;
    }

    public byte[] getDataByte() {
        return this.dataByte;
    }

    public int getStatus() {
        return this.status;
    }

    public void setData(Bundle bundle) {
        this.dataBundle = bundle;
    }

    public void setData(byte[] arrby) {
        this.dataByte = arrby;
    }

    public void setStatus(int n2) {
        this.status = n2;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt(this.status);
        parcel.writeByteArray(this.dataByte);
        parcel.writeBundle(this.dataBundle);
    }

}

