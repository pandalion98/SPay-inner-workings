package com.goodix.cap.fingerprint;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class GFDevice implements Parcelable {
    public static final Creator<GFDevice> CREATOR = new Creator<GFDevice>() {
        public GFDevice createFromParcel(Parcel in) {
            return new GFDevice(in);
        }

        public GFDevice[] newArray(int size) {
            return new GFDevice[size];
        }
    };
    public int mSafeClass;

    public GFDevice() {
        this.mSafeClass = 2;
    }

    private GFDevice(Parcel in) {
        this.mSafeClass = 2;
        this.mSafeClass = in.readInt();
    }

    public GFDevice(GFDevice config) {
        this.mSafeClass = 2;
        this.mSafeClass = config.mSafeClass;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mSafeClass);
    }
}
