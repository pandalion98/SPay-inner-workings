package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzo implements Creator<PolylineOptions> {
    static void zza(PolylineOptions polylineOptions, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, polylineOptions.getVersionCode());
        zzb.zzc(parcel, 2, polylineOptions.getPoints(), false);
        zzb.zza(parcel, 3, polylineOptions.getWidth());
        zzb.zzc(parcel, 4, polylineOptions.getColor());
        zzb.zza(parcel, 5, polylineOptions.getZIndex());
        zzb.zza(parcel, 6, polylineOptions.isVisible());
        zzb.zza(parcel, 7, polylineOptions.isGeodesic());
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdZ(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzgc(i);
    }

    public PolylineOptions zzdZ(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int zzJ = zza.zzJ(parcel);
        List list = null;
        boolean z2 = false;
        int i = 0;
        float f2 = 0.0f;
        int i2 = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i2 = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    list = zza.zzc(parcel, zzI, LatLng.CREATOR);
                    break;
                case F2m.PPB /*3*/:
                    f2 = zza.zzl(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    f = zza.zzl(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    z2 = zza.zzc(parcel, zzI);
                    break;
                case ECCurve.COORD_SKEWED /*7*/:
                    z = zza.zzc(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new PolylineOptions(i2, list, f2, i, f, z2, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public PolylineOptions[] zzgc(int i) {
        return new PolylineOptions[i];
    }
}
