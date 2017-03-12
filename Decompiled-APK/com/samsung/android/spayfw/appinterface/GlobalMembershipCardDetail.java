package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GlobalMembershipCardDetail implements Parcelable {
    public static final Creator<GlobalMembershipCardDetail> CREATOR;
    private String barcodeContent;
    private String barcodeType;
    private String cardnumber;
    private int errorCode;
    private String numericValue;
    private String pin;
    private String tokenId;

    /* renamed from: com.samsung.android.spayfw.appinterface.GlobalMembershipCardDetail.1 */
    static class C03581 implements Creator<GlobalMembershipCardDetail> {
        C03581() {
        }

        public GlobalMembershipCardDetail createFromParcel(Parcel parcel) {
            return new GlobalMembershipCardDetail(null);
        }

        public GlobalMembershipCardDetail[] newArray(int i) {
            return new GlobalMembershipCardDetail[i];
        }
    }

    static {
        CREATOR = new C03581();
    }

    private GlobalMembershipCardDetail(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
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

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.tokenId);
        parcel.writeString(this.cardnumber);
        parcel.writeString(this.pin);
        parcel.writeString(this.barcodeContent);
        parcel.writeString(this.barcodeType);
        parcel.writeString(this.numericValue);
        parcel.writeInt(this.errorCode);
    }

    public String toString() {
        return "globalMembershipCardDetail  tokenId [" + this.tokenId + "] cardnumber: [" + this.cardnumber + "] pin [" + this.pin + "] barcodeContent [" + this.barcodeContent + "] barcodeType [" + this.barcodeType + "] numericValue [" + this.numericValue + "] errorCode [" + this.errorCode + "]";
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
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

    public String getBarcodeType() {
        return this.barcodeType;
    }

    public void setBarcodeType(String str) {
        this.barcodeType = str;
    }

    public String getNumericValue() {
        return this.numericValue;
    }

    public void setNumericValue(String str) {
        this.numericValue = str;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int i) {
        this.errorCode = i;
    }
}
