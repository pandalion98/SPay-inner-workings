package com.samsung.contextclient.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

class OpenHour implements Parcelable {
    public static final Creator<OpenHour> CREATOR;
    private ArrayList<Period> periods;

    /* renamed from: com.samsung.contextclient.data.OpenHour.1 */
    static class C06051 implements Creator<OpenHour> {
        C06051() {
        }

        public OpenHour createFromParcel(Parcel parcel) {
            return new OpenHour(parcel);
        }

        public OpenHour[] newArray(int i) {
            return new OpenHour[i];
        }
    }

    protected OpenHour(Parcel parcel) {
        this.periods = parcel.createTypedArrayList(Period.CREATOR);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(this.periods);
    }

    public int describeContents() {
        return 0;
    }

    static {
        CREATOR = new C06051();
    }

    public void setPeriods(ArrayList<Period> arrayList) {
        this.periods = arrayList;
    }

    public ArrayList<Period> getPeriods() {
        return this.periods;
    }
}
