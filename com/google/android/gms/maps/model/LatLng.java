package com.google.android.gms.maps.model;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.maps.internal.zzaa;

public final class LatLng implements SafeParcelable {
    public static final zzi CREATOR;
    public final double latitude;
    public final double longitude;
    private final int zzFG;

    static {
        CREATOR = new zzi();
    }

    public LatLng(double d, double d2) {
        this(1, d, d2);
    }

    LatLng(int i, double d, double d2) {
        this.zzFG = i;
        if (-180.0d > d2 || d2 >= 180.0d) {
            this.longitude = ((((d2 - 180.0d) % 360.0d) + 360.0d) % 360.0d) - 180.0d;
        } else {
            this.longitude = d2;
        }
        this.latitude = Math.max(-90.0d, Math.min(90.0d, d));
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LatLng)) {
            return false;
        }
        LatLng latLng = (LatLng) obj;
        return Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(latLng.latitude) && Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(latLng.longitude);
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.latitude);
        int i = ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 31;
        long doubleToLongBits2 = Double.doubleToLongBits(this.longitude);
        return (i * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
    }

    public String toString() {
        return "lat/lng: (" + this.latitude + "," + this.longitude + ")";
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (zzaa.zzqF()) {
            zzj.zza(this, parcel, i);
        } else {
            zzi.zza(this, parcel, i);
        }
    }
}
