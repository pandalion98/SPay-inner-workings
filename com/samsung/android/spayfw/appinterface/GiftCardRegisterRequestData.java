package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class GiftCardRegisterRequestData implements Parcelable {
    public static final Creator<GiftCardRegisterRequestData> CREATOR;
    private Bundle extraData;
    private byte[] giftCardData;
    private byte[] serverCaCert;
    private byte[] serverEncCert;
    private byte[] serverVerCert;
    private String userId;
    private String walletId;

    /* renamed from: com.samsung.android.spayfw.appinterface.GiftCardRegisterRequestData.1 */
    static class C03561 implements Creator<GiftCardRegisterRequestData> {
        C03561() {
        }

        public GiftCardRegisterRequestData createFromParcel(Parcel parcel) {
            return new GiftCardRegisterRequestData(null);
        }

        public GiftCardRegisterRequestData[] newArray(int i) {
            return new GiftCardRegisterRequestData[i];
        }
    }

    static {
        CREATOR = new C03561();
    }

    private GiftCardRegisterRequestData(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
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

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.giftCardData);
        parcel.writeByteArray(this.serverCaCert);
        parcel.writeByteArray(this.serverEncCert);
        parcel.writeByteArray(this.serverVerCert);
        parcel.writeString(this.userId);
        parcel.writeString(this.walletId);
        parcel.writeBundle(this.extraData);
    }

    public String toString() {
        return "GiftCardRegisterRequestData  giftCardData: " + Arrays.toString(this.giftCardData) + " serverCaCert: " + Arrays.toString(this.serverCaCert) + " serverEncCert: " + Arrays.toString(this.serverEncCert) + " serverVerCert: " + Arrays.toString(this.serverVerCert) + "userId : " + this.userId + "walletId : " + this.walletId;
    }

    public byte[] getGiftCardData() {
        return this.giftCardData;
    }

    public void setGiftCardData(byte[] bArr) {
        this.giftCardData = bArr;
    }

    public byte[] getServerCaCert() {
        return this.serverCaCert;
    }

    public void setServerCaCert(byte[] bArr) {
        this.serverCaCert = bArr;
    }

    public byte[] getServerEncCert() {
        return this.serverEncCert;
    }

    public void setServerEncCert(byte[] bArr) {
        this.serverEncCert = bArr;
    }

    public byte[] getServerVerCert() {
        return this.serverVerCert;
    }

    public void setServerVerCert(byte[] bArr) {
        this.serverVerCert = bArr;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String str) {
        this.userId = str;
    }

    public String getWalletId() {
        return this.walletId;
    }

    public void setWalletId(String str) {
        this.walletId = str;
    }

    public Bundle getExtraData() {
        return this.extraData;
    }

    public void setExtraData(Bundle bundle) {
        this.extraData = bundle;
    }
}
