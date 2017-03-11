package com.google.android.gms.common.api;

import android.content.Context;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzq;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Api<O extends ApiOptions> {
    private final zzb<?, O> zzLS;
    private final zzc<?> zzLT;
    private final ArrayList<Scope> zzLU;

    public interface ApiOptions {

        public interface HasOptions extends ApiOptions {
        }

        public interface NotRequiredOptions extends ApiOptions {
        }

        public static final class NoOptions implements NotRequiredOptions {
            private NoOptions() {
            }
        }

        public interface Optional extends HasOptions, NotRequiredOptions {
        }
    }

    public interface zza {
        void connect();

        void disconnect();

        void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

        boolean isConnected();

        void zza(com.google.android.gms.common.api.GoogleApiClient.zza com_google_android_gms_common_api_GoogleApiClient_zza);

        void zza(zzq com_google_android_gms_common_internal_zzq);

        void zzb(zzq com_google_android_gms_common_internal_zzq);

        boolean zzhc();
    }

    public interface zzb<T extends zza, O> {
        int getPriority();

        T zza(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, O o, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener);
    }

    public static final class zzc<C extends zza> {
    }

    public <C extends zza> Api(zzb<C, O> com_google_android_gms_common_api_Api_zzb_C__O, zzc<C> com_google_android_gms_common_api_Api_zzc_C, Scope... scopeArr) {
        this.zzLS = com_google_android_gms_common_api_Api_zzb_C__O;
        this.zzLT = com_google_android_gms_common_api_Api_zzc_C;
        this.zzLU = new ArrayList(Arrays.asList(scopeArr));
    }

    public zzb<?, O> zzhT() {
        return this.zzLS;
    }

    public List<Scope> zzhU() {
        return this.zzLU;
    }

    public zzc<?> zzhV() {
        return this.zzLT;
    }
}
