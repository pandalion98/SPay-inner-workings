package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzll implements Creator<zzlk> {
    static void zza(zzlk com_google_android_gms_internal_zzlk, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_internal_zzlk.getVersionCode());
        zzb.zza(parcel, 2, com_google_android_gms_internal_zzlk.zzjR(), false);
        zzb.zza(parcel, 3, com_google_android_gms_internal_zzlk.zzjS(), i, false);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzT(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaZ(i);
    }

    public zzlk zzT(Parcel parcel) {
        zzlh com_google_android_gms_internal_zzlh = null;
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        Parcel parcel2 = null;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    parcel2 = zza.zzD(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    com_google_android_gms_internal_zzlh = (zzlh) zza.zza(parcel, zzI, zzlh.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzlk(i, parcel2, com_google_android_gms_internal_zzlh);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzlk[] zzaZ(int i) {
        return new zzlk[i];
    }
}
