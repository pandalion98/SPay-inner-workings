package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class TransactionDetails implements Parcelable {
    public static final Creator<TransactionDetails> CREATOR;
    private String balanceAmount;
    private String balanceCurrencyCode;
    private List<TransactionData> transactionDataList;

    /* renamed from: com.samsung.android.spayfw.appinterface.TransactionDetails.1 */
    static class C03961 implements Creator<TransactionDetails> {
        C03961() {
        }

        public TransactionDetails createFromParcel(Parcel parcel) {
            return new TransactionDetails(null);
        }

        public TransactionDetails[] newArray(int i) {
            return new TransactionDetails[i];
        }
    }

    static {
        CREATOR = new C03961();
    }

    private TransactionDetails(Parcel parcel) {
        this();
        readFromParcel(parcel);
    }

    public TransactionDetails() {
        this.transactionDataList = new ArrayList();
    }

    public String getBalanceAmount() {
        return this.balanceAmount;
    }

    public void setBalanceAmount(String str) {
        this.balanceAmount = str;
    }

    public String getBalanceCurrencyCode() {
        return this.balanceCurrencyCode;
    }

    public void setBalanceCurrencyCode(String str) {
        this.balanceCurrencyCode = str;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(this.transactionDataList);
        parcel.writeString(this.balanceAmount);
        parcel.writeString(this.balanceCurrencyCode);
    }

    public List<TransactionData> getTransactionData() {
        return this.transactionDataList;
    }

    public void setTransactionData(List<TransactionData> list) {
        this.transactionDataList = list;
    }

    public void readFromParcel(Parcel parcel) {
        parcel.readList(this.transactionDataList, getClass().getClassLoader());
        this.balanceAmount = parcel.readString();
        this.balanceCurrencyCode = parcel.readString();
    }

    public String toString() {
        return "TransactionDetails [ transactionDataList=" + this.transactionDataList + "]";
    }
}
