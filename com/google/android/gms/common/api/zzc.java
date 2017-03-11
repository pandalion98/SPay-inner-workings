package com.google.android.gms.common.api;

import com.google.android.gms.common.data.DataHolder;

public abstract class zzc implements Releasable, Result {
    protected final Status zzHb;
    protected final DataHolder zzMd;

    protected zzc(DataHolder dataHolder) {
        this(dataHolder, new Status(dataHolder.getStatusCode()));
    }

    protected zzc(DataHolder dataHolder, Status status) {
        this.zzHb = status;
        this.zzMd = dataHolder;
    }

    public Status getStatus() {
        return this.zzHb;
    }

    public void release() {
        if (this.zzMd != null) {
            this.zzMd.close();
        }
    }
}
