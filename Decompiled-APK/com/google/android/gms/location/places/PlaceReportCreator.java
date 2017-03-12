package com.google.android.gms.location.places;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class PlaceReportCreator implements Creator<PlaceReport> {
    public static final int CONTENT_DESCRIPTION = 0;

    static void zza(PlaceReport placeReport, Parcel parcel, int i) {
        int zzK = zzb.zzK(parcel);
        zzb.zzc(parcel, 1, placeReport.zzFG);
        zzb.zza(parcel, 2, placeReport.getPlaceId(), false);
        zzb.zza(parcel, 3, placeReport.getTag(), false);
        zzb.zza(parcel, 4, placeReport.getSource(), false);
        zzb.zzH(parcel, zzK);
    }

    public PlaceReport createFromParcel(Parcel parcel) {
        String str = null;
        int zzJ = zza.zzJ(parcel);
        int i = 0;
        String str2 = null;
        String str3 = null;
        while (parcel.dataPosition() < zzJ) {
            int zzI = zza.zzI(parcel);
            switch (zza.zzaP(zzI)) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    i = zza.zzg(parcel, zzI);
                    break;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    str3 = zza.zzo(parcel, zzI);
                    break;
                case F2m.PPB /*3*/:
                    str2 = zza.zzo(parcel, zzI);
                    break;
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                    str = zza.zzo(parcel, zzI);
                    break;
                default:
                    zza.zzb(parcel, zzI);
                    break;
            }
        }
        if (parcel.dataPosition() == zzJ) {
            return new PlaceReport(i, str3, str2, str);
        }
        throw new zza.zza("Overread allowed size end=" + zzJ, parcel);
    }

    public PlaceReport[] newArray(int i) {
        return new PlaceReport[i];
    }
}
