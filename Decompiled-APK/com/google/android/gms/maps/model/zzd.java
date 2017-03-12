package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzd {
    static void zza(CircleOptions circleOptions, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, circleOptions.getVersionCode());
        zzb.zza(parcel, 2, circleOptions.getCenter(), i, false);
        zzb.zza(parcel, 3, circleOptions.getRadius());
        zzb.zza(parcel, 4, circleOptions.getStrokeWidth());
        zzb.zzc(parcel, 5, circleOptions.getStrokeColor());
        zzb.zzc(parcel, 6, circleOptions.getFillColor());
        zzb.zza(parcel, 7, circleOptions.getZIndex());
        zzb.zza(parcel, 8, circleOptions.isVisible());
        zzb.zzH(parcel, zzK);
    }
}
