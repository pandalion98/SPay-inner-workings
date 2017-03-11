package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class Period implements Parcelable {
    public static final Creator<Period> CREATOR;
    private int day;
    private Hours hours;

    /* renamed from: com.samsung.contextclient.data.Period.1 */
    static class C06061 implements Creator<Period> {
        C06061() {
        }

        public Period createFromParcel(Parcel parcel) {
            return new Period(parcel);
        }

        public Period[] newArray(int i) {
            return new Period[i];
        }
    }

    protected Period(Parcel parcel) {
        this.day = parcel.readInt();
        this.hours = (Hours) parcel.readParcelable(Hours.class.getClassLoader());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.day);
        parcel.writeParcelable(this.hours, i);
    }

    public int describeContents() {
        return 0;
    }

    static {
        CREATOR = new C06061();
    }

    public void setDay(int i) {
        this.day = i;
    }

    public int getDay() {
        return this.day;
    }

    public void setHours(Hours hours) {
        this.hours = hours;
    }

    public Hours getHours() {
        return this.hours;
    }
}
