package com.google.android.gms.common.api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.common.internal.zzx;

public final class zze<L> {
    private volatile L mListener;
    private final zza zzNk;

    public interface zzb<L> {
        void zze(L l);

        void zzhX();
    }

    private final class zza extends Handler {
        final /* synthetic */ zze zzNl;

        public zza(zze com_google_android_gms_common_api_zze, Looper looper) {
            this.zzNl = com_google_android_gms_common_api_zze;
            super(looper);
        }

        public void handleMessage(Message message) {
            boolean z = true;
            if (message.what != 1) {
                z = false;
            }
            zzx.zzO(z);
            this.zzNl.zzb((zzb) message.obj);
        }
    }

    zze(Looper looper, L l) {
        this.zzNk = new zza(this, looper);
        this.mListener = zzx.zzb((Object) l, (Object) "Listener must not be null");
    }

    public void clear() {
        this.mListener = null;
    }

    public void zza(zzb<? super L> com_google_android_gms_common_api_zze_zzb__super_L) {
        zzx.zzb((Object) com_google_android_gms_common_api_zze_zzb__super_L, (Object) "Notifier must not be null");
        this.zzNk.sendMessage(this.zzNk.obtainMessage(1, com_google_android_gms_common_api_zze_zzb__super_L));
    }

    void zzb(zzb<? super L> com_google_android_gms_common_api_zze_zzb__super_L) {
        Object obj = this.mListener;
        if (obj == null) {
            com_google_android_gms_common_api_zze_zzb__super_L.zzhX();
            return;
        }
        try {
            com_google_android_gms_common_api_zze_zzb__super_L.zze(obj);
        } catch (RuntimeException e) {
            com_google_android_gms_common_api_zze_zzb__super_L.zzhX();
            throw e;
        }
    }
}
