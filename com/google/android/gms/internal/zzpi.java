package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.location.zzd;
import com.google.android.gms.location.zzd.zza;
import com.google.android.gms.location.zze;

public class zzpi implements SafeParcelable {
    public static final zzpj CREATOR;
    PendingIntent mPendingIntent;
    private final int zzFG;
    int zzanl;
    zzpg zzanm;
    zzd zzann;
    zze zzano;

    static {
        CREATOR = new zzpj();
    }

    zzpi(int i, int i2, zzpg com_google_android_gms_internal_zzpg, IBinder iBinder, PendingIntent pendingIntent, IBinder iBinder2) {
        zze com_google_android_gms_location_zze = null;
        this.zzFG = i;
        this.zzanl = i2;
        this.zzanm = com_google_android_gms_internal_zzpg;
        this.zzann = iBinder == null ? null : zza.zzbg(iBinder);
        this.mPendingIntent = pendingIntent;
        if (iBinder2 != null) {
            com_google_android_gms_location_zze = zze.zza.zzbh(iBinder2);
        }
        this.zzano = com_google_android_gms_location_zze;
    }

    public static zzpi zza(zze com_google_android_gms_location_zze) {
        return new zzpi(1, 2, null, null, null, com_google_android_gms_location_zze.asBinder());
    }

    public static zzpi zzb(zzpg com_google_android_gms_internal_zzpg, PendingIntent pendingIntent) {
        return new zzpi(1, 1, com_google_android_gms_internal_zzpg, null, pendingIntent, null);
    }

    public static zzpi zzb(zzpg com_google_android_gms_internal_zzpg, zzd com_google_android_gms_location_zzd) {
        return new zzpi(1, 1, com_google_android_gms_internal_zzpg, com_google_android_gms_location_zzd.asBinder(), null, null);
    }

    public static zzpi zzb(zzd com_google_android_gms_location_zzd) {
        return new zzpi(1, 2, null, com_google_android_gms_location_zzd.asBinder(), null, null);
    }

    public static zzpi zze(PendingIntent pendingIntent) {
        return new zzpi(1, 2, null, null, pendingIntent, null);
    }

    public int describeContents() {
        return 0;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzpj.zza(this, parcel, i);
    }

    IBinder zzpA() {
        return this.zzano == null ? null : this.zzano.asBinder();
    }

    IBinder zzpz() {
        return this.zzann == null ? null : this.zzann.asBinder();
    }
}
