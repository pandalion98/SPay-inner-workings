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

public class LoyaltyCardDetail
implements Parcelable {
    public static final String BUNDLE_KEY_AC_TOKEN_EXTRA = "acTokenExtra";
    public static final String BUNDLE_KEY_BARCODE_CONTENT = "barcodeContent";
    public static final String BUNDLE_KEY_CARD_NUMBER = "cardNumber";
    public static final String BUNDLE_KEY_IMG_SESSION_KEY = "imgSessionKey";
    public static final Parcelable.Creator<LoyaltyCardDetail> CREATOR = new Parcelable.Creator<LoyaltyCardDetail>(){

        public LoyaltyCardDetail createFromParcel(Parcel parcel) {
            return new LoyaltyCardDetail(parcel);
        }

        public LoyaltyCardDetail[] newArray(int n2) {
            return new LoyaltyCardDetail[n2];
        }
    };
    private Bundle cardDetailBundle;

    public LoyaltyCardDetail() {
    }

    private LoyaltyCardDetail(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public Bundle getCardDetailbundle() {
        return this.cardDetailBundle;
    }

    public void readFromParcel(Parcel parcel) {
        this.cardDetailBundle = parcel.readBundle();
    }

    public void setCardDetailbundle(Bundle bundle) {
        this.cardDetailBundle = bundle;
    }

    public String toString() {
        return "Card Detail Bundle =" + (Object)this.cardDetailBundle;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeBundle(this.cardDetailBundle);
    }

}

