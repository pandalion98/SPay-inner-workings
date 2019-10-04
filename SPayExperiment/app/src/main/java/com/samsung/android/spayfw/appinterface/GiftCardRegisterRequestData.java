/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Arrays
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;

public class GiftCardRegisterRequestData
implements Parcelable {
    public static final Parcelable.Creator<GiftCardRegisterRequestData> CREATOR = new Parcelable.Creator<GiftCardRegisterRequestData>(){

        public GiftCardRegisterRequestData createFromParcel(Parcel parcel) {
            return new GiftCardRegisterRequestData(parcel);
        }

        public GiftCardRegisterRequestData[] newArray(int n2) {
            return new GiftCardRegisterRequestData[n2];
        }
    };
    private Bundle extraData;
    private byte[] giftCardData;
    private byte[] serverCaCert;
    private byte[] serverEncCert;
    private byte[] serverVerCert;
    private String userId;
    private String walletId;

    public GiftCardRegisterRequestData() {
    }

    private GiftCardRegisterRequestData(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public Bundle getExtraData() {
        return this.extraData;
    }

    public byte[] getGiftCardData() {
        return this.giftCardData;
    }

    public byte[] getServerCaCert() {
        return this.serverCaCert;
    }

    public byte[] getServerEncCert() {
        return this.serverEncCert;
    }

    public byte[] getServerVerCert() {
        return this.serverVerCert;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getWalletId() {
        return this.walletId;
    }

    public void readFromParcel(Parcel parcel) {
        this.giftCardData = parcel.createByteArray();
        this.serverCaCert = parcel.createByteArray();
        this.serverEncCert = parcel.createByteArray();
        this.serverVerCert = parcel.createByteArray();
        this.userId = parcel.readString();
        this.walletId = parcel.readString();
        this.extraData = parcel.readBundle();
    }

    public void setExtraData(Bundle bundle) {
        this.extraData = bundle;
    }

    public void setGiftCardData(byte[] arrby) {
        this.giftCardData = arrby;
    }

    public void setServerCaCert(byte[] arrby) {
        this.serverCaCert = arrby;
    }

    public void setServerEncCert(byte[] arrby) {
        this.serverEncCert = arrby;
    }

    public void setServerVerCert(byte[] arrby) {
        this.serverVerCert = arrby;
    }

    public void setUserId(String string) {
        this.userId = string;
    }

    public void setWalletId(String string) {
        this.walletId = string;
    }

    public String toString() {
        return "GiftCardRegisterRequestData  giftCardData: " + Arrays.toString((byte[])this.giftCardData) + " serverCaCert: " + Arrays.toString((byte[])this.serverCaCert) + " serverEncCert: " + Arrays.toString((byte[])this.serverEncCert) + " serverVerCert: " + Arrays.toString((byte[])this.serverVerCert) + "userId : " + this.userId + "walletId : " + this.walletId;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeByteArray(this.giftCardData);
        parcel.writeByteArray(this.serverCaCert);
        parcel.writeByteArray(this.serverEncCert);
        parcel.writeByteArray(this.serverVerCert);
        parcel.writeString(this.userId);
        parcel.writeString(this.walletId);
        parcel.writeBundle(this.extraData);
    }

}

