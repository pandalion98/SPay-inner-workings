package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ExtractGiftCardDetailRequest implements Parcelable {
    public static final Creator<ExtractGiftCardDetailRequest> CREATOR;
    private String cardName;
    private byte[] cardRefID;
    private String tokenId;
    private byte[] tzEncData;

    /* renamed from: com.samsung.android.spayfw.appinterface.ExtractGiftCardDetailRequest.1 */
    static class C03491 implements Creator<ExtractGiftCardDetailRequest> {
        C03491() {
        }

        public ExtractGiftCardDetailRequest createFromParcel(Parcel parcel) {
            return new ExtractGiftCardDetailRequest(null);
        }

        public ExtractGiftCardDetailRequest[] newArray(int i) {
            return new ExtractGiftCardDetailRequest[i];
        }
    }

    static {
        CREATOR = new C03491();
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

    private ExtractGiftCardDetailRequest(Parcel parcel) {
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
