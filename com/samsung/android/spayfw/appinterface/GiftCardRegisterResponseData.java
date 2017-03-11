package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class GiftCardRegisterResponseData implements Parcelable {
    public static final Creator<GiftCardRegisterResponseData> CREATOR;
    private byte[] deviceDrk;
    private byte[] deviceEncryptCert;
    private byte[] deviceSignCert;
    private int errorCode;
    private byte[] giftCardEncryptedData;

    /* renamed from: com.samsung.android.spayfw.appinterface.GiftCardRegisterResponseData.1 */
    static class C03571 implements Creator<GiftCardRegisterResponseData> {
        C03571() {
        }

        public GiftCardRegisterResponseData createFromParcel(Parcel parcel) {
            return new GiftCardRegisterResponseData(null);
        }

        public GiftCardRegisterResponseData[] newArray(int i) {
            return new GiftCardRegisterResponseData[i];
        }
    }

    static {
        CREATOR = new C03571();
    }

    private GiftCardRegisterResponseData(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.giftCardEncryptedData = parcel.createByteArray();
        this.deviceDrk = parcel.createByteArray();
        this.deviceEncryptCert = parcel.createByteArray();
        this.deviceSignCert = parcel.createByteArray();
        this.errorCode = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.giftCardEncryptedData);
        parcel.writeByteArray(this.deviceDrk);
        parcel.writeByteArray(this.deviceEncryptCert);
        parcel.writeByteArray(this.deviceSignCert);
        parcel.writeInt(this.errorCode);
    }

    public String toString() {
        return "GiftCardRegisterResponseData  giftCardEncryptedData: " + Arrays.toString(this.giftCardEncryptedData) + " deviceDrk: " + Arrays.toString(this.deviceDrk) + " deviceEncryptCert: " + Arrays.toString(this.deviceEncryptCert) + " deviceSignCert: " + Arrays.toString(this.deviceSignCert) + " errorCode " + this.errorCode;
    }

    public byte[] getGiftCardEncryptedData() {
        return this.giftCardEncryptedData;
    }

    public void setGiftCardEncryptedData(byte[] bArr) {
        this.giftCardEncryptedData = bArr;
    }

    public byte[] getDeviceDrk() {
        return this.deviceDrk;
    }

    public void setDeviceDrk(byte[] bArr) {
        this.deviceDrk = bArr;
    }

    public byte[] getDeviceEncryptCert() {
        return this.deviceEncryptCert;
    }

    public void setDeviceEncryptCert(byte[] bArr) {
        this.deviceEncryptCert = bArr;
    }

    public byte[] getDeviceSignCert() {
        return this.deviceSignCert;
    }

    public void setDeviceSignCert(byte[] bArr) {
        this.deviceSignCert = bArr;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int i) {
        this.errorCode = i;
    }
}
