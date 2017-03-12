package com.google.android.gms.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ServerAuthCodeCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.ServerAuthCodeCallbacks.CheckResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.BinderWrapper;
import com.google.android.gms.common.internal.zzaa;
import com.google.android.gms.common.internal.zzc;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzk;
import com.google.android.gms.common.internal.zzq;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.internal.zzy;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class zzvc extends zzk<zzva> implements zzur {
    private final zzf zzPG;
    private final zzus zzPr;
    private Integer zzPs;
    private final ExecutorService zzawZ;

    private static class zza extends com.google.android.gms.internal.zzuy.zza {
        private final zzus zzPr;
        private final ExecutorService zzawZ;

        /* renamed from: com.google.android.gms.internal.zzvc.zza.1 */
        class C02331 implements Runnable {
            final /* synthetic */ List zzaxa;
            final /* synthetic */ String zzaxb;
            final /* synthetic */ zzva zzaxc;
            final /* synthetic */ zza zzaxd;

            C02331(zza com_google_android_gms_internal_zzvc_zza, List list, String str, zzva com_google_android_gms_internal_zzva) {
                this.zzaxd = com_google_android_gms_internal_zzvc_zza;
                this.zzaxa = list;
                this.zzaxb = str;
                this.zzaxc = com_google_android_gms_internal_zzva;
            }

            public void run() {
                try {
                    CheckResult onCheckServerAuthorization = this.zzaxd.zzsx().onCheckServerAuthorization(this.zzaxb, Collections.unmodifiableSet(new HashSet(this.zzaxa)));
                    this.zzaxc.zza(new zzuw(onCheckServerAuthorization.zzic(), onCheckServerAuthorization.zzid()));
                } catch (Throwable e) {
                    Log.e("SignInClientImpl", "RemoteException thrown when processing checkServerAuthorization callback", e);
                }
            }
        }

        /* renamed from: com.google.android.gms.internal.zzvc.zza.2 */
        class C02342 implements Runnable {
            final /* synthetic */ String zzaxb;
            final /* synthetic */ zzva zzaxc;
            final /* synthetic */ zza zzaxd;
            final /* synthetic */ String zzaxe;

            C02342(zza com_google_android_gms_internal_zzvc_zza, String str, String str2, zzva com_google_android_gms_internal_zzva) {
                this.zzaxd = com_google_android_gms_internal_zzvc_zza;
                this.zzaxb = str;
                this.zzaxe = str2;
                this.zzaxc = com_google_android_gms_internal_zzva;
            }

            public void run() {
                try {
                    this.zzaxc.zzaf(this.zzaxd.zzsx().onUploadServerAuthCode(this.zzaxb, this.zzaxe));
                } catch (Throwable e) {
                    Log.e("SignInClientImpl", "RemoteException thrown when processing uploadServerAuthCode callback", e);
                }
            }
        }

        public zza(zzus com_google_android_gms_internal_zzus, ExecutorService executorService) {
            this.zzPr = com_google_android_gms_internal_zzus;
            this.zzawZ = executorService;
        }

        private ServerAuthCodeCallbacks zzsx() {
            return this.zzPr.zzsx();
        }

        public void zza(String str, String str2, zzva com_google_android_gms_internal_zzva) {
            this.zzawZ.submit(new C02342(this, str, str2, com_google_android_gms_internal_zzva));
        }

        public void zza(String str, List<Scope> list, zzva com_google_android_gms_internal_zzva) {
            this.zzawZ.submit(new C02331(this, list, str, com_google_android_gms_internal_zzva));
        }
    }

    public zzvc(Context context, Looper looper, zzf com_google_android_gms_common_internal_zzf, zzus com_google_android_gms_internal_zzus, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, ExecutorService executorService) {
        super(context, looper, 44, connectionCallbacks, onConnectionFailedListener, com_google_android_gms_common_internal_zzf);
        this.zzPG = com_google_android_gms_common_internal_zzf;
        this.zzPr = com_google_android_gms_internal_zzus;
        this.zzPs = com_google_android_gms_common_internal_zzf.zziV();
        this.zzawZ = executorService;
    }

    public static Bundle zza(zzus com_google_android_gms_internal_zzus, Integer num, ExecutorService executorService) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("com.google.android.gms.signin.internal.offlineAccessRequested", com_google_android_gms_internal_zzus.zzsv());
        bundle.putBoolean("com.google.android.gms.signin.internal.idTokenRequested", com_google_android_gms_internal_zzus.zzsw());
        bundle.putString("com.google.android.gms.signin.internal.serverClientId", com_google_android_gms_internal_zzus.zzrN());
        if (com_google_android_gms_internal_zzus.zzsx() != null) {
            bundle.putParcelable("com.google.android.gms.signin.internal.signInCallbacks", new BinderWrapper(new zza(com_google_android_gms_internal_zzus, executorService).asBinder()));
        }
        if (num != null) {
            bundle.putInt("com.google.android.gms.common.internal.ClientSettings.sessionId", num.intValue());
        }
        return bundle;
    }

    public void zza(zzq com_google_android_gms_common_internal_zzq, Set<Scope> set, zzuz com_google_android_gms_internal_zzuz) {
        zzx.zzb((Object) com_google_android_gms_internal_zzuz, (Object) "Expecting a valid ISignInCallbacks");
        try {
            ((zzva) zzjb()).zza(new zzc(com_google_android_gms_common_internal_zzq, set), com_google_android_gms_internal_zzuz);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when authAccount is called");
            try {
                com_google_android_gms_internal_zzuz.zza(new ConnectionResult(8, null), new zzut());
            } catch (RemoteException e2) {
                Log.wtf("SignInClientImpl", "ISignInCallbacks#onAuthAccount should be executed from the same process, unexpected RemoteException.");
            }
        }
    }

    public void zza(zzq com_google_android_gms_common_internal_zzq, boolean z) {
        try {
            ((zzva) zzjb()).zza(com_google_android_gms_common_internal_zzq, this.zzPs.intValue(), z);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when saveDefaultAccount is called");
        }
    }

    public void zza(zzt com_google_android_gms_common_internal_zzt) {
        zzx.zzb((Object) com_google_android_gms_common_internal_zzt, (Object) "Expecting a valid IResolveAccountCallbacks");
        try {
            ((zzva) zzjb()).zza(new zzy(this.zzPG.zziN(), this.zzPs.intValue()), com_google_android_gms_common_internal_zzt);
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when resolveAccount is called");
            try {
                com_google_android_gms_common_internal_zzt.zzb(new zzaa(8));
            } catch (RemoteException e2) {
                Log.wtf("SignInClientImpl", "IResolveAccountCallbacks#onAccountResolutionComplete should be executed from the same process, unexpected RemoteException.");
            }
        }
    }

    protected String zzcF() {
        return "com.google.android.gms.signin.service.START";
    }

    protected zzva zzcG(IBinder iBinder) {
        return com.google.android.gms.internal.zzva.zza.zzcF(iBinder);
    }

    protected String zzcG() {
        return "com.google.android.gms.signin.internal.ISignInService";
    }

    protected Bundle zzhq() {
        Bundle zza = zza(this.zzPr, this.zzPG.zziV(), this.zzawZ);
        if (!getContext().getPackageName().equals(this.zzPG.zziR())) {
            zza.putString("com.google.android.gms.signin.internal.realClientPackageName", this.zzPG.zziR());
        }
        return zza;
    }

    protected /* synthetic */ IInterface zzp(IBinder iBinder) {
        return zzcG(iBinder);
    }

    public void zzsu() {
        try {
            ((zzva) zzjb()).zzhb(this.zzPs.intValue());
        } catch (RemoteException e) {
            Log.w("SignInClientImpl", "Remote service probably died when clearAccountFromSessionStore is called");
        }
    }
}
