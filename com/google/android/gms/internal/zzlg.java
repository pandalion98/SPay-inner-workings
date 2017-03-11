package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.internal.zzld.zza;
import com.google.android.gms.internal.zzlh.zzb;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzlg implements Creator<zzb> {
    static void zza(zzb com_google_android_gms_internal_zzlh_zzb, Parcel parcel, int i) {
        int zzK = com.google.android.gms.common.internal.safeparcel.zzb.zzK(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, 1, com_google_android_gms_internal_zzlh_zzb.versionCode);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, com_google_android_gms_internal_zzlh_zzb.zzfv, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 3, com_google_android_gms_internal_zzlh_zzb.zzRd, i, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzQ(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaW(i);
    }

    public zzb zzQ(Parcel parcel) {
        zza com_google_android_gms_internal_zzld_zza = null;
        int zzJ = com.google.android.gms.common.internal.safeparcel.zza.zzJ(parcel);
        int i = 0;
        String str = null;
        while (parcel.dataPosition() < zzJ) {
            int zzI = com.google.android.gms.common.internal.safeparcel.zza.zzI(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    com_google_android_gms_internal_zzld_zza = (zza) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzI, zza.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzb(i, str, com_google_android_gms_internal_zzld_zza);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzb[] zzaW(int i) {
        return new zzb[i];
    }
}
