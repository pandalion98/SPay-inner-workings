package com.google.android.gms.location.places;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmg;
import com.google.android.gms.internal.zzqr;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class zzi extends com.google.android.gms.internal.zzpv.zza {
    private static final String TAG;
    private final Context mContext;
    private final zzd zzanV;
    private final zza zzanW;
    private final zze zzanX;
    private final zzf zzanY;
    private final zzg zzanZ;
    private final zzc zzaoa;

    public static abstract class zzb<R extends Result, A extends com.google.android.gms.common.api.Api.zza> extends com.google.android.gms.common.api.zza.zza<R, A> {
        public zzb(com.google.android.gms.common.api.Api.zzc<A> com_google_android_gms_common_api_Api_zzc_A, GoogleApiClient googleApiClient) {
            super(com_google_android_gms_common_api_Api_zzc_A, googleApiClient);
        }
    }

    public static abstract class zzc<A extends com.google.android.gms.common.api.Api.zza> extends zzb<PlaceBuffer, A> {
        public zzc(com.google.android.gms.common.api.Api.zzc<A> com_google_android_gms_common_api_Api_zzc_A, GoogleApiClient googleApiClient) {
            super(com_google_android_gms_common_api_Api_zzc_A, googleApiClient);
        }

        protected /* synthetic */ Result createFailedResult(Status status) {
            return zzaA(status);
        }

        protected PlaceBuffer zzaA(Status status) {
            return new PlaceBuffer(DataHolder.zzay(status.getStatusCode()), null);
        }
    }

    public static abstract class zza<A extends com.google.android.gms.common.api.Api.zza> extends zzb<AutocompletePredictionBuffer, A> {
        public zza(com.google.android.gms.common.api.Api.zzc<A> com_google_android_gms_common_api_Api_zzc_A, GoogleApiClient googleApiClient) {
            super(com_google_android_gms_common_api_Api_zzc_A, googleApiClient);
        }

        protected /* synthetic */ Result createFailedResult(Status status) {
            return zzaz(status);
        }

        protected AutocompletePredictionBuffer zzaz(Status status) {
            return new AutocompletePredictionBuffer(DataHolder.zzay(status.getStatusCode()));
        }
    }

    public static abstract class zzd<A extends com.google.android.gms.common.api.Api.zza> extends zzb<PlaceLikelihoodBuffer, A> {
        public zzd(com.google.android.gms.common.api.Api.zzc<A> com_google_android_gms_common_api_Api_zzc_A, GoogleApiClient googleApiClient) {
            super(com_google_android_gms_common_api_Api_zzc_A, googleApiClient);
        }

        protected /* synthetic */ Result createFailedResult(Status status) {
            return zzaB(status);
        }

        protected PlaceLikelihoodBuffer zzaB(Status status) {
            return new PlaceLikelihoodBuffer(DataHolder.zzay(status.getStatusCode()), 100, null);
        }
    }

    public static abstract class zzg<A extends com.google.android.gms.common.api.Api.zza> extends zzb<Status, A> {
        public zzg(com.google.android.gms.common.api.Api.zzc<A> com_google_android_gms_common_api_Api_zzc_A, GoogleApiClient googleApiClient) {
            super(com_google_android_gms_common_api_Api_zzc_A, googleApiClient);
        }

        protected /* synthetic */ Result createFailedResult(Status status) {
            return zzb(status);
        }

        protected Status zzb(Status status) {
            return status;
        }
    }

    public static abstract class zze<A extends com.google.android.gms.common.api.Api.zza> extends zzb<zzh, A> {
        protected /* synthetic */ Result createFailedResult(Status status) {
            return zzaC(status);
        }

        protected zzh zzaC(Status status) {
            return new zzh(status, Collections.emptyList());
        }
    }

    public static abstract class zzf<A extends com.google.android.gms.common.api.Api.zza> extends zzb<zzqr, A> {
        protected /* synthetic */ Result createFailedResult(Status status) {
            return zzaD(status);
        }

        protected zzqr zzaD(Status status) {
            return zzqr.zzaE(status);
        }
    }

    static {
        TAG = zzi.class.getSimpleName();
    }

    public zzi(zza com_google_android_gms_location_places_zzi_zza) {
        this.zzanV = null;
        this.zzanX = null;
        this.zzanW = com_google_android_gms_location_places_zzi_zza;
        this.zzanY = null;
        this.zzanZ = null;
        this.zzaoa = null;
        this.mContext = null;
    }

    public zzi(zzc com_google_android_gms_location_places_zzi_zzc, Context context) {
        this.zzanV = null;
        this.zzanX = null;
        this.zzanW = null;
        this.zzanY = null;
        this.zzanZ = null;
        this.zzaoa = com_google_android_gms_location_places_zzi_zzc;
        this.mContext = context;
    }

    public zzi(zzd com_google_android_gms_location_places_zzi_zzd, Context context) {
        this.zzanV = com_google_android_gms_location_places_zzi_zzd;
        this.zzanX = null;
        this.zzanW = null;
        this.zzanY = null;
        this.zzanZ = null;
        this.zzaoa = null;
        this.mContext = context;
    }

    public zzi(zzg com_google_android_gms_location_places_zzi_zzg) {
        this.zzanV = null;
        this.zzanX = null;
        this.zzanW = null;
        this.zzanY = null;
        this.zzanZ = com_google_android_gms_location_places_zzi_zzg;
        this.zzaoa = null;
        this.mContext = null;
    }

    public void zzX(DataHolder dataHolder) {
        Object obj = 1;
        zzx.zza((this.zzanV != null ? 1 : null) != (this.zzanX != null ? 1 : null), (Object) "Only one of placeEstimator or placeReturner can be null");
        if (this.zzanV == null) {
            obj = null;
        }
        if (dataHolder == null) {
            if (Log.isLoggable(TAG, 6)) {
                Log.e(TAG, "onPlaceEstimated received null DataHolder: " + zzmg.zzkm());
            }
            if (obj != null) {
                this.zzanV.zzk(Status.zzNq);
                return;
            } else {
                this.zzanX.zzk(Status.zzNq);
                return;
            }
        }
        PlaceLikelihoodBuffer placeLikelihoodBuffer = new PlaceLikelihoodBuffer(dataHolder, 100, this.mContext);
        if (obj != null) {
            this.zzanV.setResult(placeLikelihoodBuffer);
            return;
        }
        Status status = placeLikelihoodBuffer.getStatus();
        List arrayList = new ArrayList(placeLikelihoodBuffer.getCount());
        Iterator it = placeLikelihoodBuffer.iterator();
        while (it.hasNext()) {
            arrayList.add(((PlaceLikelihood) it.next()).getPlace().freeze());
        }
        placeLikelihoodBuffer.release();
        this.zzanX.setResult(new zzh(status, arrayList));
    }

    public void zzY(DataHolder dataHolder) {
        if (dataHolder == null) {
            if (Log.isLoggable(TAG, 6)) {
                Log.e(TAG, "onAutocompletePrediction received null DataHolder: " + zzmg.zzkm());
            }
            this.zzanW.zzk(Status.zzNq);
            return;
        }
        this.zzanW.setResult(new AutocompletePredictionBuffer(dataHolder));
    }

    public void zzZ(DataHolder dataHolder) {
        if (dataHolder == null) {
            if (Log.isLoggable(TAG, 6)) {
                Log.e(TAG, "onPlaceUserDataFetched received null DataHolder: " + zzmg.zzkm());
            }
            this.zzanY.zzk(Status.zzNq);
            return;
        }
        this.zzanY.setResult(new zzqr(dataHolder));
    }

    public void zzaa(DataHolder dataHolder) {
        this.zzaoa.setResult(new PlaceBuffer(dataHolder, this.mContext));
    }

    public void zzay(Status status) {
        this.zzanZ.setResult(status);
    }
}
