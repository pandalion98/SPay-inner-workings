package com.google.android.gms.common.api;

import java.util.concurrent.TimeUnit;

public interface PendingResult<R extends Result> {

    public interface BatchCallback {
        void zzl(Status status);
    }

    void addBatchCallback(BatchCallback batchCallback);

    R await();

    R await(long j, TimeUnit timeUnit);

    void cancel();

    boolean isCanceled();

    void setResultCallback(ResultCallback<R> resultCallback);

    void setResultCallback(ResultCallback<R> resultCallback, long j, TimeUnit timeUnit);
}
