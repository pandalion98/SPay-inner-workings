package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.Collection;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzb implements Creator<AutocompleteFilter> {
    static void zza(AutocompleteFilter autocompleteFilter, Parcel parcel, int i) {
        int zzK = com.google.android.gms.common.internal.safeparcel.zzb.zzK(parcel);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 1, autocompleteFilter.zzpF());
        com.google.android.gms.common.internal.safeparcel.zzb.zzc(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, autocompleteFilter.zzFG);
        com.google.android.gms.common.internal.safeparcel.zzb.zza(parcel, 2, autocompleteFilter.zzany, false);
        com.google.android.gms.common.internal.safeparcel.zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdz(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzfA(i);
    }

    public AutocompleteFilter zzdz(Parcel parcel) {
        boolean z = false;
        int zzJ = zza.zzJ(parcel);
        Collection collection = null;
        int i = 0;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    z = zza.zzc(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    collection = zza.zzB(parcel, zzI);
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
            return new AutocompleteFilter(i, z, collection);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public AutocompleteFilter[] zzfA(int i) {
        return new AutocompleteFilter[i];
    }
}
