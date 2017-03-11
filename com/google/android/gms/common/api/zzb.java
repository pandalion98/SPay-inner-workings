package com.google.android.gms.common.api;

import com.google.android.gms.common.data.DataHolder;

public abstract class zzb<L> implements com.google.android.gms.common.api.zze.zzb<L> {
    private final DataHolder zzMd;

    protected zzb(DataHolder dataHolder) {
        this.zzMd = dataHolder;
    }

    protected abstract void zza(L l, DataHolder dataHolder);

    public final void zze(L l) {
        zza(l, this.zzMd);
    }

    public void zzhX() {
        if (this.zzMd != null) {
            this.zzMd.close();
        }
    }
}
