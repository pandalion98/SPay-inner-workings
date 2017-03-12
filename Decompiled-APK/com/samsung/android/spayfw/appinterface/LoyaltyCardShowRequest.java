package com.samsung.android.spayfw.appinterface;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class LoyaltyCardShowRequest implements Parcelable {
    public static final Creator<LoyaltyCardShowRequest> CREATOR;
    public static final String EXTRAS_KEY_CARD_NAME = "cardName";
    private Bundle extras;
    private String tokenId;

    /* renamed from: com.samsung.android.spayfw.appinterface.LoyaltyCardShowRequest.1 */
    static class C03681 implements Creator<LoyaltyCardShowRequest> {
        C03681() {
        }

        public LoyaltyCardShowRequest createFromParcel(Parcel parcel) {
            return new LoyaltyCardShowRequest(null);
        }

        public LoyaltyCardShowRequest[] newArray(int i) {
            return new LoyaltyCardShowRequest[i];
        }
    }

    static {
        CREATOR = new C03681();
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String str) {
        this.tokenId = str;
    }

    public Bundle getExtras() {
        return this.extras;
    }

    public void setExtras(Bundle bundle) {
        this.extras = bundle;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.tokenId);
        parcel.writeBundle(this.extras);
    }

    private LoyaltyCardShowRequest(Parcel parcel) {
        readFromParcel(parcel);
    }

    public void readFromParcel(Parcel parcel) {
        this.tokenId = parcel.readString();
        this.extras = parcel.readBundle();
    }

    public String toString() {
        return "LoyaltyCardShowRequest: tokenId: " + this.tokenId + " extras: " + this.extras;
    }
}
