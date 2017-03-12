package com.google.android.gms.location;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class LocationSettingsResultCreator implements Creator<LocationSettingsResult> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void zza(LocationSettingsResult locationSettingsResult, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zza(parcel, 1, locationSettingsResult.getStatus(), i, false);
        zzb.zzc(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, locationSettingsResult.getVersionCode());
        zzb.zza(parcel, 2, locationSettingsResult.getLocationSettingsStates(), i, false);
        zzb.zzH(parcel, zzK);
    }

    public LocationSettingsResult createFromParcel(Parcel parcel) {
        LocationSettingsStates locationSettingsStates = null;
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        Status status = null;
        while (parcel.dataPosition() < zzJ) {
            int i2;
            LocationSettingsStates locationSettingsStates2;
            Status status2;
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i2 = i;
                    Status status3 = (Status) zza.zza(parcel, zzI, Status.CREATOR);
                    locationSettingsStates2 = locationSettingsStates;
                    status2 = status3;
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    locationSettingsStates2 = (LocationSettingsStates) zza.zza(parcel, zzI, LocationSettingsStates.CREATOR);
                    status2 = status;
                    i2 = i;
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    LocationSettingsStates locationSettingsStates3 = locationSettingsStates;
                    status2 = status;
                    i2 = zza.zzg(parcel, zzI);
                    locationSettingsStates2 = locationSettingsStates3;
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    locationSettingsStates2 = locationSettingsStates;
                    status2 = status;
                    i2 = i;
                    break;
            }
            i = i2;
            status = status2;
            locationSettingsStates = locationSettingsStates2;
        }
        if (parcel.dataPosition() == zzJ) {
            return new LocationSettingsResult(i, status, locationSettingsStates);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public LocationSettingsResult[] newArray(int i) {
        return new LocationSettingsResult[i];
    }
}
