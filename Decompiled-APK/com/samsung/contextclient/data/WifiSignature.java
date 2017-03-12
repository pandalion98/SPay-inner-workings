package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public class WifiSignature implements Parcelable {
    public static final Creator<WifiSignature> CREATOR;
    private String bssid;
    private int bssidAppearencePercentage;
    private RssiProperties rssiProperties;
    private ArrayList<String> ssid;

    /* renamed from: com.samsung.contextclient.data.WifiSignature.1 */
    static class C06141 implements Creator<WifiSignature> {
        C06141() {
        }

        public WifiSignature createFromParcel(Parcel parcel) {
            return new WifiSignature(parcel);
        }

        public WifiSignature[] newArray(int i) {
            return new WifiSignature[i];
        }
    }

    protected WifiSignature(Parcel parcel) {
        this.bssidAppearencePercentage = parcel.readInt();
        this.bssid = parcel.readString();
        this.ssid = parcel.createStringArrayList();
        this.rssiProperties = (RssiProperties) parcel.readParcelable(RssiProperties.class.getClassLoader());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.bssidAppearencePercentage);
        parcel.writeString(this.bssid);
        parcel.writeStringList(this.ssid);
        parcel.writeParcelable(this.rssiProperties, i);
    }

    public int describeContents() {
        return 0;
    }

    static {
        CREATOR = new C06141();
    }

    public void setBssidAppearencePercentage(int i) {
        this.bssidAppearencePercentage = i;
    }

    public int getBssidAppearencePercentage() {
        return this.bssidAppearencePercentage;
    }

    public void setBssid(String str) {
        this.bssid = str;
    }

    public String getBssid() {
        return this.bssid;
    }

    public void setSsid(ArrayList<String> arrayList) {
        this.ssid = arrayList;
    }

    public ArrayList<String> getSsid() {
        return this.ssid;
    }

    public void setRssiProperties(RssiProperties rssiProperties) {
        this.rssiProperties = rssiProperties;
    }

    public RssiProperties getRssiProperties() {
        return this.rssiProperties;
    }
}
