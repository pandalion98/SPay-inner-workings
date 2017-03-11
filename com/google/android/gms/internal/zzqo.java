package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;

public class zzqo implements SafeParcelable {
    public static final zzqp CREATOR;
    public static final zzqo zzaph;
    public static final zzqo zzapi;
    final int zzFG;
    private final String zzapj;

    static {
        CREATOR = new zzqp();
        zzaph = new zzqo(0, "Home");
        zzapi = new zzqo(0, "Work");
    }

    zzqo(int i, String str) {
        this.zzFG = i;
        this.zzapj = str;
    }

    public int describeContents() {
        zzqp com_google_android_gms_internal_zzqp = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzqo)) {
            return false;
        }
        return zzw.equal(this.zzapj, ((zzqo) obj).zzapj);
    }

    public int hashCode() {
        return zzw.hashCode(this.zzapj);
    }

    public String toString() {
        return zzw.zzk(this).zza("alias", this.zzapj).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzqp com_google_android_gms_internal_zzqp = CREATOR;
        zzqp.zza(this, parcel, i);
    }

    public String zzpY() {
        return this.zzapj;
    }
}
