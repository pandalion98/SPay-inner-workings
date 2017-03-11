package com.google.android.gms.maps.model.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzf implements Creator<zze> {
    static void zza(zze com_google_android_gms_maps_model_internal_zze, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_maps_model_internal_zze.getVersionCode());
        zzb.zza(parcel, 2, com_google_android_gms_maps_model_internal_zze.zzqM(), i, false);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzej(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzgm(i);
    }

    public zze zzej(Parcel parcel) {
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        zza com_google_android_gms_maps_model_internal_zza = null;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    com_google_android_gms_maps_model_internal_zza = (zza) zza.zza(parcel, zzI, zza.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zze(i, com_google_android_gms_maps_model_internal_zza);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zze[] zzgm(int i) {
        return new zze[i];
    }
}
