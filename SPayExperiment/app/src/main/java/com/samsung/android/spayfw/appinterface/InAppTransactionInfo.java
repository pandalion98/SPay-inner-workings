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
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.BillingInfo;

public class InAppTransactionInfo
implements Parcelable {
    public static final Parcelable.Creator<InAppTransactionInfo> CREATOR = new Parcelable.Creator<InAppTransactionInfo>(){

        public InAppTransactionInfo createFromParcel(Parcel parcel) {
            return new InAppTransactionInfo(parcel);
        }

        public InAppTransactionInfo[] newArray(int n2) {
            return new InAppTransactionInfo[n2];
        }
    };
    private String amount;
    private String contextId;
    private String currencyCode;
    private BillingInfo mBillingInfo;
    private String mCardholderName;
    private String mFPANLast4Digits;
    private boolean mIsRecurring;
    private String mMerchantRefId;
    private String pid;

    public InAppTransactionInfo() {
    }

    private InAppTransactionInfo(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getAmount() {
        return this.amount;
    }

    public BillingInfo getBillingInfo() {
        return this.mBillingInfo;
    }

    public String getCardholderName() {
        return this.mCardholderName;
    }

    public String getContextId() {
        return this.contextId;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public String getFPANLast4Digits() {
        return this.mFPANLast4Digits;
    }

    public String getMerchantRefId() {
        return this.mMerchantRefId;
    }

    public String getPid() {
        return this.pid;
    }

    public boolean isRecurring() {
        return this.mIsRecurring;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void readFromParcel(Parcel parcel) {
        this.contextId = parcel.readString();
        this.amount = parcel.readString();
        this.currencyCode = parcel.readString();
        this.pid = parcel.readString();
        this.mMerchantRefId = parcel.readString();
        this.mCardholderName = parcel.readString();
        this.mBillingInfo = (BillingInfo)parcel.readParcelable(this.getClass().getClassLoader());
        boolean bl = parcel.readInt() != 0;
        this.mIsRecurring = bl;
        this.mFPANLast4Digits = parcel.readString();
    }

    public void setAmount(String string) {
        this.amount = string;
    }

    public void setBillingInfo(BillingInfo billingInfo) {
        this.mBillingInfo = billingInfo;
    }

    public void setCardholderName(String string) {
        this.mCardholderName = string;
    }

    public void setContextId(String string) {
        this.contextId = string;
    }

    public void setCurrencyCode(String string) {
        this.currencyCode = string;
    }

    public void setFPANLast4Digits(String string) {
        this.mFPANLast4Digits = string;
    }

    public void setMerchantRefId(String string) {
        this.mMerchantRefId = string;
    }

    public void setPid(String string) {
        this.pid = string;
    }

    public void setRecurring(boolean bl) {
        this.mIsRecurring = bl;
    }

    public String toString() {
        return "InAppTransactionInfo{contextId='" + this.contextId + '\'' + ", amount='" + this.amount + '\'' + ", currencyCode='" + this.currencyCode + '\'' + ", pid='" + this.pid + '\'' + ", mMerchantRefId='" + this.mMerchantRefId + '\'' + ", mBillingInfo=" + this.mBillingInfo + ", mCardholderName='" + this.mCardholderName + '\'' + ", mIsRecurring=" + this.mIsRecurring + ", mFPANLast4Digits='" + this.mFPANLast4Digits + '\'' + '}';
    }

    /*
     * Enabled aggressive block sorting
     */
    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.contextId);
        parcel.writeString(this.amount);
        parcel.writeString(this.currencyCode);
        parcel.writeString(this.pid);
        parcel.writeString(this.mMerchantRefId);
        parcel.writeString(this.mCardholderName);
        parcel.writeParcelable((Parcelable)this.mBillingInfo, n2);
        int n3 = this.mIsRecurring ? 1 : 0;
        parcel.writeInt(n3);
        parcel.writeString(this.mFPANLast4Digits);
    }

}

