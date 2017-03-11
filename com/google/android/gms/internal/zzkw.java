package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class zzkw implements SafeParcelable {
    public static final zzkx CREATOR;
    final int zzFG;
    public final String zzQH;
    public final int zzQI;

    static {
        CREATOR = new zzkx();
    }

    public zzkw(int i, String str, int i2) {
        this.zzFG = i;
        this.zzQH = str;
        this.zzQI = i2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzkx.zza(this, parcel, i);
    }
}
