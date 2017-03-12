package com.google.android.gms.internal;

import android.os.SystemClock;

public final class zzlx implements zzlv {
    private static zzlx zzRO;

    public static synchronized zzlv zzkc() {
        zzlv com_google_android_gms_internal_zzlv;
        synchronized (zzlx.class) {
            if (zzRO == null) {
                zzRO = new zzlx();
            }
            com_google_android_gms_internal_zzlv = zzRO;
        }
        return com_google_android_gms_internal_zzlv;
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    public long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }
}
