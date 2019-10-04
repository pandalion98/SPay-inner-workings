/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.samsung.contextclient.data.Period;
import java.util.ArrayList;
import java.util.List;

class OpenHour
implements Parcelable {
    public static final Parcelable.Creator<OpenHour> CREATOR = new Parcelable.Creator<OpenHour>(){

        public OpenHour createFromParcel(Parcel parcel) {
            return new OpenHour(parcel);
        }

        public OpenHour[] newArray(int n2) {
            return new OpenHour[n2];
        }
    };
    private ArrayList<Period> periods;

    protected OpenHour(Parcel parcel) {
        this.periods = parcel.createTypedArrayList(Period.CREATOR);
    }

    public int describeContents() {
        return 0;
    }

    public ArrayList<Period> getPeriods() {
        return this.periods;
    }

    public void setPeriods(ArrayList<Period> arrayList) {
        this.periods = arrayList;
    }

    public void writeToParcel(Parcel parcel, int n2) {
        parcel.writeTypedList(this.periods);
    }

}

