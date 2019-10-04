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

public class GlobalMembershipCardRegisterRequestData
implements Parcelable {
    public static final Parcelable.Creator<GlobalMembershipCardRegisterRequestData> CREATOR = new Parcelable.Creator<GlobalMembershipCardRegisterRequestData>(){

        public GlobalMembershipCardRegisterRequestData createFromParcel(Parcel parcel) {
            return new GlobalMembershipCardRegisterRequestData(parcel);
        }

        public GlobalMembershipCardRegisterRequestData[] newArray(int n2) {
            return new GlobalMembershipCardRegisterRequestData[n2];
        }
    };
    private byte[] globalMembershipCardData;
    private String partnerId;
    private byte[] serverCaCert;
    private byte[] serverEncCert;
    private byte[] serverVerCert;
    private String tokenId;
    private String userId;

    public GlobalMembershipCardRegisterRequestData() {
    }

    private GlobalMembershipCardRegisterRequestData(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public boolean allServerCertsNotNull() {
        return this.serverCaCert != null && this.serverEncCert != null && this.serverVerCert != null;
    }

    public int describeContents() {
        return 0;
    }

    public byte[] getGlobalMembershipCardData() {
        return this.globalMembershipCardData;
    }

    public String getPartnerId() {
        return this.partnerId;
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

    public String getTokenId() {
        return this.tokenId;
    }

    public String getUserId() {
        return this.userId;
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

    public void setGlobalMembershipCardData(byte[] arrby) {
        this.globalMembershipCardData = arrby;
    }

    public void setPartnerId(String string) {
        this.partnerId = string;
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

    public void setTokenId(String string) {
        this.tokenId = string;
    }

    public void setUserId(String string) {
        this.userId = string;
    }

    public String toString() {
        return "GlobalMembershipCardRegisterRequestData  globalMembershipCardData: " + Arrays.toString((byte[])this.globalMembershipCardData) + " serverCaCert: " + Arrays.toString((byte[])this.serverCaCert) + " serverEncCert: " + Arrays.toString((byte[])this.serverEncCert) + " serverVerCert: " + Arrays.toString((byte[])this.serverVerCert) + " userId : " + this.userId + " partnerId : " + this.partnerId + " tokenId : " + this.tokenId;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeByteArray(this.globalMembershipCardData);
        parcel.writeByteArray(this.serverCaCert);
        parcel.writeByteArray(this.serverEncCert);
        parcel.writeByteArray(this.serverVerCert);
        parcel.writeString(this.userId);
        parcel.writeString(this.partnerId);
        parcel.writeString(this.tokenId);
    }

}

