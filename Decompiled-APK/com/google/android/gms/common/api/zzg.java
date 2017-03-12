package com.google.android.gms.common.api;

import android.os.Looper;

public class zzg extends AbstractPendingResult<Status> {
    public zzg(Looper looper) {
        super(looper);
    }

    protected /* synthetic */ Result createFailedResult(Status status) {
        return zzb(status);
    }

    protected Status zzb(Status status) {
        return status;
    }
}
