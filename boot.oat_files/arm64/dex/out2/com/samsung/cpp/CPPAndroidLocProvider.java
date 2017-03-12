package com.samsung.cpp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.location.SLocationManager;
import java.util.HashMap;

public class CPPAndroidLocProvider {
    private static final String TAG = "CPPAndroidLocProvider";
    private final HashMap<ICPPLocationListener, androidLocListener> mLocListeners = new HashMap();
    private LocationManager mLocationManager;

    private static class androidLocListener implements LocationListener {
        private ICPPLocationListener mCpLocListener;

        androidLocListener(ICPPLocationListener cpLocListener) {
            this.mCpLocListener = cpLocListener;
        }

        public void onLocationChanged(Location location) {
            Log.d(CPPAndroidLocProvider.TAG, "onLocationChanged : Latitude : " + location.getLatitude() + "Longitude : " + location.getLongitude());
            try {
                this.mCpLocListener.onLocationChanged(location);
            } catch (RemoteException ex) {
                Log.e(CPPAndroidLocProvider.TAG, "onLocationChanged: RemoteException " + ex.toString());
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    }

    public CPPAndroidLocProvider(Context context) {
        Log.d(TAG, "CPPAndroidLocProvider : constructed");
        this.mLocationManager = (LocationManager) context.getSystemService(SLocationManager.GEOFENCE_LOCATION);
    }

    public void requestLocationUpdates(int interval, int typeOfLoc, ICPPLocationListener cppLocListener, String pkgName) {
        Log.d(TAG, "requestLocationUpdates  app name : " + pkgName + "Interval : " + interval + "Provider(0-GPS, 1-google NLP) : " + typeOfLoc);
        if (cppLocListener == null) {
            Log.e(TAG, "parameters are not valid");
            return;
        }
        synchronized (this.mLocListeners) {
            try {
                androidLocListener LocListener;
                androidLocListener LocListener2 = null;
                for (ICPPLocationListener record : this.mLocListeners.keySet()) {
                    try {
                        if (record.asBinder() == cppLocListener.asBinder()) {
                            LocListener = (androidLocListener) this.mLocListeners.get(record);
                        } else {
                            LocListener = LocListener2;
                        }
                        LocListener2 = LocListener;
                    } catch (SecurityException e) {
                        e.printStackTrace();
                        LocListener = LocListener2;
                    } catch (Throwable th) {
                        Throwable th2 = th;
                        LocListener = LocListener2;
                    }
                }
                if (LocListener2 == null) {
                    LocListener = new androidLocListener(cppLocListener);
                    this.mLocListeners.put(cppLocListener, LocListener);
                    Log.d(TAG, "requestLocationUpdates - new android Location listener created");
                } else {
                    this.mLocationManager.removeUpdates(LocListener2);
                    Log.d(TAG, "requestLocationUpdates: CallBack already present..re-registering ");
                    LocListener = LocListener2;
                }
                if (typeOfLoc == 0) {
                    this.mLocationManager.requestLocationUpdates(CPPositioningManager.GPS_PROVIDER, (long) interval, 0.0f, LocListener);
                }
                if (1 == typeOfLoc) {
                    this.mLocationManager.requestLocationUpdates(CPPositioningManager.NETWORK_PROVIDER, (long) interval, 0.0f, LocListener);
                }
            } catch (SecurityException e2) {
                e2.printStackTrace();
            } catch (Throwable th3) {
                th2 = th3;
                throw th2;
            }
        }
    }

    public void stopLocationUpdates(ICPPLocationListener cppLocListener) {
        Log.d(TAG, "stopLocationUpdates... ");
        androidLocListener LocListener = null;
        synchronized (this.mLocListeners) {
            for (ICPPLocationListener record : this.mLocListeners.keySet()) {
                if (record.asBinder() == cppLocListener.asBinder()) {
                    LocListener = (androidLocListener) this.mLocListeners.remove(record);
                }
            }
            if (LocListener == null) {
                Log.e(TAG, "All ready stopped location updates");
            } else {
                try {
                    this.mLocationManager.removeUpdates(LocListener);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
