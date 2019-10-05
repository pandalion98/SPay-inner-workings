/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  com.google.android.gms.common.ConnectionResult
 *  com.google.android.gms.common.GooglePlayServicesUtil
 *  com.google.android.gms.common.api.Api
 *  com.google.android.gms.common.api.GoogleApiClient
 *  com.google.android.gms.common.api.GoogleApiClient$Builder
 *  com.google.android.gms.common.api.GoogleApiClient$ConnectionCallbacks
 *  com.google.android.gms.common.api.GoogleApiClient$OnConnectionFailedListener
 *  com.google.android.gms.location.LocationServices
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 */
package com.samsung.android.spayfw.utils;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.samsung.android.spayfw.b.Log;

public class d {
    private static d CX;
    private GoogleApiClient.ConnectionCallbacks CY = null;
    private GoogleApiClient.OnConnectionFailedListener CZ = null;
    private boolean Da = false;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient = null;

    private d(Context context) {
        this.mContext = context;
    }

    public static d ag(Context context) {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            if (CX == null) {
                CX = new d(context);
            }
            d d2 = CX;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return d2;
        }
    }

    public static d fH() {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            d d2 = CX;
            // ** MonitorExit[var2] (shouldn't be in output)
            return d2;
        }
    }

    public GoogleApiClient fI() {
        if (this.Da) {
            Log.i("GoogleApiHelper", " Google Api Client Suspended. so return null");
            return null;
        }
        return this.mGoogleApiClient;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void fJ() {
        d d2 = this;
        synchronized (d2) {
            block10 : {
                this.CY = new GoogleApiClient.ConnectionCallbacks(){

                    public void onConnected(Bundle bundle) {
                        Log.d("GoogleApiHelper", "google api client connected");
                        d.this.Da = false;
                    }

                    public void onConnectionSuspended(int n2) {
                        Log.i("GoogleApiHelper", "google api client connection suspended: " + n2);
                        d.this.Da = true;
                    }
                };
                this.CZ = new GoogleApiClient.OnConnectionFailedListener(){

                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.w("GoogleApiHelper", "google api client connection failed");
                    }
                };
                try {
                    int n2 = GooglePlayServicesUtil.isGooglePlayServicesAvailable((Context)this.mContext);
                    Log.d("GoogleApiHelper", "google play services available = " + n2);
                    if (n2 != 0) break block10;
                }
                catch (Exception exception) {
                    Log.e("GoogleApiHelper", "Exception in build Google API Client");
                    return;
                }
                Log.d("GoogleApiHelper", "google play services available = ");
                this.mGoogleApiClient = new GoogleApiClient.Builder(this.mContext).addConnectionCallbacks(this.CY).addOnConnectionFailedListener(this.CZ).addApi(LocationServices.API).build();
                if (this.mGoogleApiClient != null) {
                    this.mGoogleApiClient.connect();
                }
                do {
                    return;
                    break;
                } while (true);
            }
            Log.d("GoogleApiHelper", "google play services not available");
            return;
        }
    }

}

