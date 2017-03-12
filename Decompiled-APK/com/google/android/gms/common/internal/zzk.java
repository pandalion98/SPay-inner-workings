package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.internal.zzmh;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.crypto.tls.NamedCurve;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public abstract class zzk<T extends IInterface> implements com.google.android.gms.common.api.Api.zza, com.google.android.gms.common.internal.zzl.zza {
    public static final String[] zzPR;
    private final Context mContext;
    final Handler mHandler;
    private final Account zzFN;
    private final List<String> zzMM;
    private final Looper zzMc;
    private final zzl zzMu;
    private final zzf zzPG;
    private final zzm zzPH;
    private zzs zzPI;
    private boolean zzPJ;
    private com.google.android.gms.common.api.GoogleApiClient.zza zzPK;
    private T zzPL;
    private final ArrayList<zzc<?>> zzPM;
    private zze zzPN;
    private int zzPO;
    boolean zzPP;
    private final int zzPQ;
    private final Object zznh;

    protected abstract class zzc<TListener> {
        private TListener mListener;
        final /* synthetic */ zzk zzPT;
        private boolean zzPU;

        public zzc(zzk com_google_android_gms_common_internal_zzk, TListener tListener) {
            this.zzPT = com_google_android_gms_common_internal_zzk;
            this.mListener = tListener;
            this.zzPU = false;
        }

        public void unregister() {
            zzjh();
            synchronized (this.zzPT.zzPM) {
                this.zzPT.zzPM.remove(this);
            }
        }

        protected abstract void zzi(TListener tListener);

        protected abstract void zzjf();

        public void zzjg() {
            synchronized (this) {
                Object obj = this.mListener;
                if (this.zzPU) {
                    Log.w("GmsClient", "Callback proxy " + this + " being reused. This is not safe.");
                }
            }
            if (obj != null) {
                try {
                    zzi(obj);
                } catch (RuntimeException e) {
                    zzjf();
                    throw e;
                }
            }
            zzjf();
            synchronized (this) {
                this.zzPU = true;
            }
            unregister();
        }

        public void zzjh() {
            synchronized (this) {
                this.mListener = null;
            }
        }
    }

    private abstract class zza extends zzc<Boolean> {
        public final int statusCode;
        public final Bundle zzPS;
        final /* synthetic */ zzk zzPT;

        protected zza(zzk com_google_android_gms_common_internal_zzk, int i, Bundle bundle) {
            this.zzPT = com_google_android_gms_common_internal_zzk;
            super(com_google_android_gms_common_internal_zzk, Boolean.valueOf(true));
            this.statusCode = i;
            this.zzPS = bundle;
        }

        protected void zzc(Boolean bool) {
            if (bool == null) {
                this.zzPT.zza(1, null);
                return;
            }
            switch (this.statusCode) {
                case ECCurve.COORD_AFFINE /*0*/:
                    if (!zzje()) {
                        if (this.zzPT.zzPN != null) {
                            this.zzPT.zzPH.zzb(this.zzPT.zzcF(), this.zzPT.zzPN, this.zzPT.zziZ());
                            this.zzPT.zzPN = null;
                        }
                        this.zzPT.zza(1, null);
                        zzf(new ConnectionResult(8, null));
                    }
                case NamedCurve.sect283r1 /*10*/:
                    this.zzPT.zza(1, null);
                    throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
                default:
                    PendingIntent pendingIntent = this.zzPS != null ? (PendingIntent) this.zzPS.getParcelable("pendingIntent") : null;
                    if (this.zzPT.zzPN != null) {
                        this.zzPT.zzPH.zzb(this.zzPT.zzcF(), this.zzPT.zzPN, this.zzPT.zziZ());
                        this.zzPT.zzPN = null;
                    }
                    this.zzPT.zza(1, null);
                    zzf(new ConnectionResult(this.statusCode, pendingIntent));
            }
        }

        protected void zzf(ConnectionResult connectionResult) {
            this.zzPT.zzMu.zzg(connectionResult);
        }

        protected /* synthetic */ void zzi(Object obj) {
            zzc((Boolean) obj);
        }

        protected abstract boolean zzje();

        protected void zzjf() {
        }
    }

    final class zzb extends Handler {
        final /* synthetic */ zzk zzPT;

        public zzb(zzk com_google_android_gms_common_internal_zzk, Looper looper) {
            this.zzPT = com_google_android_gms_common_internal_zzk;
            super(looper);
        }

        public void handleMessage(Message message) {
            zzc com_google_android_gms_common_internal_zzk_zzc;
            if ((message.what == 1 || message.what == 5 || message.what == 6) && !this.zzPT.isConnecting()) {
                com_google_android_gms_common_internal_zzk_zzc = (zzc) message.obj;
                com_google_android_gms_common_internal_zzk_zzc.zzjf();
                com_google_android_gms_common_internal_zzk_zzc.unregister();
            } else if (message.what == 3) {
                this.zzPT.zzMu.zzg(new ConnectionResult(((Integer) message.obj).intValue(), null));
            } else if (message.what == 4) {
                this.zzPT.zza(4, null);
                this.zzPT.zzMu.zzaJ(((Integer) message.obj).intValue());
                this.zzPT.zza(4, 1, null);
            } else if (message.what == 2 && !this.zzPT.isConnected()) {
                com_google_android_gms_common_internal_zzk_zzc = (zzc) message.obj;
                com_google_android_gms_common_internal_zzk_zzc.zzjf();
                com_google_android_gms_common_internal_zzk_zzc.unregister();
            } else if (message.what == 2 || message.what == 1 || message.what == 5 || message.what == 6) {
                ((zzc) message.obj).zzjg();
            } else {
                Log.wtf("GmsClient", "Don't know how to handle this message.");
            }
        }
    }

    public static final class zzd extends com.google.android.gms.common.internal.zzr.zza {
        private zzk zzPV;

        public zzd(zzk com_google_android_gms_common_internal_zzk) {
            this.zzPV = com_google_android_gms_common_internal_zzk;
        }

        private void zzji() {
            this.zzPV = null;
        }

        public void zzb(int i, IBinder iBinder, Bundle bundle) {
            zzx.zzb(this.zzPV, (Object) "onPostInitComplete can be called only once per call to getRemoteService");
            this.zzPV.zza(i, iBinder, bundle);
            zzji();
        }

        public void zzc(int i, Bundle bundle) {
            zzx.zzb(this.zzPV, (Object) "onAccountValidationComplete can be called only once per call to validateAccount");
            this.zzPV.zzb(i, bundle);
            zzji();
        }
    }

    public final class zze implements ServiceConnection {
        final /* synthetic */ zzk zzPT;

        public zze(zzk com_google_android_gms_common_internal_zzk) {
            this.zzPT = com_google_android_gms_common_internal_zzk;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            zzx.zzb((Object) iBinder, (Object) "Expecting a valid IBinder");
            this.zzPT.zzPI = com.google.android.gms.common.internal.zzs.zza.zzU(iBinder);
            this.zzPT.mHandler.sendMessage(this.zzPT.mHandler.obtainMessage(6, new zzg(this.zzPT)));
        }

        public void onServiceDisconnected(ComponentName componentName) {
            this.zzPT.mHandler.sendMessage(this.zzPT.mHandler.obtainMessage(4, Integer.valueOf(1)));
        }
    }

    protected final class zzf extends zza {
        final /* synthetic */ zzk zzPT;
        public final IBinder zzPW;

        public zzf(zzk com_google_android_gms_common_internal_zzk, int i, IBinder iBinder, Bundle bundle) {
            this.zzPT = com_google_android_gms_common_internal_zzk;
            super(com_google_android_gms_common_internal_zzk, i, bundle);
            this.zzPW = iBinder;
        }

        protected boolean zzje() {
            try {
                if (!this.zzPT.zzcG().equals(this.zzPW.getInterfaceDescriptor())) {
                    return false;
                }
                IInterface zzp = this.zzPT.zzp(this.zzPW);
                if (zzp == null || !this.zzPT.zza(2, 3, zzp)) {
                    return false;
                }
                this.zzPT.zzMu.zzfe();
                GooglePlayServicesUtil.zzL(this.zzPT.mContext);
                return true;
            } catch (RemoteException e) {
                Log.w("GmsClient", "service probably died");
                return false;
            }
        }
    }

    protected final class zzg extends zza {
        final /* synthetic */ zzk zzPT;

        public zzg(zzk com_google_android_gms_common_internal_zzk) {
            this.zzPT = com_google_android_gms_common_internal_zzk;
            super(com_google_android_gms_common_internal_zzk, 0, null);
        }

        protected boolean zzje() {
            if (this.zzPT.zzPJ) {
                zzx.zza(this.zzPT.zzPK != null, (Object) "mConnectionProgressReportCallbacks should not be null if mReportProgress");
                this.zzPT.zzPK.zzia();
            } else {
                this.zzPT.zzb(null);
            }
            return true;
        }
    }

    protected final class zzh extends zza {
        final /* synthetic */ zzk zzPT;

        public zzh(zzk com_google_android_gms_common_internal_zzk, int i, Bundle bundle) {
            this.zzPT = com_google_android_gms_common_internal_zzk;
            super(com_google_android_gms_common_internal_zzk, i, bundle);
        }

        protected boolean zzje() {
            boolean z = this.zzPT.zzPJ && this.zzPT.zzPK != null;
            zzx.zza(z, (Object) "PostValidationCallback should not happen when mReportProgress is false ormConnectionProgressReportCallbacks is null");
            this.zzPT.zzPK.zzib();
            return true;
        }
    }

    static {
        zzPR = new String[]{"service_esmobile", "service_googleme"};
    }

    @Deprecated
    protected zzk(Context context, Looper looper, int i, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this.zznh = new Object();
        this.zzPJ = false;
        this.zzPM = new ArrayList();
        this.zzPO = 1;
        this.zzPP = false;
        this.mContext = (Context) zzx.zzl(context);
        this.zzMc = (Looper) zzx.zzb((Object) looper, (Object) "Looper must not be null");
        this.zzPH = zzm.zzP(context);
        this.zzMu = new zzl(looper, this);
        this.mHandler = new zzb(this, looper);
        this.zzPQ = i;
        this.zzFN = null;
        this.zzMM = null;
        this.zzPG = new Builder(context).zzhY();
        registerConnectionCallbacks((ConnectionCallbacks) zzx.zzl(connectionCallbacks));
        registerConnectionFailedListener((OnConnectionFailedListener) zzx.zzl(onConnectionFailedListener));
    }

    protected zzk(Context context, Looper looper, int i, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener, zzf com_google_android_gms_common_internal_zzf) {
        this(context, looper, zzm.zzP(context), i, com_google_android_gms_common_internal_zzf, connectionCallbacks, onConnectionFailedListener);
    }

    protected zzk(Context context, Looper looper, zzm com_google_android_gms_common_internal_zzm, int i, zzf com_google_android_gms_common_internal_zzf) {
        this.zznh = new Object();
        this.zzPJ = false;
        this.zzPM = new ArrayList();
        this.zzPO = 1;
        this.zzPP = false;
        this.mContext = (Context) zzx.zzb((Object) context, (Object) "Context must not be null");
        this.zzMc = (Looper) zzx.zzb((Object) looper, (Object) "Looper must not be null");
        this.zzPH = (zzm) zzx.zzb((Object) com_google_android_gms_common_internal_zzm, (Object) "Supervisor must not be null");
        this.zzMu = new zzl(looper, this);
        this.mHandler = new zzb(this, looper);
        this.zzPQ = i;
        this.zzPG = (zzf) zzx.zzl(com_google_android_gms_common_internal_zzf);
        this.zzFN = com_google_android_gms_common_internal_zzf.getAccount();
        this.zzMM = zzh(com_google_android_gms_common_internal_zzf.zziP());
    }

    protected zzk(Context context, Looper looper, zzm com_google_android_gms_common_internal_zzm, int i, zzf com_google_android_gms_common_internal_zzf, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        this(context, looper, com_google_android_gms_common_internal_zzm, i, com_google_android_gms_common_internal_zzf);
        registerConnectionCallbacks((ConnectionCallbacks) zzx.zzl(connectionCallbacks));
        registerConnectionFailedListener((OnConnectionFailedListener) zzx.zzl(onConnectionFailedListener));
    }

    private void zza(int i, T t) {
        boolean z = true;
        if ((i == 3) != (t != null)) {
            z = false;
        }
        zzx.zzO(z);
        synchronized (this.zznh) {
            this.zzPO = i;
            this.zzPL = t;
        }
    }

    private boolean zza(int i, int i2, T t) {
        boolean z;
        synchronized (this.zznh) {
            if (this.zzPO != i) {
                z = false;
            } else {
                zza(i2, (IInterface) t);
                z = true;
            }
        }
        return z;
    }

    private List<String> zzh(List<String> list) {
        List<String> zzf = zzf((List) list);
        if (zzf == null || zzf == list) {
            return zzf;
        }
        for (String contains : zzf) {
            if (!list.contains(contains)) {
                throw new IllegalStateException("Expanding scopes is not permitted, use implied scopes instead");
            }
        }
        return zzf;
    }

    public void connect() {
        this.zzPP = true;
        zza(2, null);
        int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mContext);
        if (isGooglePlayServicesAvailable != 0) {
            zza(1, null);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, Integer.valueOf(isGooglePlayServicesAvailable)));
            return;
        }
        if (this.zzPN != null) {
            Log.e("GmsClient", "Calling connect() while still connected, missing disconnect() for " + zzcF());
            this.zzPH.zzb(zzcF(), this.zzPN, zziZ());
        }
        this.zzPN = new zze(this);
        if (!this.zzPH.zza(zzcF(), this.zzPN, zziZ())) {
            Log.e("GmsClient", "unable to connect to service: " + zzcF());
            this.mHandler.sendMessage(this.mHandler.obtainMessage(3, Integer.valueOf(9)));
        }
    }

    public void disconnect() {
        this.zzPP = false;
        synchronized (this.zzPM) {
            int size = this.zzPM.size();
            for (int i = 0; i < size; i++) {
                ((zzc) this.zzPM.get(i)).zzjh();
            }
            this.zzPM.clear();
        }
        zza(1, null);
        if (this.zzPN != null) {
            this.zzPH.zzb(zzcF(), this.zzPN, zziZ());
            this.zzPN = null;
        }
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.append(str).println("GmsClient:");
        CharSequence charSequence = str + "  ";
        printWriter.append(charSequence).append("mStartServiceAction=").println(zzcF());
        synchronized (this.zznh) {
            int i = this.zzPO;
            IInterface iInterface = this.zzPL;
        }
        printWriter.append(charSequence).append("mConnectState=");
        switch (i) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                printWriter.print("DISCONNECTED");
                break;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                printWriter.print("CONNECTING");
                break;
            case F2m.PPB /*3*/:
                printWriter.print("CONNECTED");
                break;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                printWriter.print("DISCONNECTING");
                break;
            default:
                printWriter.print(PaymentFramework.CARD_TYPE_UNKNOWN);
                break;
        }
        printWriter.append(" mService=");
        if (iInterface == null) {
            printWriter.println("null");
        } else {
            printWriter.append(zzcG()).append("@").println(Integer.toHexString(System.identityHashCode(iInterface.asBinder())));
        }
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final Looper getLooper() {
        return this.zzMc;
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.zznh) {
            z = this.zzPO == 3;
        }
        return z;
    }

    public boolean isConnecting() {
        boolean z;
        synchronized (this.zznh) {
            z = this.zzPO == 2;
        }
        return z;
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        this.zzMu.registerConnectionCallbacks(connectionCallbacks);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        this.zzMu.registerConnectionFailedListener(onConnectionFailedListener);
    }

    protected void zza(int i, IBinder iBinder, Bundle bundle) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1, new zzf(this, i, iBinder, bundle)));
    }

    public void zza(com.google.android.gms.common.api.GoogleApiClient.zza com_google_android_gms_common_api_GoogleApiClient_zza) {
        this.zzPK = (com.google.android.gms.common.api.GoogleApiClient.zza) zzx.zzb((Object) com_google_android_gms_common_api_GoogleApiClient_zza, (Object) "Must provide a non-null ConnectionStatusReportCallbacks");
        this.zzPJ = true;
    }

    @Deprecated
    public final void zza(zzc<?> com_google_android_gms_common_internal_zzk_zzc_) {
        synchronized (this.zzPM) {
            this.zzPM.add(com_google_android_gms_common_internal_zzk_zzc_);
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(2, com_google_android_gms_common_internal_zzk_zzc_));
    }

    public void zza(zzq com_google_android_gms_common_internal_zzq) {
        try {
            this.zzPI.zza(new zzd(this), new zzae(com_google_android_gms_common_internal_zzq, this.zzMM == null ? null : zzmh.zzi(this.zzMM), this.mContext.getPackageName(), zzjc()));
        } catch (DeadObjectException e) {
            Log.w("GmsClient", "service died");
            zzaI(1);
        } catch (Throwable e2) {
            Log.w("GmsClient", "Remote exception occurred", e2);
        }
    }

    public void zzaI(int i) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(4, Integer.valueOf(i)));
    }

    protected void zzb(int i, Bundle bundle) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(5, new zzh(this, i, bundle)));
    }

    public void zzb(zzq com_google_android_gms_common_internal_zzq) {
        try {
            zzi zzi = new zzi(this.zzPQ).zzbg(this.mContext.getPackageName()).zzi(zzhq());
            if (this.zzMM != null) {
                zzi.zza(zzmh.zzi(this.zzMM));
            }
            if (zzhc()) {
                zzi.zzb(zziN()).zzd(com_google_android_gms_common_internal_zzq);
            } else if (zzjd()) {
                zzi.zzb(this.zzFN);
            }
            this.zzPI.zza(new zzd(this), zzi);
        } catch (DeadObjectException e) {
            Log.w("GmsClient", "service died");
            zzaI(1);
        } catch (Throwable e2) {
            Log.w("GmsClient", "Remote exception occurred", e2);
        }
    }

    protected abstract String zzcF();

    protected abstract String zzcG();

    protected List<String> zzf(List<String> list) {
        return list;
    }

    protected final void zzfc() {
        if (!isConnected()) {
            throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
        }
    }

    public boolean zzhc() {
        return false;
    }

    public Bundle zzhp() {
        return null;
    }

    protected Bundle zzhq() {
        return new Bundle();
    }

    public final Account zziN() {
        return this.zzFN != null ? this.zzFN : new Account("<<default account>>", GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
    }

    public final List<String> zziP() {
        return this.zzMM;
    }

    protected String zziZ() {
        return this.zzPG.zziS();
    }

    public boolean zzin() {
        return this.zzPP;
    }

    protected final zzf zzja() {
        return this.zzPG;
    }

    public final T zzjb() {
        T t;
        synchronized (this.zznh) {
            if (this.zzPO == 4) {
                throw new DeadObjectException();
            }
            zzfc();
            zzx.zza(this.zzPL != null, (Object) "Client is connected but service is null");
            t = this.zzPL;
        }
        return t;
    }

    protected Bundle zzjc() {
        return null;
    }

    public boolean zzjd() {
        return false;
    }

    protected abstract T zzp(IBinder iBinder);
}
