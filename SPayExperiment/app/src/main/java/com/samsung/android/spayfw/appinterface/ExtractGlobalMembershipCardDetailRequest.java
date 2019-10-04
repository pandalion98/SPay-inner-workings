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

public class ExtractGlobalMembershipCardDetailRequest
implements Parcelable {
    public static final Parcelable.Creator<ExtractGlobalMembershipCardDetailRequest> CREATOR = new Parcelable.Creator<ExtractGlobalMembershipCardDetailRequest>(){

        public ExtractGlobalMembershipCardDetailRequest createFromParcel(Parcel parcel) {
            return new ExtractGlobalMembershipCardDetailRequest(parcel);
        }

        public ExtractGlobalMembershipCardDetailRequest[] newArray(int n2) {
            return new ExtractGlobalMembershipCardDetailRequest[n2];
        }
    };
    private String tokenId;
    private byte[] tzEncData;

    public ExtractGlobalMembershipCardDetailRequest() {
    }

    private ExtractGlobalMembershipCardDetailRequest(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public byte[] getTzEncData() {
        return this.tzEncData;
    }

    public void readFromParcel(Parcel parcel) {
        this.tokenId = parcel.readString();
        this.tzEncData = parcel.createByteArray();
    }

    public void setTokenId(String string) {
        this.tokenId = string;
    }

    public void setTzEncData(byte[] arrby) {
        this.tzEncData = arrby;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.tokenId);
        parcel.writeByteArray(this.tzEncData);
    }

}

