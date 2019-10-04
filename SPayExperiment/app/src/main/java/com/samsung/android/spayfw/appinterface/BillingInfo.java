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

public class BillingInfo
implements Parcelable {
    public static final Parcelable.Creator<BillingInfo> CREATOR = new Parcelable.Creator<BillingInfo>(){

        public BillingInfo createFromParcel(Parcel parcel) {
            return new BillingInfo(parcel);
        }

        public BillingInfo[] newArray(int n2) {
            return new BillingInfo[n2];
        }
    };
    private String city;
    private String country;
    private String fname;
    private String lname;
    private String state;
    private String street1;
    private String street2;
    private String street3;
    private String zip;

    public BillingInfo(Parcel parcel) {
        this.readFromParcel(parcel);
    }

    public BillingInfo(String string, String string2) {
        this.fname = string;
        this.lname = string2;
    }

    public int describeContents() {
        return 0;
    }

    public String getCity() {
        return this.city;
    }

    public String getCountry() {
        return this.country;
    }

    public String getFName() {
        return this.fname;
    }

    public String getLName() {
        return this.lname;
    }

    public String getState() {
        return this.state;
    }

    public String getStreet1() {
        return this.street1;
    }

    public String getStreet2() {
        return this.street2;
    }

    public String getStreet3() {
        return this.street3;
    }

    public String getZip() {
        return this.zip;
    }

    public void readFromParcel(Parcel parcel) {
        this.fname = parcel.readString();
        this.lname = parcel.readString();
        this.city = parcel.readString();
        this.country = parcel.readString();
        this.state = parcel.readString();
        this.street1 = parcel.readString();
        this.street2 = parcel.readString();
        this.street3 = parcel.readString();
        this.zip = parcel.readString();
    }

    public void setCity(String string) {
        this.city = string;
    }

    public void setCountry(String string) {
        this.country = string;
    }

    public void setState(String string) {
        this.state = string;
    }

    public void setStreet1(String string) {
        this.street1 = string;
    }

    public void setStreet2(String string) {
        this.street2 = string;
    }

    public void setStreet3(String string) {
        this.street3 = string;
    }

    public void setZip(String string) {
        this.zip = string;
    }

    public String toString() {
        return "BillingInfo [fname=" + this.fname + ", lname=" + this.lname + ", city=" + this.city + ", country=" + this.country + ", state=" + this.state + ", street1=" + this.street1 + ", street2=" + this.street2 + ", street3=" + this.street3 + ", zip=" + this.zip + "]";
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.fname);
        parcel.writeString(this.lname);
        parcel.writeString(this.city);
        parcel.writeString(this.country);
        parcel.writeString(this.state);
        parcel.writeString(this.street1);
        parcel.writeString(this.street2);
        parcel.writeString(this.street3);
        parcel.writeString(this.zip);
    }

}

