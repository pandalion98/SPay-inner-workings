package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.Api.zzb;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzk;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.PlacesOptions;
import com.google.android.gms.location.places.PlacesOptions.Builder;
import com.google.android.gms.location.places.zzi;
import com.samsung.android.spayfw.cncc.CNCCCommands;
import java.util.Locale;

public class zzpx extends zzk<zzpt> {
    private final zzqh zzaop;
    private final Locale zzaoq;

    public static class zza implements zzb<zzpx, PlacesOptions> {
        private final String zzaor;
        private final String zzaos;

        public zza(String str, String str2) {
            this.zzaor = str;
            this.zzaos = str2;
        }

        public int getPriority() {
            return CNCCCommands.CMD_CNCC_CMD_UNKNOWN;
        }

        public /* synthetic */ com.google.android.gms.common.api.Api.zza zza(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, Object obj, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return zzb(context, looper, com_google_android_gms_common_internal_zzf, (PlacesOptions) obj, connectionCallbacks, onConnectionFailedListener);
        }

        public zzpx zzb(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, PlacesOptions placesOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzpx(context, looper, com_google_android_gms_common_internal_zzf, connectionCallbacks, onConnectionFailedListener, this.zzaor != null ? this.zzaor : context.getPackageName(), this.zzaos != null ? this.zzaos : context.getPackageName(), placesOptions == null ? new Builder().build() : placesOptions);
        }
    }

    public zzpx(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str, String str2, PlacesOptions placesOptions) {
        super(context, looper, 67, connectionCallbacks, onConnectionFailedListener, com_google_android_gms_common_internal_zzf);
        this.zzaoq = Locale.getDefault();
        String str3 = null;
        if (com_google_android_gms_common_internal_zzf.getAccount() != null) {
            str3 = com_google_android_gms_common_internal_zzf.getAccount().name;
        }
        this.zzaop = new zzqh(str, this.zzaoq, str3, placesOptions.zzaob, str2);
    }

    public void zza(zzi com_google_android_gms_location_places_zzi, PlaceFilter placeFilter) {
        if (placeFilter == null) {
            placeFilter = PlaceFilter.zzpJ();
        }
        ((zzpt) zzjb()).zza(placeFilter, this.zzaop, (zzpv) com_google_android_gms_location_places_zzi);
    }

    public void zza(zzi com_google_android_gms_location_places_zzi, PlaceReport placeReport) {
        zzx.zzl(placeReport);
        ((zzpt) zzjb()).zza(placeReport, this.zzaop, (zzpv) com_google_android_gms_location_places_zzi);
    }

    protected zzpt zzbq(IBinder iBinder) {
        return com.google.android.gms.internal.zzpt.zza.zzbn(iBinder);
    }

    protected String zzcF() {
        return "com.google.android.gms.location.places.PlaceDetectionApi";
    }

    protected String zzcG() {
        return "com.google.android.gms.location.places.internal.IGooglePlaceDetectionService";
    }

    protected /* synthetic */ IInterface zzp(IBinder iBinder) {
        return zzbq(iBinder);
    }
}
