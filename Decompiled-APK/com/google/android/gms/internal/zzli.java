package com.google.android.gms.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.ArrayList;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzli implements Creator<zzlh> {
    static void zza(zzlh com_google_android_gms_internal_zzlh, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_internal_zzlh.getVersionCode());
        zzb.zzc(parcel, 2, com_google_android_gms_internal_zzlh.zzjO(), false);
        zzb.zza(parcel, 3, com_google_android_gms_internal_zzlh.zzjP(), false);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzR(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzaX(i);
    }

    public zzlh zzR(Parcel parcel) {
        String str = null;
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        ArrayList arrayList = null;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    arrayList = zza.zzc(parcel, zzI, zzlh.zza.CREATOR);
                    break;
                case F2m.PPB /*3*/:
                    str = zza.zzo(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzlh(i, arrayList, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzlh[] zzaX(int i) {
        return new zzlh[i];
    }
}
