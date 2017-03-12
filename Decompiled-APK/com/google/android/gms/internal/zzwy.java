package com.google.android.gms.internal;

import java.io.IOException;

public abstract class zzwy {
    protected volatile int zzaHM;

    public zzwy() {
        this.zzaHM = -1;
    }

    public static final <T extends zzwy> T zza(T t, byte[] bArr) {
        return zzb(t, bArr, 0, bArr.length);
    }

    public static final void zza(zzwy com_google_android_gms_internal_zzwy, byte[] bArr, int i, int i2) {
        try {
            zzwr zzb = zzwr.zzb(bArr, i, i2);
            com_google_android_gms_internal_zzwy.zza(zzb);
            zzb.zzvK();
        } catch (Throwable e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    public static final <T extends zzwy> T zzb(T t, byte[] bArr, int i, int i2) {
        try {
            zzwq zza = zzwq.zza(bArr, i, i2);
            t.zzb(zza);
            zza.zzim(0);
            return t;
        } catch (zzwx e) {
            throw e;
        } catch (IOException e2) {
            throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).");
        }
    }

    public static final byte[] zzf(zzwy com_google_android_gms_internal_zzwy) {
        byte[] bArr = new byte[com_google_android_gms_internal_zzwy.zzvY()];
        zza(com_google_android_gms_internal_zzwy, bArr, 0, bArr.length);
        return bArr;
    }

    public /* synthetic */ Object clone() {
        return zzvN();
    }

    public String toString() {
        return zzwz.zzg(this);
    }

    public void zza(zzwr com_google_android_gms_internal_zzwr) {
    }

    public abstract zzwy zzb(zzwq com_google_android_gms_internal_zzwq);

    protected int zzc() {
        return 0;
    }

    public zzwy zzvN() {
        return (zzwy) super.clone();
    }

    public int zzvX() {
        if (this.zzaHM < 0) {
            zzvY();
        }
        return this.zzaHM;
    }

    public int zzvY() {
        int zzc = zzc();
        this.zzaHM = zzc;
        return zzc;
    }
}
