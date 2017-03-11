package com.google.android.gms.location;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.ApiOptions.NoOptions;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.internal.zzov;
import com.google.android.gms.internal.zzpf;
import com.samsung.android.spayfw.cncc.CNCCCommands;

public class ActivityRecognition {
    public static final Api<NoOptions> API;
    public static ActivityRecognitionApi ActivityRecognitionApi = null;
    public static final String CLIENT_NAME = "activity_recognition";
    private static final zzc<zzpf> zzGR;
    private static final zzb<zzpf, NoOptions> zzGS;

    public static abstract class zza<R extends Result> extends com.google.android.gms.common.api.zza.zza<R, zzpf> {
        public zza(GoogleApiClient googleApiClient) {
            super(ActivityRecognition.zzGR, googleApiClient);
        }
    }

    /* renamed from: com.google.android.gms.location.ActivityRecognition.1 */
    static class C02351 implements zzb<zzpf, NoOptions> {
        C02351() {
        }

        public int getPriority() {
            return CNCCCommands.CMD_CNCC_CMD_UNKNOWN;
        }

        public /* synthetic */ com.google.android.gms.common.api.Api.zza zza(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return zzk(context, looper, com_google_android_gms_common_internal_zzf, (NoOptions) obj, connectionCallbacks, onConnectionFailedListener);
        }

        public zzpf zzk(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzpf(context, looper, context.getPackageName(), connectionCallbacks, onConnectionFailedListener, ActivityRecognition.CLIENT_NAME);
        }
    }

    static {
        zzGR = new zzc();
        zzGS = new C02351();
        API = new Api(zzGS, zzGR, new Scope[0]);
        ActivityRecognitionApi = new zzov();
    }

    private ActivityRecognition() {
    }
}
