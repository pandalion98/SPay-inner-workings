package com.google.android.gms.maps.model;

import android.os.Parcel;

public class zzb {
    static void zza(CameraPosition cameraPosition, Parcel parcel, int i) {
        int zzK = com.google.android.gms.common.internal.safeparcel.zzb.zzK(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, cameraPosition.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, cameraPosition.target, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, cameraPosition.zoom);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, cameraPosition.tilt);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 5, cameraPosition.bearing);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzK);
    }
}
