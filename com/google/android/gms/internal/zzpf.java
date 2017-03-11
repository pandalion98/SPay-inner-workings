package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.List;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzpf extends zzow {
    private final zzpe zzamX;
    private final zzot zzamY;

    private final class zza extends zzc<com.google.android.gms.location.zzf.zza> {
        private final int zzLs;
        private final String[] zzamZ;
        final /* synthetic */ zzpf zzana;

        public zza(zzpf com_google_android_gms_internal_zzpf, com.google.android.gms.location.zzf.zza com_google_android_gms_location_zzf_zza, int i, String[] strArr) {
            this.zzana = com_google_android_gms_internal_zzpf;
            super(com_google_android_gms_internal_zzpf, com_google_android_gms_location_zzf_zza);
            this.zzLs = LocationStatusCodes.zzfq(i);
            this.zzamZ = strArr;
        }

        protected void zza(com.google.android.gms.location.zzf.zza com_google_android_gms_location_zzf_zza) {
            if (com_google_android_gms_location_zzf_zza != null) {
                com_google_android_gms_location_zzf_zza.zza(this.zzLs, this.zzamZ);
            }
        }

        protected /* synthetic */ void zzi(Object obj) {
            zza((com.google.android.gms.location.zzf.zza) obj);
        }

        protected void zzjf() {
        }
    }

    private static final class zzb extends com.google.android.gms.internal.zzpb.zza {
        private com.google.android.gms.location.zzf.zza zzanb;
        private com.google.android.gms.location.zzf.zzb zzanc;
        private zzpf zzand;

        public zzb(com.google.android.gms.location.zzf.zza com_google_android_gms_location_zzf_zza, zzpf com_google_android_gms_internal_zzpf) {
            this.zzanb = com_google_android_gms_location_zzf_zza;
            this.zzanc = null;
            this.zzand = com_google_android_gms_internal_zzpf;
        }

        public zzb(com.google.android.gms.location.zzf.zzb com_google_android_gms_location_zzf_zzb, zzpf com_google_android_gms_internal_zzpf) {
            this.zzanc = com_google_android_gms_location_zzf_zzb;
            this.zzanb = null;
            this.zzand = com_google_android_gms_internal_zzpf;
        }

        public void zza(int i, String[] strArr) {
            if (this.zzand == null) {
                Log.wtf("LocationClientImpl", "onAddGeofenceResult called multiple times");
                return;
            }
            zzpf com_google_android_gms_internal_zzpf = this.zzand;
            zzpf com_google_android_gms_internal_zzpf2 = this.zzand;
            com_google_android_gms_internal_zzpf2.getClass();
            com_google_android_gms_internal_zzpf.zza(new zza(com_google_android_gms_internal_zzpf2, this.zzanb, i, strArr));
            this.zzand = null;
            this.zzanb = null;
            this.zzanc = null;
        }

        public void zzb(int i, PendingIntent pendingIntent) {
            if (this.zzand == null) {
                Log.wtf("LocationClientImpl", "onRemoveGeofencesByPendingIntentResult called multiple times");
                return;
            }
            zzpf com_google_android_gms_internal_zzpf = this.zzand;
            zzpf com_google_android_gms_internal_zzpf2 = this.zzand;
            com_google_android_gms_internal_zzpf2.getClass();
            com_google_android_gms_internal_zzpf.zza(new zzc(com_google_android_gms_internal_zzpf2, 1, this.zzanc, i, pendingIntent));
            this.zzand = null;
            this.zzanb = null;
            this.zzanc = null;
        }

        public void zzb(int i, String[] strArr) {
            if (this.zzand == null) {
                Log.wtf("LocationClientImpl", "onRemoveGeofencesByRequestIdsResult called multiple times");
                return;
            }
            zzpf com_google_android_gms_internal_zzpf = this.zzand;
            zzpf com_google_android_gms_internal_zzpf2 = this.zzand;
            com_google_android_gms_internal_zzpf2.getClass();
            com_google_android_gms_internal_zzpf.zza(new zzc(com_google_android_gms_internal_zzpf2, 2, this.zzanc, i, strArr));
            this.zzand = null;
            this.zzanb = null;
            this.zzanc = null;
        }
    }

    private final class zzc extends zzc<com.google.android.gms.location.zzf.zzb> {
        private final PendingIntent mPendingIntent;
        private final int zzLs;
        private final String[] zzamZ;
        final /* synthetic */ zzpf zzana;
        private final int zzane;

        public zzc(zzpf com_google_android_gms_internal_zzpf, int i, com.google.android.gms.location.zzf.zzb com_google_android_gms_location_zzf_zzb, int i2, PendingIntent pendingIntent) {
            boolean z = true;
            this.zzana = com_google_android_gms_internal_zzpf;
            super(com_google_android_gms_internal_zzpf, com_google_android_gms_location_zzf_zzb);
            if (i != 1) {
                z = false;
            }
            com.google.android.gms.common.internal.zzb.zzN(z);
            this.zzane = i;
            this.zzLs = LocationStatusCodes.zzfq(i2);
            this.mPendingIntent = pendingIntent;
            this.zzamZ = null;
        }

        public zzc(zzpf com_google_android_gms_internal_zzpf, int i, com.google.android.gms.location.zzf.zzb com_google_android_gms_location_zzf_zzb, int i2, String[] strArr) {
            this.zzana = com_google_android_gms_internal_zzpf;
            super(com_google_android_gms_internal_zzpf, com_google_android_gms_location_zzf_zzb);
            com.google.android.gms.common.internal.zzb.zzN(i == 2);
            this.zzane = i;
            this.zzLs = LocationStatusCodes.zzfq(i2);
            this.zzamZ = strArr;
            this.mPendingIntent = null;
        }

        protected void zza(com.google.android.gms.location.zzf.zzb com_google_android_gms_location_zzf_zzb) {
            if (com_google_android_gms_location_zzf_zzb != null) {
                switch (this.zzane) {
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        com_google_android_gms_location_zzf_zzb.zzb(this.zzLs, this.mPendingIntent);
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        com_google_android_gms_location_zzf_zzb.zzb(this.zzLs, this.zzamZ);
                    default:
                        Log.wtf("LocationClientImpl", "Unsupported action: " + this.zzane);
                }
            }
        }

        protected /* synthetic */ void zzi(Object obj) {
            zza((com.google.android.gms.location.zzf.zzb) obj);
        }

        protected void zzjf() {
        }
    }

    private static final class zzd extends com.google.android.gms.internal.zzpd.zza {
        private com.google.android.gms.common.api.zza.zzb<LocationSettingsResult> zzanf;

        public zzd(com.google.android.gms.common.api.zza.zzb<LocationSettingsResult> com_google_android_gms_common_api_zza_zzb_com_google_android_gms_location_LocationSettingsResult) {
            zzx.zzb(com_google_android_gms_common_api_zza_zzb_com_google_android_gms_location_LocationSettingsResult != null, (Object) "listener can't be null.");
            this.zzanf = com_google_android_gms_common_api_zza_zzb_com_google_android_gms_location_LocationSettingsResult;
        }

        public void zza(LocationSettingsResult locationSettingsResult) {
            this.zzanf.zzd(locationSettingsResult);
            this.zzanf = null;
        }
    }

    public zzpf(Context context, Looper looper, String str, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str2) {
        this(context, looper, str, connectionCallbacks, onConnectionFailedListener, str2, null);
    }

    public zzpf(Context context, Looper looper, String str, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str2, String str3) {
        this(context, looper, str, connectionCallbacks, onConnectionFailedListener, str2, str3, null);
    }

    public zzpf(Context context, Looper looper, String str, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, String str2, String str3, String str4) {
        super(context, looper, connectionCallbacks, onConnectionFailedListener, str2);
        this.zzamX = new zzpe(context, this.zzamz);
        this.zzamY = zzot.zza(context, str3, str4, this.zzamz);
    }

    public void disconnect() {
        synchronized (this.zzamX) {
            if (isConnected()) {
                try {
                    this.zzamX.removeAllListeners();
                    this.zzamX.zzpy();
                } catch (Throwable e) {
                    Log.e("LocationClientImpl", "Client disconnected before listeners could be cleaned up", e);
                }
            }
            super.disconnect();
        }
    }

    public void zzW(boolean z) {
        this.zzamX.zzW(z);
    }

    public void zza(long j, PendingIntent pendingIntent) {
        zzfc();
        zzx.zzl(pendingIntent);
        zzx.zzb(j >= 0, (Object) "detectionIntervalMillis must be >= 0");
        ((zzpc) zzjb()).zza(j, true, pendingIntent);
    }

    public void zza(PendingIntent pendingIntent) {
        zzfc();
        zzx.zzl(pendingIntent);
        ((zzpc) zzjb()).zza(pendingIntent);
    }

    public void zza(PendingIntent pendingIntent, com.google.android.gms.location.zzf.zzb com_google_android_gms_location_zzf_zzb) {
        zzpb com_google_android_gms_internal_zzpb;
        zzfc();
        zzx.zzb((Object) pendingIntent, (Object) "PendingIntent must be specified.");
        zzx.zzb((Object) com_google_android_gms_location_zzf_zzb, (Object) "OnRemoveGeofencesResultListener not provided.");
        if (com_google_android_gms_location_zzf_zzb == null) {
            com_google_android_gms_internal_zzpb = null;
        } else {
            Object com_google_android_gms_internal_zzpf_zzb = new zzb(com_google_android_gms_location_zzf_zzb, this);
        }
        ((zzpc) zzjb()).zza(pendingIntent, com_google_android_gms_internal_zzpb, getContext().getPackageName());
    }

    public void zza(GeofencingRequest geofencingRequest, PendingIntent pendingIntent, com.google.android.gms.location.zzf.zza com_google_android_gms_location_zzf_zza) {
        zzpb com_google_android_gms_internal_zzpb;
        zzfc();
        zzx.zzb((Object) geofencingRequest, (Object) "geofencingRequest can't be null.");
        zzx.zzb((Object) pendingIntent, (Object) "PendingIntent must be specified.");
        zzx.zzb((Object) com_google_android_gms_location_zzf_zza, (Object) "OnAddGeofencesResultListener not provided.");
        if (com_google_android_gms_location_zzf_zza == null) {
            com_google_android_gms_internal_zzpb = null;
        } else {
            Object com_google_android_gms_internal_zzpf_zzb = new zzb(com_google_android_gms_location_zzf_zza, this);
        }
        ((zzpc) zzjb()).zza(geofencingRequest, pendingIntent, com_google_android_gms_internal_zzpb);
    }

    public void zza(LocationListener locationListener) {
        this.zzamX.zza(locationListener);
    }

    public void zza(LocationRequest locationRequest, LocationListener locationListener, Looper looper) {
        synchronized (this.zzamX) {
            this.zzamX.zza(locationRequest, locationListener, looper);
        }
    }

    public void zza(LocationSettingsRequest locationSettingsRequest, com.google.android.gms.common.api.zza.zzb<LocationSettingsResult> com_google_android_gms_common_api_zza_zzb_com_google_android_gms_location_LocationSettingsResult, String str) {
        boolean z = true;
        zzfc();
        zzx.zzb(locationSettingsRequest != null, (Object) "locationSettingsRequest can't be null nor empty.");
        if (com_google_android_gms_common_api_zza_zzb_com_google_android_gms_location_LocationSettingsResult == null) {
            z = false;
        }
        zzx.zzb(z, (Object) "listener can't be null.");
        ((zzpc) zzjb()).zza(locationSettingsRequest, new zzd(com_google_android_gms_common_api_zza_zzb_com_google_android_gms_location_LocationSettingsResult), str);
    }

    public void zza(List<String> list, com.google.android.gms.location.zzf.zzb com_google_android_gms_location_zzf_zzb) {
        zzpb com_google_android_gms_internal_zzpb;
        zzfc();
        boolean z = list != null && list.size() > 0;
        zzx.zzb(z, (Object) "geofenceRequestIds can't be null nor empty.");
        zzx.zzb((Object) com_google_android_gms_location_zzf_zzb, (Object) "OnRemoveGeofencesResultListener not provided.");
        String[] strArr = (String[]) list.toArray(new String[0]);
        if (com_google_android_gms_location_zzf_zzb == null) {
            com_google_android_gms_internal_zzpb = null;
        } else {
            Object com_google_android_gms_internal_zzpf_zzb = new zzb(com_google_android_gms_location_zzf_zzb, this);
        }
        ((zzpc) zzjb()).zza(strArr, com_google_android_gms_internal_zzpb, getContext().getPackageName());
    }

    public void zzb(Location location) {
        this.zzamX.zzb(location);
    }

    public void zzb(LocationRequest locationRequest, PendingIntent pendingIntent) {
        this.zzamX.zzb(locationRequest, pendingIntent);
    }

    public void zzd(PendingIntent pendingIntent) {
        this.zzamX.zzd(pendingIntent);
    }

    public Location zzpx() {
        return this.zzamX.zzpx();
    }
}
