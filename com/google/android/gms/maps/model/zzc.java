package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzc implements Creator<CircleOptions> {
    static void zza(CircleOptions circleOptions, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, circleOptions.getVersionCode());
        zzb.zza(parcel, 2, circleOptions.getCenter(), i, false);
        zzb.zza(parcel, 3, circleOptions.getRadius());
        zzb.zza(parcel, 4, circleOptions.getStrokeWidth());
        zzb.zzc(parcel, 5, circleOptions.getStrokeColor());
        zzb.zzc(parcel, 6, circleOptions.getFillColor());
        zzb.zza(parcel, 7, circleOptions.getZIndex());
        zzb.zza(parcel, 8, circleOptions.isVisible());
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdT(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzfW(i);
    }

    public CircleOptions zzdT(Parcel parcel) {
        float f = 0.0f;
        boolean z = false;
        int zzJ = zza.zzJ(parcel);
        LatLng latLng = null;
        double d = 0.0d;
        int i = 0;
        int i2 = 0;
        float f2 = 0.0f;
        int i3 = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i3 = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    latLng = (LatLng) zza.zza(parcel, zzI, LatLng.CREATOR);
                    break;
                case F2m.PPB /*3*/:
                    d = zza.zzm(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    f2 = zza.zzl(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    i2 = zza.zzg(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case ECCurve.COORD_SKEWED /*7*/:
                    f = zza.zzl(parcel, zzI);
                    break;
                case X509KeyUsage.keyAgreement /*8*/:
                    z = zza.zzc(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new CircleOptions(i3, latLng, d, f2, i2, i, f, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public CircleOptions[] zzfW(int i) {
        return new CircleOptions[i];
    }
}
