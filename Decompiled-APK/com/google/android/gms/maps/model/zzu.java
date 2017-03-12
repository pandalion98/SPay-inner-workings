package com.google.android.gms.maps.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzu implements Creator<Tile> {
    static void zza(Tile tile, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, tile.getVersionCode());
        zzb.zzc(parcel, 2, tile.width);
        zzb.zzc(parcel, 3, tile.height);
        zzb.zza(parcel, 4, tile.data, false);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzee(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzgh(i);
    }

    public Tile zzee(Parcel parcel) {
        int i = 0;
        int zzJ = zza.zzJ(parcel);
        byte[] bArr = null;
        int i2 = 0;
        int i3 = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i3 = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    i2 = zza.zzg(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    bArr = zza.zzr(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new Tile(i3, i2, i, bArr);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public Tile[] zzgh(int i) {
        return new Tile[i];
    }
}
