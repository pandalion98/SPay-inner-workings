package com.google.android.gms.common;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Looper;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class zza implements ServiceConnection {
    boolean zzLp;
    private final BlockingQueue<IBinder> zzLq;

    public zza() {
        this.zzLp = false;
        this.zzLq = new LinkedBlockingQueue();
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.zzLq.add(iBinder);
    }

    public void onServiceDisconnected(ComponentName componentName) {
    }

    public IBinder zzhJ() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("BlockingServiceConnection.getService() called on main thread");
        } else if (this.zzLp) {
            throw new IllegalStateException();
        } else {
            this.zzLp = true;
            return (IBinder) this.zzLq.take();
        }
    }
}
