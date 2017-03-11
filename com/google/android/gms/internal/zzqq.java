package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.List;

public class zzqq implements SafeParcelable {
    public static final zzqs CREATOR;
    final int zzFG;
    private final String zzHg;
    private final String zzanM;
    private final List<zzqu> zzapk;
    private final List<zzqo> zzapl;
    private final List<zzqm> zzapm;

    static {
        CREATOR = new zzqs();
    }

    zzqq(int i, String str, String str2, List<zzqu> list, List<zzqo> list2, List<zzqm> list3) {
        this.zzFG = i;
        this.zzHg = str;
        this.zzanM = str2;
        this.zzapk = list;
        this.zzapl = list2;
        this.zzapm = list3;
    }

    public int describeContents() {
        zzqs com_google_android_gms_internal_zzqs = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzqq)) {
            return false;
        }
        zzqq com_google_android_gms_internal_zzqq = (zzqq) obj;
        return this.zzHg.equals(com_google_android_gms_internal_zzqq.zzHg) && this.zzanM.equals(com_google_android_gms_internal_zzqq.zzanM) && this.zzapk.equals(com_google_android_gms_internal_zzqq.zzapk) && this.zzapl.equals(com_google_android_gms_internal_zzqq.zzapl) && this.zzapm.equals(com_google_android_gms_internal_zzqq.zzapm);
    }

    public String getPlaceId() {
        return this.zzanM;
    }

    public int hashCode() {
        return zzw.hashCode(this.zzHg, this.zzanM, this.zzapk, this.zzapl, this.zzapm);
    }

    public String toString() {
        return zzw.zzk(this).zza("accountName", this.zzHg).zza("placeId", this.zzanM).zza("testDataImpls", this.zzapk).zza("placeAliases", this.zzapl).zza("hereContents", this.zzapm).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzqs com_google_android_gms_internal_zzqs = CREATOR;
        zzqs.zza(this, parcel, i);
    }

    public String zzpZ() {
        return this.zzHg;
    }

    public List<zzqo> zzqa() {
        return this.zzapl;
    }

    public List<zzqm> zzqb() {
        return this.zzapm;
    }

    public List<zzqu> zzqc() {
        return this.zzapk;
    }
}
