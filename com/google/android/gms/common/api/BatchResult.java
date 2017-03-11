package com.google.android.gms.common.api;

import com.google.android.gms.common.internal.zzx;
import java.util.concurrent.TimeUnit;

public final class BatchResult implements Result {
    private final Status zzHb;
    private final PendingResult<?>[] zzLZ;

    BatchResult(Status status, PendingResult<?>[] pendingResultArr) {
        this.zzHb = status;
        this.zzLZ = pendingResultArr;
    }

    public Status getStatus() {
        return this.zzHb;
    }

    public <R extends Result> R take(BatchResultToken<R> batchResultToken) {
        zzx.zzb(batchResultToken.mId < this.zzLZ.length, (Object) "The result token does not belong to this batch");
        return this.zzLZ[batchResultToken.mId].await(0, TimeUnit.MILLISECONDS);
    }
}
