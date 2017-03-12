package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.List;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzj implements Creator<LocationSettingsRequest> {
    static void zza(LocationSettingsRequest locationSettingsRequest, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, locationSettingsRequest.zzme(), false);
        zzb.zzc(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, locationSettingsRequest.getVersionCode());
        zzb.zza(parcel, 2, locationSettingsRequest.zzps());
        zzb.zza(parcel, 3, locationSettingsRequest.zzpt());
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdr(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzfn(i);
    }

    public LocationSettingsRequest zzdr(Parcel parcel) {
        boolean z = false;
        int zzJ = zza.zzJ(parcel);
        List list = null;
        boolean z2 = false;
        int i = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    list = zza.zzc(parcel, zzI, LocationRequest.CREATOR);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    z2 = zza.zzc(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    z = zza.zzc(parcel, zzI);
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
            return new LocationSettingsRequest(i, list, z2, z);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public LocationSettingsRequest[] zzfn(int i) {
        return new LocationSettingsRequest[i];
    }
}
