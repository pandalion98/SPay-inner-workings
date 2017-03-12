package com.google.android.gms.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.internal.zzk;
import com.google.android.gms.internal.zzku.zza;

public class zzks extends zzk<zzku> {
    public zzks(Context context, Looper looper, ConnectionCallbacks connectionCallbacks, OnConnectionFailedListener onConnectionFailedListener) {
        super(context, looper, 39, connectionCallbacks, onConnectionFailedListener);
    }

    protected zzku zzY(IBinder iBinder) {
        return zza.zzaa(iBinder);
    }

    public String zzcF() {
        return "com.google.android.gms.common.service.START";
    }

    protected String zzcG() {
        return "com.google.android.gms.common.internal.service.ICommonService";
    }

    protected /* synthetic */ IInterface zzp(IBinder iBinder) {
        return zzY(iBinder);
    }
}
