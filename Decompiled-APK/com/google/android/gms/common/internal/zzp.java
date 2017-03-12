package com.google.android.gms.common.internal;

import android.content.Context;
import android.util.Log;
import com.google.android.gms.internal.zztc;

public final class zzp {
    private final String zzQq;

    public zzp(String str) {
        this.zzQq = (String) zzx.zzl(str);
    }

    public void zza(Context context, String str, String str2, Throwable th) {
        StackTraceElement[] stackTrace = th.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        while (i < stackTrace.length && i < 2) {
            stringBuilder.append(stackTrace[i].toString());
            stringBuilder.append("\n");
            i++;
        }
        zztc com_google_android_gms_internal_zztc = new zztc(context, 10);
        com_google_android_gms_internal_zztc.zza("GMS_WTF", null, "GMS_WTF", stringBuilder.toString());
        com_google_android_gms_internal_zztc.send();
        if (zzaK(7)) {
            Log.e(str, str2, th);
            Log.wtf(str, str2, th);
        }
    }

    public void zza(String str, String str2, Throwable th) {
        if (zzaK(4)) {
            Log.i(str, str2, th);
        }
    }

    public boolean zzaK(int i) {
        return Log.isLoggable(this.zzQq, i);
    }

    public void zzb(String str, String str2, Throwable th) {
        if (zzaK(5)) {
            Log.w(str, str2, th);
        }
    }

    public void zzc(String str, String str2, Throwable th) {
        if (zzaK(6)) {
            Log.e(str, str2, th);
        }
    }

    public void zzq(String str, String str2) {
        if (zzaK(3)) {
            Log.d(str, str2);
        }
    }

    public void zzr(String str, String str2) {
        if (zzaK(5)) {
            Log.w(str, str2);
        }
    }

    public void zzs(String str, String str2) {
        if (zzaK(6)) {
            Log.e(str, str2);
        }
    }
}
