package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzz implements Creator<zzy> {
    static void zza(zzy com_google_android_gms_common_internal_zzy, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_common_internal_zzy.zzFG);
        zzb.zza(parcel, 2, com_google_android_gms_common_internal_zzy.getAccount(), i, false);
        zzb.zzc(parcel, 3, com_google_android_gms_common_internal_zzy.getSessionId());
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzF(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaM(i);
    }

    public zzy zzF(Parcel parcel) {
        int i = 0;
        int zzJ = zza.zzJ(parcel);
        Account account = null;
        int i2 = 0;
        while (parcel.dataPosition() < zzJ) {
            Account account2;
            int zzg;
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    int i3 = i;
                    account2 = account;
                    zzg = zza.zzg(parcel, zzI);
                    zzI = i3;
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    zzg = i2;
                    Account account3 = (Account) zza.zza(parcel, zzI, Account.CREATOR);
                    zzI = i;
                    account2 = account3;
                    break;
                case F2m.PPB /*3*/:
                    zzI = zza.zzg(parcel, zzI);
                    account2 = account;
                    zzg = i2;
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    zzI = i;
                    account2 = account;
                    zzg = i2;
                    break;
            }
            i2 = zzg;
            account = account2;
            i = zzI;
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzy(i2, account, i);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzy[] zzaM(int i) {
        return new zzy[i];
    }
}
