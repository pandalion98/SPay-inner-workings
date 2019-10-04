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

public class LoyaltyCardShowRequest
implements Parcelable {
    public static final Parcelable.Creator<LoyaltyCardShowRequest> CREATOR = new Parcelable.Creator<LoyaltyCardShowRequest>(){

        public LoyaltyCardShowRequest createFromParcel(Parcel parcel) {
            return new LoyaltyCardShowRequest(parcel);
        }

        public LoyaltyCardShowRequest[] newArray(int n2) {
            return new LoyaltyCardShowRequest[n2];
        }
    };
    public static final String EXTRAS_KEY_CARD_NAME = "cardName";
    private Bundle extras;
    private String tokenId;

    public LoyaltyCardShowRequest() {
    }

    private LoyaltyCardShowRequest(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public Bundle getExtras() {
        return this.extras;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void readFromParcel(Parcel parcel) {
        this.tokenId = parcel.readString();
        this.extras = parcel.readBundle();
    }

    public void setExtras(Bundle bundle) {
        this.extras = bundle;
    }

    public void setTokenId(String string) {
        this.tokenId = string;
    }

    public String toString() {
        return "LoyaltyCardShowRequest: tokenId: " + this.tokenId + " extras: " + (Object)this.extras;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.tokenId);
        parcel.writeBundle(this.extras);
    }

}

