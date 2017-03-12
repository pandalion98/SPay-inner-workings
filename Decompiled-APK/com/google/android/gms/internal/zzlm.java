package com.google.android.gms.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class zzlm implements SafeParcelable {
    public static final zzln CREATOR;
    final int zzFG;
    private final long zzRi;
    private String zzRj;
    private final String zzRk;
    private final String zzRl;
    private final String zzRm;
    private final String zzRn;
    private final String zzRo;
    private final long zzRp;
    private long zzRq;

    static {
        CREATOR = new zzln();
    }

    zzlm(int i, long j, String str, String str2, String str3, String str4, String str5, String str6, long j2) {
        this.zzFG = i;
        this.zzRi = j;
        this.zzRj = str;
        this.zzRk = str2;
        this.zzRl = str3;
        this.zzRm = str4;
        this.zzRn = str5;
        this.zzRq = -1;
        this.zzRo = str6;
        this.zzRp = j2;
    }

    public zzlm(long j, String str, String str2, String str3, String str4, String str5, String str6, long j2) {
        this(1, j, str, str2, str3, str4, str5, str6, j2);
    }

    public int describeContents() {
        return 0;
    }

    public long getTimeMillis() {
        return this.zzRi;
    }

    public void writeToParcel(Parcel parcel, int i) {
        zzln.zza(this, parcel, i);
    }

    public String zzjT() {
        return this.zzRj;
    }

    public String zzjU() {
        return this.zzRk;
    }

    public String zzjV() {
        return this.zzRl;
    }

    public String zzjW() {
        return this.zzRm;
    }

    public String zzjX() {
        return this.zzRn;
    }

    public String zzjY() {
        return this.zzRo;
    }

    public long zzjZ() {
        return this.zzRp;
    }
}
