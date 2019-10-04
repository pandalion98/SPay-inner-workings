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
import com.samsung.contextclient.data.Location;
import com.samsung.contextclient.data.OpenHour;
import java.util.ArrayList;
import java.util.List;

public class PoiAttribute
implements Parcelable {
    public static final Parcelable.Creator<PoiAttribute> CREATOR = new Parcelable.Creator<PoiAttribute>(){

        public PoiAttribute createFromParcel(Parcel parcel) {
            return new PoiAttribute(parcel);
        }

        public PoiAttribute[] newArray(int n2) {
            return new PoiAttribute[n2];
        }
    };
    private ArrayList<String> categories;
    private Location location;
    private String merchantId;
    private String merchantName;
    private OpenHour openHours;

    protected PoiAttribute(Parcel parcel) {
        this.merchantId = parcel.readString();
        this.merchantName = parcel.readString();
        this.openHours = (OpenHour)parcel.readParcelable(OpenHour.class.getClassLoader());
        this.categories = parcel.createStringArrayList();
        this.location = (Location)parcel.readParcelable(Location.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public ArrayList<String> getCategories() {
        return this.categories;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public OpenHour getOpenHours() {
        return this.openHours;
    }

    public void setCategories(ArrayList<String> arrayList) {
        this.categories = arrayList;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setMerchantId(String string) {
        this.merchantId = string;
    }

    public void setMerchantName(String string) {
        this.merchantName = string;
    }

    public void setOpenHours(OpenHour openHour) {
        this.openHours = openHour;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeString(this.merchantId);
        parcel.writeString(this.merchantName);
        parcel.writeParcelable((Parcelable)this.openHours, n2);
        parcel.writeStringList(this.categories);
        parcel.writeParcelable((Parcelable)this.location, n2);
    }

}

