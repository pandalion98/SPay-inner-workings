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

public class ExtractLoyaltyCardDetailRequest
implements Parcelable {
    public static final Parcelable.Creator<ExtractLoyaltyCardDetailRequest> CREATOR = new Parcelable.Creator<ExtractLoyaltyCardDetailRequest>(){

        public ExtractLoyaltyCardDetailRequest createFromParcel(Parcel parcel) {
            return new ExtractLoyaltyCardDetailRequest(parcel);
        }

        public ExtractLoyaltyCardDetailRequest[] newArray(int n2) {
            return new ExtractLoyaltyCardDetailRequest[n2];
        }
    };
    private String cardName;
    private byte[] cardRefID;
    private String tokenId;
    private byte[] tzEncData;

    public ExtractLoyaltyCardDetailRequest() {
    }

    private ExtractLoyaltyCardDetailRequest(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getCardName() {
        return this.cardName;
    }

    public byte[] getCardRefID() {
        return this.cardRefID;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public byte[] getTzEncData() {
        return this.tzEncData;
    }

    public void readFromParcel(Parcel parcel) {
        this.tokenId = parcel.readString();
        this.cardRefID = parcel.createByteArray();
        this.tzEncData = parcel.createByteArray();
        this.cardName = parcel.readString();
    }

    public void setCardName(String string) {
        this.cardName = string;
    }

    public void setCardRefID(byte[] arrby) {
        this.cardRefID = arrby;
    }

    public void setTokenId(String string) {
        this.tokenId = string;
    }

    public void setTzEncData(byte[] arrby) {
        this.tzEncData = arrby;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.tokenId);
        parcel.writeByteArray(this.cardRefID);
        parcel.writeByteArray(this.tzEncData);
        parcel.writeString(this.cardName);
    }

}

