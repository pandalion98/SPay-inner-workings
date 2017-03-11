package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzw;
import java.util.Locale;

public class zzqh implements SafeParcelable {
    public static final zzqi CREATOR;
    public static final zzqh zzapa;
    public final int versionCode;
    public final String zzaob;
    public final String zzapb;
    public final String zzapc;
    public final String zzapd;
    public final String zzape;

    static {
        zzapa = new zzqh(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, Locale.getDefault(), null);
        CREATOR = new zzqi();
    }

    public zzqh(int i, String str, String str2, String str3, String str4, String str5) {
        this.versionCode = i;
        this.zzapb = str;
        this.zzapc = str2;
        this.zzapd = str3;
        this.zzaob = str4;
        this.zzape = str5;
    }

    public zzqh(String str, Locale locale, String str2) {
        this(1, str, locale.toString(), str2, null, null);
    }

    public zzqh(String str, Locale locale, String str2, String str3, String str4) {
        this(1, str, locale.toString(), str2, str3, str4);
    }

    public int describeContents() {
        zzqi com_google_android_gms_internal_zzqi = CREATOR;
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof zzqh)) {
            return false;
        }
        zzqh com_google_android_gms_internal_zzqh = (zzqh) obj;
        return this.zzapc.equals(com_google_android_gms_internal_zzqh.zzapc) && this.zzapb.equals(com_google_android_gms_internal_zzqh.zzapb) && zzw.equal(this.zzapd, com_google_android_gms_internal_zzqh.zzapd) && zzw.equal(this.zzaob, com_google_android_gms_internal_zzqh.zzaob) && zzw.equal(this.zzape, com_google_android_gms_internal_zzqh.zzape);
    }

    public int hashCode() {
        return zzw.hashCode(this.zzapb, this.zzapc, this.zzapd, this.zzaob, this.zzape);
    }

    public String toString() {
        return zzw.zzk(this).zza("clientPackageName", this.zzapb).zza("locale", this.zzapc).zza("accountName", this.zzapd).zza("gCoreClientName", this.zzaob).zza("chargedPackageName", this.zzape).toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzqi com_google_android_gms_internal_zzqi = CREATOR;
        zzqi.zza(this, parcel, i);
    }
}
