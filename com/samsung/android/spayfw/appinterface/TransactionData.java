package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class TransactionData implements Parcelable {
    public static final Creator<TransactionData> CREATOR;
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

    /* renamed from: com.samsung.android.spayfw.appinterface.TransactionData.1 */
    static class C03951 implements Creator<TransactionData> {
        C03951() {
        }

        public TransactionData createFromParcel(Parcel parcel) {
            return new TransactionData(null);
        }

        public TransactionData[] newArray(int i) {
            return new TransactionData[i];
        }
    }

    static {
        CREATOR = new C03951();
    }

    private TransactionData(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.transactionId);
        parcel.writeString(this.transactionType);
        parcel.writeString(this.transactionDate);
        parcel.writeString(this.currencyCode);
        parcel.writeString(this.merchantName);
        parcel.writeString(this.amount);
        parcel.writeString(this.transactionStatus);
        parcel.writeBundle(this.customData);
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

    public String getTransactionId() {
        return this.transactionId;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public String getTransactionDate() {
        return this.transactionDate;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public String getMechantName() {
        return this.merchantName;
    }

    public String getAmount() {
        return this.amount;
    }

    public String getTransactionStatus() {
        return this.transactionStatus;
    }

    public Bundle getCustomData() {
        return this.customData;
    }

    public void setTransactionId(String str) {
        this.transactionId = str;
    }

    public void setTransactionType(String str) {
        this.transactionType = str;
    }

    public void setTransactionDate(String str) {
        this.transactionDate = str;
    }

    public void setCurrencyCode(String str) {
        this.currencyCode = str;
    }

    public void setMechantName(String str) {
        this.merchantName = str;
    }

    public void setAmount(String str) {
        this.amount = str;
    }

    public void setTransactionStatus(String str) {
        this.transactionStatus = str;
    }

    public void setCustomData(Bundle bundle) {
        this.customData = bundle;
    }

    public String toString() {
        return "TransactionData [transactionId=" + this.transactionId + ", transactionType=" + this.transactionType + ", transactionDate=" + this.transactionDate + ", currencyCode=" + this.currencyCode + ", merchantName=" + this.merchantName + ", amount=" + this.amount + ", transactionStatus=" + this.transactionStatus + ", customData=" + this.customData + "]";
    }
}
