package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzj {
    static void zza(LatLng latLng, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, latLng.getVersionCode());
        zzb.zza(parcel, 2, latLng.latitude);
        zzb.zza(parcel, 3, latLng.longitude);
        zzb.zzH(parcel, zzK);
    }
}
