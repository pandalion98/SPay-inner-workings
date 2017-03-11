package com.google.android.gms.internal;

import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzxf.zzd;
import java.util.ArrayList;

public class zztg {
    private final ArrayList<zza> zzatP;
    private int zzatQ;

    public static class zza {
        public final zztj zzatR;
        public final zztf zzatS;
        public final zzd zzatT;

        private zza(zztj com_google_android_gms_internal_zztj, zztf com_google_android_gms_internal_zztf) {
            this.zzatR = (zztj) zzx.zzl(com_google_android_gms_internal_zztj);
            this.zzatS = (zztf) zzx.zzl(com_google_android_gms_internal_zztf);
            this.zzatT = null;
        }
    }

    public zztg() {
        this(100);
    }

    public zztg(int i) {
        this.zzatP = new ArrayList();
        this.zzatQ = i;
    }

    private void zzrH() {
        while (getSize() > getCapacity()) {
            this.zzatP.remove(0);
        }
    }

    public void clear() {
        this.zzatP.clear();
    }

    public int getCapacity() {
        return this.zzatQ;
    }

    public int getSize() {
        return this.zzatP.size();
    }

    public boolean isEmpty() {
        return this.zzatP.isEmpty();
    }

    public void zza(zztj com_google_android_gms_internal_zztj, zztf com_google_android_gms_internal_zztf) {
        this.zzatP.add(new zza(com_google_android_gms_internal_zztf, null));
        zzrH();
    }

    public ArrayList<zza> zzrG() {
        return this.zzatP;
    }
}
