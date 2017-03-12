package com.google.android.gms.location.places;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;

public final class zzc implements SafeParcelable {
    public static final zzd CREATOR;
    private final int zzFG;
    private final int zzalN;
    private final int zzanA;
    private final PlaceFilter zzanB;

    static {
        CREATOR = new zzd();
    }

    zzc(int i, int i2, int i3, PlaceFilter placeFilter) {
        this.zzFG = i;
        this.zzalN = i2;
        this.zzanA = i3;
        this.zzanB = placeFilter;
    }

    public int describeContents() {
        zzd com_google_android_gms_location_places_zzd = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzc)) {
            return false;
        }
        zzc com_google_android_gms_location_places_zzc = (zzc) obj;
        return this.zzalN == com_google_android_gms_location_places_zzc.zzalN && this.zzanA == com_google_android_gms_location_places_zzc.zzanA && this.zzanB.equals(com_google_android_gms_location_places_zzc.zzanB);
    }

    public int getVersionCode() {
        return this.zzFG;
    }

    public int hashCode() {
        return zzw.hashCode(Integer.valueOf(this.zzalN), Integer.valueOf(this.zzanA));
    }

    public String toString() {
        return zzw.zzk(this).zza("transitionTypes", Integer.valueOf(this.zzalN)).zza("loiteringTimeMillis", Integer.valueOf(this.zzanA)).zza("placeFilter", this.zzanB).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzd com_google_android_gms_location_places_zzd = CREATOR;
        zzd.zza(this, parcel, i);
    }

    public int zzpD() {
        return this.zzalN;
    }

    public int zzpG() {
        return this.zzanA;
    }

    public PlaceFilter zzpH() {
        return this.zzanB;
    }
}
