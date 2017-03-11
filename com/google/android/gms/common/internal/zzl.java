package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import java.util.ArrayList;
import java.util.Iterator;

public final class zzl implements Callback {
    private final Handler mHandler;
    private final zza zzPX;
    private final ArrayList<ConnectionCallbacks> zzPY;
    final ArrayList<ConnectionCallbacks> zzPZ;
    private boolean zzQa;
    private final ArrayList<OnConnectionFailedListener> zzQb;

    public interface zza {
        boolean isConnected();

        Bundle zzhp();

        boolean zzin();
    }

    public zzl(Looper looper, zza com_google_android_gms_common_internal_zzl_zza) {
        this.zzPY = new ArrayList();
        this.zzPZ = new ArrayList();
        this.zzQa = false;
        this.zzQb = new ArrayList();
        this.zzPX = com_google_android_gms_common_internal_zzl_zza;
        this.mHandler = new Handler(looper, this);
    }

    public boolean handleMessage(Message message) {
        if (message.what == 1) {
            ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) message.obj;
            synchronized (this.zzPY) {
                if (this.zzPX.zzin() && this.zzPX.isConnected() && this.zzPY.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnected(this.zzPX.zzhp());
                }
            }
            return true;
        }
        Log.wtf("GmsClientEvents", "Don't know how to handle this message.");
        return false;
    }

    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        boolean contains;
        zzx.zzl(connectionCallbacks);
        synchronized (this.zzPY) {
            contains = this.zzPY.contains(connectionCallbacks);
        }
        return contains;
    }

    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        boolean contains;
        zzx.zzl(onConnectionFailedListener);
        synchronized (this.zzQb) {
            contains = this.zzQb.contains(onConnectionFailedListener);
        }
        return contains;
    }

    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        zzx.zzl(connectionCallbacks);
        synchronized (this.zzPY) {
            if (this.zzPY.contains(connectionCallbacks)) {
                Log.w("GmsClientEvents", "registerConnectionCallbacks(): listener " + connectionCallbacks + " is already registered");
            } else {
                this.zzPY.add(connectionCallbacks);
            }
        }
        if (this.zzPX.isConnected()) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1, connectionCallbacks));
        }
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzl(onConnectionFailedListener);
        synchronized (this.zzQb) {
            if (this.zzQb.contains(onConnectionFailedListener)) {
                Log.w("GmsClientEvents", "registerConnectionFailedListener(): listener " + onConnectionFailedListener + " is already registered");
            } else {
                this.zzQb.add(onConnectionFailedListener);
            }
        }
    }

    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        zzx.zzl(connectionCallbacks);
        synchronized (this.zzPY) {
            if (!this.zzPY.remove(connectionCallbacks)) {
                Log.w("GmsClientEvents", "unregisterConnectionCallbacks(): listener " + connectionCallbacks + " not found");
            } else if (this.zzQa) {
                this.zzPZ.add(connectionCallbacks);
            }
        }
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        zzx.zzl(onConnectionFailedListener);
        synchronized (this.zzQb) {
            if (!this.zzQb.remove(onConnectionFailedListener)) {
                Log.w("GmsClientEvents", "unregisterConnectionFailedListener(): listener " + onConnectionFailedListener + " not found");
            }
        }
    }

    public void zzaJ(int i) {
        this.mHandler.removeMessages(1);
        synchronized (this.zzPY) {
            this.zzQa = true;
            Iterator it = new ArrayList(this.zzPY).iterator();
            while (it.hasNext()) {
                ConnectionCallbacks connectionCallbacks = (ConnectionCallbacks) it.next();
                if (!this.zzPX.zzin()) {
                    break;
                } else if (this.zzPY.contains(connectionCallbacks)) {
                    connectionCallbacks.onConnectionSuspended(i);
                }
            }
            this.zzPZ.clear();
            this.zzQa = false;
        }
    }

    protected void zzfe() {
        synchronized (this.zzPY) {
            zzj(this.zzPX.zzhp());
        }
    }

    public void zzg(ConnectionResult connectionResult) {
        this.mHandler.removeMessages(1);
        synchronized (this.zzQb) {
            Iterator it = new ArrayList(this.zzQb).iterator();
            while (it.hasNext()) {
                OnConnectionFailedListener onConnectionFailedListener = (OnConnectionFailedListener) it.next();
                if (!this.zzPX.zzin()) {
                    return;
                } else if (this.zzQb.contains(onConnectionFailedListener)) {
                    onConnectionFailedListener.onConnectionFailed(connectionResult);
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void zzj(android.os.Bundle r6) {
        /*
        r5 = this;
        r1 = 0;
        r0 = 1;
        r3 = r5.zzPY;
        monitor-enter(r3);
        r2 = r5.zzQa;	 Catch:{ all -> 0x0062 }
        if (r2 != 0) goto L_0x0052;
    L_0x0009:
        r2 = r0;
    L_0x000a:
        com.google.android.gms.common.internal.zzx.zzN(r2);	 Catch:{ all -> 0x0062 }
        r2 = r5.mHandler;	 Catch:{ all -> 0x0062 }
        r4 = 1;
        r2.removeMessages(r4);	 Catch:{ all -> 0x0062 }
        r2 = 1;
        r5.zzQa = r2;	 Catch:{ all -> 0x0062 }
        r2 = r5.zzPZ;	 Catch:{ all -> 0x0062 }
        r2 = r2.size();	 Catch:{ all -> 0x0062 }
        if (r2 != 0) goto L_0x0054;
    L_0x001e:
        com.google.android.gms.common.internal.zzx.zzN(r0);	 Catch:{ all -> 0x0062 }
        r0 = new java.util.ArrayList;	 Catch:{ all -> 0x0062 }
        r1 = r5.zzPY;	 Catch:{ all -> 0x0062 }
        r0.<init>(r1);	 Catch:{ all -> 0x0062 }
        r1 = r0.iterator();	 Catch:{ all -> 0x0062 }
    L_0x002c:
        r0 = r1.hasNext();	 Catch:{ all -> 0x0062 }
        if (r0 == 0) goto L_0x0048;
    L_0x0032:
        r0 = r1.next();	 Catch:{ all -> 0x0062 }
        r0 = (com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks) r0;	 Catch:{ all -> 0x0062 }
        r2 = r5.zzPX;	 Catch:{ all -> 0x0062 }
        r2 = r2.zzin();	 Catch:{ all -> 0x0062 }
        if (r2 == 0) goto L_0x0048;
    L_0x0040:
        r2 = r5.zzPX;	 Catch:{ all -> 0x0062 }
        r2 = r2.isConnected();	 Catch:{ all -> 0x0062 }
        if (r2 != 0) goto L_0x0056;
    L_0x0048:
        r0 = r5.zzPZ;	 Catch:{ all -> 0x0062 }
        r0.clear();	 Catch:{ all -> 0x0062 }
        r0 = 0;
        r5.zzQa = r0;	 Catch:{ all -> 0x0062 }
        monitor-exit(r3);	 Catch:{ all -> 0x0062 }
        return;
    L_0x0052:
        r2 = r1;
        goto L_0x000a;
    L_0x0054:
        r0 = r1;
        goto L_0x001e;
    L_0x0056:
        r2 = r5.zzPZ;	 Catch:{ all -> 0x0062 }
        r2 = r2.contains(r0);	 Catch:{ all -> 0x0062 }
        if (r2 != 0) goto L_0x002c;
    L_0x005e:
        r0.onConnected(r6);	 Catch:{ all -> 0x0062 }
        goto L_0x002c;
    L_0x0062:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0062 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzl.zzj(android.os.Bundle):void");
    }
}
