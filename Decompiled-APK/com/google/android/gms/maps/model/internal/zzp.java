package com.google.android.gms.maps.model.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class zzp implements SafeParcelable {
    public static final zzq CREATOR;
    private final int zzFG;
    private zza zzarR;

    static {
        CREATOR = new zzq();
    }

    public zzp() {
        this.zzFG = 1;
    }

    zzp(int i, zza com_google_android_gms_maps_model_internal_zza) {
        this.zzFG = i;
        this.zzarR = com_google_android_gms_maps_model_internal_zza;
    }

    public int describeContents() {
        return 0;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzq.zza(this, parcel, i);
    }

    public zza zzqO() {
        return this.zzarR;
    }
}
