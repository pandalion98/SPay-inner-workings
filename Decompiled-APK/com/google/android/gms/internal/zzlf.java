package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.internal.zzld.zza;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzlf implements Creator<zza> {
    static void zza(zza com_google_android_gms_internal_zzld_zza, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_internal_zzld_zza.getVersionCode());
        zzb.zzc(parcel, 2, com_google_android_gms_internal_zzld_zza.zzjx());
        zzb.zza(parcel, 3, com_google_android_gms_internal_zzld_zza.zzjD());
        zzb.zzc(parcel, 4, com_google_android_gms_internal_zzld_zza.zzjy());
        zzb.zza(parcel, 5, com_google_android_gms_internal_zzld_zza.zzjE());
        zzb.zza(parcel, 6, com_google_android_gms_internal_zzld_zza.zzjF(), false);
        zzb.zzc(parcel, 7, com_google_android_gms_internal_zzld_zza.zzjG());
        zzb.zza(parcel, 8, com_google_android_gms_internal_zzld_zza.zzjI(), false);
        zzb.zza(parcel, 9, com_google_android_gms_internal_zzld_zza.zzjK(), i, false);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzP(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaV(i);
    }

    public zza zzP(Parcel parcel) {
        zzky com_google_android_gms_internal_zzky = null;
        int i = 0;
        int zzJ = com.google.android.gms.common.internal.safeparcel.zza.zzJ(parcel);
        String str = null;
        String str2 = null;
        boolean z = false;
        int i2 = 0;
        boolean z2 = false;
        int i3 = 0;
        int i4 = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = com.google.android.gms.common.internal.safeparcel.zza.zzI(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i4 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    i3 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    z2 = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    i2 = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    z = com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzI);
                    break;
                case ECCurve.COORD_SKEWED /*7*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzI);
                    break;
                case X509KeyUsage.keyAgreement /*8*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzI);
                    break;
                case NamedCurve.sect283k1 /*9*/:
                    com_google_android_gms_internal_zzky = (zzky) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzI, zzky.CREATOR);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zza(i4, i3, z2, i2, z, str2, i, str, com_google_android_gms_internal_zzky);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zza[] zzaV(int i) {
        return new zza[i];
    }
}
