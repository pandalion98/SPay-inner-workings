package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;

public abstract class zzm {
    private static final Object zzQc;
    private static zzm zzQd;

    static {
        zzQc = new Object();
    }

    public static zzm zzP(Context context) {
        synchronized (zzQc) {
            if (zzQd == null) {
                zzQd = new zzn(context.getApplicationContext());
            }
        }
        return zzQd;
    }

    public abstract boolean zza(ComponentName componentName, ServiceConnection serviceConnection, String str);

    public abstract boolean zza(String str, ServiceConnection serviceConnection, String str2);

    public abstract void zzb(ComponentName componentName, ServiceConnection serviceConnection, String str);

    public abstract void zzb(String str, ServiceConnection serviceConnection, String str2);
}
