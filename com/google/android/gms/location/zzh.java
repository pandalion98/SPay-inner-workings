package com.google.android.gms.location;

import android.location.Location;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class zzh implements SafeParcelable {
    public static final zzi CREATOR;
    static final List<Location> zzamk;
    private final int zzFG;
    List<Location> zzaml;

    static {
        zzamk = Collections.emptyList();
        CREATOR = new zzi();
    }

    zzh(int i, List<Location> list) {
        this.zzFG = i;
        this.zzaml = list;
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof zzh)) {
            return false;
        }
        zzh com_google_android_gms_location_zzh = (zzh) obj;
        if (com_google_android_gms_location_zzh.zzaml.size() != this.zzaml.size()) {
            return false;
        }
        Iterator it = this.zzaml.iterator();
        for (Location time : com_google_android_gms_location_zzh.zzaml) {
            if (((Location) it.next()).getTime() != time.getTime()) {
                return false;
            }
        }
        return true;
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public int hashCode() {
        int i = 17;
        for (Location time : this.zzaml) {
            long time2 = time.getTime();
            i = ((int) (time2 ^ (time2 >>> 32))) + (i * 31);
        }
        return i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LocationResult[success: ").append(zzpr());
        stringBuilder.append(", locations: ").append(this.zzaml);
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzi.zza(this, parcel, i);
    }

    public boolean zzpr() {
        return !this.zzaml.isEmpty();
    }
}
