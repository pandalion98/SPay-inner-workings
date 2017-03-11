package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ExtractGlobalMembershipCardDetailRequest implements Parcelable {
    public static final Creator<ExtractGlobalMembershipCardDetailRequest> CREATOR;
    private String tokenId;
    private byte[] tzEncData;

    /* renamed from: com.samsung.android.spayfw.appinterface.ExtractGlobalMembershipCardDetailRequest.1 */
    static class C03501 implements Creator<ExtractGlobalMembershipCardDetailRequest> {
        C03501() {
        }

        public ExtractGlobalMembershipCardDetailRequest createFromParcel(Parcel parcel) {
            return new ExtractGlobalMembershipCardDetailRequest(null);
        }

        public ExtractGlobalMembershipCardDetailRequest[] newArray(int i) {
            return new ExtractGlobalMembershipCardDetailRequest[i];
        }
    }

    static {
        CREATOR = new C03501();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.tokenId);
        parcel.writeByteArray(this.tzEncData);
    }

    private ExtractGlobalMembershipCardDetailRequest(Parcel parcel) {
        readFromParcel(parcel);
    }

    public void readFromParcel(Parcel parcel) {
        this.tokenId = parcel.readString();
        this.tzEncData = parcel.createByteArray();
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
    }

    public byte[] getTzEncData() {
        return this.tzEncData;
    }

    public void setTzEncData(byte[] bArr) {
        this.tzEncData = bArr;
    }
}
