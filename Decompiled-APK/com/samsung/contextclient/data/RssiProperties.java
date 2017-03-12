package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RssiProperties implements Parcelable {
    public static final Creator<RssiProperties> CREATOR;
    private double mean;
    private double median;
    private double stdDev;

    /* renamed from: com.samsung.contextclient.data.RssiProperties.1 */
    static class C06121 implements Creator<RssiProperties> {
        C06121() {
        }

        public RssiProperties createFromParcel(Parcel parcel) {
            return new RssiProperties(parcel);
        }

        public RssiProperties[] newArray(int i) {
            return new RssiProperties[i];
        }
    }

    protected RssiProperties(Parcel parcel) {
        this.mean = parcel.readDouble();
        this.median = parcel.readDouble();
        this.stdDev = parcel.readDouble();
    }

    static {
        CREATOR = new C06121();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(this.mean);
        parcel.writeDouble(this.median);
        parcel.writeDouble(this.stdDev);
    }
}
