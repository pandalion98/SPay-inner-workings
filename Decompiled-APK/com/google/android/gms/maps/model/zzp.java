package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzp {
    static void zza(PolylineOptions polylineOptions, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, polylineOptions.getVersionCode());
        zzb.zzc(parcel, 2, polylineOptions.getPoints(), false);
        zzb.zza(parcel, 3, polylineOptions.getWidth());
        zzb.zzc(parcel, 4, polylineOptions.getColor());
        zzb.zza(parcel, 5, polylineOptions.getZIndex());
        zzb.zza(parcel, 6, polylineOptions.isVisible());
        zzb.zza(parcel, 7, polylineOptions.isGeodesic());
        zzb.zzH(parcel, zzK);
    }
}
