package com.google.android.gms.common.api;

import android.os.Looper;
import com.google.android.gms.common.internal.zzx;

public final class PendingResults {

    private static final class zza<R extends Result> extends AbstractPendingResult<R> {
        private final R zzNm;

        public zza(R r) {
            super(Looper.getMainLooper());
            this.zzNm = r;
        }

        protected R createFailedResult(Status status) {
            if (status.getStatusCode() == this.zzNm.getStatus().getStatusCode()) {
                return this.zzNm;
            }
            throw new UnsupportedOperationException("Creating failed results is not supported");
        }
    }

    private static final class zzb<R extends Result> extends AbstractPendingResult<R> {
        public zzb() {
            super(Looper.getMainLooper());
        }

        protected R createFailedResult(Status status) {
            throw new UnsupportedOperationException("Creating failed results is not supported");
        }
    }

    private PendingResults() {
    }

    public static PendingResult<Status> canceledPendingResult() {
        PendingResult com_google_android_gms_common_api_zzg = new zzg(Looper.getMainLooper());
        com_google_android_gms_common_api_zzg.cancel();
        return com_google_android_gms_common_api_zzg;
    }

    public static <R extends Result> PendingResult<R> canceledPendingResult(R r) {
        zzx.zzb((Object) r, (Object) "Result must not be null");
        zzx.zzb(r.getStatus().getStatusCode() == 16, (Object) "Status code must be CommonStatusCodes.CANCELED");
        PendingResult com_google_android_gms_common_api_PendingResults_zza = new zza(r);
        com_google_android_gms_common_api_PendingResults_zza.cancel();
        return com_google_android_gms_common_api_PendingResults_zza;
    }

    public static <R extends Result> PendingResult<R> immediatePendingResult(R r) {
        zzx.zzb((Object) r, (Object) "Result must not be null");
        PendingResult com_google_android_gms_common_api_PendingResults_zzb = new zzb();
        com_google_android_gms_common_api_PendingResults_zzb.setResult(r);
        return com_google_android_gms_common_api_PendingResults_zzb;
    }

    public static PendingResult<Status> immediatePendingResult(Status status) {
        zzx.zzb((Object) status, (Object) "Result must not be null");
        PendingResult com_google_android_gms_common_api_zzg = new zzg(Looper.getMainLooper());
        com_google_android_gms_common_api_zzg.setResult(status);
        return com_google_android_gms_common_api_zzg;
    }
}
