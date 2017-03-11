package com.samsung.android.spayfw.appinterface;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class BillingInfo implements Parcelable {
    public static final Creator<BillingInfo> CREATOR;
    private String city;
    private String country;
    private String fname;
    private String lname;
    private String state;
    private String street1;
    private String street2;
    private String street3;
    private String zip;

    /* renamed from: com.samsung.android.spayfw.appinterface.BillingInfo.1 */
    static class C03301 implements Creator<BillingInfo> {
        C03301() {
        }

        public BillingInfo createFromParcel(Parcel parcel) {
            return new BillingInfo(parcel);
        }

        public BillingInfo[] newArray(int i) {
            return new BillingInfo[i];
        }
    }

    public String toString() {
        return "BillingInfo [fname=" + this.fname + ", lname=" + this.lname + ", city=" + this.city + ", country=" + this.country + ", state=" + this.state + ", street1=" + this.street1 + ", street2=" + this.street2 + ", street3=" + this.street3 + ", zip=" + this.zip + "]";
    }

    static {
        CREATOR = new C03301();
    }

    public BillingInfo(Parcel parcel) {
        readFromParcel(parcel);
    }

    public BillingInfo(String str, String str2) {
        this.fname = str;
        this.lname = str2;
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

    public void setCity(String str) {
        this.city = str;
    }

    public void setCountry(String str) {
        this.country = str;
    }

    public void setState(String str) {
        this.state = str;
    }

    public void setStreet1(String str) {
        this.street1 = str;
    }

    public void setStreet2(String str) {
        this.street2 = str;
    }

    public void setStreet3(String str) {
        this.street3 = str;
    }

    public void setZip(String str) {
        this.zip = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
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
