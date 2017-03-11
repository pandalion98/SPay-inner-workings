package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class InAppTransactionInfo implements Parcelable {
    public static final Creator<InAppTransactionInfo> CREATOR;
    private String amount;
    private String contextId;
    private String currencyCode;
    private BillingInfo mBillingInfo;
    private String mCardholderName;
    private String mFPANLast4Digits;
    private boolean mIsRecurring;
    private String mMerchantRefId;
    private String pid;

    /* renamed from: com.samsung.android.spayfw.appinterface.InAppTransactionInfo.1 */
    static class C03651 implements Creator<InAppTransactionInfo> {
        C03651() {
        }

        public InAppTransactionInfo createFromParcel(Parcel parcel) {
            return new InAppTransactionInfo(null);
        }

        public InAppTransactionInfo[] newArray(int i) {
            return new InAppTransactionInfo[i];
        }
    }

    static {
        CREATOR = new C03651();
    }

    private InAppTransactionInfo(Parcel parcel) {
        readFromParcel(parcel);
    }

    public void readFromParcel(Parcel parcel) {
        this.contextId = parcel.readString();
        this.amount = parcel.readString();
        this.currencyCode = parcel.readString();
        this.pid = parcel.readString();
        this.mMerchantRefId = parcel.readString();
        this.mCardholderName = parcel.readString();
        this.mBillingInfo = (BillingInfo) parcel.readParcelable(getClass().getClassLoader());
        this.mIsRecurring = parcel.readInt() != 0;
        this.mFPANLast4Digits = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.contextId);
        parcel.writeString(this.amount);
        parcel.writeString(this.currencyCode);
        parcel.writeString(this.pid);
        parcel.writeString(this.mMerchantRefId);
        parcel.writeString(this.mCardholderName);
        parcel.writeParcelable(this.mBillingInfo, i);
        parcel.writeInt(this.mIsRecurring ? 1 : 0);
        parcel.writeString(this.mFPANLast4Digits);
    }

    public int describeContents() {
        return 0;
    }

    public String getContextId() {
        return this.contextId;
    }

    public void setContextId(String str) {
        this.contextId = str;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String str) {
        this.amount = str;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public void setCurrencyCode(String str) {
        this.currencyCode = str;
    }

    public String getPid() {
        return this.pid;
    }

    public void setPid(String str) {
        this.pid = str;
    }

    public String getMerchantRefId() {
        return this.mMerchantRefId;
    }

    public void setMerchantRefId(String str) {
        this.mMerchantRefId = str;
    }

    public BillingInfo getBillingInfo() {
        return this.mBillingInfo;
    }

    public void setBillingInfo(BillingInfo billingInfo) {
        this.mBillingInfo = billingInfo;
    }

    public String getCardholderName() {
        return this.mCardholderName;
    }

    public void setCardholderName(String str) {
        this.mCardholderName = str;
    }

    public boolean isRecurring() {
        return this.mIsRecurring;
    }

    public void setRecurring(boolean z) {
        this.mIsRecurring = z;
    }

    public String toString() {
        return "InAppTransactionInfo{contextId='" + this.contextId + '\'' + ", amount='" + this.amount + '\'' + ", currencyCode='" + this.currencyCode + '\'' + ", pid='" + this.pid + '\'' + ", mMerchantRefId='" + this.mMerchantRefId + '\'' + ", mBillingInfo=" + this.mBillingInfo + ", mCardholderName='" + this.mCardholderName + '\'' + ", mIsRecurring=" + this.mIsRecurring + ", mFPANLast4Digits='" + this.mFPANLast4Digits + '\'' + '}';
    }

    public String getFPANLast4Digits() {
        return this.mFPANLast4Digits;
    }

    public void setFPANLast4Digits(String str) {
        this.mFPANLast4Digits = str;
    }
}
