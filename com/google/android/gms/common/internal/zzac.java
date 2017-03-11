package com.google.android.gms.common.internal;

import android.content.Context;
import android.os.IBinder;
import android.view.View;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.dynamic.zzg;
import com.google.android.gms.dynamic.zzg.zza;

public final class zzac extends zzg<zzu> {
    private static final zzac zzQu;

    static {
        zzQu = new zzac();
    }

    private zzac() {
        super("com.google.android.gms.common.ui.SignInButtonCreatorImpl");
    }

    public static View zzb(Context context, int i, int i2) {
        return zzQu.zzc(context, i, i2);
    }

    private View zzc(Context context, int i, int i2) {
        try {
            return (View) zze.zzf(((zzu) zzS(context)).zza(zze.zzn(context), i, i2));
        } catch (Throwable e) {
            throw new zza("Could not get button with size " + i + " and color " + i2, e);
        }
    }

    public zzu zzX(IBinder iBinder) {
        return zzu.zza.zzW(iBinder);
    }

    public /* synthetic */ Object zzd(IBinder iBinder) {
        return zzX(iBinder);
    }
}
