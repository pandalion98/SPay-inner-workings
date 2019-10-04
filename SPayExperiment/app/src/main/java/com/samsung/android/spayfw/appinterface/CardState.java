/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.TokenStatus;

public class CardState
implements Parcelable {
    public static final Parcelable.Creator<CardState> CREATOR = new Parcelable.Creator<CardState>(){

        public CardState createFromParcel(Parcel parcel) {
            return new CardState(parcel);
        }

        public CardState[] newArray(int n2) {
            return new CardState[n2];
        }
    };
    private boolean availableForPay;
    private String enrollmentId;
    private TokenStatus status;
    private String tokenId;

    public CardState() {
    }

    private CardState(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void readFromParcel(Parcel parcel) {
        int n2 = 1;
        this.enrollmentId = parcel.readString();
        this.tokenId = parcel.readString();
        if (parcel.readInt() != n2) {
            n2 = 0;
        }
        this.availableForPay = n2;
        this.status = (TokenStatus)parcel.readParcelable(TokenStatus.class.getClassLoader());
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

    public TokenStatus getTokenStatus() {
        return this.status;
    }

    public boolean isAvailableForPay() {
        return this.availableForPay;
    }

    public void setAvailableForPayState(boolean bl) {
        this.availableForPay = bl;
    }

    public void setEnrollmentId(String string) {
        this.enrollmentId = string;
    }

    public void setTokenId(String string) {
        this.tokenId = string;
    }

    public void setTokenStatus(TokenStatus tokenStatus) {
        this.status = tokenStatus;
    }

    public String toString() {
        return "CardState [enrollmentId=" + this.enrollmentId + ", tokenId=" + this.tokenId + ", availableForPay=" + this.availableForPay + ", status=" + this.status + "]";
    }

    /*
     * Enabled aggressive block sorting
     */
    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.enrollmentId);
        parcel.writeString(this.tokenId);
        int n3 = this.availableForPay ? 1 : 0;
        parcel.writeInt(n3);
        parcel.writeParcelable((Parcelable)this.status, n2);
    }

}

