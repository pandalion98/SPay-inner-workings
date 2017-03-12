package com.google.android.gms.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Parcelable;
import android.os.Process;
import android.util.Log;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.internal.zze;
import com.google.android.gms.internal.zzlp.zza;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;

public class zzlo {
    private static final Object zzQc;
    private static zzlo zzRr;
    private static final ComponentName zzRw;
    private final List<String> zzRs;
    private final List<String> zzRt;
    private final List<String> zzRu;
    private final List<String> zzRv;
    private zzlr zzRx;

    static {
        zzQc = new Object();
        zzRw = new ComponentName(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE, "com.google.android.gms.common.stats.GmsCoreStatsService");
    }

    private zzlo() {
        if (getLogLevel() == zzlq.zzRF) {
            this.zzRs = Collections.EMPTY_LIST;
            this.zzRt = Collections.EMPTY_LIST;
            this.zzRu = Collections.EMPTY_LIST;
            this.zzRv = Collections.EMPTY_LIST;
            return;
        }
        String str = (String) zza.zzRA.get();
        this.zzRs = str == null ? Collections.EMPTY_LIST : Arrays.asList(str.split(","));
        str = (String) zza.zzRB.get();
        this.zzRt = str == null ? Collections.EMPTY_LIST : Arrays.asList(str.split(","));
        str = (String) zza.zzRC.get();
        this.zzRu = str == null ? Collections.EMPTY_LIST : Arrays.asList(str.split(","));
        str = (String) zza.zzRD.get();
        this.zzRv = str == null ? Collections.EMPTY_LIST : Arrays.asList(str.split(","));
        this.zzRx = new zzlr(SkeinMac.SKEIN_1024, ((Long) zza.zzRE.get()).longValue());
    }

    private int getLogLevel() {
        try {
            return (zze.zzPq && zzki.isInitialized() && zzki.zzis() == Process.myUid()) ? ((Integer) zza.zzRz.get()).intValue() : zzlq.zzRF;
        } catch (SecurityException e) {
            return zzlq.zzRF;
        }
    }

    private void zza(Context context, ServiceConnection serviceConnection, String str, Intent intent, String str2) {
        if (zze.zzPq) {
            long zzb = zzb(serviceConnection);
            if (zza(context, str, intent, zzb, str2)) {
                Parcelable com_google_android_gms_internal_zzlm;
                long currentTimeMillis = System.currentTimeMillis();
                String zzl = zzmf.zzl(3, 5);
                if (!str2.equals("UNBIND")) {
                    if (!str2.equals("DISCONNECT")) {
                        ServiceInfo zzb2 = zzb(context, intent);
                        String str3 = str2;
                        String str4 = str;
                        com_google_android_gms_internal_zzlm = new zzlm(currentTimeMillis, str3, zzmf.zzR(context), str4, zzb2.processName, zzb2.name, zzl, zzb);
                        context.startService(new Intent().setComponent(zzRw).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", com_google_android_gms_internal_zzlm));
                    }
                }
                com_google_android_gms_internal_zzlm = new zzlm(currentTimeMillis, str2, null, null, null, null, zzl, zzb);
                context.startService(new Intent().setComponent(zzRw).putExtra("com.google.android.gms.common.stats.EXTRA_LOG_EVENT", com_google_android_gms_internal_zzlm));
            }
        }
    }

    private boolean zza(Context context, String str, Intent intent, long j, String str2) {
        int logLevel = getLogLevel();
        if (logLevel == zzlq.zzRF || this.zzRx == null) {
            return false;
        }
        if (str2 == "DISCONNECT" || str2 == "UNBIND") {
            return this.zzRx.zzr(j);
        } else {
            ServiceInfo zzb = zzb(context, intent);
            if (zzb == null) {
                return false;
            }
            String zzR = zzmf.zzR(context);
            String str3 = zzb.processName;
            String str4 = zzb.name;
            if (this.zzRs.contains(zzR) || this.zzRt.contains(str) || this.zzRu.contains(str3) || this.zzRv.contains(str4)) {
                return false;
            }
            if (str3.equals(zzR) && (logLevel & zzlq.zzRJ) != 0) {
                return false;
            }
            this.zzRx.zza(Long.valueOf(j));
            return true;
        }
    }

    private long zzb(ServiceConnection serviceConnection) {
        return (((long) Process.myPid()) << 32) | ((long) System.identityHashCode(serviceConnection));
    }

    private static ServiceInfo zzb(Context context, Intent intent) {
        List queryIntentServices = context.getPackageManager().queryIntentServices(intent, X509KeyUsage.digitalSignature);
        if (queryIntentServices == null || queryIntentServices.size() == 0) {
            Log.w("ConnectionTracker", "There are no handler of this intent: " + intent.toUri(0));
            return null;
        }
        if (queryIntentServices.size() > 1) {
            Log.w("ConnectionTracker", "There are multiple handlers for this intent: " + intent.toUri(0));
            Iterator it = queryIntentServices.iterator();
            if (it.hasNext()) {
                Log.w("ConnectionTracker", ((ResolveInfo) it.next()).serviceInfo.name);
                return null;
            }
        }
        return ((ResolveInfo) queryIntentServices.get(0)).serviceInfo;
    }

    public static zzlo zzka() {
        synchronized (zzQc) {
            if (zzRr == null) {
                zzRr = new zzlo();
            }
        }
        return zzRr;
    }

    public void zza(Context context, ServiceConnection serviceConnection) {
        zza(context, serviceConnection, null, null, "UNBIND");
        context.unbindService(serviceConnection);
    }

    public void zza(Context context, ServiceConnection serviceConnection, String str, Intent intent) {
        zza(context, serviceConnection, str, intent, "CONNECT");
    }

    public boolean zza(Context context, Intent intent, ServiceConnection serviceConnection, int i) {
        return zza(context, context.getClass().getName(), intent, serviceConnection, i);
    }

    public boolean zza(Context context, String str, Intent intent, ServiceConnection serviceConnection, int i) {
        zza(context, serviceConnection, str, intent, "BIND");
        return context.bindService(intent, serviceConnection, i);
    }

    public void zzb(Context context, ServiceConnection serviceConnection) {
        zza(context, serviceConnection, null, null, "DISCONNECT");
    }
}
