package com.google.android.gms.internal;

import java.util.Arrays;

final class zzxa {
    final int tag;
    final byte[] zzaHN;

    zzxa(int i, byte[] bArr) {
        this.tag = i;
        this.zzaHN = bArr;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzxa)) {
            return false;
        }
        zzxa com_google_android_gms_internal_zzxa = (zzxa) obj;
        return this.tag == com_google_android_gms_internal_zzxa.tag && Arrays.equals(this.zzaHN, com_google_android_gms_internal_zzxa.zzaHN);
    }

    public int hashCode() {
        return ((this.tag + 527) * 31) + Arrays.hashCode(this.zzaHN);
    }

    void zza(zzwr com_google_android_gms_internal_zzwr) {
        com_google_android_gms_internal_zzwr.zziA(this.tag);
        com_google_android_gms_internal_zzwr.zzx(this.zzaHN);
    }

    int zzc() {
        return (0 + zzwr.zziB(this.tag)) + this.zzaHN.length;
    }
}
