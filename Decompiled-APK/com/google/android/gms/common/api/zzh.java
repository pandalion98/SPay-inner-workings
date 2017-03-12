package com.google.android.gms.common.api;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.SparseArray;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzx;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class zzh extends Fragment implements OnCancelListener, LoaderCallbacks<ConnectionResult> {
    private boolean zzNu;
    private int zzNv;
    private ConnectionResult zzNw;
    private final Handler zzNx;
    private final SparseArray<zzb> zzNy;

    static class zza extends Loader<ConnectionResult> implements ConnectionCallbacks, OnConnectionFailedListener {
        private boolean zzNA;
        private ConnectionResult zzNB;
        public final GoogleApiClient zzNz;

        public zza(Context context, GoogleApiClient googleApiClient) {
            super(context);
            this.zzNz = googleApiClient;
        }

        private void zze(ConnectionResult connectionResult) {
            this.zzNB = connectionResult;
            if (isStarted() && !isAbandoned()) {
                deliverResult(connectionResult);
            }
        }

        public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            super.dump(str, fileDescriptor, printWriter, strArr);
            this.zzNz.dump(str, fileDescriptor, printWriter, strArr);
        }

        public void onConnected(Bundle bundle) {
            this.zzNA = false;
            zze(ConnectionResult.zzLr);
        }

        public void onConnectionFailed(ConnectionResult connectionResult) {
            this.zzNA = true;
            zze(connectionResult);
        }

        public void onConnectionSuspended(int i) {
        }

        protected void onReset() {
            this.zzNB = null;
            this.zzNA = false;
            this.zzNz.unregisterConnectionCallbacks(this);
            this.zzNz.unregisterConnectionFailedListener(this);
            this.zzNz.disconnect();
        }

        protected void onStartLoading() {
            super.onStartLoading();
            this.zzNz.registerConnectionCallbacks(this);
            this.zzNz.registerConnectionFailedListener(this);
            if (this.zzNB != null) {
                deliverResult(this.zzNB);
            }
            if (!this.zzNz.isConnected() && !this.zzNz.isConnecting() && !this.zzNA) {
                this.zzNz.connect();
            }
        }

        protected void onStopLoading() {
            this.zzNz.disconnect();
        }

        public boolean zzir() {
            return this.zzNA;
        }
    }

    private static class zzb {
        public final OnConnectionFailedListener zzNC;
        public final GoogleApiClient zzNz;

        private zzb(GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
            this.zzNz = googleApiClient;
            this.zzNC = onConnectionFailedListener;
        }
    }

    private class zzc implements Runnable {
        private final int zzND;
        private final ConnectionResult zzNE;
        final /* synthetic */ zzh zzNF;

        public zzc(zzh com_google_android_gms_common_api_zzh, int i, ConnectionResult connectionResult) {
            this.zzNF = com_google_android_gms_common_api_zzh;
            this.zzND = i;
            this.zzNE = connectionResult;
        }

        public void run() {
            if (this.zzNE.hasResolution()) {
                try {
                    this.zzNE.startResolutionForResult(this.zzNF.getActivity(), ((this.zzNF.getActivity().getSupportFragmentManager().getFragments().indexOf(this.zzNF) + 1) << 16) + 1);
                } catch (SendIntentException e) {
                    this.zzNF.zziq();
                }
            } else if (GooglePlayServicesUtil.isUserRecoverableError(this.zzNE.getErrorCode())) {
                GooglePlayServicesUtil.showErrorDialogFragment(this.zzNE.getErrorCode(), this.zzNF.getActivity(), this.zzNF, 2, this.zzNF);
            } else {
                this.zzNF.zzb(this.zzND, this.zzNE);
            }
        }
    }

    public zzh() {
        this.zzNv = -1;
        this.zzNx = new Handler(Looper.getMainLooper());
        this.zzNy = new SparseArray();
    }

    public static zzh zza(FragmentActivity fragmentActivity) {
        zzx.zzbd("Must be called from main thread of process");
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        try {
            zzh com_google_android_gms_common_api_zzh = (zzh) supportFragmentManager.findFragmentByTag("GmsSupportLifecycleFragment");
            if (com_google_android_gms_common_api_zzh != null && !com_google_android_gms_common_api_zzh.isRemoving()) {
                return com_google_android_gms_common_api_zzh;
            }
            Fragment com_google_android_gms_common_api_zzh2 = new zzh();
            supportFragmentManager.beginTransaction().add(com_google_android_gms_common_api_zzh2, "GmsSupportLifecycleFragment").commit();
            supportFragmentManager.executePendingTransactions();
            return com_google_android_gms_common_api_zzh2;
        } catch (Throwable e) {
            throw new IllegalStateException("Fragment with tag GmsSupportLifecycleFragment is not a SupportLifecycleFragment", e);
        }
    }

    private void zza(int i, ConnectionResult connectionResult) {
        if (!this.zzNu) {
            this.zzNu = true;
            this.zzNv = i;
            this.zzNw = connectionResult;
            this.zzNx.post(new zzc(this, i, connectionResult));
        }
    }

    private void zzb(int i, ConnectionResult connectionResult) {
        Log.w("GmsSupportLifecycleFragment", "Unresolved error while connecting client. Stopping auto-manage.");
        zzb com_google_android_gms_common_api_zzh_zzb = (zzb) this.zzNy.get(i);
        if (com_google_android_gms_common_api_zzh_zzb != null) {
            zzas(i);
            OnConnectionFailedListener onConnectionFailedListener = com_google_android_gms_common_api_zzh_zzb.zzNC;
            if (onConnectionFailedListener != null) {
                onConnectionFailedListener.onConnectionFailed(connectionResult);
            }
        }
        zziq();
    }

    private void zziq() {
        int i = 0;
        this.zzNu = false;
        this.zzNv = -1;
        this.zzNw = null;
        LoaderManager loaderManager = getLoaderManager();
        while (i < this.zzNy.size()) {
            int keyAt = this.zzNy.keyAt(i);
            zza zzat = zzat(keyAt);
            if (zzat != null && zzat.zzir()) {
                loaderManager.destroyLoader(keyAt);
                loaderManager.initLoader(keyAt, null, this);
            }
            i++;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityResult(int r4, int r5, android.content.Intent r6) {
        /*
        r3 = this;
        r0 = 1;
        r1 = 0;
        switch(r4) {
            case 1: goto L_0x0017;
            case 2: goto L_0x000c;
            default: goto L_0x0005;
        };
    L_0x0005:
        r0 = r1;
    L_0x0006:
        if (r0 == 0) goto L_0x001b;
    L_0x0008:
        r3.zziq();
    L_0x000b:
        return;
    L_0x000c:
        r2 = r3.getActivity();
        r2 = com.google.android.gms.common.GooglePlayServicesUtil.isGooglePlayServicesAvailable(r2);
        if (r2 != 0) goto L_0x0005;
    L_0x0016:
        goto L_0x0006;
    L_0x0017:
        r2 = -1;
        if (r5 != r2) goto L_0x0005;
    L_0x001a:
        goto L_0x0006;
    L_0x001b:
        r0 = r3.zzNv;
        r1 = r3.zzNw;
        r3.zzb(r0, r1);
        goto L_0x000b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.api.zzh.onActivityResult(int, int, android.content.Intent):void");
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        int i = 0;
        while (i < this.zzNy.size()) {
            int keyAt = this.zzNy.keyAt(i);
            zza zzat = zzat(keyAt);
            if (zzat == null || ((zzb) this.zzNy.valueAt(i)).zzNz == zzat.zzNz) {
                getLoaderManager().initLoader(keyAt, null, this);
            } else {
                getLoaderManager().restartLoader(keyAt, null, this);
            }
            i++;
        }
    }

    public void onCancel(DialogInterface dialogInterface) {
        zzb(this.zzNv, this.zzNw);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.zzNu = bundle.getBoolean("resolving_error", false);
            this.zzNv = bundle.getInt("failed_client_id", -1);
            if (this.zzNv >= 0) {
                this.zzNw = new ConnectionResult(bundle.getInt("failed_status"), (PendingIntent) bundle.getParcelable("failed_resolution"));
            }
        }
    }

    public Loader<ConnectionResult> onCreateLoader(int i, Bundle bundle) {
        return new zza(getActivity(), ((zzb) this.zzNy.get(i)).zzNz);
    }

    public /* synthetic */ void onLoadFinished(Loader loader, Object obj) {
        zza(loader, (ConnectionResult) obj);
    }

    public void onLoaderReset(Loader<ConnectionResult> loader) {
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("resolving_error", this.zzNu);
        if (this.zzNv >= 0) {
            bundle.putInt("failed_client_id", this.zzNv);
            bundle.putInt("failed_status", this.zzNw.getErrorCode());
            bundle.putParcelable("failed_resolution", this.zzNw.getResolution());
        }
    }

    public void onStart() {
        super.onStart();
        if (!this.zzNu) {
            for (int i = 0; i < this.zzNy.size(); i++) {
                getLoaderManager().initLoader(this.zzNy.keyAt(i), null, this);
            }
        }
    }

    public void zza(int i, GoogleApiClient googleApiClient, OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzb((Object) googleApiClient, (Object) "GoogleApiClient instance cannot be null");
        zzx.zza(this.zzNy.indexOfKey(i) < 0, "Already managing a GoogleApiClient with id " + i);
        this.zzNy.put(i, new zzb(onConnectionFailedListener, null));
        if (getActivity() != null) {
            LoaderManager.enableDebugLogging(false);
            getLoaderManager().initLoader(i, null, this);
        }
    }

    public void zza(Loader<ConnectionResult> loader, ConnectionResult connectionResult) {
        if (!connectionResult.isSuccess()) {
            zza(loader.getId(), connectionResult);
        }
    }

    public GoogleApiClient zzar(int i) {
        if (getActivity() != null) {
            zza zzat = zzat(i);
            if (zzat != null) {
                return zzat.zzNz;
            }
        }
        return null;
    }

    public void zzas(int i) {
        this.zzNy.remove(i);
        getLoaderManager().destroyLoader(i);
    }

    zza zzat(int i) {
        try {
            return (zza) getLoaderManager().getLoader(i);
        } catch (Throwable e) {
            throw new IllegalStateException("Unknown loader in SupportLifecycleFragment", e);
        }
    }
}
