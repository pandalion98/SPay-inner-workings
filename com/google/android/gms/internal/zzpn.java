package com.google.android.gms.internal;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zza.zzb;
import com.google.android.gms.location.LocationServices.zza;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.SettingsApi;

public class zzpn implements SettingsApi {

    /* renamed from: com.google.android.gms.internal.zzpn.1 */
    class C02231 extends zza<LocationSettingsResult> {
        final /* synthetic */ LocationSettingsRequest zzanq;
        final /* synthetic */ String zzanr;
        final /* synthetic */ zzpn zzans;

        C02231(zzpn com_google_android_gms_internal_zzpn, GoogleApiClient googleApiClient, LocationSettingsRequest locationSettingsRequest, String str) {
            this.zzans = com_google_android_gms_internal_zzpn;
            this.zzanq = locationSettingsRequest;
            this.zzanr = str;
            super(googleApiClient);
        }

        public /* synthetic */ Result createFailedResult(Status status) {
            return zzax(status);
        }

        protected void zza(zzpf com_google_android_gms_internal_zzpf) {
            com_google_android_gms_internal_zzpf.zza(this.zzanq, (zzb) this, this.zzanr);
        }

        public LocationSettingsResult zzax(Status status) {
            return new LocationSettingsResult(status);
        }
    }

    public PendingResult<LocationSettingsResult> checkLocationSettings(GoogleApiClient googleApiClient, LocationSettingsRequest locationSettingsRequest) {
        return zza(googleApiClient, locationSettingsRequest, null);
    }

    public PendingResult<LocationSettingsResult> zza(GoogleApiClient googleApiClient, LocationSettingsRequest locationSettingsRequest, String str) {
        return googleApiClient.zza(new C02231(this, googleApiClient, locationSettingsRequest, str));
    }
}
