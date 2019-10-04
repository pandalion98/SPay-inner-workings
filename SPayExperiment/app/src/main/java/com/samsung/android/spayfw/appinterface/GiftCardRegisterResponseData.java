/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

public class GiftCardRegisterResponseData
implements Parcelable {
    public static final Parcelable.Creator<GiftCardRegisterResponseData> CREATOR = new Parcelable.Creator<GiftCardRegisterResponseData>(){

        public GiftCardRegisterResponseData createFromParcel(Parcel parcel) {
            return new GiftCardRegisterResponseData(parcel);
        }

        public GiftCardRegisterResponseData[] newArray(int n2) {
            return new GiftCardRegisterResponseData[n2];
        }
    };
    private byte[] deviceDrk;
    private byte[] deviceEncryptCert;
    private byte[] deviceSignCert;
    private int errorCode;
    private byte[] giftCardEncryptedData;

    public GiftCardRegisterResponseData() {
    }

    private GiftCardRegisterResponseData(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public byte[] getDeviceDrk() {
        return this.deviceDrk;
    }

    public byte[] getDeviceEncryptCert() {
        return this.deviceEncryptCert;
    }

    public byte[] getDeviceSignCert() {
        return this.deviceSignCert;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public byte[] getGiftCardEncryptedData() {
        return this.giftCardEncryptedData;
    }

    public void readFromParcel(Parcel parcel) {
        this.giftCardEncryptedData = parcel.createByteArray();
        this.deviceDrk = parcel.createByteArray();
        this.deviceEncryptCert = parcel.createByteArray();
        this.deviceSignCert = parcel.createByteArray();
        this.errorCode = parcel.readInt();
    }

    public void setDeviceDrk(byte[] arrby) {
        this.deviceDrk = arrby;
    }

    public void setDeviceEncryptCert(byte[] arrby) {
        this.deviceEncryptCert = arrby;
    }

    public void setDeviceSignCert(byte[] arrby) {
        this.deviceSignCert = arrby;
    }

    public void setErrorCode(int n2) {
        this.errorCode = n2;
    }

    public void setGiftCardEncryptedData(byte[] arrby) {
        this.giftCardEncryptedData = arrby;
    }

    public String toString() {
        return "GiftCardRegisterResponseData  giftCardEncryptedData: " + Arrays.toString((byte[])this.giftCardEncryptedData) + " deviceDrk: " + Arrays.toString((byte[])this.deviceDrk) + " deviceEncryptCert: " + Arrays.toString((byte[])this.deviceEncryptCert) + " deviceSignCert: " + Arrays.toString((byte[])this.deviceSignCert) + " errorCode " + this.errorCode;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeByteArray(this.giftCardEncryptedData);
        parcel.writeByteArray(this.deviceDrk);
        parcel.writeByteArray(this.deviceEncryptCert);
        parcel.writeByteArray(this.deviceSignCert);
        parcel.writeInt(this.errorCode);
    }

}

