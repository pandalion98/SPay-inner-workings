package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class CardIssuerApp implements Parcelable {
    public static final Creator<CardIssuerApp> CREATOR;
    public static final String STORE_AMAZON = "AMAZON";
    public static final String STORE_GOOGLE = "GOOGLE";
    public static final String STORE_SAMSUNG = "SAMSUNG";
    private CardArts cardArt;
    private String description;
    private String downloadUrl;
    private String name;
    private String packageName;
    private String store;

    /* renamed from: com.samsung.android.spayfw.appinterface.CardIssuerApp.1 */
    static class C03371 implements Creator<CardIssuerApp> {
        C03371() {
        }

        public CardIssuerApp createFromParcel(Parcel parcel) {
            return new CardIssuerApp(null);
        }

        public CardIssuerApp[] newArray(int i) {
            return new CardIssuerApp[i];
        }
    }

    static {
        CREATOR = new C03371();
    }

    private CardIssuerApp(Parcel parcel) {
        readFromParcel(parcel);
    }

    public int describeContents() {
        return 0;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStore() {
        return this.store;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public CardArts getCardArt() {
        return this.cardArt;
    }

    public void readFromParcel(Parcel parcel) {
        this.name = parcel.readString();
        this.packageName = parcel.readString();
        this.store = parcel.readString();
        this.description = parcel.readString();
        this.downloadUrl = parcel.readString();
        this.cardArt = (CardArts) parcel.readParcelable(getClass().getClassLoader());
    }

    public void setPackageName(String str) {
        this.packageName = str;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public void setStore(String str) {
        this.store = str;
    }

    public void setDownloadUrl(String str) {
        this.downloadUrl = str;
    }

    public void setCardArt(CardArts cardArts) {
        this.cardArt = cardArts;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.packageName);
        parcel.writeString(this.store);
        parcel.writeString(this.description);
        parcel.writeString(this.downloadUrl);
        parcel.writeParcelable(this.cardArt, i);
    }

    public String toString() {
        return "CardIssuerApp [packageName=" + this.packageName + ", name=" + this.name + ", store=" + this.store + ", description=" + this.description + ", downloadUrl=" + this.downloadUrl + ", cardArt=" + this.cardArt + "]";
    }
}
