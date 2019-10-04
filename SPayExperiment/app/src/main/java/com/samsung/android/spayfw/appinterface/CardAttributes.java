/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;

public class CardAttributes
implements Parcelable {
    public static final Parcelable.Creator<CardAttributes> CREATOR = new Parcelable.Creator<CardAttributes>(){

        public CardAttributes createFromParcel(Parcel parcel) {
            return new CardAttributes(parcel);
        }

        public CardAttributes[] newArray(int n2) {
            return new CardAttributes[n2];
        }
    };
    private boolean billingInfo;
    private String cardBrand;
    private boolean cvv;
    private boolean expiry;
    private boolean panValidated;
    private boolean zip;

    public CardAttributes() {
        this.cvv = true;
        this.expiry = true;
        this.panValidated = false;
        this.zip = true;
        this.billingInfo = false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public CardAttributes(Parcel parcel) {
        int n2 = 1;
        this.cvv = n2;
        this.expiry = n2;
        this.panValidated = false;
        this.zip = n2;
        this.billingInfo = false;
        int n3 = parcel.readInt() == n2 ? n2 : 0;
        this.cvv = n3;
        int n4 = parcel.readInt() == n2 ? n2 : 0;
        this.expiry = n4;
        this.cardBrand = parcel.readString();
        int n5 = parcel.readInt() == n2 ? n2 : 0;
        this.panValidated = n5;
        int n6 = parcel.readInt() == n2 ? n2 : 0;
        this.zip = n6;
        if (parcel.readInt() != n2) {
            n2 = 0;
        }
        this.billingInfo = n2;
    }

    public int describeContents() {
        return 0;
    }

    public String getCardBrand() {
        return this.cardBrand;
    }

    public boolean isBillingInfoRequired() {
        return this.billingInfo;
    }

    public boolean isCvvRequired() {
        return this.cvv;
    }

    public boolean isExpiryRequired() {
        return this.expiry;
    }

    public boolean isPanValidated() {
        return this.panValidated;
    }

    public boolean isZipRequired() {
        return this.zip;
    }

    public void setBillingInfoRequired(boolean bl) {
        this.billingInfo = bl;
    }

    public void setCardBrand(String string) {
        this.cardBrand = string;
    }

    public void setCvvRequired(boolean bl) {
        this.cvv = bl;
    }

    public void setExpiryRequired(boolean bl) {
        this.expiry = bl;
    }

    public void setPanValidated(boolean bl) {
        this.panValidated = bl;
    }

    public void setZipRequired(boolean bl) {
        this.zip = bl;
    }

    public String toString() {
        return "CardAttributes [cvv=" + this.cvv + ", expiry=" + this.expiry + ", cardBrand=" + this.cardBrand + ", panValidated=" + this.panValidated + ", zip=" + this.zip + ", billingInfo=" + this.billingInfo + "]";
    }

    /*
     * Enabled aggressive block sorting
     */
    public void writeToParcel(Parcel parcel, int n2) {
        int n3 = 1;
        int n4 = this.cvv ? n3 : 0;
        parcel.writeInt(n4);
        int n5 = this.expiry ? n3 : 0;
        parcel.writeInt(n5);
        parcel.writeString(this.cardBrand);
        int n6 = this.panValidated ? n3 : 0;
        parcel.writeInt(n6);
        int n7 = this.zip ? n3 : 0;
        parcel.writeInt(n7);
        if (!this.billingInfo) {
            n3 = 0;
        }
        parcel.writeInt(n3);
    }

}

