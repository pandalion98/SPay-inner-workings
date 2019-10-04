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
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.CardIssuerApp;
import com.samsung.android.spayfw.appinterface.TnC;
import java.util.ArrayList;
import java.util.List;

public class CardIssuer
implements Parcelable {
    public static final Parcelable.Creator<CardIssuer> CREATOR = new Parcelable.Creator<CardIssuer>(){

        public CardIssuer createFromParcel(Parcel parcel) {
            return new CardIssuer(parcel);
        }

        public CardIssuer[] newArray(int n2) {
            return new CardIssuer[n2];
        }
    };
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

    public CardIssuer() {
        this.tnc = new ArrayList();
    }

    private CardIssuer(Parcel parcel) {
        this();
        this.readFromParcel(parcel);
    }

    public CardIssuer(String string, String string2, String string3, String string4) {
        this.name = string;
        this.email = string2;
        this.webAddress = string3;
        this.phone = string4;
    }

    public int describeContents() {
        return 0;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFacebookAdderess() {
        return this.facebook;
    }

    public CardIssuerApp getIssuerAppDetails() {
        return this.app;
    }

    public String getName() {
        return this.name;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getPinterestAddress() {
        return this.pinterest;
    }

    public List<TnC> getTnC() {
        return this.tnc;
    }

    public String getTwitterAddress() {
        return this.twitter;
    }

    public String getWeb() {
        return this.webAddress;
    }

    public void readFromParcel(Parcel parcel) {
        this.name = parcel.readString();
        this.email = parcel.readString();
        this.webAddress = parcel.readString();
        this.phone = parcel.readString();
        this.facebook = parcel.readString();
        this.twitter = parcel.readString();
        this.pinterest = parcel.readString();
        parcel.readList(this.tnc, this.getClass().getClassLoader());
        this.app = (CardIssuerApp)parcel.readParcelable(CardIssuerApp.class.getClassLoader());
        this.accountNumber = parcel.readString();
    }

    public void setAccountNumber(String string) {
        this.accountNumber = string;
    }

    public void setEmail(String string) {
        this.email = string;
    }

    public void setFacebookAddress(String string) {
        this.facebook = string;
    }

    public void setIssuerAppDetails(CardIssuerApp cardIssuerApp) {
        this.app = cardIssuerApp;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void setPhone(String string) {
        this.phone = string;
    }

    public void setPinterestAddress(String string) {
        this.pinterest = string;
    }

    public void setTnC(List<TnC> list) {
        this.tnc = list;
    }

    public void setTwitterAddress(String string) {
        this.twitter = string;
    }

    public void setWeb(String string) {
        this.webAddress = string;
    }

    public String toString() {
        return "CardIssuer [email=" + this.email + ", name=" + this.name + ", phone=" + this.phone + ", webAddress=" + this.webAddress + ", facebook=" + this.facebook + ", twitter=" + this.twitter + ", pinterest=" + this.pinterest + ", tnc=" + this.tnc + ", app=" + this.app + "]";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.name);
        parcel.writeString(this.email);
        parcel.writeString(this.webAddress);
        parcel.writeString(this.phone);
        parcel.writeString(this.facebook);
        parcel.writeString(this.twitter);
        parcel.writeString(this.pinterest);
        parcel.writeList(this.tnc);
        parcel.writeParcelable((Parcelable)this.app, n2);
        parcel.writeString(this.accountNumber);
    }

}

