package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzq implements Creator<StreetViewPanoramaCamera> {
    static void zza(StreetViewPanoramaCamera streetViewPanoramaCamera, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, streetViewPanoramaCamera.getVersionCode());
        zzb.zza(parcel, 2, streetViewPanoramaCamera.zoom);
        zzb.zza(parcel, 3, streetViewPanoramaCamera.tilt);
        zzb.zza(parcel, 4, streetViewPanoramaCamera.bearing);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzea(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzgd(i);
    }

    public StreetViewPanoramaCamera zzea(Parcel parcel) {
        float f = 0.0f;
        int zzJ = zza.zzJ(parcel);
        float f2 = 0.0f;
        int i = 0;
        float f3 = 0.0f;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    f2 = zza.zzl(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    f3 = zza.zzl(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    f = zza.zzl(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new StreetViewPanoramaCamera(i, f2, f3, f);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public StreetViewPanoramaCamera[] zzgd(int i) {
        return new StreetViewPanoramaCamera[i];
    }
}
