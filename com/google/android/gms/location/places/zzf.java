package com.google.android.gms.location.places;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.concurrent.TimeUnit;

public final class zzf implements SafeParcelable {
    public static final zzg CREATOR;
    static final long zzanO;
    private final int mPriority;
    final int zzFG;
    private final long zzalO;
    private final long zzamf;
    private final PlaceFilter zzanP;

    static {
        CREATOR = new zzg();
        zzanO = TimeUnit.HOURS.toMillis(1);
    }

    public zzf(int i, PlaceFilter placeFilter, long j, int i2, long j2) {
        this.zzFG = i;
        this.zzanP = placeFilter;
        this.zzamf = j;
        this.mPriority = i2;
        this.zzalO = j2;
    }

    public int describeContents() {
        zzg com_google_android_gms_location_places_zzg = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzf)) {
            return false;
        }
        zzf com_google_android_gms_location_places_zzf = (zzf) obj;
        return zzw.equal(this.zzanP, com_google_android_gms_location_places_zzf.zzanP) && this.zzamf == com_google_android_gms_location_places_zzf.zzamf && this.mPriority == com_google_android_gms_location_places_zzf.mPriority && this.zzalO == com_google_android_gms_location_places_zzf.zzalO;
    }

    public long getExpirationTime() {
        return this.zzalO;
    }

    public long getInterval() {
        return this.zzamf;
    }

    public int getPriority() {
        return this.mPriority;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzanP, Long.valueOf(this.zzamf), Integer.valueOf(this.mPriority), Long.valueOf(this.zzalO));
    }

    public String toString() {
        return zzw.zzk(this).zza("filter", this.zzanP).zza("interval", Long.valueOf(this.zzamf)).zza("priority", Integer.valueOf(this.mPriority)).zza("expireAt", Long.valueOf(this.zzalO)).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzg com_google_android_gms_location_places_zzg = CREATOR;
        zzg.zza(this, parcel, i);
    }

    public PlaceFilter zzpH() {
        return this.zzanP;
    }
}
