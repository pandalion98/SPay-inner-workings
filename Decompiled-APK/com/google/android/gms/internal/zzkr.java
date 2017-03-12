package com.google.android.gms.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

abstract class zzkr<R extends Result> extends com.google.android.gms.common.api.zza.zza<R, zzks> {

    static abstract class zza extends zzkr<Status> {
        public zza(GoogleApiClient googleApiClient) {
            super(googleApiClient);
        }

        public /* synthetic */ Result createFailedResult(Status status) {
            return zzb(status);
        }

        public Status zzb(Status status) {
            return status;
        }
    }

    public zzkr(GoogleApiClient googleApiClient) {
        super(zzko.zzGR, googleApiClient);
    }
}
