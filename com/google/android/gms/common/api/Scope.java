package com.google.android.gms.common.api;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzx;

public final class Scope implements SafeParcelable {
    public static final Creator<Scope> CREATOR;
    final int zzFG;
    private final String zzNn;

    static {
        CREATOR = new zzf();
    }

    Scope(int i, String str) {
        zzx.zzb(str, (Object) "scopeUri must not be null or empty");
        this.zzFG = i;
        this.zzNn = str;
    }

    public Scope(String str) {
        this(1, str);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        return this == obj ? true : !(obj instanceof Scope) ? false : this.zzNn.equals(((Scope) obj).zzNn);
    }

    public int hashCode() {
        return this.zzNn.hashCode();
    }

    public String toString() {
        return this.zzNn;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzf.zza(this, parcel, i);
    }

    public String zzio() {
        return this.zzNn;
    }
}
