package com.google.android.gms.location.places;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zzb;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zza implements Creator<AddPlaceRequest> {
    static void zza(AddPlaceRequest addPlaceRequest, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zza(parcel, 1, addPlaceRequest.getName(), false);
        zzb.zzc(parcel, LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, addPlaceRequest.zzFG);
        zzb.zza(parcel, 2, addPlaceRequest.getLatLng(), i, false);
        zzb.zza(parcel, 3, addPlaceRequest.getAddress(), false);
        zzb.zza(parcel, 4, addPlaceRequest.getPlaceTypes(), false);
        zzb.zza(parcel, 5, addPlaceRequest.getPhoneNumber(), false);
        zzb.zza(parcel, 6, addPlaceRequest.getWebsiteUri(), i, false);
        zzb.zzH(parcel, zzK);
    }

    public /* synthetic */ Object createFromParcel(Parcel parcel) {
        return zzdy(parcel);
    }

    public /* synthetic */ Object[] newArray(int i) {
        return zzfz(i);
    }

    public AddPlaceRequest zzdy(Parcel parcel) {
        Uri uri = null;
        int zzJ = com.google.android.gms.common.internal.safeparcel.zza.zzJ(parcel);
        int i = 0;
        String str = null;
        List list = null;
        String str2 = null;
        LatLng latLng = null;
        String str3 = null;
        while (parcel.dataPosition() < zzJ) {
            int zzI = com.google.android.gms.common.internal.safeparcel.zza.zzI(parcel);
            switch (com.google.android.gms.common.internal.safeparcel.zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    str3 = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    latLng = (LatLng) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzI, LatLng.CREATOR);
                    break;
                case F2m.PPB /*3*/:
                    str2 = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    list = com.google.android.gms.common.internal.safeparcel.zza.zzB(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                    str = com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, zzI);
                    break;
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    uri = (Uri) com.google.android.gms.common.internal.safeparcel.zza.zza(parcel, zzI, Uri.CREATOR);
                    break;
                case LocationStatusCodes.GEOFENCE_NOT_AVAILABLE /*1000*/:
                    i = com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, zzI);
                    break;
                default:
                    com.google.android.gms.common.internal.safeparcel.zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new AddPlaceRequest(i, str3, latLng, str2, list, str, uri);
        }
        throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public AddPlaceRequest[] zzfz(int i) {
        return new AddPlaceRequest[i];
    }
}
