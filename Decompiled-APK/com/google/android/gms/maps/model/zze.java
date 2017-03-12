package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zze implements Creator<GroundOverlayOptions> {
    static void zza(GroundOverlayOptions groundOverlayOptions, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, groundOverlayOptions.getVersionCode());
        zzb.zza(parcel, 2, groundOverlayOptions.zzqH(), false);
        zzb.zza(parcel, 3, groundOverlayOptions.getLocation(), i, false);
        zzb.zza(parcel, 4, groundOverlayOptions.getWidth());
        zzb.zza(parcel, 5, groundOverlayOptions.getHeight());
        zzb.zza(parcel, 6, groundOverlayOptions.getBounds(), i, false);
        zzb.zza(parcel, 7, groundOverlayOptions.getBearing());
        zzb.zza(parcel, 8, groundOverlayOptions.getZIndex());
        zzb.zza(parcel, 9, groundOverlayOptions.isVisible());
        zzb.zza(parcel, 10, groundOverlayOptions.getTransparency());
        zzb.zza(parcel, 11, groundOverlayOptions.getAnchorU());
        zzb.zza(parcel, 12, groundOverlayOptions.getAnchorV());
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdU(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzfX(i);
    }

    public GroundOverlayOptions zzdU(Parcel parcel) {
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        IBinder iBinder = null;
        LatLng latLng = null;
        float f = 0.0f;
        float f2 = 0.0f;
        LatLngBounds latLngBounds = null;
        float f3 = 0.0f;
        float f4 = 0.0f;
        boolean z = false;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    iBinder = zza.zzp(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    latLng = (LatLng) zza.zza(parcel, zzI, LatLng.CREATOR);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    f = zza.zzl(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    f2 = zza.zzl(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    latLngBounds = (LatLngBounds) zza.zza(parcel, zzI, LatLngBounds.CREATOR);
                    break;
                case ECCurve.COORD_SKEWED /*7*/:
                    f3 = zza.zzl(parcel, zzI);
                    break;
                case X509KeyUsage.keyAgreement /*8*/:
                    f4 = zza.zzl(parcel, zzI);
                    break;
                case NamedCurve.sect283k1 /*9*/:
                    z = zza.zzc(parcel, zzI);
                    break;
                case NamedCurve.sect283r1 /*10*/:
                    f5 = zza.zzl(parcel, zzI);
                    break;
                case CertStatus.UNREVOKED /*11*/:
                    f6 = zza.zzl(parcel, zzI);
                    break;
                case CertStatus.UNDETERMINED /*12*/:
                    f7 = zza.zzl(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new GroundOverlayOptions(i, iBinder, latLng, f, f2, latLngBounds, f3, f4, z, f5, f6, f7);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public GroundOverlayOptions[] zzfX(int i) {
        return new GroundOverlayOptions[i];
    }
}
