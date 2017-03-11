package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;
import com.google.android.gms.internal.zztd.zza;

public class zztc implements zza {
    private final zztd zzatI;
    private boolean zzatJ;

    public zztc(Context context, int i) {
        this(context, i, null);
    }

    public zztc(Context context, int i, String str) {
        this(context, i, str, null, true);
    }

    public zztc(Context context, int i, String str, String str2, boolean z) {
        this.zzatI = new zztd(context, i, str, str2, this, z, context != context.getApplicationContext() ? context.getClass().getName() : "OneTimePlayLogger");
        this.zzatJ = true;
    }

    private void zzrD() {
        if (!this.zzatJ) {
            throw new IllegalStateException("Cannot reuse one-time logger after sending.");
        }
    }

    public void send() {
        zzrD();
        this.zzatI.start();
        this.zzatJ = false;
    }

    public void zza(String str, byte[] bArr, String... strArr) {
        zzrD();
        this.zzatI.zzb(str, bArr, strArr);
    }

    public void zzf(PendingIntent pendingIntent) {
        Log.w("OneTimePlayLogger", "logger connection failed: " + pendingIntent);
    }

    public void zzrE() {
        this.zzatI.stop();
    }

    public void zzrF() {
        Log.w("OneTimePlayLogger", "logger connection failed");
    }
}
