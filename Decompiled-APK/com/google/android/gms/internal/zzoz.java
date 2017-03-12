package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.location.Location;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class zzoz implements FusedLocationProviderApi {

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

    /* renamed from: com.google.android.gms.internal.zzoz.1 */
    class C02101 extends zza {
        final /* synthetic */ LocationRequest zzamG;
        final /* synthetic */ LocationListener zzamH;
        final /* synthetic */ zzoz zzamI;

        C02101(zzoz com_google_android_gms_internal_zzoz, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener) {
            this.zzamI = com_google_android_gms_internal_zzoz;
            this.zzamG = locationRequest;
            this.zzamH = locationListener;
            super(googleApiClient);
        }

        protected void zza(zzpf com_google_android_gms_internal_zzpf) {
            com_google_android_gms_internal_zzpf.zza(this.zzamG, this.zzamH, null);
            setResult(Status.zzNo);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzoz.2 */
    class C02112 extends zza {
        final /* synthetic */ zzoz zzamI;
        final /* synthetic */ boolean zzamJ;

        C02112(zzoz com_google_android_gms_internal_zzoz, GoogleApiClient googleApiClient, boolean z) {
            this.zzamI = com_google_android_gms_internal_zzoz;
            this.zzamJ = z;
            super(googleApiClient);
        }

        protected void zza(zzpf com_google_android_gms_internal_zzpf) {
            com_google_android_gms_internal_zzpf.zzW(this.zzamJ);
            setResult(Status.zzNo);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzoz.3 */
    class C02123 extends zza {
        final /* synthetic */ zzoz zzamI;
        final /* synthetic */ Location zzamK;

        C02123(zzoz com_google_android_gms_internal_zzoz, GoogleApiClient googleApiClient, Location location) {
            this.zzamI = com_google_android_gms_internal_zzoz;
            this.zzamK = location;
            super(googleApiClient);
        }

        protected void zza(zzpf com_google_android_gms_internal_zzpf) {
            com_google_android_gms_internal_zzpf.zzb(this.zzamK);
            setResult(Status.zzNo);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzoz.4 */
    class C02134 extends zza {
        final /* synthetic */ LocationRequest zzamG;
        final /* synthetic */ LocationListener zzamH;
        final /* synthetic */ zzoz zzamI;
        final /* synthetic */ Looper zzamL;

        C02134(zzoz com_google_android_gms_internal_zzoz, GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener, Looper looper) {
            this.zzamI = com_google_android_gms_internal_zzoz;
            this.zzamG = locationRequest;
            this.zzamH = locationListener;
            this.zzamL = looper;
            super(googleApiClient);
        }

        protected void zza(zzpf com_google_android_gms_internal_zzpf) {
            com_google_android_gms_internal_zzpf.zza(this.zzamG, this.zzamH, this.zzamL);
            setResult(Status.zzNo);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzoz.5 */
    class C02145 extends zza {
        final /* synthetic */ PendingIntent zzamC;
        final /* synthetic */ LocationRequest zzamG;
        final /* synthetic */ zzoz zzamI;

        C02145(zzoz com_google_android_gms_internal_zzoz, GoogleApiClient googleApiClient, LocationRequest locationRequest, PendingIntent pendingIntent) {
            this.zzamI = com_google_android_gms_internal_zzoz;
            this.zzamG = locationRequest;
            this.zzamC = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzpf com_google_android_gms_internal_zzpf) {
            com_google_android_gms_internal_zzpf.zzb(this.zzamG, this.zzamC);
            setResult(Status.zzNo);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzoz.6 */
    class C02156 extends zza {
        final /* synthetic */ LocationListener zzamH;
        final /* synthetic */ zzoz zzamI;

        C02156(zzoz com_google_android_gms_internal_zzoz, GoogleApiClient googleApiClient, LocationListener locationListener) {
            this.zzamI = com_google_android_gms_internal_zzoz;
            this.zzamH = locationListener;
            super(googleApiClient);
        }

        protected void zza(zzpf com_google_android_gms_internal_zzpf) {
            com_google_android_gms_internal_zzpf.zza(this.zzamH);
            setResult(Status.zzNo);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzoz.7 */
    class C02167 extends zza {
        final /* synthetic */ PendingIntent zzamC;
        final /* synthetic */ zzoz zzamI;

        C02167(zzoz com_google_android_gms_internal_zzoz, GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
            this.zzamI = com_google_android_gms_internal_zzoz;
            this.zzamC = pendingIntent;
            super(googleApiClient);
        }

        protected void zza(zzpf com_google_android_gms_internal_zzpf) {
            com_google_android_gms_internal_zzpf.zzd(this.zzamC);
            setResult(Status.zzNo);
        }
    }

    public Location getLastLocation(GoogleApiClient googleApiClient) {
        try {
            return LocationServices.zze(googleApiClient).zzpx();
        } catch (Exception e) {
            return null;
        }
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient googleApiClient, PendingIntent pendingIntent) {
        return googleApiClient.zzb(new C02167(this, googleApiClient, pendingIntent));
    }

    public PendingResult<Status> removeLocationUpdates(GoogleApiClient googleApiClient, LocationListener locationListener) {
        return googleApiClient.zzb(new C02156(this, googleApiClient, locationListener));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient googleApiClient, LocationRequest locationRequest, PendingIntent pendingIntent) {
        return googleApiClient.zzb(new C02145(this, googleApiClient, locationRequest, pendingIntent));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener) {
        return googleApiClient.zzb(new C02101(this, googleApiClient, locationRequest, locationListener));
    }

    public PendingResult<Status> requestLocationUpdates(GoogleApiClient googleApiClient, LocationRequest locationRequest, LocationListener locationListener, Looper looper) {
        return googleApiClient.zzb(new C02134(this, googleApiClient, locationRequest, locationListener, looper));
    }

    public PendingResult<Status> setMockLocation(GoogleApiClient googleApiClient, Location location) {
        return googleApiClient.zzb(new C02123(this, googleApiClient, location));
    }

    public PendingResult<Status> setMockMode(GoogleApiClient googleApiClient, boolean z) {
        return googleApiClient.zzb(new C02112(this, googleApiClient, z));
    }
}
