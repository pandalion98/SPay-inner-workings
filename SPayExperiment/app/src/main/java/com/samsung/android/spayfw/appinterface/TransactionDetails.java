/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Class
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.TransactionData;
import java.util.ArrayList;
import java.util.List;

public class TransactionDetails
implements Parcelable {
    public static final Parcelable.Creator<TransactionDetails> CREATOR = new Parcelable.Creator<TransactionDetails>(){

        public TransactionDetails createFromParcel(Parcel parcel) {
            return new TransactionDetails(parcel);
        }

        public TransactionDetails[] newArray(int n2) {
            return new TransactionDetails[n2];
        }
    };
    private String balanceAmount;
    private String balanceCurrencyCode;
    private List<TransactionData> transactionDataList = new ArrayList();

    public TransactionDetails() {
    }

    private TransactionDetails(Parcel parcel) {
        this();
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getBalanceAmount() {
        return this.balanceAmount;
    }

    public String getBalanceCurrencyCode() {
        return this.balanceCurrencyCode;
    }

    public List<TransactionData> getTransactionData() {
        return this.transactionDataList;
    }

    public void readFromParcel(Parcel parcel) {
        parcel.readList(this.transactionDataList, this.getClass().getClassLoader());
        this.balanceAmount = parcel.readString();
        this.balanceCurrencyCode = parcel.readString();
    }

    public void setBalanceAmount(String string) {
        this.balanceAmount = string;
    }

    public void setBalanceCurrencyCode(String string) {
        this.balanceCurrencyCode = string;
    }

    public void setTransactionData(List<TransactionData> list) {
        this.transactionDataList = list;
    }

    public String toString() {
        return "TransactionDetails [ transactionDataList=" + this.transactionDataList + "]";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeList(this.transactionDataList);
        parcel.writeString(this.balanceAmount);
        parcel.writeString(this.balanceCurrencyCode);
    }

}

