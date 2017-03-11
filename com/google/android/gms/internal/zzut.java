package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class zzut implements SafeParcelable {
    public static final Creator<zzut> CREATOR;
    final int zzFG;

    static {
        CREATOR = new zzuu();
    }

    public zzut() {
        this(1);
    }

    zzut(int i) {
        this.zzFG = i;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzuu.zza(this, parcel, i);
    }
}
