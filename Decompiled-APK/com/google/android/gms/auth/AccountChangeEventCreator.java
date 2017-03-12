package com.google.android.gms.auth;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class AccountChangeEventCreator implements Creator<AccountChangeEvent> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void zza(AccountChangeEvent accountChangeEvent, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, accountChangeEvent.zzHe);
        zzb.zza(parcel, 2, accountChangeEvent.zzHf);
        zzb.zza(parcel, 3, accountChangeEvent.zzHg, false);
        zzb.zzc(parcel, 4, accountChangeEvent.zzHh);
        zzb.zzc(parcel, 5, accountChangeEvent.zzHi);
        zzb.zza(parcel, 6, accountChangeEvent.zzHj, false);
        zzb.zzH(parcel, zzK);
    }

    public AccountChangeEvent createFromParcel(Parcel parcel) {
        String str = null;
        int i = 0;
        int zzJ = zza.zzJ(parcel);
        long j = 0;
        int i2 = 0;
        String str2 = null;
        int i3 = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i3 = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    j = zza.zzi(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    str2 = zza.zzo(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    i2 = zza.zzg(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    str = zza.zzo(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new AccountChangeEvent(i3, j, str2, i2, i, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public AccountChangeEvent[] newArray(int i) {
        return new AccountChangeEvent[i];
    }
}
