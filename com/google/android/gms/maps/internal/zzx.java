package com.google.android.gms.maps.internal;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.dynamic.zze;
import com.google.android.gms.maps.internal.zzc.zza;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import org.bouncycastle.math.ec.ECCurve;

public class zzx {
    private static Context zzaqM;
    private static zzc zzaqN;

    private static Context getRemoteContext(Context context) {
        if (zzaqM == null) {
            if (zzqC()) {
                zzaqM = context.getApplicationContext();
            } else {
                zzaqM = GooglePlayServicesUtil.getRemoteContext(context);
            }
        }
        return zzaqM;
    }

    private static <T> T zza(ClassLoader classLoader, String str) {
        try {
            return zzc(((ClassLoader) com.google.android.gms.common.internal.zzx.zzl(classLoader)).loadClass(str));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Unable to find dynamic class " + str);
        }
    }

    public static zzc zzac(Context context) {
        com.google.android.gms.common.internal.zzx.zzl(context);
        if (zzaqN != null) {
            return zzaqN;
        }
        zzad(context);
        zzaqN = zzae(context);
        try {
            zzaqN.zzb(zze.zzn(getRemoteContext(context).getResources()), GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE);
            return zzaqN;
        } catch (RemoteException e) {
            throw new RuntimeRemoteException(e);
        }
    }

    private static void zzad(Context context) {
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        switch (isGooglePlayServicesAvailable) {
            case ECCurve.COORD_AFFINE /*0*/:
            default:
                throw new GooglePlayServicesNotAvailableException(isGooglePlayServicesAvailable);
        }
    }

    private static zzc zzae(Context context) {
        if (zzqC()) {
            Log.i(zzx.class.getSimpleName(), "Making Creator statically");
            return (zzc) zzc(zzqD());
        }
        Log.i(zzx.class.getSimpleName(), "Making Creator dynamically");
        return zza.zzbt((IBinder) zza(getRemoteContext(context).getClassLoader(), "com.google.android.gms.maps.internal.CreatorImpl"));
    }

    private static <T> T zzc(Class<?> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException("Unable to instantiate the dynamic class " + cls.getName());
        } catch (IllegalAccessException e2) {
            throw new IllegalStateException("Unable to call the default constructor of " + cls.getName());
        }
    }

    public static boolean zzqC() {
        return false;
    }

    private static Class<?> zzqD() {
        try {
            return VERSION.SDK_INT < 15 ? Class.forName("com.google.android.gms.maps.internal.CreatorImplGmm6") : Class.forName("com.google.android.gms.maps.internal.CreatorImpl");
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
