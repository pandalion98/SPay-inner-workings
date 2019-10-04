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
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;

public class SelectCardResult
implements Parcelable {
    public static final Parcelable.Creator<SelectCardResult> CREATOR = new Parcelable.Creator<SelectCardResult>(){

        public SelectCardResult createFromParcel(Parcel parcel) {
            return new SelectCardResult(parcel);
        }

        public SelectCardResult[] newArray(int n2) {
            return null;
        }
    };
    public static final int SELECTCARD_FAIL = -1;
    public static final int SELECTCARD_SUCCESS = 0;
    public static final int SELECTCARD_SUCCESS_USE_PREVIOUS = 1;
    private int mstTransmitTime;
    private byte[] nonce;
    private int status;
    private String taid;

    public SelectCardResult() {
    }

    private SelectCardResult(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public SelectCardResult(String string, byte[] arrby) {
        this.taid = string;
        this.nonce = arrby;
    }

    public int describeContents() {
        return 0;
    }

    public int getMstTransmitTime() {
        return this.mstTransmitTime;
    }

    public byte[] getNonce() {
        return this.nonce;
    }

    public int getStatus() {
        return this.status;
    }

    public String getTaid() {
        return this.taid;
    }

    public void readFromParcel(Parcel parcel) {
        this.status = parcel.readInt();
        this.taid = parcel.readString();
        this.nonce = parcel.createByteArray();
        this.mstTransmitTime = parcel.readInt();
    }

    public void setMstTransmitTime(int n2) {
        this.mstTransmitTime = n2;
    }

    public void setNonce(byte[] arrby) {
        this.nonce = arrby;
    }

    public void setStatus(int n2) {
        this.status = n2;
    }

    public void setTaid(String string) {
        this.taid = string;
    }

    public String toString() {
        return "SelectCardResult: status: " + this.status + "taid: " + this.taid + " nonce: " + this.nonce + " mstTransmitTime: " + this.mstTransmitTime;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt(this.status);
        parcel.writeString(this.taid);
        parcel.writeByteArray(this.nonce);
        parcel.writeInt(this.mstTransmitTime);
    }

}

