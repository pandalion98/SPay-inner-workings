package com.google.android.gms.internal;

import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.internal.zztd.zza;

public class zzti implements ConnectionCallbacks, OnConnectionFailedListener {
    private zztl zzatK;
    private final zza zzatU;
    private boolean zzatV;

    public zzti(zza com_google_android_gms_internal_zztd_zza) {
        this.zzatU = com_google_android_gms_internal_zztd_zza;
        this.zzatK = null;
        this.zzatV = true;
    }

    public void onConnected(Bundle bundle) {
        this.zzatK.zzae(false);
        if (this.zzatV && this.zzatU != null) {
            this.zzatU.zzrE();
        }
        this.zzatV = false;
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        this.zzatK.zzae(true);
        if (this.zzatV && this.zzatU != null) {
            if (connectionResult.hasResolution()) {
                this.zzatU.zzf(connectionResult.getResolution());
            } else {
                this.zzatU.zzrF();
            }
        }
        this.zzatV = false;
    }

    public void onConnectionSuspended(int i) {
        this.zzatK.zzae(true);
    }

    public void zza(zztl com_google_android_gms_internal_zztl) {
        this.zzatK = com_google_android_gms_internal_zztl;
    }

    public void zzad(boolean z) {
        this.zzatV = z;
    }
}
