package com.google.android.gms.maps.model.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class zze implements SafeParcelable {
    public static final zzf CREATOR;
    private final int zzFG;
    private zza zzarQ;

    static {
        CREATOR = new zzf();
    }

    public zze() {
        this.zzFG = 1;
    }

    zze(int i, zza com_google_android_gms_maps_model_internal_zza) {
        this.zzFG = i;
        this.zzarQ = com_google_android_gms_maps_model_internal_zza;
    }

    public int describeContents() {
        return 0;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzf.zza(this, parcel, i);
    }

    public zza zzqM() {
        return this.zzarQ;
    }
}
