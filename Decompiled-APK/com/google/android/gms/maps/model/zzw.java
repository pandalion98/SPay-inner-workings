package com.google.android.gms.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzw implements Creator<TileOverlayOptions> {
    static void zza(TileOverlayOptions tileOverlayOptions, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, tileOverlayOptions.getVersionCode());
        zzb.zza(parcel, 2, tileOverlayOptions.zzqK(), false);
        zzb.zza(parcel, 3, tileOverlayOptions.isVisible());
        zzb.zza(parcel, 4, tileOverlayOptions.getZIndex());
        zzb.zza(parcel, 5, tileOverlayOptions.getFadeIn());
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzef(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzgi(i);
    }

    public TileOverlayOptions zzef(Parcel parcel) {
        boolean z = false;
        int zzJ = zza.zzJ(parcel);
        IBinder iBinder = null;
        float f = 0.0f;
        boolean z2 = true;
        int i = 0;
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
                    z = zza.zzc(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    f = zza.zzl(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    z2 = zza.zzc(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new TileOverlayOptions(i, iBinder, z, f, z2);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public TileOverlayOptions[] zzgi(int i) {
        return new TileOverlayOptions[i];
    }
}
