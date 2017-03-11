package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.ContentProviderClient;
import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.zzh;
import java.util.HashMap;
import java.util.Map;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class zzpe {
    private final Context mContext;
    private Map<LocationListener, zzb> zzZH;
    private ContentProviderClient zzamS;
    private boolean zzamT;
    private Map<Object, zzc> zzamU;
    private final zzpm<zzpc> zzamz;

    private static class zza extends Handler {
        private final LocationListener zzamV;

        public zza(LocationListener locationListener) {
            this.zzamV = locationListener;
        }

        public zza(LocationListener locationListener, Looper looper) {
            super(looper);
            this.zzamV = locationListener;
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    this.zzamV.onLocationChanged(new Location((Location) message.obj));
                default:
                    Log.e("LocationClientHelper", "unknown message in LocationHandler.handleMessage");
            }
        }
    }

    private static class zzb extends com.google.android.gms.location.zzd.zza {
        private Handler zzamW;

        zzb(LocationListener locationListener, Looper looper) {
            this.zzamW = looper == null ? new zza(locationListener) : new zza(locationListener, looper);
        }

        public void onLocationChanged(Location location) {
            if (this.zzamW == null) {
                Log.e("LocationClientHelper", "Received a location in client after calling removeLocationUpdates.");
                return;
            }
            Message obtain = Message.obtain();
            obtain.what = 1;
            obtain.obj = location;
            this.zzamW.sendMessage(obtain);
        }

        public void release() {
            this.zzamW = null;
        }
    }

    private static class zzc extends com.google.android.gms.location.zze.zza {
        private Handler zzamW;

        public void zza(zzh com_google_android_gms_location_zzh) {
            if (this.zzamW == null) {
                Log.e("LocationClientHelper", "Received a location in client after calling removeLocationUpdates.");
                return;
            }
            Message obtain = Message.obtain();
            obtain.obj = com_google_android_gms_location_zzh;
            this.zzamW.sendMessage(obtain);
        }
    }

    public zzpe(Context context, zzpm<zzpc> com_google_android_gms_internal_zzpm_com_google_android_gms_internal_zzpc) {
        this.zzamS = null;
        this.zzamT = false;
        this.zzZH = new HashMap();
        this.zzamU = new HashMap();
        this.mContext = context;
        this.zzamz = com_google_android_gms_internal_zzpm_com_google_android_gms_internal_zzpc;
    }

    private zzb zza(LocationListener locationListener, Looper looper) {
        zzb com_google_android_gms_internal_zzpe_zzb;
        if (looper == null) {
            zzx.zzb(Looper.myLooper(), (Object) "Can't create handler inside thread that has not called Looper.prepare()");
        }
        synchronized (this.zzZH) {
            com_google_android_gms_internal_zzpe_zzb = (zzb) this.zzZH.get(locationListener);
            if (com_google_android_gms_internal_zzpe_zzb == null) {
                com_google_android_gms_internal_zzpe_zzb = new zzb(locationListener, looper);
            }
            this.zzZH.put(locationListener, com_google_android_gms_internal_zzpe_zzb);
        }
        return com_google_android_gms_internal_zzpe_zzb;
    }

    public void removeAllListeners() {
        try {
            synchronized (this.zzZH) {
                for (zzb com_google_android_gms_internal_zzpe_zzb : this.zzZH.values()) {
                    if (com_google_android_gms_internal_zzpe_zzb != null) {
                        ((zzpc) this.zzamz.zzjb()).zza(zzpi.zzb(com_google_android_gms_internal_zzpe_zzb));
                    }
                }
                this.zzZH.clear();
                for (zzc com_google_android_gms_internal_zzpe_zzc : this.zzamU.values()) {
                    if (com_google_android_gms_internal_zzpe_zzc != null) {
                        ((zzpc) this.zzamz.zzjb()).zza(zzpi.zza(com_google_android_gms_internal_zzpe_zzc));
                    }
                }
                this.zzamU.clear();
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void zzW(boolean z) {
        this.zzamz.zzfc();
        ((zzpc) this.zzamz.zzjb()).zzW(z);
        this.zzamT = z;
    }

    public void zza(LocationListener locationListener) {
        this.zzamz.zzfc();
        zzx.zzb((Object) locationListener, (Object) "Invalid null listener");
        synchronized (this.zzZH) {
            zzb com_google_android_gms_internal_zzpe_zzb = (zzb) this.zzZH.remove(locationListener);
            if (this.zzamS != null && this.zzZH.isEmpty()) {
                this.zzamS.release();
                this.zzamS = null;
            }
            if (com_google_android_gms_internal_zzpe_zzb != null) {
                com_google_android_gms_internal_zzpe_zzb.release();
                ((zzpc) this.zzamz.zzjb()).zza(zzpi.zzb(com_google_android_gms_internal_zzpe_zzb));
            }
        }
    }

    public void zza(LocationRequest locationRequest, LocationListener locationListener, Looper looper) {
        this.zzamz.zzfc();
        ((zzpc) this.zzamz.zzjb()).zza(zzpi.zzb(zzpg.zzb(locationRequest), zza(locationListener, looper)));
    }

    public void zzb(Location location) {
        this.zzamz.zzfc();
        ((zzpc) this.zzamz.zzjb()).zzb(location);
    }

    public void zzb(LocationRequest locationRequest, PendingIntent pendingIntent) {
        this.zzamz.zzfc();
        ((zzpc) this.zzamz.zzjb()).zza(zzpi.zzb(zzpg.zzb(locationRequest), pendingIntent));
    }

    public void zzd(PendingIntent pendingIntent) {
        this.zzamz.zzfc();
        ((zzpc) this.zzamz.zzjb()).zza(zzpi.zze(pendingIntent));
    }

    public Location zzpx() {
        this.zzamz.zzfc();
        try {
            return ((zzpc) this.zzamz.zzjb()).zzcj(this.mContext.getPackageName());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public void zzpy() {
        if (this.zzamT) {
            try {
                zzW(false);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
