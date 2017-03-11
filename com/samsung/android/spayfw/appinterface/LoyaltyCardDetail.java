package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class LoyaltyCardDetail implements Parcelable {
    public static final String BUNDLE_KEY_AC_TOKEN_EXTRA = "acTokenExtra";
    public static final String BUNDLE_KEY_BARCODE_CONTENT = "barcodeContent";
    public static final String BUNDLE_KEY_CARD_NUMBER = "cardNumber";
    public static final String BUNDLE_KEY_IMG_SESSION_KEY = "imgSessionKey";
    public static final Creator<LoyaltyCardDetail> CREATOR;
    private Bundle cardDetailBundle;

    /* renamed from: com.samsung.android.spayfw.appinterface.LoyaltyCardDetail.1 */
    static class C03671 implements Creator<LoyaltyCardDetail> {
        C03671() {
        }

        public LoyaltyCardDetail createFromParcel(Parcel parcel) {
            return new LoyaltyCardDetail(null);
        }

        public LoyaltyCardDetail[] newArray(int i) {
            return new LoyaltyCardDetail[i];
        }
    }

    static {
        CREATOR = new C03671();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeBundle(this.cardDetailBundle);
    }

    private LoyaltyCardDetail(Parcel parcel) {
        readFromParcel(parcel);
    }

    public void readFromParcel(Parcel parcel) {
        this.cardDetailBundle = parcel.readBundle();
    }

    public Bundle getCardDetailbundle() {
        return this.cardDetailBundle;
    }

    public void setCardDetailbundle(Bundle bundle) {
        this.cardDetailBundle = bundle;
    }

    public String toString() {
        return "Card Detail Bundle =" + this.cardDetailBundle;
    }
}
