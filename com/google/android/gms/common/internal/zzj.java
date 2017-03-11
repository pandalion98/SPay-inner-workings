package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzj implements Creator<zzi> {
    static void zza(zzi com_google_android_gms_common_internal_zzi, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_common_internal_zzi.version);
        zzb.zzc(parcel, 2, com_google_android_gms_common_internal_zzi.zzPz);
        zzb.zzc(parcel, 3, com_google_android_gms_common_internal_zzi.zzPA);
        zzb.zza(parcel, 4, com_google_android_gms_common_internal_zzi.zzPB, false);
        zzb.zza(parcel, 5, com_google_android_gms_common_internal_zzi.zzPC, false);
        zzb.zza(parcel, 6, com_google_android_gms_common_internal_zzi.zzPD, i, false);
        zzb.zza(parcel, 7, com_google_android_gms_common_internal_zzi.zzPE, false);
        zzb.zza(parcel, 8, com_google_android_gms_common_internal_zzi.zzPF, i, false);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzE(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaH(i);
    }

    public zzi zzE(Parcel parcel) {
        int i = 0;
        Account account = null;
        int zzJ = zza.zzJ(parcel);
        Bundle bundle = null;
        Scope[] scopeArr = null;
        IBinder iBinder = null;
        String str = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i3 = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    i2 = zza.zzg(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    str = zza.zzo(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    iBinder = zza.zzp(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    scopeArr = (Scope[]) zza.zzb(parcel, zzI, Scope.CREATOR);
                    break;
                case ECCurve.COORD_SKEWED /*7*/:
                    bundle = zza.zzq(parcel, zzI);
                    break;
                case X509KeyUsage.keyAgreement /*8*/:
                    account = (Account) zza.zza(parcel, zzI, Account.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzi(i3, i2, i, str, iBinder, scopeArr, bundle, account);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzi[] zzaH(int i) {
        return new zzi[i];
    }
}
