package com.google.android.gms.maps;

import android.os.Parcel;

public class zzb {
    static void zza(GoogleMapOptions googleMapOptions, Parcel parcel, int i) {
        int zzK = com.google.android.gms.common.internal.safeparcel.zzb.zzK(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, googleMapOptions.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, googleMapOptions.zzqh());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, googleMapOptions.zzqi());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 4, googleMapOptions.getMapType());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, googleMapOptions.getCamera(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 6, googleMapOptions.zzqj());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 7, googleMapOptions.zzqk());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 8, googleMapOptions.zzql());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 9, googleMapOptions.zzqm());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 10, googleMapOptions.zzqn());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 11, googleMapOptions.zzqo());
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzK);
    }
}
