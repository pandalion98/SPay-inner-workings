package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GiftCardDetail implements Parcelable {
    public static final Creator<GiftCardDetail> CREATOR;
    private String barcodeContent;
    private String cardnumber;
    private int errorCode;
    private String pin;

    /* renamed from: com.samsung.android.spayfw.appinterface.GiftCardDetail.1 */
    static class C03531 implements Creator<GiftCardDetail> {
        C03531() {
        }

        public GiftCardDetail createFromParcel(Parcel parcel) {
            return new GiftCardDetail(null);
        }

        public GiftCardDetail[] newArray(int i) {
            return new GiftCardDetail[i];
        }
    }

    static {
        CREATOR = new C03531();
    }

    private GiftCardDetail(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel parcel) {
        this.cardnumber = parcel.readString();
        this.pin = parcel.readString();
        this.barcodeContent = parcel.readString();
        this.errorCode = parcel.readInt();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.cardnumber);
        parcel.writeString(this.pin);
        parcel.writeString(this.barcodeContent);
        parcel.writeInt(this.errorCode);
    }

    public String toString() {
        return "GiftCardDetail  cardnumber: " + this.cardnumber + " pin " + this.pin + " barcodeContent " + this.barcodeContent + "errorCode " + this.errorCode;
    }

    public String getCardnumber() {
        return this.cardnumber;
    }

    public void setCardnumber(String str) {
        this.cardnumber = str;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String str) {
        this.pin = str;
    }

    public String getBarcodeContent() {
        return this.barcodeContent;
    }

    public void setBarcodeContent(String str) {
        this.barcodeContent = str;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int i) {
        this.errorCode = i;
    }
}
