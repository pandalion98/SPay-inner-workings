package com.google.android.gms.common.api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Api.ApiOptions;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.common.internal.zzl;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzko;
import com.google.android.gms.internal.zzmh;
import com.google.android.gms.internal.zzur;
import com.google.android.gms.internal.zzus;
import com.google.android.gms.internal.zzut;
import com.google.android.gms.internal.zzuv;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

final class zzd implements GoogleApiClient {
    private final Context mContext;
    private final Lock zzCN;
    private volatile boolean zzMA;
    private int zzMB;
    private boolean zzMC;
    private int zzMD;
    private long zzME;
    private long zzMF;
    private final zzc zzMG;
    BroadcastReceiver zzMH;
    private final Bundle zzMI;
    private final Map<com.google.android.gms.common.api.Api.zzc<?>, com.google.android.gms.common.api.Api.zza> zzMJ;
    private final Set<com.google.android.gms.common.api.Api.zzc<?>> zzMK;
    private final Map<com.google.android.gms.common.api.Api.zzc<?>, ConnectionResult> zzML;
    private final List<String> zzMM;
    private boolean zzMN;
    private zzur zzMO;
    private int zzMP;
    private boolean zzMQ;
    private boolean zzMR;
    private zzq zzMS;
    private boolean zzMT;
    private boolean zzMU;
    private final Set<zze<?>> zzMV;
    final Set<zzg<?>> zzMW;
    private final zze zzMX;
    private final ConnectionCallbacks zzMY;
    private final com.google.android.gms.common.api.GoogleApiClient.zza zzMZ;
    private final Looper zzMc;
    private final Condition zzMt;
    private final zzl zzMu;
    private final int zzMv;
    final Queue<zzg<?>> zzMw;
    private ConnectionResult zzMx;
    private int zzMy;
    private volatile int zzMz;
    private final com.google.android.gms.common.internal.zzl.zza zzNa;

    interface zzg<A extends com.google.android.gms.common.api.Api.zza> {
        void cancel();

        void zza(zze com_google_android_gms_common_api_zzd_zze);

        void zzb(A a);

        com.google.android.gms.common.api.Api.zzc<A> zzhV();

        int zzhW();

        void zzk(Status status);
    }

    private abstract class zzf implements ConnectionCallbacks {
        final /* synthetic */ zzd zzNb;

        private zzf(zzd com_google_android_gms_common_api_zzd) {
            this.zzNb = com_google_android_gms_common_api_zzd;
        }

        public void onConnectionSuspended(int i) {
            this.zzNb.zzCN.lock();
            switch (i) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    if (!this.zzNb.zzil()) {
                        this.zzNb.zzMA = true;
                        if (this.zzNb.zzMH == null) {
                            this.zzNb.zzMH = new zzd(this.zzNb);
                            IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
                            intentFilter.addDataScheme("package");
                            this.zzNb.mContext.getApplicationContext().registerReceiver(this.zzNb.zzMH, intentFilter);
                        }
                        this.zzNb.zzMG.sendMessageDelayed(this.zzNb.zzMG.obtainMessage(1), this.zzNb.zzME);
                        this.zzNb.zzMG.sendMessageDelayed(this.zzNb.zzMG.obtainMessage(2), this.zzNb.zzMF);
                        this.zzNb.zzao(i);
                        break;
                    }
                    this.zzNb.zzCN.unlock();
                    return;
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    try {
                        this.zzNb.zzao(i);
                        this.zzNb.connect();
                        break;
                    } catch (Throwable th) {
                        this.zzNb.zzCN.unlock();
                    }
            }
            this.zzNb.zzCN.unlock();
        }
    }

    /* renamed from: com.google.android.gms.common.api.zzd.12 */
    class AnonymousClass12 implements Runnable {
        final /* synthetic */ zzd zzNb;
        final /* synthetic */ zzaa zzNh;

        AnonymousClass12(zzd com_google_android_gms_common_api_zzd, zzaa com_google_android_gms_common_internal_zzaa) {
            this.zzNb = com_google_android_gms_common_api_zzd;
            this.zzNh = com_google_android_gms_common_internal_zzaa;
        }

        public void run() {
            this.zzNb.zzMS = this.zzNh.zzjn();
            this.zzNb.zzMR = true;
            this.zzNb.zzMT = this.zzNh.zzjp();
            this.zzNb.zzMU = this.zzNh.zzjq();
            this.zzNb.zzii();
        }
    }

    /* renamed from: com.google.android.gms.common.api.zzd.13 */
    class AnonymousClass13 implements Runnable {
        final /* synthetic */ zzd zzNb;
        final /* synthetic */ ConnectionResult zzNi;

        AnonymousClass13(zzd com_google_android_gms_common_api_zzd, ConnectionResult connectionResult) {
            this.zzNb = com_google_android_gms_common_api_zzd;
            this.zzNi = connectionResult;
        }

        public void run() {
            this.zzNb.zzCN.lock();
            try {
                if (this.zzNb.zzc(this.zzNi)) {
                    this.zzNb.zzMN = false;
                    for (com.google.android.gms.common.api.Api.zzc com_google_android_gms_common_api_Api_zzc : this.zzNb.zzMK) {
                        com.google.android.gms.common.api.Api.zza com_google_android_gms_common_api_Api_zza = (com.google.android.gms.common.api.Api.zza) this.zzNb.zzMJ.get(com_google_android_gms_common_api_Api_zzc);
                        if (com_google_android_gms_common_api_Api_zza.isConnected()) {
                            com_google_android_gms_common_api_Api_zza.disconnect();
                        }
                        if (!this.zzNb.zzML.containsKey(com_google_android_gms_common_api_Api_zzc)) {
                            this.zzNb.zzML.put(com_google_android_gms_common_api_Api_zzc, new ConnectionResult(17, null));
                        }
                    }
                    this.zzNb.zzMN = true;
                    this.zzNb.zzij();
                } else {
                    this.zzNb.zza(this.zzNi);
                }
                this.zzNb.zzCN.unlock();
            } catch (Throwable th) {
                this.zzNb.zzCN.unlock();
            }
        }
    }

    interface zze {
        void zzb(zzg<?> com_google_android_gms_common_api_zzd_zzg_);
    }

    /* renamed from: com.google.android.gms.common.api.zzd.1 */
    class C00801 implements zze {
        final /* synthetic */ zzd zzNb;

        C00801(zzd com_google_android_gms_common_api_zzd) {
            this.zzNb = com_google_android_gms_common_api_zzd;
        }

        public void zzb(zzg<?> com_google_android_gms_common_api_zzd_zzg_) {
            this.zzNb.zzMW.remove(com_google_android_gms_common_api_zzd_zzg_);
        }
    }

    /* renamed from: com.google.android.gms.common.api.zzd.2 */
    class C00812 implements Runnable {
        final /* synthetic */ zzd zzNb;

        C00812(zzd com_google_android_gms_common_api_zzd) {
            this.zzNb = com_google_android_gms_common_api_zzd;
        }

        public void run() {
            this.zzNb.zzCN.lock();
            try {
                this.zzNb.zzij();
            } finally {
                this.zzNb.zzCN.unlock();
            }
        }
    }

    /* renamed from: com.google.android.gms.common.api.zzd.3 */
    class C00823 implements ConnectionCallbacks {
        final /* synthetic */ zzd zzNb;
        final /* synthetic */ AtomicReference zzNc;
        final /* synthetic */ zzg zzNd;

        C00823(zzd com_google_android_gms_common_api_zzd, AtomicReference atomicReference, zzg com_google_android_gms_common_api_zzg) {
            this.zzNb = com_google_android_gms_common_api_zzd;
            this.zzNc = atomicReference;
            this.zzNd = com_google_android_gms_common_api_zzg;
        }

        public void onConnected(Bundle bundle) {
            this.zzNb.zza((GoogleApiClient) this.zzNc.get(), this.zzNd, true);
        }

        public void onConnectionSuspended(int i) {
        }
    }

    /* renamed from: com.google.android.gms.common.api.zzd.4 */
    class C00834 implements OnConnectionFailedListener {
        final /* synthetic */ zzd zzNb;
        final /* synthetic */ zzg zzNd;

        C00834(zzd com_google_android_gms_common_api_zzd, zzg com_google_android_gms_common_api_zzg) {
            this.zzNb = com_google_android_gms_common_api_zzd;
            this.zzNd = com_google_android_gms_common_api_zzg;
        }

        public void onConnectionFailed(ConnectionResult connectionResult) {
            this.zzNd.setResult(new Status(8));
        }
    }

    /* renamed from: com.google.android.gms.common.api.zzd.5 */
    class C00845 implements ResultCallback<Status> {
        final /* synthetic */ GoogleApiClient zzJx;
        final /* synthetic */ zzd zzNb;
        final /* synthetic */ zzg zzNd;
        final /* synthetic */ boolean zzNe;

        C00845(zzd com_google_android_gms_common_api_zzd, zzg com_google_android_gms_common_api_zzg, boolean z, GoogleApiClient googleApiClient) {
            this.zzNb = com_google_android_gms_common_api_zzd;
            this.zzNd = com_google_android_gms_common_api_zzg;
            this.zzNe = z;
            this.zzJx = googleApiClient;
        }

        public /* synthetic */ void onResult(Result result) {
            zzi((Status) result);
        }

        public void zzi(Status status) {
            if (status.isSuccess() && this.zzNb.isConnected()) {
                this.zzNb.reconnect();
            }
            this.zzNd.setResult(status);
            if (this.zzNe) {
                this.zzJx.disconnect();
            }
        }
    }

    /* renamed from: com.google.android.gms.common.api.zzd.6 */
    class C00856 extends zzf {
        final /* synthetic */ zzd zzNb;

        C00856(zzd com_google_android_gms_common_api_zzd) {
            this.zzNb = com_google_android_gms_common_api_zzd;
            super(null);
        }

        public void onConnected(Bundle bundle) {
            this.zzNb.zzCN.lock();
            try {
                if (this.zzNb.isConnecting()) {
                    if (bundle != null) {
                        this.zzNb.zzMI.putAll(bundle);
                    }
                    this.zzNb.zzie();
                    this.zzNb.zzCN.unlock();
                }
            } finally {
                this.zzNb.zzCN.unlock();
            }
        }
    }

    /* renamed from: com.google.android.gms.common.api.zzd.7 */
    class C00867 implements com.google.android.gms.common.api.GoogleApiClient.zza {
        final /* synthetic */ zzd zzNb;

        C00867(zzd com_google_android_gms_common_api_zzd) {
            this.zzNb = com_google_android_gms_common_api_zzd;
        }

        private void zzap(int i) {
            this.zzNb.zzCN.lock();
            try {
                if (this.zzNb.zzMB != i) {
                    Log.wtf("GoogleApiClientImpl", String.format("Internal error: step mismatch. Expected: %d, Actual: %d", new Object[]{Integer.valueOf(this.zzNb.zzMB), Integer.valueOf(i)}));
                    this.zzNb.zzao(4);
                    return;
                }
                if (this.zzNb.zzMz == 1) {
                    this.zzNb.zzie();
                }
                this.zzNb.zzCN.unlock();
            } finally {
                this.zzNb.zzCN.unlock();
            }
        }

        public void zzia() {
            zzap(0);
        }

        public void zzib() {
            zzap(1);
        }
    }

    /* renamed from: com.google.android.gms.common.api.zzd.8 */
    class C00878 implements com.google.android.gms.common.internal.zzl.zza {
        final /* synthetic */ zzd zzNb;

        C00878(zzd com_google_android_gms_common_api_zzd) {
            this.zzNb = com_google_android_gms_common_api_zzd;
        }

        public boolean isConnected() {
            return this.zzNb.isConnected();
        }

        public Bundle zzhp() {
            return null;
        }

        public boolean zzin() {
            return this.zzNb.zzMN;
        }
    }

    /* renamed from: com.google.android.gms.common.api.zzd.9 */
    class C00889 implements OnConnectionFailedListener {
        final /* synthetic */ zzd zzNb;
        final /* synthetic */ int zzNf;
        final /* synthetic */ Api zzNg;

        C00889(zzd com_google_android_gms_common_api_zzd, int i, Api api) {
            this.zzNb = com_google_android_gms_common_api_zzd;
            this.zzNf = i;
            this.zzNg = api;
        }

        public void onConnectionFailed(ConnectionResult connectionResult) {
            this.zzNb.zzCN.lock();
            try {
                if (this.zzNb.isConnecting()) {
                    if (this.zzNf != 2) {
                        int priority = this.zzNg.zzhT().getPriority();
                        if (this.zzNb.zza(priority, this.zzNf, connectionResult)) {
                            this.zzNb.zzMx = connectionResult;
                            this.zzNb.zzMy = priority;
                        }
                    }
                    this.zzNb.zzML.put(this.zzNg.zzhV(), connectionResult);
                    this.zzNb.zzie();
                    this.zzNb.zzCN.unlock();
                }
            } finally {
                this.zzNb.zzCN.unlock();
            }
        }
    }

    private static class zza extends zzuv {
        private WeakReference<zzd> zzNj;

        zza(zzd com_google_android_gms_common_api_zzd) {
            this.zzNj = new WeakReference(com_google_android_gms_common_api_zzd);
        }

        public void zza(ConnectionResult connectionResult, zzut com_google_android_gms_internal_zzut) {
            zzd com_google_android_gms_common_api_zzd = (zzd) this.zzNj.get();
            if (com_google_android_gms_common_api_zzd != null) {
                com_google_android_gms_common_api_zzd.zzd(connectionResult);
            }
        }
    }

    private static class zzb extends com.google.android.gms.common.internal.zzt.zza {
        private WeakReference<zzd> zzNj;

        zzb(zzd com_google_android_gms_common_api_zzd) {
            this.zzNj = new WeakReference(com_google_android_gms_common_api_zzd);
        }

        public void zzb(zzaa com_google_android_gms_common_internal_zzaa) {
            zzd com_google_android_gms_common_api_zzd = (zzd) this.zzNj.get();
            if (com_google_android_gms_common_api_zzd != null) {
                com_google_android_gms_common_api_zzd.zza(com_google_android_gms_common_internal_zzaa);
            }
        }
    }

    private final class zzc extends Handler {
        final /* synthetic */ zzd zzNb;

        zzc(zzd com_google_android_gms_common_api_zzd, Looper looper) {
            this.zzNb = com_google_android_gms_common_api_zzd;
            super(looper);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                    this.zzNb.zzim();
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                    this.zzNb.resume();
                default:
                    Log.w("GoogleApiClientImpl", "Unknown message id: " + message.what);
            }
        }
    }

    private static class zzd extends BroadcastReceiver {
        private WeakReference<zzd> zzNj;

        zzd(zzd com_google_android_gms_common_api_zzd) {
            this.zzNj = new WeakReference(com_google_android_gms_common_api_zzd);
        }

        public void onReceive(Context context, Intent intent) {
            Uri data = intent.getData();
            String str = null;
            if (data != null) {
                str = data.getSchemeSpecificPart();
            }
            if (str != null && str.equals(GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_PACKAGE)) {
                zzd com_google_android_gms_common_api_zzd = (zzd) this.zzNj.get();
                if (com_google_android_gms_common_api_zzd != null) {
                    com_google_android_gms_common_api_zzd.resume();
                }
            }
        }
    }

    public zzd(Context context, Looper looper, com.google.android.gms.common.internal.zzf com_google_android_gms_common_internal_zzf, com.google.android.gms.common.api.Api.zzb<? extends zzur, zzus> com_google_android_gms_common_api_Api_zzb__extends_com_google_android_gms_internal_zzur__com_google_android_gms_internal_zzus, Map<Api<?>, ApiOptions> map, Map<Api<?>, Boolean> map2, Set<ConnectionCallbacks> set, Set<OnConnectionFailedListener> set2, int i) {
        this.zzCN = new ReentrantLock();
        this.zzMt = this.zzCN.newCondition();
        this.zzMw = new LinkedList();
        this.zzMz = 4;
        this.zzMB = 0;
        this.zzMC = false;
        this.zzME = 120000;
        this.zzMF = 5000;
        this.zzMI = new Bundle();
        this.zzMJ = new HashMap();
        this.zzMK = new HashSet();
        this.zzML = new HashMap();
        this.zzMV = Collections.newSetFromMap(new WeakHashMap());
        this.zzMW = Collections.newSetFromMap(new ConcurrentHashMap(16, 0.75f, 2));
        this.zzMX = new C00801(this);
        this.zzMY = new C00856(this);
        this.zzMZ = new C00867(this);
        this.zzNa = new C00878(this);
        this.mContext = context;
        this.zzMu = new zzl(looper, this.zzNa);
        this.zzMc = looper;
        this.zzMG = new zzc(this, looper);
        this.zzMv = i;
        for (ConnectionCallbacks registerConnectionCallbacks : set) {
            this.zzMu.registerConnectionCallbacks(registerConnectionCallbacks);
        }
        for (OnConnectionFailedListener registerConnectionFailedListener : set2) {
            this.zzMu.registerConnectionFailedListener(registerConnectionFailedListener);
        }
        this.zzMM = Collections.unmodifiableList(com_google_android_gms_common_internal_zzf.zziP());
        this.zzMQ = false;
        this.zzMP = 2;
        int i2 = 0;
        for (Api api : map.keySet()) {
            int i3;
            Object obj = map.get(api);
            if (map2.get(api) != null) {
                i3 = ((Boolean) map2.get(api)).booleanValue() ? 2 : 1;
            } else {
                i3 = 0;
            }
            com.google.android.gms.common.api.Api.zza zza = zza(api.zzhT(), obj, context, looper, com_google_android_gms_common_internal_zzf, this.zzMY, zza(api, i3));
            zza.zza(this.zzMZ);
            this.zzMJ.put(api.zzhV(), zza);
            int i4 = (api.zzhT().getPriority() == 1 ? 1 : 0) | i2;
            if (zza.zzhc()) {
                this.zzMQ = true;
                if (i3 < this.zzMP) {
                    this.zzMP = i3;
                }
                if (i3 != 0) {
                    this.zzMK.add(api.zzhV());
                }
            }
            i2 = i4;
        }
        if (i2 != 0) {
            this.zzMQ = false;
        }
        if (this.zzMQ) {
            com_google_android_gms_common_internal_zzf.zza(Integer.valueOf(getSessionId()));
            zza(context, looper, com_google_android_gms_common_internal_zzf, (com.google.android.gms.common.api.Api.zzb) com_google_android_gms_common_api_Api_zzb__extends_com_google_android_gms_internal_zzur__com_google_android_gms_internal_zzus);
        }
    }

    private void resume() {
        this.zzCN.lock();
        try {
            if (zzil()) {
                connect();
            }
            this.zzCN.unlock();
        } catch (Throwable th) {
            this.zzCN.unlock();
        }
    }

    private void zzM(boolean z) {
        if (this.zzMO != null) {
            if (this.zzMO.isConnected()) {
                if (z) {
                    this.zzMO.zzsu();
                }
                this.zzMO.disconnect();
            }
            this.zzMS = null;
        }
    }

    private static <C extends com.google.android.gms.common.api.Api.zza, O> C zza(com.google.android.gms.common.api.Api.zzb<C, O> com_google_android_gms_common_api_Api_zzb_C__O, Object obj, Context context, Looper looper, com.google.android.gms.common.internal.zzf com_google_android_gms_common_internal_zzf, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        return com_google_android_gms_common_api_Api_zzb_C__O.zza(context, looper, com_google_android_gms_common_internal_zzf, obj, connectionCallbacks, onConnectionFailedListener);
    }

    private final OnConnectionFailedListener zza(Api<?> api, int i) {
        return new C00889(this, i, api);
    }

    private void zza(Context context, Looper looper, com.google.android.gms.common.internal.zzf com_google_android_gms_common_internal_zzf, com.google.android.gms.common.api.Api.zzb<? extends zzur, zzus> com_google_android_gms_common_api_Api_zzb__extends_com_google_android_gms_internal_zzur__com_google_android_gms_internal_zzus) {
        this.zzMO = (zzur) zza(com_google_android_gms_common_api_Api_zzb__extends_com_google_android_gms_internal_zzur__com_google_android_gms_internal_zzus, com_google_android_gms_common_internal_zzf.zziU(), context, looper, com_google_android_gms_common_internal_zzf, new zzf() {
            final /* synthetic */ zzd zzNb;

            {
                this.zzNb = r2;
            }

            public void onConnected(Bundle bundle) {
                this.zzNb.zzif();
            }
        }, new OnConnectionFailedListener() {
            final /* synthetic */ zzd zzNb;

            {
                this.zzNb = r1;
            }

            public void onConnectionFailed(ConnectionResult connectionResult) {
                this.zzNb.zzCN.lock();
                try {
                    this.zzNb.zzb(new ConnectionResult(8, null));
                } finally {
                    this.zzNb.zzCN.unlock();
                }
            }
        });
    }

    private void zza(ConnectionResult connectionResult) {
        this.zzMC = false;
        zzM(!connectionResult.hasResolution());
        zzao(3);
        if (!(zzil() && GooglePlayServicesUtil.zzh(this.mContext, connectionResult.getErrorCode()))) {
            zzim();
            this.zzMu.zzg(connectionResult);
        }
        this.zzMN = false;
    }

    private void zza(GoogleApiClient googleApiClient, zzg com_google_android_gms_common_api_zzg, boolean z) {
        zzko.zzQy.zzc(googleApiClient).setResultCallback(new C00845(this, com_google_android_gms_common_api_zzg, z, googleApiClient));
    }

    private <A extends com.google.android.gms.common.api.Api.zza> void zza(zzg<A> com_google_android_gms_common_api_zzd_zzg_A) {
        this.zzCN.lock();
        try {
            zzx.zzb(com_google_android_gms_common_api_zzd_zzg_A.zzhV() != null, (Object) "This task can not be executed or enqueued (it's probably a Batch or malformed)");
            this.zzMW.add(com_google_android_gms_common_api_zzd_zzg_A);
            com_google_android_gms_common_api_zzd_zzg_A.zza(this.zzMX);
            if (zzil()) {
                com_google_android_gms_common_api_zzd_zzg_A.zzk(new Status(8));
                return;
            }
            com.google.android.gms.common.api.Api.zza zza = zza(com_google_android_gms_common_api_zzd_zzg_A.zzhV());
            if (zza.isConnected() || !this.zzML.containsKey(com_google_android_gms_common_api_zzd_zzg_A.zzhV())) {
                com_google_android_gms_common_api_zzd_zzg_A.zzb(zza);
                this.zzCN.unlock();
                return;
            }
            com_google_android_gms_common_api_zzd_zzg_A.zzk(new Status(17));
            this.zzCN.unlock();
        } finally {
            this.zzCN.unlock();
        }
    }

    private void zza(zzaa com_google_android_gms_common_internal_zzaa) {
        ConnectionResult zzjo = com_google_android_gms_common_internal_zzaa.zzjo();
        if (zzjo.isSuccess()) {
            this.zzMG.post(new AnonymousClass12(this, com_google_android_gms_common_internal_zzaa));
        } else {
            zzb(zzjo);
        }
    }

    private boolean zza(int i, int i2, ConnectionResult connectionResult) {
        return (i2 != 1 || connectionResult.hasResolution()) ? this.zzMx == null || i < this.zzMy : false;
    }

    private void zzao(int i) {
        this.zzCN.lock();
        try {
            if (this.zzMz != 3) {
                if (i == -1) {
                    Iterator it;
                    zzg com_google_android_gms_common_api_zzd_zzg;
                    if (isConnecting()) {
                        it = this.zzMw.iterator();
                        while (it.hasNext()) {
                            com_google_android_gms_common_api_zzd_zzg = (zzg) it.next();
                            if (com_google_android_gms_common_api_zzd_zzg.zzhW() != 1) {
                                com_google_android_gms_common_api_zzd_zzg.cancel();
                                it.remove();
                            }
                        }
                    } else {
                        for (zzg com_google_android_gms_common_api_zzd_zzg2 : this.zzMw) {
                            com_google_android_gms_common_api_zzd_zzg2.cancel();
                        }
                        this.zzMw.clear();
                    }
                    for (zzg com_google_android_gms_common_api_zzd_zzg22 : this.zzMW) {
                        com_google_android_gms_common_api_zzd_zzg22.zza(null);
                        com_google_android_gms_common_api_zzd_zzg22.cancel();
                    }
                    this.zzMW.clear();
                    for (zze clear : this.zzMV) {
                        clear.clear();
                    }
                    this.zzMV.clear();
                    if (this.zzMx != null || this.zzMw.isEmpty()) {
                        this.zzML.clear();
                    } else {
                        this.zzMC = true;
                        return;
                    }
                }
                boolean isConnecting = isConnecting();
                boolean isConnected = isConnected();
                this.zzMz = 3;
                if (isConnecting) {
                    if (i == -1) {
                        this.zzMx = null;
                    }
                    this.zzMt.signalAll();
                }
                this.zzMN = false;
                for (com.google.android.gms.common.api.Api.zza disconnect : this.zzMJ.values()) {
                    disconnect.disconnect();
                }
                zzM(i == -1);
                this.zzMN = true;
                this.zzMz = 4;
                if (isConnected) {
                    if (i != -1) {
                        this.zzMu.zzaJ(i);
                    }
                    this.zzMN = false;
                }
            }
            this.zzCN.unlock();
        } finally {
            this.zzCN.unlock();
        }
    }

    private void zzb(ConnectionResult connectionResult) {
        this.zzMG.post(new AnonymousClass13(this, connectionResult));
    }

    private boolean zzc(ConnectionResult connectionResult) {
        return this.zzMP != 2 ? this.zzMP == 1 && !connectionResult.hasResolution() : true;
    }

    private void zzd(ConnectionResult connectionResult) {
        if (connectionResult.isSuccess()) {
            this.zzMG.post(new C00812(this));
        } else {
            zzb(connectionResult);
        }
    }

    private void zzie() {
        this.zzMD--;
        if (this.zzMD != 0) {
            return;
        }
        if (this.zzMx != null) {
            zza(this.zzMx);
            return;
        }
        switch (this.zzMB) {
            case ECCurve.COORD_AFFINE /*0*/:
                if (this.zzMQ) {
                    this.zzMB = 1;
                    zzii();
                    return;
                }
                zzij();
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                zzih();
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                this.zzMz = 2;
                zzim();
                if (this.zzMO != null) {
                    if (this.zzMT) {
                        this.zzMO.zza(this.zzMS, this.zzMU);
                    }
                    zzM(false);
                }
                this.zzMt.signalAll();
                zzik();
                if (this.zzMC) {
                    this.zzMC = false;
                    zzao(-1);
                    return;
                }
                this.zzMu.zzj(this.zzMI.isEmpty() ? null : this.zzMI);
            default:
        }
    }

    private void zzif() {
        this.zzMO.zza(new zzb(this));
    }

    private Set<Scope> zzig() {
        return new HashSet(Arrays.asList(zzmh.zzi(this.zzMM)));
    }

    private void zzih() {
        this.zzMO.zza(this.zzMS, zzig(), new zza(this));
    }

    private void zzii() {
        zzx.zza(getLooper() == this.zzMG.getLooper(), (Object) "This method must be run on the mHandlerForCallbacks thread");
        if (this.zzMB == 1 && this.zzMR) {
            this.zzMD = this.zzMJ.size();
            Iterator it = this.zzMJ.keySet().iterator();
            while (it.hasNext()) {
                com.google.android.gms.common.api.Api.zzc com_google_android_gms_common_api_Api_zzc = (com.google.android.gms.common.api.Api.zzc) it.next();
                if (this.zzML.containsKey(com_google_android_gms_common_api_Api_zzc)) {
                    this.zzCN.lock();
                    try {
                        zzie();
                    } finally {
                        it = this.zzCN;
                        it.unlock();
                    }
                } else {
                    ((com.google.android.gms.common.api.Api.zza) this.zzMJ.get(com_google_android_gms_common_api_Api_zzc)).zza(this.zzMS);
                }
            }
        }
    }

    private void zzij() {
        this.zzMB = 2;
        this.zzMD = this.zzMJ.size();
        for (com.google.android.gms.common.api.Api.zzc com_google_android_gms_common_api_Api_zzc : this.zzMJ.keySet()) {
            if (this.zzML.containsKey(com_google_android_gms_common_api_Api_zzc)) {
                zzie();
            } else {
                ((com.google.android.gms.common.api.Api.zza) this.zzMJ.get(com_google_android_gms_common_api_Api_zzc)).zzb(this.zzMS);
            }
        }
    }

    private void zzik() {
        this.zzCN.lock();
        try {
            boolean z = isConnected() || zzil();
            zzx.zza(z, (Object) "GoogleApiClient is not connected yet.");
            while (!this.zzMw.isEmpty()) {
                zza((zzg) this.zzMw.remove());
            }
            this.zzCN.unlock();
        } catch (Throwable e) {
            Log.w("GoogleApiClientImpl", "Service died while flushing queue", e);
        } catch (Throwable th) {
            this.zzCN.unlock();
        }
    }

    private void zzim() {
        this.zzCN.lock();
        try {
            if (this.zzMA) {
                this.zzMA = false;
                this.zzMG.removeMessages(2);
                this.zzMG.removeMessages(1);
                if (this.zzMH != null) {
                    this.mContext.getApplicationContext().unregisterReceiver(this.zzMH);
                    this.zzMH = null;
                }
                this.zzCN.unlock();
            }
        } finally {
            this.zzCN.unlock();
        }
    }

    public ConnectionResult blockingConnect() {
        ConnectionResult connectionResult;
        zzx.zza(Looper.myLooper() != Looper.getMainLooper(), (Object) "blockingConnect must not be called on the UI thread");
        this.zzCN.lock();
        try {
            connect();
            while (isConnecting()) {
                this.zzMt.await();
            }
            if (isConnected()) {
                connectionResult = ConnectionResult.zzLr;
            } else if (this.zzMx != null) {
                connectionResult = this.zzMx;
                this.zzCN.unlock();
            } else {
                connectionResult = new ConnectionResult(13, null);
                this.zzCN.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            connectionResult = new ConnectionResult(15, null);
        } finally {
            this.zzCN.unlock();
        }
        return connectionResult;
    }

    public ConnectionResult blockingConnect(long j, TimeUnit timeUnit) {
        ConnectionResult connectionResult;
        zzx.zza(Looper.myLooper() != Looper.getMainLooper(), (Object) "blockingConnect must not be called on the UI thread");
        this.zzCN.lock();
        try {
            connect();
            long toNanos = timeUnit.toNanos(j);
            while (isConnecting()) {
                toNanos = this.zzMt.awaitNanos(toNanos);
                if (toNanos <= 0) {
                    connectionResult = new ConnectionResult(14, null);
                    break;
                }
            }
            if (isConnected()) {
                connectionResult = ConnectionResult.zzLr;
                this.zzCN.unlock();
            } else if (this.zzMx != null) {
                connectionResult = this.zzMx;
                this.zzCN.unlock();
            } else {
                connectionResult = new ConnectionResult(13, null);
                this.zzCN.unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            connectionResult = new ConnectionResult(15, null);
        } finally {
            this.zzCN.unlock();
        }
        return connectionResult;
    }

    public PendingResult<Status> clearDefaultAccountAndReconnect() {
        zzx.zza(isConnected(), (Object) "GoogleApiClient is not connected yet.");
        zzg com_google_android_gms_common_api_zzg = new zzg(this.zzMc);
        if (this.zzMJ.containsKey(zzko.zzGR)) {
            zza((GoogleApiClient) this, com_google_android_gms_common_api_zzg, false);
        } else {
            AtomicReference atomicReference = new AtomicReference();
            ConnectionCallbacks c00823 = new C00823(this, atomicReference, com_google_android_gms_common_api_zzg);
            GoogleApiClient build = new Builder(this.mContext).addApi(zzko.API).addConnectionCallbacks(c00823).addOnConnectionFailedListener(new C00834(this, com_google_android_gms_common_api_zzg)).setHandler(this.zzMG).build();
            atomicReference.set(build);
            build.connect();
        }
        return com_google_android_gms_common_api_zzg;
    }

    public void connect() {
        this.zzCN.lock();
        try {
            this.zzMC = false;
            if (isConnected() || isConnecting()) {
                this.zzCN.unlock();
                return;
            }
            this.zzMN = true;
            this.zzMx = null;
            this.zzMz = 1;
            this.zzMB = 0;
            this.zzMI.clear();
            this.zzMD = this.zzMJ.size();
            this.zzML.clear();
            this.zzMR = false;
            this.zzMT = false;
            this.zzMU = false;
            if (this.zzMQ) {
                this.zzMO.connect();
            }
            for (com.google.android.gms.common.api.Api.zza connect : this.zzMJ.values()) {
                connect.connect();
            }
            this.zzCN.unlock();
        } catch (Throwable th) {
            this.zzCN.unlock();
        }
    }

    public void disconnect() {
        zzim();
        zzao(-1);
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append(str).println("GoogleApiClient:");
        Object obj = str + "  ";
        printWriter.append(obj).append("mConnectionState=");
        switch (this.zzMz) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                printWriter.print("CONNECTING");
                break;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                printWriter.print("CONNECTED");
                break;
            case F2m.PPB /*3*/:
                printWriter.print("DISCONNECTING");
                break;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                printWriter.print("DISCONNECTED");
                break;
            default:
                printWriter.print(PaymentFramework.CARD_TYPE_UNKNOWN);
                break;
        }
        printWriter.append(" mResuming=").print(this.zzMA);
        printWriter.append(" mWaitingToDisconnect=").println(this.zzMC);
        printWriter.append(obj).append("mWorkQueue.size()=").print(this.zzMw.size());
        printWriter.append(" mUnconsumedRunners.size()=").println(this.zzMW.size());
        for (com.google.android.gms.common.api.Api.zza dump : this.zzMJ.values()) {
            dump.dump(obj, fileDescriptor, printWriter, strArr);
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public Looper getLooper() {
        return this.zzMc;
    }

    public int getSessionId() {
        return System.identityHashCode(this);
    }

    public boolean isConnected() {
        return this.zzMz == 2;
    }

    public boolean isConnecting() {
        return this.zzMz == 1;
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        return this.zzMu.isConnectionCallbacksRegistered(connectionCallbacks);
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        return this.zzMu.isConnectionFailedListenerRegistered(onConnectionFailedListener);
    }

    public void reconnect() {
        disconnect();
        connect();
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.zzMu.registerConnectionCallbacks(connectionCallbacks);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.zzMu.registerConnectionFailedListener(onConnectionFailedListener);
    }

    public void stopAutoManage(FragmentActivity fragmentActivity) {
        zzx.zza(this.zzMv >= 0, (Object) "Called stopAutoManage but automatic lifecycle management is not enabled.");
        zzh.zza(fragmentActivity).zzas(this.zzMv);
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.zzMu.unregisterConnectionCallbacks(connectionCallbacks);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.zzMu.unregisterConnectionFailedListener(onConnectionFailedListener);
    }

    public <C extends com.google.android.gms.common.api.Api.zza> C zza(com.google.android.gms.common.api.Api.zzc<C> com_google_android_gms_common_api_Api_zzc_C) {
        Object obj = (com.google.android.gms.common.api.Api.zza) this.zzMJ.get(com_google_android_gms_common_api_Api_zzc_C);
        zzx.zzb(obj, (Object) "Appropriate Api was not requested.");
        return obj;
    }

    public <A extends com.google.android.gms.common.api.Api.zza, R extends Result, T extends com.google.android.gms.common.api.zza.zza<R, A>> T zza(T t) {
        zzx.zzb(this.zzMJ.containsKey(t.zzhV()), (Object) "GoogleApiClient is not configured to use the API required for this call.");
        this.zzCN.lock();
        try {
            if (isConnected()) {
                zzb((com.google.android.gms.common.api.zza.zza) t);
            } else {
                this.zzMw.add(t);
            }
            this.zzCN.unlock();
            return t;
        } catch (Throwable th) {
            this.zzCN.unlock();
        }
    }

    public boolean zza(Api<?> api) {
        return this.zzMJ.containsKey(api.zzhV());
    }

    public boolean zza(Scope scope) {
        return this.zzMM.contains(scope.zzio());
    }

    public <A extends com.google.android.gms.common.api.Api.zza, T extends com.google.android.gms.common.api.zza.zza<? extends Result, A>> T zzb(T t) {
        boolean z = isConnected() || zzil();
        zzx.zza(z, (Object) "GoogleApiClient is not connected yet.");
        zzik();
        try {
            zza((zzg) t);
        } catch (DeadObjectException e) {
            zzao(1);
        }
        return t;
    }

    public boolean zzb(Api<?> api) {
        com.google.android.gms.common.api.Api.zza com_google_android_gms_common_api_Api_zza = (com.google.android.gms.common.api.Api.zza) this.zzMJ.get(api.zzhV());
        return com_google_android_gms_common_api_Api_zza == null ? false : com_google_android_gms_common_api_Api_zza.isConnected();
    }

    public <L> zze<L> zzf(L l) {
        zzx.zzb((Object) l, (Object) "Listener must not be null");
        this.zzCN.lock();
        try {
            zze<L> com_google_android_gms_common_api_zze = new zze(this.zzMc, l);
            this.zzMV.add(com_google_android_gms_common_api_zze);
            return com_google_android_gms_common_api_zze;
        } finally {
            this.zzCN.unlock();
        }
    }

    boolean zzil() {
        return this.zzMA;
    }
}
