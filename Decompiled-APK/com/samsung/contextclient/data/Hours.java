package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Hours implements Parcelable {
    public static final Creator<Hours> CREATOR;
    private String end;
    private String start;

    /* renamed from: com.samsung.contextclient.data.Hours.1 */
    static class C06021 implements Creator<Hours> {
        C06021() {
        }

        public Hours createFromParcel(Parcel parcel) {
            return new Hours(parcel);
        }

        public Hours[] newArray(int i) {
            return new Hours[i];
        }
    }

    protected Hours(Parcel parcel) {
        this.start = parcel.readString();
        this.end = parcel.readString();
    }

    static {
        CREATOR = new C06021();
    }

    public void setStart(String str) {
        this.start = str;
    }

    public String getStart() {
        return this.start;
    }

    public void setEnd(String str) {
        this.end = str;
    }

    public String getEnd() {
        return this.end;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.start);
        parcel.writeString(this.end);
    }
}
