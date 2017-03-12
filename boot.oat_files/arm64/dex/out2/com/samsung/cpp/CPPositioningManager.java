package com.samsung.cpp;

import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.cpp.ICPPLocationListener.Stub;
import java.util.HashMap;

public class CPPositioningManager {
    public static final int ERROR_EXCEPTION = -4;
    public static final int ERROR_ILLEGAL_ARGUMENT = -2;
    public static final int GEOFENCE_DWELL = 3;
    public static final String GEOFENCE_ID = "clientID";
    public static final int GEOFENCE_INBOUND = 1;
    public static final int GEOFENCE_OUTBOUND = 2;
    public static final String GPS_PROVIDER = "gps";
    public static final String INTENT_CLM_TT_GEO_FENCE_UPDATE = "android.intent.action.ACTION_CLM_TT_GEO_FENCE_UPDATE";
    public static final int LOCATION_TYPE_CELL_BASED = 0;
    public static final int LOCATION_TYPE_COMBINED = 1;
    public static final String NETWORK_PROVIDER = "network";
    public static final int OPERATION_SUCCESS = 0;
    private static final String TAG = "CPPositioningManager";
    private HashMap<CPPLocationListener, LocListenerTransport> mLocListeners = new HashMap();
    private final ICPPositioningService mService;

    private class LocListenerTransport extends Stub {
        public static final int TYPE_LOCATION_CHANGED = 1;
        private CPPLocationListener mListener;
        private final Handler mListenerHandler;

        LocListenerTransport(CPPLocationListener cppLocationListener) {
            this.mListener = cppLocationListener;
            this.mListenerHandler = new Handler(CPPositioningManager.this) {
                public void handleMessage(Message msg) {
                    LocListenerTransport.this._handleMessage(msg);
                }
            };
        }

        public void onLocationChanged(Location location) throws RemoteException {
            if (location == null) {
                Log.e(CPPositioningManager.TAG, "onLocationChanged location is null");
                return;
            }
            Log.e(CPPositioningManager.TAG, "LocListenerTransport.onLocationChanged : " + location.toString());
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = location;
            this.mListenerHandler.sendMessage(msg);
        }

        private void _handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    this.mListener.onLocationChanged(msg.obj);
                    return;
                default:
                    return;
            }
        }
    }

    public CPPositioningManager(ICPPositioningService service) {
        Log.e(TAG, TAG);
        this.mService = service;
    }

    public int requestLocationUpdates(int interval, int typeOfLoc, CPPLocationListener cppLocListener, String pkgName) {
        if (this.mService == null) {
            Log.e(TAG, "requestLocationUpdates: service is not supported");
            return -2;
        } else if (cppLocListener == null) {
            Log.e(TAG, "requestLocationUpdates: listener parameter is not vaild");
            return -2;
        } else {
            try {
                int requestLocationUpdates;
                synchronized (this.mLocListeners) {
                    LocListenerTransport transport = (LocListenerTransport) this.mLocListeners.get(cppLocListener);
                    if (transport == null) {
                        transport = new LocListenerTransport(cppLocListener);
                    }
                    Log.d(TAG, "requestLocationUpdates : interval(" + interval + "), typeOfLoc(" + typeOfLoc + "), pkgName(" + pkgName + ")");
                    this.mLocListeners.put(cppLocListener, transport);
                    requestLocationUpdates = this.mService.requestLocationUpdates(interval, typeOfLoc, transport, pkgName);
                }
                return requestLocationUpdates;
            } catch (RemoteException ex) {
                Log.e(TAG, "requestLocationUpdates: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int stopLocationUpdates(CPPLocationListener cppLocListener) {
        if (this.mService == null) {
            Log.e(TAG, "stopLocationUpdates: service is not supported");
            return -2;
        } else if (cppLocListener == null) {
            Log.e(TAG, "stopLocationUpdates: listener parameter is not vaild");
            return -2;
        } else {
            try {
                LocListenerTransport transport = (LocListenerTransport) this.mLocListeners.remove(cppLocListener);
                if (transport == null) {
                    Log.e(TAG, "stopLocationUpdates: Already stopped location");
                    return -4;
                }
                Log.d(TAG, "stopLocationUpdates");
                return this.mService.stopLocationUpdates(transport);
            } catch (RemoteException ex) {
                Log.e(TAG, "stopLocationUpdates: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int requestCPLocationUpdates(int interval, int minDist, int mode, CPPLocationListener cppCpLocListener) {
        if (this.mService == null) {
            Log.e(TAG, "requestCPLocationUpdates: service is not supported");
            return -2;
        } else if (cppCpLocListener == null) {
            Log.e(TAG, "requestCPLocationUpdates: listener parameter is not vaild");
            return -2;
        } else {
            try {
                int requestCPLocationUpdates;
                synchronized (this.mLocListeners) {
                    LocListenerTransport transport = (LocListenerTransport) this.mLocListeners.get(cppCpLocListener);
                    if (transport == null) {
                        transport = new LocListenerTransport(cppCpLocListener);
                    }
                    Log.d(TAG, "requestCPLocationUpdates : interval(" + interval + "), minDist(" + minDist + "), mode(" + mode + ")");
                    this.mLocListeners.put(cppCpLocListener, transport);
                    requestCPLocationUpdates = this.mService.requestCPLocationUpdates(interval, minDist, mode, transport);
                }
                return requestCPLocationUpdates;
            } catch (RemoteException ex) {
                Log.e(TAG, "requestCPLocationUpdates: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int stopCPLocationUpdates(CPPLocationListener cpplocListener) {
        if (this.mService == null) {
            Log.e(TAG, "stopCPLocationUpdates: service is not supported");
            return -2;
        } else if (cpplocListener == null) {
            Log.e(TAG, "stopCPLocationUpdates: listener parameter is not vaild");
            return -2;
        } else {
            try {
                LocListenerTransport transport = (LocListenerTransport) this.mLocListeners.remove(cpplocListener);
                if (transport == null) {
                    Log.e(TAG, "stopCPLocationUpdates: Already stopped location");
                    return -4;
                }
                Log.d(TAG, "stopCPLocationUpdates");
                return this.mService.stopCPLocationUpdates(transport);
            } catch (RemoteException ex) {
                Log.e(TAG, "stopCPLocationUpdates: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int registerCPGeoFence(double latitude, double longitude, int geoMode, int radius, int period) {
        if (this.mService == null) {
            Log.e(TAG, "registerCPGeoFence: service is not supported");
            return -2;
        }
        try {
            Log.d(TAG, "registerCPGeoFence : latitude(" + latitude + "), longitude(" + longitude + "), geoMode(" + geoMode + "), radius(" + radius + "), period(" + period + ")");
            return this.mService.registerCPGeoFence(latitude, longitude, geoMode, radius, period);
        } catch (RemoteException ex) {
            Log.d(TAG, "registerCPGeoFence: RemoteException " + ex.toString());
            return -4;
        }
    }

    public int deRegisterCPGeoFence(int clientID) {
        if (this.mService == null) {
            Log.e(TAG, "deRegisterCPGeoFence: service is not supported");
            return -2;
        }
        try {
            Log.d(TAG, "deRegisterCPGeoFence : cliendID(" + clientID + ")");
            return this.mService.deRegisterCPGeoFence(clientID);
        } catch (RemoteException ex) {
            Log.e(TAG, "deRegisterCPGeoFence: RemoteException " + ex.toString());
            return -4;
        }
    }
}
