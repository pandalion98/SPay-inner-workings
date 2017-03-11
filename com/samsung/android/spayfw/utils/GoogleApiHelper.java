package com.samsung.android.spayfw.utils;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.Builder;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.samsung.android.spayfw.p002b.Log;

/* renamed from: com.samsung.android.spayfw.utils.d */
public class GoogleApiHelper {
    private static GoogleApiHelper CX;
    private ConnectionCallbacks CY;
    private OnConnectionFailedListener CZ;
    private boolean Da;
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;

    /* renamed from: com.samsung.android.spayfw.utils.d.1 */
    class GoogleApiHelper implements ConnectionCallbacks {
        final /* synthetic */ GoogleApiHelper Db;

        GoogleApiHelper(GoogleApiHelper googleApiHelper) {
            this.Db = googleApiHelper;
        }

        public void onConnected(Bundle bundle) {
            Log.m285d("GoogleApiHelper", "google api client connected");
            this.Db.Da = false;
        }

        public void onConnectionSuspended(int i) {
            Log.m287i("GoogleApiHelper", "google api client connection suspended: " + i);
            this.Db.Da = true;
        }
    }

    /* renamed from: com.samsung.android.spayfw.utils.d.2 */
    class GoogleApiHelper implements OnConnectionFailedListener {
        final /* synthetic */ GoogleApiHelper Db;

        GoogleApiHelper(GoogleApiHelper googleApiHelper) {
            this.Db = googleApiHelper;
        }

        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.m290w("GoogleApiHelper", "google api client connection failed");
        }
    }

    public static synchronized GoogleApiHelper ag(Context context) {
        GoogleApiHelper googleApiHelper;
        synchronized (GoogleApiHelper.class) {
            if (CX == null) {
                CX = new GoogleApiHelper(context);
            }
            googleApiHelper = CX;
        }
        return googleApiHelper;
    }

    public static synchronized GoogleApiHelper fH() {
        GoogleApiHelper googleApiHelper;
        synchronized (GoogleApiHelper.class) {
            googleApiHelper = CX;
        }
        return googleApiHelper;
    }

    private GoogleApiHelper(Context context) {
        this.mGoogleApiClient = null;
        this.CY = null;
        this.CZ = null;
        this.Da = false;
        this.mContext = context;
    }

    public GoogleApiClient fI() {
        if (!this.Da) {
            return this.mGoogleApiClient;
        }
        Log.m287i("GoogleApiHelper", " Google Api Client Suspended. so return null");
        return null;
    }

    public synchronized void fJ() {
        this.CY = new GoogleApiHelper(this);
        this.CZ = new GoogleApiHelper(this);
        try {
            int isGooglePlayServicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.mContext);
            Log.m285d("GoogleApiHelper", "google play services available = " + isGooglePlayServicesAvailable);
            if (isGooglePlayServicesAvailable == 0) {
                Log.m285d("GoogleApiHelper", "google play services available = ");
                this.mGoogleApiClient = new Builder(this.mContext).addConnectionCallbacks(this.CY).addOnConnectionFailedListener(this.CZ).addApi(LocationServices.API).build();
                if (this.mGoogleApiClient != null) {
                    this.mGoogleApiClient.connect();
                }
            } else {
                Log.m285d("GoogleApiHelper", "google play services not available");
            }
        } catch (Exception e) {
            Log.m286e("GoogleApiHelper", "Exception in build Google API Client");
        }
    }
}
