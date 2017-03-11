package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CardState implements Parcelable {
    public static final Creator<CardState> CREATOR;
    private boolean availableForPay;
    private String enrollmentId;
    private TokenStatus status;
    private String tokenId;

    /* renamed from: com.samsung.android.spayfw.appinterface.CardState.1 */
    static class C03381 implements Creator<CardState> {
        C03381() {
        }

        public CardState createFromParcel(Parcel parcel) {
            return new CardState(null);
        }

        public CardState[] newArray(int i) {
            return new CardState[i];
        }
    }

    static {
        CREATOR = new C03381();
    }

    private CardState(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        boolean z = true;
        this.enrollmentId = parcel.readString();
        this.tokenId = parcel.readString();
        if (parcel.readInt() != 1) {
            z = false;
        }
        this.availableForPay = z;
        this.status = (TokenStatus) parcel.readParcelable(TokenStatus.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public String getEnrollmentId() {
        return this.enrollmentId;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public boolean isAvailableForPay() {
        return this.availableForPay;
    }

    public TokenStatus getTokenStatus() {
        return this.status;
    }

    public void setEnrollmentId(String str) {
        this.enrollmentId = str;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
    }

    public void setAvailableForPayState(boolean z) {
        this.availableForPay = z;
    }

    public void setTokenStatus(TokenStatus tokenStatus) {
        this.status = tokenStatus;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.enrollmentId);
        parcel.writeString(this.tokenId);
        parcel.writeInt(this.availableForPay ? 1 : 0);
        parcel.writeParcelable(this.status, i);
    }

    public String toString() {
        return "CardState [enrollmentId=" + this.enrollmentId + ", tokenId=" + this.tokenId + ", availableForPay=" + this.availableForPay + ", status=" + this.status + "]";
    }
}
