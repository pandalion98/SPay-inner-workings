package com.google.android.gms.security;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zzx;
import java.lang.reflect.Method;

public class ProviderInstaller {
    public static final String PROVIDER_NAME = "GmsCore_OpenSSL";
    private static Method zzawM;
    private static final Object zzmz;

    /* renamed from: com.google.android.gms.security.ProviderInstaller.1 */
    static class C02651 extends AsyncTask<Void, Void, Integer> {
        final /* synthetic */ ProviderInstallListener zzawN;
        final /* synthetic */ Context zznK;

        C02651(Context context, ProviderInstallListener providerInstallListener) {
            this.zznK = context;
            this.zzawN = providerInstallListener;
        }

        protected /* synthetic */ Object doInBackground(Object[] objArr) {
            return zzb((Void[]) objArr);
        }

        protected /* synthetic */ void onPostExecute(Object obj) {
            zze((Integer) obj);
        }

        protected Integer zzb(Void... voidArr) {
            try {
                ProviderInstaller.installIfNeeded(this.zznK);
                return Integer.valueOf(0);
            } catch (GooglePlayServicesRepairableException e) {
                return Integer.valueOf(e.getConnectionStatusCode());
            } catch (GooglePlayServicesNotAvailableException e2) {
                return Integer.valueOf(e2.errorCode);
            }
        }

        protected void zze(Integer num) {
            if (num.intValue() == 0) {
                this.zzawN.onProviderInstalled();
                return;
            }
            this.zzawN.onProviderInstallFailed(num.intValue(), GooglePlayServicesUtil.zzan(num.intValue()));
        }
    }

    public interface ProviderInstallListener {
        void onProviderInstallFailed(int i, Intent intent);

        void onProviderInstalled();
    }

    static {
        zzmz = new Object();
        zzawM = null;
    }

    public static void installIfNeeded(Context context) {
        zzx.zzb((Object) context, (Object) "Context must not be null");
        GooglePlayServicesUtil.zzI(context);
        Context remoteContext = GooglePlayServicesUtil.getRemoteContext(context);
        if (remoteContext == null) {
            Log.e("ProviderInstaller", "Failed to get remote context");
            throw new GooglePlayServicesNotAvailableException(8);
        }
        synchronized (zzmz) {
            try {
                if (zzawM == null) {
                    zzai(remoteContext);
                }
                zzawM.invoke(null, new Object[]{remoteContext});
            } catch (Exception e) {
                Log.e("ProviderInstaller", "Failed to install provider: " + e.getMessage());
                throw new GooglePlayServicesNotAvailableException(8);
            }
        }
    }

    public static void installIfNeededAsync(Context context, ProviderInstallListener providerInstallListener) {
        zzx.zzb((Object) context, (Object) "Context must not be null");
        zzx.zzb((Object) providerInstallListener, (Object) "Listener must not be null");
        zzx.zzbd("Must be called on the UI thread");
        new C02651(context, providerInstallListener).execute(new Void[0]);
    }

    private static void zzai(Context context) {
        zzawM = context.getClassLoader().loadClass("com.google.android.gms.common.security.ProviderInstallerImpl").getMethod("insertProvider", new Class[]{Context.class});
    }
}
