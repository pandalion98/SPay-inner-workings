package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class CardIssuer implements Parcelable {
    public static final Creator<CardIssuer> CREATOR;
    private String accountNumber;
    private CardIssuerApp app;
    private String email;
    private String facebook;
    private String name;
    private String phone;
    private String pinterest;
    private List<TnC> tnc;
    private String twitter;
    private String webAddress;

    /* renamed from: com.samsung.android.spayfw.appinterface.CardIssuer.1 */
    static class C03361 implements Creator<CardIssuer> {
        C03361() {
        }

        public CardIssuer createFromParcel(Parcel parcel) {
            return new CardIssuer(null);
        }

        public CardIssuer[] newArray(int i) {
            return new CardIssuer[i];
        }
    }

    static {
        CREATOR = new C03361();
    }

    private CardIssuer(Parcel parcel) {
        this();
        readFromParcel(parcel);
    }

    public CardIssuer() {
        this.tnc = new ArrayList();
    }

    public CardIssuer(String str, String str2, String str3, String str4) {
        this.name = str;
        this.email = str2;
        this.webAddress = str3;
        this.phone = str4;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(String str) {
        this.accountNumber = str;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.email);
        parcel.writeString(this.webAddress);
        parcel.writeString(this.phone);
        parcel.writeString(this.facebook);
        parcel.writeString(this.twitter);
        parcel.writeString(this.pinterest);
        parcel.writeList(this.tnc);
        parcel.writeParcelable(this.app, i);
        parcel.writeString(this.accountNumber);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String str) {
        this.phone = str;
    }

    public String getFacebookAdderess() {
        return this.facebook;
    }

    public String getTwitterAddress() {
        return this.twitter;
    }

    public void setTwitterAddress(String str) {
        this.twitter = str;
    }

    public String getPinterestAddress() {
        return this.pinterest;
    }

    public void setPinterestAddress(String str) {
        this.pinterest = str;
    }

    public String getWeb() {
        return this.webAddress;
    }

    public void setWeb(String str) {
        this.webAddress = str;
    }

    public List<TnC> getTnC() {
        return this.tnc;
    }

    public void setTnC(List<TnC> list) {
        this.tnc = list;
    }

    public CardIssuerApp getIssuerAppDetails() {
        return this.app;
    }

    public void setIssuerAppDetails(CardIssuerApp cardIssuerApp) {
        this.app = cardIssuerApp;
    }

    public void readFromParcel(Parcel parcel) {
        this.name = parcel.readString();
        this.email = parcel.readString();
        this.webAddress = parcel.readString();
        this.phone = parcel.readString();
        this.facebook = parcel.readString();
        this.twitter = parcel.readString();
        this.pinterest = parcel.readString();
        parcel.readList(this.tnc, getClass().getClassLoader());
        this.app = (CardIssuerApp) parcel.readParcelable(CardIssuerApp.class.getClassLoader());
        this.accountNumber = parcel.readString();
    }

    public void setFacebookAddress(String str) {
        this.facebook = str;
    }

    public String toString() {
        return "CardIssuer [email=" + this.email + ", name=" + this.name + ", phone=" + this.phone + ", webAddress=" + this.webAddress + ", facebook=" + this.facebook + ", twitter=" + this.twitter + ", pinterest=" + this.pinterest + ", tnc=" + this.tnc + ", app=" + this.app + "]";
    }
}
