package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzkz implements Creator<zzky> {
    static void zza(zzky com_google_android_gms_internal_zzky, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_internal_zzky.getVersionCode());
        zzb.zza(parcel, 2, com_google_android_gms_internal_zzky.zzju(), i, false);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzM(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaS(i);
    }

    public zzky zzM(Parcel parcel) {
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        zzla com_google_android_gms_internal_zzla = null;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    com_google_android_gms_internal_zzla = (zzla) zza.zza(parcel, zzI, zzla.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzky(i, com_google_android_gms_internal_zzla);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzky[] zzaS(int i) {
        return new zzky[i];
    }
}
