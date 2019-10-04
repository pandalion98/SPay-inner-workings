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

public class GlobalMembershipCardDetail
implements Parcelable {
    public static final Parcelable.Creator<GlobalMembershipCardDetail> CREATOR = new Parcelable.Creator<GlobalMembershipCardDetail>(){

        public GlobalMembershipCardDetail createFromParcel(Parcel parcel) {
            return new GlobalMembershipCardDetail(parcel);
        }

        public GlobalMembershipCardDetail[] newArray(int n2) {
            return new GlobalMembershipCardDetail[n2];
        }
    };
    private String barcodeContent;
    private String barcodeType;
    private String cardnumber;
    private int errorCode;
    private String numericValue;
    private String pin;
    private String tokenId;

    public GlobalMembershipCardDetail() {
    }

    private GlobalMembershipCardDetail(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getBarcodeContent() {
        return this.barcodeContent;
    }

    public String getBarcodeType() {
        return this.barcodeType;
    }

    public String getCardnumber() {
        return this.cardnumber;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getNumericValue() {
        return this.numericValue;
    }

    public String getPin() {
        return this.pin;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void readFromParcel(Parcel parcel) {
        this.tokenId = parcel.readString();
        this.cardnumber = parcel.readString();
        this.pin = parcel.readString();
        this.barcodeContent = parcel.readString();
        this.barcodeType = parcel.readString();
        this.numericValue = parcel.readString();
        this.errorCode = parcel.readInt();
    }

    public void setBarcodeContent(String string) {
        this.barcodeContent = string;
    }

    public void setBarcodeType(String string) {
        this.barcodeType = string;
    }

    public void setCardnumber(String string) {
        this.cardnumber = string;
    }

    public void setErrorCode(int n2) {
        this.errorCode = n2;
    }

    public void setNumericValue(String string) {
        this.numericValue = string;
    }

    public void setPin(String string) {
        this.pin = string;
    }

    public void setTokenId(String string) {
        this.tokenId = string;
    }

    public String toString() {
        return "globalMembershipCardDetail  tokenId [" + this.tokenId + "] cardnumber: [" + this.cardnumber + "] pin [" + this.pin + "] barcodeContent [" + this.barcodeContent + "] barcodeType [" + this.barcodeType + "] numericValue [" + this.numericValue + "] errorCode [" + this.errorCode + "]";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.tokenId);
        parcel.writeString(this.cardnumber);
        parcel.writeString(this.pin);
        parcel.writeString(this.barcodeContent);
        parcel.writeString(this.barcodeType);
        parcel.writeString(this.numericValue);
        parcel.writeInt(this.errorCode);
    }

}

