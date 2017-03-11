package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzuu implements Creator<zzut> {
    static void zza(zzut com_google_android_gms_internal_zzut, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_internal_zzut.zzFG);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzeM(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzgZ(i);
    }

    public zzut zzeM(Parcel parcel) {
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzut(i);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzut[] zzgZ(int i) {
        return new zzut[i];
    }
}
