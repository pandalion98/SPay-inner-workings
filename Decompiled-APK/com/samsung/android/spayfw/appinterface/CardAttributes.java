package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CardAttributes implements Parcelable {
    public static final Creator<CardAttributes> CREATOR;
    private boolean billingInfo;
    private String cardBrand;
    private boolean cvv;
    private boolean expiry;
    private boolean panValidated;
    private boolean zip;

    /* renamed from: com.samsung.android.spayfw.appinterface.CardAttributes.1 */
    static class C03331 implements Creator<CardAttributes> {
        C03331() {
        }

        public CardAttributes createFromParcel(Parcel parcel) {
            return new CardAttributes(parcel);
        }

        public CardAttributes[] newArray(int i) {
            return new CardAttributes[i];
        }
    }

    static {
        CREATOR = new C03331();
    }

    public CardAttributes(Parcel parcel) {
        boolean z;
        boolean z2 = true;
        this.cvv = true;
        this.expiry = true;
        this.panValidated = false;
        this.zip = true;
        this.billingInfo = false;
        this.cvv = parcel.readInt() == 1;
        if (parcel.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.expiry = z;
        this.cardBrand = parcel.readString();
        if (parcel.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.panValidated = z;
        if (parcel.readInt() == 1) {
            z = true;
        } else {
            z = false;
        }
        this.zip = z;
        if (parcel.readInt() != 1) {
            z2 = false;
        }
        this.billingInfo = z2;
    }

    public CardAttributes() {
        this.cvv = true;
        this.expiry = true;
        this.panValidated = false;
        this.zip = true;
        this.billingInfo = false;
    }

    public boolean isCvvRequired() {
        return this.cvv;
    }

    public boolean isExpiryRequired() {
        return this.expiry;
    }

    public String getCardBrand() {
        return this.cardBrand;
    }

    public boolean isPanValidated() {
        return this.panValidated;
    }

    public boolean isZipRequired() {
        return this.zip;
    }

    public boolean isBillingInfoRequired() {
        return this.billingInfo;
    }

    public void setCvvRequired(boolean z) {
        this.cvv = z;
    }

    public void setExpiryRequired(boolean z) {
        this.expiry = z;
    }

    public void setCardBrand(String str) {
        this.cardBrand = str;
    }

    public void setPanValidated(boolean z) {
        this.panValidated = z;
    }

    public void setZipRequired(boolean z) {
        this.zip = z;
    }

    public void setBillingInfoRequired(boolean z) {
        this.billingInfo = z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int i2;
        int i3 = 1;
        parcel.writeInt(this.cvv ? 1 : 0);
        if (this.expiry) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        parcel.writeInt(i2);
        parcel.writeString(this.cardBrand);
        if (this.panValidated) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        parcel.writeInt(i2);
        if (this.zip) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        parcel.writeInt(i2);
        if (!this.billingInfo) {
            i3 = 0;
        }
        parcel.writeInt(i3);
    }

    public String toString() {
        return "CardAttributes [cvv=" + this.cvv + ", expiry=" + this.expiry + ", cardBrand=" + this.cardBrand + ", panValidated=" + this.panValidated + ", zip=" + this.zip + ", billingInfo=" + this.billingInfo + "]";
    }
}
