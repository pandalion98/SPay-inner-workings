package com.google.android.gms.internal;

import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import com.google.android.gms.common.internal.zzf;

public class zztd {
    private final zztl zzatK;
    private zztj zzatL;

    public interface zza {
        void zzf(PendingIntent pendingIntent);

        void zzrE();

        void zzrF();
    }

    public zztd(Context context, int i, String str, String str2, zza com_google_android_gms_internal_zztd_zza, boolean z, String str3) {
        String packageName = context.getPackageName();
        int i2 = 0;
        try {
            i2 = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            Log.wtf("PlayLogger", "This can't happen.");
        }
        this.zzatL = new zztj(packageName, i2, i, str, str2, z);
        this.zzatK = new zztl(context, context.getMainLooper(), new zzti(com_google_android_gms_internal_zztd_zza), new zzf(null, null, 49, null, packageName, str3, null));
    }

    public void start() {
        this.zzatK.start();
    }

    public void stop() {
        this.zzatK.stop();
    }

    public void zza(long j, String str, byte[] bArr, String... strArr) {
        this.zzatK.zzb(this.zzatL, new zztf(j, str, bArr, strArr));
    }

    public void zzb(String str, byte[] bArr, String... strArr) {
        zza(System.currentTimeMillis(), str, bArr, strArr);
    }
}
