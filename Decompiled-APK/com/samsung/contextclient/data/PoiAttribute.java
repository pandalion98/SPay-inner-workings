package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public class PoiAttribute implements Parcelable {
    public static final Creator<PoiAttribute> CREATOR;
    private ArrayList<String> categories;
    private Location location;
    private String merchantId;
    private String merchantName;
    private OpenHour openHours;

    /* renamed from: com.samsung.contextclient.data.PoiAttribute.1 */
    static class C06081 implements Creator<PoiAttribute> {
        C06081() {
        }

        public PoiAttribute createFromParcel(Parcel parcel) {
            return new PoiAttribute(parcel);
        }

        public PoiAttribute[] newArray(int i) {
            return new PoiAttribute[i];
        }
    }

    protected PoiAttribute(Parcel parcel) {
        this.merchantId = parcel.readString();
        this.merchantName = parcel.readString();
        this.openHours = (OpenHour) parcel.readParcelable(OpenHour.class.getClassLoader());
        this.categories = parcel.createStringArrayList();
        this.location = (Location) parcel.readParcelable(Location.class.getClassLoader());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.merchantId);
        parcel.writeString(this.merchantName);
        parcel.writeParcelable(this.openHours, i);
        parcel.writeStringList(this.categories);
        parcel.writeParcelable(this.location, i);
    }

    public int describeContents() {
        return 0;
    }

    static {
        CREATOR = new C06081();
    }

    public void setMerchantId(String str) {
        this.merchantId = str;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantName(String str) {
        this.merchantName = str;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public void setOpenHours(OpenHour openHour) {
        this.openHours = openHour;
    }

    public OpenHour getOpenHours() {
        return this.openHours;
    }

    public void setCategories(ArrayList<String> arrayList) {
        this.categories = arrayList;
    }

    public ArrayList<String> getCategories() {
        return this.categories;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return this.location;
    }
}
