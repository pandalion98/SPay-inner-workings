package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.List;

@Deprecated
public final class zzqd implements SafeParcelable {
    public static final zzqe CREATOR;
    public final String name;
    public final int versionCode;
    public final String zzaoQ;
    public final String zzaoR;
    public final String zzaoS;
    public final List<String> zzaoT;

    static {
        CREATOR = new zzqe();
    }

    public zzqd(int i, String str, String str2, String str3, String str4, List<String> list) {
        this.versionCode = i;
        this.name = str;
        this.zzaoQ = str2;
        this.zzaoR = str3;
        this.zzaoS = str4;
        this.zzaoT = list;
    }

    public static zzqd zza(String str, String str2, String str3, String str4, List<String> list) {
        return new zzqd(0, str, str2, str3, str4, list);
    }

    public int describeContents() {
        zzqe com_google_android_gms_internal_zzqe = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzqd)) {
            return false;
        }
        zzqd com_google_android_gms_internal_zzqd = (zzqd) obj;
        return zzw.equal(this.name, com_google_android_gms_internal_zzqd.name) && zzw.equal(this.zzaoQ, com_google_android_gms_internal_zzqd.zzaoQ) && zzw.equal(this.zzaoR, com_google_android_gms_internal_zzqd.zzaoR) && zzw.equal(this.zzaoS, com_google_android_gms_internal_zzqd.zzaoS) && zzw.equal(this.zzaoT, com_google_android_gms_internal_zzqd.zzaoT);
    }

    public int hashCode() {
        return zzw.hashCode(this.name, this.zzaoQ, this.zzaoR, this.zzaoS);
    }

    public String toString() {
        return zzw.zzk(this).zza("name", this.name).zza("address", this.zzaoQ).zza("internationalPhoneNumber", this.zzaoR).zza("regularOpenHours", this.zzaoS).zza("attributions", this.zzaoT).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzqe com_google_android_gms_internal_zzqe = CREATOR;
        zzqe.zza(this, parcel, i);
    }
}
