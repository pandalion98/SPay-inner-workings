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
import com.samsung.android.spayfw.appinterface.CardArts;

public class CardIssuerApp
implements Parcelable {
    public static final Parcelable.Creator<CardIssuerApp> CREATOR = new Parcelable.Creator<CardIssuerApp>(){

        public CardIssuerApp createFromParcel(Parcel parcel) {
            return new CardIssuerApp(parcel);
        }

        public CardIssuerApp[] newArray(int n2) {
            return new CardIssuerApp[n2];
        }
    };
    public static final String STORE_AMAZON = "AMAZON";
    public static final String STORE_GOOGLE = "GOOGLE";
    public static final String STORE_SAMSUNG = "SAMSUNG";
    private CardArts cardArt;
    private String description;
    private String downloadUrl;
    private String name;
    private String packageName;
    private String store;

    public CardIssuerApp() {
    }

    private CardIssuerApp(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public CardArts getCardArt() {
        return this.cardArt;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public String getName() {
        return this.name;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getStore() {
        return this.store;
    }

    public void readFromParcel(Parcel parcel) {
        this.name = parcel.readString();
        this.packageName = parcel.readString();
        this.store = parcel.readString();
        this.description = parcel.readString();
        this.downloadUrl = parcel.readString();
        this.cardArt = (CardArts)parcel.readParcelable(this.getClass().getClassLoader());
    }

    public void setCardArt(CardArts cardArts) {
        this.cardArt = cardArts;
    }

    public void setDescription(String string) {
        this.description = string;
    }

    public void setDownloadUrl(String string) {
        this.downloadUrl = string;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void setPackageName(String string) {
        this.packageName = string;
    }

    public void setStore(String string) {
        this.store = string;
    }

    public String toString() {
        return "CardIssuerApp [packageName=" + this.packageName + ", name=" + this.name + ", store=" + this.store + ", description=" + this.description + ", downloadUrl=" + this.downloadUrl + ", cardArt=" + this.cardArt + "]";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.name);
        parcel.writeString(this.packageName);
        parcel.writeString(this.store);
        parcel.writeString(this.description);
        parcel.writeString(this.downloadUrl);
        parcel.writeParcelable((Parcelable)this.cardArt, n2);
    }

}

