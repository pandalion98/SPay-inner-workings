package com.google.android.gms.internal;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zzf;
import com.samsung.android.spayfw.cncc.CNCCCommands;
import java.util.concurrent.Executors;

public final class zzup {
    public static final Api<zzus> API;
    public static final zzc<zzvc> zzGR;
    public static final zzb<zzvc, zzus> zzGS;
    public static final zzuq zzawO;

    /* renamed from: com.google.android.gms.internal.zzup.1 */
    static class C02311 implements zzb<zzvc, zzus> {
        C02311() {
        }

        public int getPriority() {
            return CNCCCommands.CMD_CNCC_CMD_UNKNOWN;
        }

        public zzvc zza(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, zzus com_google_android_gms_internal_zzus, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzvc(context, looper, com_google_android_gms_common_internal_zzf, com_google_android_gms_internal_zzus == null ? zzus.zzawP : com_google_android_gms_internal_zzus, connectionCallbacks, onConnectionFailedListener, Executors.newSingleThreadExecutor());
        }
    }

    static {
        zzGR = new zzc();
        zzGS = new C02311();
        API = new Api(zzGS, zzGR, new Scope[0]);
        zzawO = new zzvb();
    }
}
