/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class TransactionData
implements Parcelable {
    public static final Parcelable.Creator<TransactionData> CREATOR = new Parcelable.Creator<TransactionData>(){

        public TransactionData createFromParcel(Parcel parcel) {
            return new TransactionData(parcel);
        }

        public TransactionData[] newArray(int n2) {
            return new TransactionData[n2];
        }
    };
    public static final String DISPLAYUNTIL = "displayUntil";
    public static final String INDUSTRYCATGCODE = "industryCatgCode";
    public static final String INDUSTRYCATGNAME = "industryCatgName";
    public static final String INDUSTRYCODE = "industryCode";
    public static final String INDUSTRYNAME = "industryName";
    public static final String MERCHANTTYPE = "merchantType";
    public static final String MERCHANTZIPCODE = "merchantZipCode";
    public static final String MESSAGE = "message";
    public static final String MESSAGEDETAILURL = "MessageDetailUrl";
    public static final String MESSAGEVAILDUNTIL = "messageValidUntil";
    public static final String TRANSACTIONDETAILURL = "trnasactionDetailUrl";
    public static final String TRANSACTIONIDENTIFIER = "transactionIdentifier";
    public static final String TRANSACTION_STATUS_APPROVED = "Approved";
    public static final String TRANSACTION_STATUS_DECLINED = "Declined";
    public static final String TRANSACTION_STATUS_PENDING = "Pending";
    public static final String TRANSACTION_STATUS_REFUNDED = "Refunded";
    public static final String TRANSACTION_TYPE_PURCHASE = "Purchase";
    public static final String TRANSACTION_TYPE_REFUND = "Refund";
    public static final String TRANSACTION_TYPE_REWARD = "Reward";
    private String amount;
    private String currencyCode;
    private Bundle customData;
    private String merchantName;
    private String transactionDate;
    private String transactionId;
    private String transactionStatus;
    private String transactionType;

    public TransactionData() {
    }

    private TransactionData(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public Bundle getCustomData() {
        return this.customData;
    }

    public String getMechantName() {
        return this.merchantName;
    }

    public String getTransactionDate() {
        return this.transactionDate;
    }

    public String getTransactionId() {
        return this.transactionId;
    }

    public String getTransactionStatus() {
        return this.transactionStatus;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public void readFromParcel(Parcel parcel) {
        this.transactionId = parcel.readString();
        this.transactionType = parcel.readString();
        this.transactionDate = parcel.readString();
        this.currencyCode = parcel.readString();
        this.merchantName = parcel.readString();
        this.amount = parcel.readString();
        this.transactionStatus = parcel.readString();
        this.customData = parcel.readBundle();
    }

    public void setAmount(String string) {
        this.amount = string;
    }

    public void setCurrencyCode(String string) {
        this.currencyCode = string;
    }

    public void setCustomData(Bundle bundle) {
        this.customData = bundle;
    }

    public void setMechantName(String string) {
        this.merchantName = string;
    }

    public void setTransactionDate(String string) {
        this.transactionDate = string;
    }

    public void setTransactionId(String string) {
        this.transactionId = string;
    }

    public void setTransactionStatus(String string) {
        this.transactionStatus = string;
    }

    public void setTransactionType(String string) {
        this.transactionType = string;
    }

    public String toString() {
        return "TransactionData [transactionId=" + this.transactionId + ", transactionType=" + this.transactionType + ", transactionDate=" + this.transactionDate + ", currencyCode=" + this.currencyCode + ", merchantName=" + this.merchantName + ", amount=" + this.amount + ", transactionStatus=" + this.transactionStatus + ", customData=" + (Object)this.customData + "]";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.transactionId);
        parcel.writeString(this.transactionType);
        parcel.writeString(this.transactionDate);
        parcel.writeString(this.currencyCode);
        parcel.writeString(this.merchantName);
        parcel.writeString(this.amount);
        parcel.writeString(this.transactionStatus);
        parcel.writeBundle(this.customData);
    }

}

