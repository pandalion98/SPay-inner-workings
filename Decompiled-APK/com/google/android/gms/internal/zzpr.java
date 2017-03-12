package com.google.android.gms.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.zzi;
import com.google.android.gms.location.places.zzi.zza;
import com.google.android.gms.location.places.zzi.zzc;
import com.google.android.gms.maps.model.LatLngBounds;
import java.util.Arrays;

public class zzpr implements GeoDataApi {
    private static final String TAG;

    /* renamed from: com.google.android.gms.internal.zzpr.1 */
    class C02241 extends zzc<zzps> {
        final /* synthetic */ AddPlaceRequest zzaok;
        final /* synthetic */ zzpr zzaol;

        C02241(zzpr com_google_android_gms_internal_zzpr, Api.zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, AddPlaceRequest addPlaceRequest) {
            this.zzaol = com_google_android_gms_internal_zzpr;
            this.zzaok = addPlaceRequest;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zzps com_google_android_gms_internal_zzps) {
            com_google_android_gms_internal_zzps.zza(new zzi((zzc) this, com_google_android_gms_internal_zzps.getContext()), this.zzaok);
        }
    }

    /* renamed from: com.google.android.gms.internal.zzpr.2 */
    class C02252 extends zzc<zzps> {
        final /* synthetic */ zzpr zzaol;
        final /* synthetic */ String[] zzaom;

        C02252(zzpr com_google_android_gms_internal_zzpr, Api.zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, String[] strArr) {
            this.zzaol = com_google_android_gms_internal_zzpr;
            this.zzaom = strArr;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zzps com_google_android_gms_internal_zzps) {
            com_google_android_gms_internal_zzps.zza(new zzi((zzc) this, com_google_android_gms_internal_zzps.getContext()), Arrays.asList(this.zzaom));
        }
    }

    /* renamed from: com.google.android.gms.internal.zzpr.3 */
    class C02263 extends zza<zzps> {
        final /* synthetic */ String zzafV;
        final /* synthetic */ zzpr zzaol;
        final /* synthetic */ LatLngBounds zzaon;
        final /* synthetic */ AutocompleteFilter zzaoo;

        C02263(zzpr com_google_android_gms_internal_zzpr, Api.zzc com_google_android_gms_common_api_Api_zzc, GoogleApiClient googleApiClient, String str, LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter) {
            this.zzaol = com_google_android_gms_internal_zzpr;
            this.zzafV = str;
            this.zzaon = latLngBounds;
            this.zzaoo = autocompleteFilter;
            super(com_google_android_gms_common_api_Api_zzc, googleApiClient);
        }

        protected void zza(zzps com_google_android_gms_internal_zzps) {
            com_google_android_gms_internal_zzps.zza(new zzi((zza) this), this.zzafV, this.zzaon, this.zzaoo);
        }
    }

    static {
        TAG = zzpr.class.getSimpleName();
    }

    public PendingResult<PlaceBuffer> addPlace(GoogleApiClient googleApiClient, AddPlaceRequest addPlaceRequest) {
        return googleApiClient.zzb(new C02241(this, Places.zzanT, googleApiClient, addPlaceRequest));
    }

    public PendingResult<AutocompletePredictionBuffer> getAutocompletePredictions(GoogleApiClient googleApiClient, String str, LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter) {
        return googleApiClient.zza(new C02263(this, Places.zzanT, googleApiClient, str, latLngBounds, autocompleteFilter));
    }

    public PendingResult<PlaceBuffer> getPlaceById(GoogleApiClient googleApiClient, String... strArr) {
        boolean z = true;
        if (strArr == null || strArr.length < 1) {
            z = false;
        }
        zzx.zzO(z);
        return googleApiClient.zza(new C02252(this, Places.zzanT, googleApiClient, strArr));
    }
}
