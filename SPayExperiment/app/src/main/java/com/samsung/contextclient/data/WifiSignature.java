/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.ClassLoader
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.contextclient.data.RssiProperties;
import java.util.ArrayList;
import java.util.List;

public class WifiSignature
implements Parcelable {
    public static final Parcelable.Creator<WifiSignature> CREATOR = new Parcelable.Creator<WifiSignature>(){

        public WifiSignature createFromParcel(Parcel parcel) {
            return new WifiSignature(parcel);
        }

        public WifiSignature[] newArray(int n2) {
            return new WifiSignature[n2];
        }
    };
    private String bssid;
    private int bssidAppearencePercentage;
    private RssiProperties rssiProperties;
    private ArrayList<String> ssid;

    protected WifiSignature(Parcel parcel) {
        this.bssidAppearencePercentage = parcel.readInt();
        this.bssid = parcel.readString();
        this.ssid = parcel.createStringArrayList();
        this.rssiProperties = (RssiProperties)parcel.readParcelable(RssiProperties.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public String getBssid() {
        return this.bssid;
    }

    public int getBssidAppearencePercentage() {
        return this.bssidAppearencePercentage;
    }

    public RssiProperties getRssiProperties() {
        return this.rssiProperties;
    }

    public ArrayList<String> getSsid() {
        return this.ssid;
    }

    public void setBssid(String string) {
        this.bssid = string;
    }

    public void setBssidAppearencePercentage(int n2) {
        this.bssidAppearencePercentage = n2;
    }

    public void setRssiProperties(RssiProperties rssiProperties) {
        this.rssiProperties = rssiProperties;
    }

    public void setSsid(ArrayList<String> arrayList) {
        this.ssid = arrayList;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeInt(this.bssidAppearencePercentage);
        parcel.writeString(this.bssid);
        parcel.writeStringList(this.ssid);
        parcel.writeParcelable((Parcelable)this.rssiProperties, n2);
    }

}

