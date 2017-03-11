package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.internal.zzld.zzb;

public class zzky implements SafeParcelable {
    public static final zzkz CREATOR;
    private final int zzFG;
    private final zzla zzQJ;

    static {
        CREATOR = new zzkz();
    }

    zzky(int i, zzla com_google_android_gms_internal_zzla) {
        this.zzFG = i;
        this.zzQJ = com_google_android_gms_internal_zzla;
    }

    private zzky(zzla com_google_android_gms_internal_zzla) {
        this.zzFG = 1;
        this.zzQJ = com_google_android_gms_internal_zzla;
    }

    public static zzky zza(zzb<?, ?> com_google_android_gms_internal_zzld_zzb___) {
        if (com_google_android_gms_internal_zzld_zzb___ instanceof zzla) {
            return new zzky((zzla) com_google_android_gms_internal_zzld_zzb___);
        }
        throw new IllegalArgumentException("Unsupported safe parcelable field converter class.");
    }

    public int describeContents() {
        zzkz com_google_android_gms_internal_zzkz = CREATOR;
        return 0;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzkz com_google_android_gms_internal_zzkz = CREATOR;
        zzkz.zza(this, parcel, i);
    }

    zzla zzju() {
        return this.zzQJ;
    }

    public zzb<?, ?> zzjv() {
        if (this.zzQJ != null) {
            return this.zzQJ;
        }
        throw new IllegalStateException("There was no converter wrapped in this ConverterWrapper.");
    }
}
