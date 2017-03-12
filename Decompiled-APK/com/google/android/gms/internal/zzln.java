package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzln implements Creator<zzlm> {
    static void zza(zzlm com_google_android_gms_internal_zzlm, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_internal_zzlm.zzFG);
        zzb.zza(parcel, 2, com_google_android_gms_internal_zzlm.getTimeMillis());
        zzb.zza(parcel, 3, com_google_android_gms_internal_zzlm.zzjT(), false);
        zzb.zza(parcel, 4, com_google_android_gms_internal_zzlm.zzjU(), false);
        zzb.zza(parcel, 5, com_google_android_gms_internal_zzlm.zzjV(), false);
        zzb.zza(parcel, 6, com_google_android_gms_internal_zzlm.zzjW(), false);
        zzb.zza(parcel, 7, com_google_android_gms_internal_zzlm.zzjX(), false);
        zzb.zza(parcel, 8, com_google_android_gms_internal_zzlm.zzjY(), false);
        zzb.zza(parcel, 9, com_google_android_gms_internal_zzlm.zzjZ());
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzU(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzba(i);
    }

    public zzlm zzU(Parcel parcel) {
        long j = 0;
        String str = null;
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        String str6 = null;
        long j2 = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    j2 = zza.zzi(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    str6 = zza.zzo(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    str5 = zza.zzo(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    str4 = zza.zzo(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    str3 = zza.zzo(parcel, zzI);
                    break;
                case ECCurve.COORD_SKEWED /*7*/:
                    str2 = zza.zzo(parcel, zzI);
                    break;
                case X509KeyUsage.keyAgreement /*8*/:
                    str = zza.zzo(parcel, zzI);
                    break;
                case NamedCurve.sect283k1 /*9*/:
                    j = zza.zzi(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzlm(i, j2, str6, str5, str4, str3, str2, str, j);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzlm[] zzba(int i) {
        return new zzlm[i];
    }
}
