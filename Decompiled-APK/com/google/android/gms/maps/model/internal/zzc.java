package com.google.android.gms.maps.model.internal;

import android.os.Bundle;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class zzc implements SafeParcelable {
    public static final zzd CREATOR;
    private int type;
    private final int zzFG;
    private Bundle zzarO;

    static {
        CREATOR = new zzd();
    }

    zzc(int i, int i2, Bundle bundle) {
        this.zzFG = i;
        this.type = i2;
        this.zzarO = bundle;
    }

    public int describeContents() {
        return 0;
    }

    public int getType() {
        return this.type;
    }

    public int getVersionCode() {
        return this.zzFG;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzd.zza(this, parcel, i);
    }

    public Bundle zzqL() {
        return this.zzarO;
    }
}
