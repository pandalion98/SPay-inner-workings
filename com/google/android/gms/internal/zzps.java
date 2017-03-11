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
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.PlacesOptions;
import com.google.android.gms.location.places.PlacesOptions.Builder;
import com.google.android.gms.location.places.zzi;
import com.google.android.gms.maps.model.LatLngBounds;
import com.samsung.android.spayfw.cncc.CNCCCommands;
import java.util.List;
import java.util.Locale;

public class zzps extends zzk<zzpu> {
    private final zzqh zzaop;
    private final Locale zzaoq;

    public static class zza implements zzb<zzps, PlacesOptions> {
        private final String zzaor;
        private final String zzaos;

        public zza(String str, String str2) {
            this.zzaor = str;
            this.zzaos = str2;
        }

        public int getPriority() {
            return CNCCCommands.CMD_CNCC_CMD_UNKNOWN;
        }

        public zzps zza(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, PlacesOptions placesOptions, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
            return new zzps(context, looper, com_google_android_gms_common_internal_zzf, connectionCallbacks, onConnectionFailedListener, this.zzaor != null ? this.zzaor : context.getPackageName(), this.zzaos != null ? this.zzaos : context.getPackageName(), placesOptions == null ? new Builder().build() : placesOptions);
        }
    }

    public zzps(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str, String str2, PlacesOptions placesOptions) {
        super(context, looper, 65, connectionCallbacks, onConnectionFailedListener, com_google_android_gms_common_internal_zzf);
        this.zzaoq = Locale.getDefault();
        String str3 = null;
        if (com_google_android_gms_common_internal_zzf.getAccount() != null) {
            str3 = com_google_android_gms_common_internal_zzf.getAccount().name;
        }
        this.zzaop = new zzqh(str, this.zzaoq, str3, placesOptions.zzaob, str2);
    }

    public void zza(zzi com_google_android_gms_location_places_zzi, AddPlaceRequest addPlaceRequest) {
        zzx.zzb((Object) addPlaceRequest, (Object) "userAddedPlace == null");
        ((zzpu) zzjb()).zza(addPlaceRequest, this.zzaop, (zzpv) com_google_android_gms_location_places_zzi);
    }

    public void zza(zzi com_google_android_gms_location_places_zzi, String str, LatLngBounds latLngBounds, AutocompleteFilter autocompleteFilter) {
        zzx.zzb((Object) str, (Object) "query == null");
        zzx.zzb((Object) latLngBounds, (Object) "bounds == null");
        zzx.zzb((Object) com_google_android_gms_location_places_zzi, (Object) "callback == null");
        ((zzpu) zzjb()).zza(str, latLngBounds, autocompleteFilter == null ? AutocompleteFilter.create(null) : autocompleteFilter, this.zzaop, (zzpv) com_google_android_gms_location_places_zzi);
    }

    public void zza(zzi com_google_android_gms_location_places_zzi, List<String> list) {
        ((zzpu) zzjb()).zzb((List) list, this.zzaop, (zzpv) com_google_android_gms_location_places_zzi);
    }

    protected zzpu zzbm(IBinder iBinder) {
        return com.google.android.gms.internal.zzpu.zza.zzbo(iBinder);
    }

    protected String zzcF() {
        return "com.google.android.gms.location.places.GeoDataApi";
    }

    protected String zzcG() {
        return "com.google.android.gms.location.places.internal.IGooglePlacesService";
    }

    protected /* synthetic */ IInterface zzp(IBinder iBinder) {
        return zzbm(iBinder);
    }
}
