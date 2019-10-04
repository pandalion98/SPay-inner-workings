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

public class GiftCardDetail
implements Parcelable {
    public static final Parcelable.Creator<GiftCardDetail> CREATOR = new Parcelable.Creator<GiftCardDetail>(){

        public GiftCardDetail createFromParcel(Parcel parcel) {
            return new GiftCardDetail(parcel);
        }

        public GiftCardDetail[] newArray(int n2) {
            return new GiftCardDetail[n2];
        }
    };
    private String barcodeContent;
    private String cardnumber;
    private int errorCode;
    private String pin;

    public GiftCardDetail() {
    }

    private GiftCardDetail(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getBarcodeContent() {
        return this.barcodeContent;
    }

    public String getCardnumber() {
        return this.cardnumber;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getPin() {
        return this.pin;
    }

    public void readFromParcel(Parcel parcel) {
        this.cardnumber = parcel.readString();
        this.pin = parcel.readString();
        this.barcodeContent = parcel.readString();
        this.errorCode = parcel.readInt();
    }

    public void setBarcodeContent(String string) {
        this.barcodeContent = string;
    }

    public void setCardnumber(String string) {
        this.cardnumber = string;
    }

    public void setErrorCode(int n2) {
        this.errorCode = n2;
    }

    public void setPin(String string) {
        this.pin = string;
    }

    public String toString() {
        return "GiftCardDetail  cardnumber: " + this.cardnumber + " pin " + this.pin + " barcodeContent " + this.barcodeContent + "errorCode " + this.errorCode;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.cardnumber);
        parcel.writeString(this.pin);
        parcel.writeString(this.barcodeContent);
        parcel.writeInt(this.errorCode);
    }

}

