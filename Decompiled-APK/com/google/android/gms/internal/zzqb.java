package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.location.LocationStatusCodes;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzqb implements Creator<zzqa> {
    static void zza(zzqa com_google_android_gms_internal_zzqa, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zza(parcel, 1, com_google_android_gms_internal_zzqa.zzaoO, i, false);
        zzb.zzc(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, com_google_android_gms_internal_zzqa.zzFG);
        zzb.zza(parcel, 2, com_google_android_gms_internal_zzqa.zzaoP);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdG(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzfJ(i);
    }

    public zzqa zzdG(Parcel parcel) {
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        zzpy com_google_android_gms_internal_zzpy = null;
        float f = 0.0f;
        while (parcel.dataPosition() < zzJ) {
            int i2;
            float f2;
            zzpy com_google_android_gms_internal_zzpy2;
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i2 = i;
                    zzpy com_google_android_gms_internal_zzpy3 = (zzpy) zza.zza(parcel, zzI, zzpy.CREATOR);
                    f2 = f;
                    com_google_android_gms_internal_zzpy2 = com_google_android_gms_internal_zzpy3;
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    f2 = zza.zzl(parcel, zzI);
                    com_google_android_gms_internal_zzpy2 = com_google_android_gms_internal_zzpy;
                    i2 = i;
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    float f3 = f;
                    com_google_android_gms_internal_zzpy2 = com_google_android_gms_internal_zzpy;
                    i2 = zza.zzg(parcel, zzI);
                    f2 = f3;
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    f2 = f;
                    com_google_android_gms_internal_zzpy2 = com_google_android_gms_internal_zzpy;
                    i2 = i;
                    break;
            }
            i = i2;
            com_google_android_gms_internal_zzpy = com_google_android_gms_internal_zzpy2;
            f = f2;
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzqa(i, com_google_android_gms_internal_zzpy, f);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzqa[] zzfJ(int i) {
        return new zzqa[i];
    }
}
