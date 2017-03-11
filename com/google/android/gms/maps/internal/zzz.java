package com.google.android.gms.maps.internal;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzz implements Creator<zzy> {
    static void zza(zzy com_google_android_gms_maps_internal_zzy, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_maps_internal_zzy.getVersionCode());
        zzb.zza(parcel, 2, com_google_android_gms_maps_internal_zzy.zzqE(), i, false);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdR(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzfU(i);
    }

    public zzy zzdR(Parcel parcel) {
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        Point point = null;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    point = (Point) zza.zza(parcel, zzI, Point.CREATOR);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzy(i, point);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzy[] zzfU(int i) {
        return new zzy[i];
    }
}
