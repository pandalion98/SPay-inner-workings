package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ExtractLoyaltyCardDetailRequest implements Parcelable {
    public static final Creator<ExtractLoyaltyCardDetailRequest> CREATOR;
    private String cardName;
    private byte[] cardRefID;
    private String tokenId;
    private byte[] tzEncData;

    /* renamed from: com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailRequest.1 */
    static class C03511 implements Creator<ExtractLoyaltyCardDetailRequest> {
        C03511() {
        }

        public ExtractLoyaltyCardDetailRequest createFromParcel(Parcel parcel) {
            return new ExtractLoyaltyCardDetailRequest(null);
        }

        public ExtractLoyaltyCardDetailRequest[] newArray(int i) {
            return new ExtractLoyaltyCardDetailRequest[i];
        }
    }

    static {
        CREATOR = new C03511();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.tokenId);
        parcel.writeByteArray(this.cardRefID);
        parcel.writeByteArray(this.tzEncData);
        parcel.writeString(this.cardName);
    }

    private ExtractLoyaltyCardDetailRequest(Parcel parcel) {
        readFromParcel(parcel);
    }

    public void readFromParcel(Parcel parcel) {
        this.tokenId = parcel.readString();
        this.cardRefID = parcel.createByteArray();
        this.tzEncData = parcel.createByteArray();
        this.cardName = parcel.readString();
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
    }

    public byte[] getCardRefID() {
        return this.cardRefID;
    }

    public void setCardRefID(byte[] bArr) {
        this.cardRefID = bArr;
    }

    public byte[] getTzEncData() {
        return this.tzEncData;
    }

    public void setTzEncData(byte[] bArr) {
        this.tzEncData = bArr;
    }

    public String getCardName() {
        return this.cardName;
    }

    public void setCardName(String str) {
        this.cardName = str;
    }
}
