package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class GlobalMembershipCardRegisterResponseData implements Parcelable {
    public static final Creator<GlobalMembershipCardRegisterResponseData> CREATOR;
    private byte[] deviceDrk;
    private byte[] deviceEncryptCert;
    private byte[] deviceSignCert;
    private int errorCode;
    private byte[] globalMembershipCardEncryptedData;

    /* renamed from: com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterResponseData.1 */
    static class C03621 implements Creator<GlobalMembershipCardRegisterResponseData> {
        C03621() {
        }

        public GlobalMembershipCardRegisterResponseData createFromParcel(Parcel parcel) {
            return new GlobalMembershipCardRegisterResponseData(null);
        }

        public GlobalMembershipCardRegisterResponseData[] newArray(int i) {
            return new GlobalMembershipCardRegisterResponseData[i];
        }
    }

    static {
        CREATOR = new C03621();
    }

    private GlobalMembershipCardRegisterResponseData(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.globalMembershipCardEncryptedData = parcel.createByteArray();
        this.deviceDrk = parcel.createByteArray();
        this.deviceEncryptCert = parcel.createByteArray();
        this.deviceSignCert = parcel.createByteArray();
        this.errorCode = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.globalMembershipCardEncryptedData);
        parcel.writeByteArray(this.deviceDrk);
        parcel.writeByteArray(this.deviceEncryptCert);
        parcel.writeByteArray(this.deviceSignCert);
        parcel.writeInt(this.errorCode);
    }

    public String toString() {
        return "globalMembershipCardRegisterResponseData  globalMembershipCardEncryptedData: " + Arrays.toString(this.globalMembershipCardEncryptedData) + " deviceDrk: " + Arrays.toString(this.deviceDrk) + " deviceEncryptCert: " + Arrays.toString(this.deviceEncryptCert) + " deviceSignCert: " + Arrays.toString(this.deviceSignCert) + " errorCode " + this.errorCode;
    }

    public byte[] getGlobalMembershipCardEncryptedData() {
        return this.globalMembershipCardEncryptedData;
    }

    public void setGlobalMembershipCardEncryptedData(byte[] bArr) {
        this.globalMembershipCardEncryptedData = bArr;
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
