package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class GlobalMembershipCardRegisterRequestData implements Parcelable {
    public static final Creator<GlobalMembershipCardRegisterRequestData> CREATOR;
    private byte[] globalMembershipCardData;
    private String partnerId;
    private byte[] serverCaCert;
    private byte[] serverEncCert;
    private byte[] serverVerCert;
    private String tokenId;
    private String userId;

    /* renamed from: com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterRequestData.1 */
    static class C03611 implements Creator<GlobalMembershipCardRegisterRequestData> {
        C03611() {
        }

        public GlobalMembershipCardRegisterRequestData createFromParcel(Parcel parcel) {
            return new GlobalMembershipCardRegisterRequestData(null);
        }

        public GlobalMembershipCardRegisterRequestData[] newArray(int i) {
            return new GlobalMembershipCardRegisterRequestData[i];
        }
    }

    static {
        CREATOR = new C03611();
    }

    private GlobalMembershipCardRegisterRequestData(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.globalMembershipCardData = parcel.createByteArray();
        this.serverCaCert = parcel.createByteArray();
        this.serverEncCert = parcel.createByteArray();
        this.serverVerCert = parcel.createByteArray();
        this.userId = parcel.readString();
        this.partnerId = parcel.readString();
        this.tokenId = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.globalMembershipCardData);
        parcel.writeByteArray(this.serverCaCert);
        parcel.writeByteArray(this.serverEncCert);
        parcel.writeByteArray(this.serverVerCert);
        parcel.writeString(this.userId);
        parcel.writeString(this.partnerId);
        parcel.writeString(this.tokenId);
    }

    public String toString() {
        return "GlobalMembershipCardRegisterRequestData  globalMembershipCardData: " + Arrays.toString(this.globalMembershipCardData) + " serverCaCert: " + Arrays.toString(this.serverCaCert) + " serverEncCert: " + Arrays.toString(this.serverEncCert) + " serverVerCert: " + Arrays.toString(this.serverVerCert) + " userId : " + this.userId + " partnerId : " + this.partnerId + " tokenId : " + this.tokenId;
    }

    public byte[] getGlobalMembershipCardData() {
        return this.globalMembershipCardData;
    }

    public void setGlobalMembershipCardData(byte[] bArr) {
        this.globalMembershipCardData = bArr;
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

    public String getPartnerId() {
        return this.partnerId;
    }

    public void setPartnerId(String str) {
        this.partnerId = str;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
    }

    public boolean allServerCertsNotNull() {
        if (this.serverCaCert == null || this.serverEncCert == null || this.serverVerCert == null) {
            return false;
        }
        return true;
    }
}
