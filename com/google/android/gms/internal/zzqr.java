package com.google.android.gms.internal;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.data.zze;
import com.google.android.gms.common.internal.zzx;

public final class zzqr extends zze<zzqq> implements Result {
    private final Status zzHb;

    public zzqr(DataHolder dataHolder) {
        this(dataHolder, new Status(dataHolder.getStatusCode()));
    }

    private zzqr(DataHolder dataHolder, Status status) {
        super(dataHolder, zzqq.CREATOR);
        boolean z = dataHolder == null || dataHolder.getStatusCode() == status.getStatusCode();
        zzx.zzO(z);
        this.zzHb = status;
    }

    public static zzqr zzaE(Status status) {
        return new zzqr(null, status);
    }

    public Status getStatus() {
        return this.zzHb;
    }
}
