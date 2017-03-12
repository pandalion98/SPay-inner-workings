package com.google.android.gms.internal;

import com.google.android.gms.common.api.Api.zzc;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.zzi;
import com.google.android.gms.location.places.zzi.zzd;
import com.google.android.gms.location.places.zzi.zzg;

public class zzpw implements PlaceDetectionApi {

    /* renamed from: com.google.android.gms.internal.zzpw.1 */
    class C02271 extends zzd<zzpx> {
        final /* synthetic */ PlaceFilter zzaot;
        final /* synthetic */ zzpw zzaou;

        C02271(zzpw com_google_android_gms_internal_zzpw, zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, PlaceFilter placeFilter) {
            this.zzaou = com_google_android_gms_internal_zzpw;
            this.zzaot = placeFilter;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zzpx com_google_android_gms_internal_zzpx) {
            com_google_android_gms_internal_zzpx.zza(new zzi((zzd) this, com_google_android_gms_internal_zzpx.getContext()), this.zzaot);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzpw.2 */
    class C02282 extends zzg<zzpx> {
        final /* synthetic */ zzpw zzaou;
        final /* synthetic */ PlaceReport zzaov;

        C02282(zzpw com_google_android_gms_internal_zzpw, zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, PlaceReport placeReport) {
            this.zzaou = com_google_android_gms_internal_zzpw;
            this.zzaov = placeReport;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zzpx com_google_android_gms_internal_zzpx) {
            com_google_android_gms_internal_zzpx.zza(new zzi((zzg) this), this.zzaov);
        }
    }

    public PendingResult<PlaceLikelihoodBuffer> getCurrentPlace(GoogleApiClient googleApiClient, PlaceFilter placeFilter) {
        return googleApiClient.zza(new C02271(this, Places.zzanU, googleApiClient, placeFilter));
    }

    public PendingResult<Status> reportDeviceAtPlace(GoogleApiClient googleApiClient, PlaceReport placeReport) {
        return googleApiClient.zzb(new C02282(this, Places.zzanU, googleApiClient, placeReport));
    }
}
