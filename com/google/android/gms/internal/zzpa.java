package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.GeofencingRequest.Builder;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.location.zzf.zzb;
import java.util.List;

public class zzpa implements GeofencingApi {

    private static abstract class zza extends com.google.android.gms.location.LocationServices.zza<Status> {
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

    /* renamed from: com.google.android.gms.internal.zzpa.1 */
    class C02181 extends zza {
        final /* synthetic */ PendingIntent zzaaA;
        final /* synthetic */ GeofencingRequest zzamM;
        final /* synthetic */ zzpa zzamN;

        /* renamed from: com.google.android.gms.internal.zzpa.1.1 */
        class C02171 implements com.google.android.gms.location.zzf.zza {
            final /* synthetic */ C02181 zzamO;

            C02171(C02181 c02181) {
                this.zzamO = c02181;
            }

            public void zza(int i, String[] strArr) {
                this.zzamO.setResult(LocationStatusCodes.zzfr(i));
            }
        }

        C02181(zzpa com_google_android_gms_internal_zzpa, GoogleApiClient googleApiClient, GeofencingRequest geofencingRequest, PendingIntent pendingIntent) {
            this.zzamN = com_google_android_gms_internal_zzpa;
            this.zzamM = geofencingRequest;
            this.zzaaA = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzpf com_google_android_gms_internal_zzpf) {
            com_google_android_gms_internal_zzpf.zza(this.zzamM, this.zzaaA, new C02171(this));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzpa.2 */
    class C02202 extends zza {
        final /* synthetic */ PendingIntent zzaaA;
        final /* synthetic */ zzpa zzamN;

        /* renamed from: com.google.android.gms.internal.zzpa.2.1 */
        class C02191 implements zzb {
            final /* synthetic */ C02202 zzamP;

            C02191(C02202 c02202) {
                this.zzamP = c02202;
            }

            public void zzb(int i, PendingIntent pendingIntent) {
                this.zzamP.setResult(LocationStatusCodes.zzfr(i));
            }

            public void zzb(int i, String[] strArr) {
                Log.wtf("GeofencingImpl", "Request ID callback shouldn't have been called");
            }
        }

        C02202(zzpa com_google_android_gms_internal_zzpa, GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
            this.zzamN = com_google_android_gms_internal_zzpa;
            this.zzaaA = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzpf com_google_android_gms_internal_zzpf) {
            com_google_android_gms_internal_zzpf.zza(this.zzaaA, new C02191(this));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzpa.3 */
    class C02223 extends zza {
        final /* synthetic */ zzpa zzamN;
        final /* synthetic */ List zzamQ;

        /* renamed from: com.google.android.gms.internal.zzpa.3.1 */
        class C02211 implements zzb {
            final /* synthetic */ C02223 zzamR;

            C02211(C02223 c02223) {
                this.zzamR = c02223;
            }

            public void zzb(int i, PendingIntent pendingIntent) {
                Log.wtf("GeofencingImpl", "PendingIntent callback shouldn't have been called");
            }

            public void zzb(int i, String[] strArr) {
                this.zzamR.setResult(LocationStatusCodes.zzfr(i));
            }
        }

        C02223(zzpa com_google_android_gms_internal_zzpa, GoogleApiClient googleApiClient, List list) {
            this.zzamN = com_google_android_gms_internal_zzpa;
            this.zzamQ = list;
            super(googleApiClient);
        }

        protected void zza(zzpf com_google_android_gms_internal_zzpf) {
            com_google_android_gms_internal_zzpf.zza(this.zzamQ, new C02211(this));
        }
    }

    public PendingResult<Status> addGeofences(GoogleApiClient googleApiClient, GeofencingRequest geofencingRequest, PendingIntent pendingIntent) {
        return googleApiClient.zzb(new C02181(this, googleApiClient, geofencingRequest, pendingIntent));
    }

    @Deprecated
    public PendingResult<Status> addGeofences(GoogleApiClient googleApiClient, List<Geofence> list, PendingIntent pendingIntent) {
        Builder builder = new Builder();
        builder.addGeofences(list);
        builder.setInitialTrigger(5);
        return addGeofences(googleApiClient, builder.build(), pendingIntent);
    }

    public PendingResult<Status> removeGeofences(GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
        return googleApiClient.zzb(new C02202(this, googleApiClient, pendingIntent));
    }

    public PendingResult<Status> removeGeofences(GoogleApiClient googleApiClient, List<String> list) {
        return googleApiClient.zzb(new C02223(this, googleApiClient, list));
    }
}
