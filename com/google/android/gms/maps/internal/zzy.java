package com.google.android.gms.maps.internal;

import android.graphics.Point;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class zzy implements SafeParcelable {
    public static final zzz CREATOR;
    private final int versionCode;
    private final Point zzaqO;

    static {
        CREATOR = new zzz();
    }

    public zzy(int i, Point point) {
        this.versionCode = i;
        this.zzaqO = point;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzy)) {
            return false;
        }
        return this.zzaqO.equals(((zzy) obj).zzaqO);
    }

    int getVersionCode() {
        return this.versionCode;
    }

    public int hashCode() {
        return this.zzaqO.hashCode();
    }

    public String toString() {
        return this.zzaqO.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzz.zza(this, parcel, i);
    }

    public Point zzqE() {
        return this.zzaqO;
    }
}
