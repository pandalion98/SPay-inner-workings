package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzr implements Creator<StreetViewPanoramaLink> {
    static void zza(StreetViewPanoramaLink streetViewPanoramaLink, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, streetViewPanoramaLink.getVersionCode());
        zzb.zza(parcel, 2, streetViewPanoramaLink.panoId, false);
        zzb.zza(parcel, 3, streetViewPanoramaLink.bearing);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzeb(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzge(i);
    }

    public StreetViewPanoramaLink zzeb(Parcel parcel) {
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        String str = null;
        float f = 0.0f;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    str = zza.zzo(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    f = zza.zzl(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new StreetViewPanoramaLink(i, str, f);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public StreetViewPanoramaLink[] zzge(int i) {
        return new StreetViewPanoramaLink[i];
    }
}
