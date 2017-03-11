package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.zzb;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzk;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zztg.zza;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class zztl extends zzk<zzte> {
    private final String zzFO;
    private final zzti zzauc;
    private final zztg zzaud;
    private boolean zzaue;
    private final Object zznh;

    public zztl(Context context, Looper looper, zzti com_google_android_gms_internal_zzti, zzf com_google_android_gms_common_internal_zzf) {
        super(context, looper, 24, com_google_android_gms_internal_zzti, com_google_android_gms_internal_zzti, com_google_android_gms_common_internal_zzf);
        this.zzFO = context.getPackageName();
        this.zzauc = (zzti) zzx.zzl(com_google_android_gms_internal_zzti);
        this.zzauc.zza(this);
        this.zzaud = new zztg();
        this.zznh = new Object();
        this.zzaue = true;
    }

    private void zzc(zztj com_google_android_gms_internal_zztj, zztf com_google_android_gms_internal_zztf) {
        this.zzaud.zza(com_google_android_gms_internal_zztj, com_google_android_gms_internal_zztf);
    }

    private void zzd(zztj com_google_android_gms_internal_zztj, zztf com_google_android_gms_internal_zztf) {
        try {
            zzrI();
            ((zzte) zzjb()).zza(this.zzFO, com_google_android_gms_internal_zztj, com_google_android_gms_internal_zztf);
        } catch (RemoteException e) {
            Log.e("PlayLoggerImpl", "Couldn't send log event.  Will try caching.");
            zzc(com_google_android_gms_internal_zztj, com_google_android_gms_internal_zztf);
        } catch (IllegalStateException e2) {
            Log.e("PlayLoggerImpl", "Service was disconnected.  Will try caching.");
            zzc(com_google_android_gms_internal_zztj, com_google_android_gms_internal_zztf);
        }
    }

    private void zzrI() {
        zzb.zzN(!this.zzaue);
        if (!this.zzaud.isEmpty()) {
            zztj com_google_android_gms_internal_zztj = null;
            try {
                List arrayList = new ArrayList();
                Iterator it = this.zzaud.zzrG().iterator();
                while (it.hasNext()) {
                    zza com_google_android_gms_internal_zztg_zza = (zza) it.next();
                    if (com_google_android_gms_internal_zztg_zza.zzatT != null) {
                        ((zzte) zzjb()).zza(this.zzFO, com_google_android_gms_internal_zztg_zza.zzatR, zzwy.zzf(com_google_android_gms_internal_zztg_zza.zzatT));
                    } else {
                        zztj com_google_android_gms_internal_zztj2;
                        if (com_google_android_gms_internal_zztg_zza.zzatR.equals(com_google_android_gms_internal_zztj)) {
                            arrayList.add(com_google_android_gms_internal_zztg_zza.zzatS);
                            com_google_android_gms_internal_zztj2 = com_google_android_gms_internal_zztj;
                        } else {
                            if (!arrayList.isEmpty()) {
                                ((zzte) zzjb()).zza(this.zzFO, com_google_android_gms_internal_zztj, arrayList);
                                arrayList.clear();
                            }
                            zztj com_google_android_gms_internal_zztj3 = com_google_android_gms_internal_zztg_zza.zzatR;
                            arrayList.add(com_google_android_gms_internal_zztg_zza.zzatS);
                            com_google_android_gms_internal_zztj2 = com_google_android_gms_internal_zztj3;
                        }
                        com_google_android_gms_internal_zztj = com_google_android_gms_internal_zztj2;
                    }
                }
                if (!arrayList.isEmpty()) {
                    ((zzte) zzjb()).zza(this.zzFO, com_google_android_gms_internal_zztj, arrayList);
                }
                this.zzaud.clear();
            } catch (RemoteException e) {
                Log.e("PlayLoggerImpl", "Couldn't send cached log events to AndroidLog service.  Retaining in memory cache.");
            }
        }
    }

    public void start() {
        synchronized (this.zznh) {
            if (isConnecting() || isConnected()) {
                return;
            }
            this.zzauc.zzad(true);
            connect();
        }
    }

    public void stop() {
        synchronized (this.zznh) {
            this.zzauc.zzad(false);
            disconnect();
        }
    }

    void zzae(boolean z) {
        synchronized (this.zznh) {
            boolean z2 = this.zzaue;
            this.zzaue = z;
            if (z2 && !this.zzaue) {
                zzrI();
            }
        }
    }

    public void zzb(zztj com_google_android_gms_internal_zztj, zztf com_google_android_gms_internal_zztf) {
        synchronized (this.zznh) {
            if (this.zzaue) {
                zzc(com_google_android_gms_internal_zztj, com_google_android_gms_internal_zztf);
            } else {
                zzd(com_google_android_gms_internal_zztj, com_google_android_gms_internal_zztf);
            }
        }
    }

    protected String zzcF() {
        return "com.google.android.gms.playlog.service.START";
    }

    protected String zzcG() {
        return "com.google.android.gms.playlog.internal.IPlayLogService";
    }

    protected zzte zzcv(IBinder iBinder) {
        return zzte.zza.zzcu(iBinder);
    }

    protected /* synthetic */ IInterface zzp(IBinder iBinder) {
        return zzcv(iBinder);
    }
}
