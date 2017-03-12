package com.android.internal.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ParcelableString implements Parcelable {
    public static final Creator<ParcelableString> CREATOR = new Creator<ParcelableString>() {
        public ParcelableString createFromParcel(Parcel in) {
            ParcelableString ret = new ParcelableString();
            ret.string = in.readString();
            return ret;
        }

        public ParcelableString[] newArray(int size) {
            return new ParcelableString[size];
        }
    };
    public String string;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.string);
    }
}
