package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import com.google.android.gms.location.LocationRequest;
import java.util.Collections;
import java.util.List;

public class zzpg implements SafeParcelable {
    public static final zzph CREATOR;
    static final List<zzox> zzang;
    final String mTag;
    private final int zzFG;
    LocationRequest zzabx;
    boolean zzanh;
    boolean zzani;
    boolean zzanj;
    List<zzox> zzank;

    static {
        zzang = Collections.emptyList();
        CREATOR = new zzph();
    }

    zzpg(int i, LocationRequest locationRequest, boolean z, boolean z2, boolean z3, List<zzox> list, String str) {
        this.zzFG = i;
        this.zzabx = locationRequest;
        this.zzanh = z;
        this.zzani = z2;
        this.zzanj = z3;
        this.zzank = list;
        this.mTag = str;
    }

    private zzpg(String str, LocationRequest locationRequest) {
        this(1, locationRequest, false, true, true, zzang, str);
    }

    public static zzpg zza(String str, LocationRequest locationRequest) {
        return new zzpg(str, locationRequest);
    }

    public static zzpg zzb(LocationRequest locationRequest) {
        return zza(null, locationRequest);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof zzpg)) {
            return false;
        }
        zzpg com_google_android_gms_internal_zzpg = (zzpg) obj;
        return zzw.equal(this.zzabx, com_google_android_gms_internal_zzpg.zzabx) && this.zzanh == com_google_android_gms_internal_zzpg.zzanh && this.zzani == com_google_android_gms_internal_zzpg.zzani && this.zzanj == com_google_android_gms_internal_zzpg.zzanj && zzw.equal(this.zzank, com_google_android_gms_internal_zzpg.zzank);
    }

    int getVersionCode() {
        return this.zzFG;
    }

    public int hashCode() {
        return this.zzabx.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.zzabx.toString());
        stringBuilder.append(" requestNlpDebugInfo=");
        stringBuilder.append(this.zzanh);
        stringBuilder.append(" restorePendingIntentListeners=");
        stringBuilder.append(this.zzani);
        stringBuilder.append(" triggerUpdate=");
        stringBuilder.append(this.zzanj);
        stringBuilder.append(" clients=");
        stringBuilder.append(this.zzank);
        if (this.mTag != null) {
            stringBuilder.append(" tag=");
            stringBuilder.append(this.mTag);
        }
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzph.zza(this, parcel, i);
    }
}
