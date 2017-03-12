package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.location.LocationStatusCodes;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzd implements Creator<zzc> {
    static void zza(zzc com_google_android_gms_location_places_zzc, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, com_google_android_gms_location_places_zzc.zzpD());
        zzb.zzc(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, com_google_android_gms_location_places_zzc.getVersionCode());
        zzb.zzc(parcel, 2, com_google_android_gms_location_places_zzc.zzpG());
        zzb.zza(parcel, 3, com_google_android_gms_location_places_zzc.zzpH(), i, false);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdA(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzfB(i);
    }

    public zzc zzdA(Parcel parcel) {
        int i = 0;
        int zzJ = zza.zzJ(parcel);
        int i2 = -1;
        PlaceFilter placeFilter = null;
        int i3 = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    i2 = zza.zzg(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    placeFilter = (PlaceFilter) zza.zza(parcel, zzI, PlaceFilter.CREATOR);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i3 = zza.zzg(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new zzc(i3, i, i2, placeFilter);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public zzc[] zzfB(int i) {
        return new zzc[i];
    }
}
