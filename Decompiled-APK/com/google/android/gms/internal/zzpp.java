package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.List;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzpp implements Creator<zzpo> {
    static void zza(zzpo com_google_android_gms_internal_zzpo, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zza(parcel, 1, com_google_android_gms_internal_zzpo.zzZO, false);
        zzb.zzc(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, com_google_android_gms_internal_zzpo.zzFG);
        zzb.zza(parcel, 2, com_google_android_gms_internal_zzpo.zzanM, false);
        zzb.zza(parcel, 3, com_google_android_gms_internal_zzpo.zzanu, false);
        zzb.zzc(parcel, 4, com_google_android_gms_internal_zzpo.zzaoi, false);
        zzb.zzc(parcel, 5, com_google_android_gms_internal_zzpo.zzaoj);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdE(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzfG(i);
    }

    public zzpo zzdE(Parcel parcel) {
        int i = 0;
        List list = null;
        int zzJ = zza.zzJ(parcel);
        List list2 = null;
        String str = null;
        String str2 = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    str2 = zza.zzo(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    str = zza.zzo(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    list2 = zza.zzB(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    list = zza.zzc(parcel, zzI, zzpo.zza.CREATOR);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i2 = zza.zzg(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzpo(i2, str2, str, list2, list, i);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzpo[] zzfG(int i) {
        return new zzpo[i];
    }
}
