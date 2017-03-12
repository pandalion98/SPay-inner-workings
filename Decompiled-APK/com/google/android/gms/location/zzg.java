package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.samsung.android.spayfw.cncc.CNCCCommands;
import org.bouncycastle.crypto.tls.EncryptionAlgorithm;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzg implements Creator<LocationRequest> {
    static void zza(LocationRequest locationRequest, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, locationRequest.mPriority);
        zzb.zzc(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, locationRequest.getVersionCode());
        zzb.zza(parcel, 2, locationRequest.zzamf);
        zzb.zza(parcel, 3, locationRequest.zzamg);
        zzb.zza(parcel, 4, locationRequest.zzabz);
        zzb.zza(parcel, 5, locationRequest.zzalO);
        zzb.zzc(parcel, 6, locationRequest.zzamh);
        zzb.zza(parcel, 7, locationRequest.zzami);
        zzb.zza(parcel, 8, locationRequest.zzamj);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdp(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzfl(i);
    }

    public LocationRequest zzdp(Parcel parcel) {
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        int i2 = EncryptionAlgorithm.AEAD_CHACHA20_POLY1305;
        long j = 3600000;
        long j2 = 600000;
        boolean z = false;
        long j3 = Long.MAX_VALUE;
        int i3 = CNCCCommands.CMD_CNCC_CMD_UNKNOWN;
        float f = 0.0f;
        long j4 = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i2 = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    j = zza.zzi(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    j2 = zza.zzi(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    z = zza.zzc(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    j3 = zza.zzi(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    i3 = zza.zzg(parcel, zzI);
                    break;
                case ECCurve.COORD_SKEWED /*7*/:
                    f = zza.zzl(parcel, zzI);
                    break;
                case X509KeyUsage.keyAgreement /*8*/:
                    j4 = zza.zzi(parcel, zzI);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new LocationRequest(i, i2, j, j2, z, j3, i3, f, j4);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public LocationRequest[] zzfl(int i) {
        return new LocationRequest[i];
    }
}
