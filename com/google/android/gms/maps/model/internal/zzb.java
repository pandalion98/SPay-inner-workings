package com.google.android.gms.maps.model.internal;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzb implements Creator<zza> {
    static void zza(zza com_google_android_gms_maps_model_internal_zza, Parcel parcel, int i) {
        int zzK = com.google.android.gms.common.internal.safeparcel.zzb.zzK(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, com_google_android_gms_maps_model_internal_zza.getVersionCode());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, com_google_android_gms_maps_model_internal_zza.getType());
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, com_google_android_gms_maps_model_internal_zza.zzqL(), false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 4, com_google_android_gms_maps_model_internal_zza.getBitmap(), i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzeh(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzgk(i);
    }

    public zza zzeh(Parcel parcel) {
        Bitmap bitmap = null;
        byte b = (byte) 0;
        int zzJ = zza.zzJ(parcel);
        Bundle bundle = null;
        int i = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    b = zza.zze(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    bundle = zza.zzq(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    bitmap = (Bitmap) zza.zza(parcel, zzI, Bitmap.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zza(i, b, bundle, bitmap);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zza[] zzgk(int i) {
        return new zza[i];
    }
}
