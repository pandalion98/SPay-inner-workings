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
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzoz;
import com.google.android.gms.internal.zzpa;
import com.google.android.gms.internal.zzpf;
import com.google.android.gms.internal.zzpn;
import com.samsung.android.spayfw.cncc.CNCCCommands;

public class LocationServices {
    public static final Api<NoOptions> API;
    public static FusedLocationProviderApi FusedLocationApi;
    public static GeofencingApi GeofencingApi;
    public static SettingsApi SettingsApi;
    private static final zzc<zzpf> zzGR;
    private static final zzb<zzpf, NoOptions> zzGS;

    public static abstract class zza<R extends Result> extends com.google.android.gms.common.api.zza.zza<R, zzpf> {
        public zza(GoogleApiClient googleApiClient) {
            super(LocationServices.zzGR, googleApiClient);
        }
    }

    /* renamed from: com.google.android.gms.location.LocationServices.1 */
    static class C02381 implements zzb<zzpf, NoOptions> {
        C02381() {
        }

        public int getPriority() {
            return CNCCCommands.CMD_CNCC_CMD_UNKNOWN;
        }

        public /* synthetic */ com.google.android.gms.common.api.Api.zza zza(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return zzk(context, looper, com_google_android_gms_common_internal_zzf, (NoOptions) obj, connectionCallbacks, onConnectionFailedListener);
        }

        public zzpf zzk(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, NoOptions noOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzpf(context, looper, context.getPackageName(), connectionCallbacks, onConnectionFailedListener, "locationServices", com_google_android_gms_common_internal_zzf.getAccountName());
        }
    }

    static {
        zzGR = new zzc();
        zzGS = new C02381();
        API = new Api(zzGS, zzGR, new Scope[0]);
        FusedLocationApi = new zzoz();
        GeofencingApi = new zzpa();
        SettingsApi = new zzpn();
    }

    private LocationServices() {
    }

    public static zzpf zze(GoogleApiClient googleApiClient) {
        boolean z = true;
        zzx.zzb(googleApiClient != null, (Object) "GoogleApiClient parameter is required.");
        zzpf com_google_android_gms_internal_zzpf = (zzpf) googleApiClient.zza(zzGR);
        if (com_google_android_gms_internal_zzpf == null) {
            z = false;
        }
        zzx.zza(z, (Object) "GoogleApiClient is not configured to use the LocationServices.API Api. Pass thisinto GoogleApiClient.Builder#addApi() to use this feature.");
        return com_google_android_gms_internal_zzpf;
    }
}
