package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzux implements Creator<zzuw> {
    static void zza(zzuw com_google_android_gms_internal_zzuw, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_internal_zzuw.zzFG);
        zzb.zza(parcel, 2, com_google_android_gms_internal_zzuw.zzawX);
        zzb.zzc(parcel, 3, com_google_android_gms_internal_zzuw.zzawY, false);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzeN(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzha(i);
    }

    public zzuw zzeN(Parcel parcel) {
        boolean z = false;
        int zzJ = zza.zzJ(parcel);
        List list = null;
        int i = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    z = zza.zzc(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    list = zza.zzc(parcel, zzI, Scope.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzuw(i, z, list);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzuw[] zzha(int i) {
        return new zzuw[i];
    }
}
