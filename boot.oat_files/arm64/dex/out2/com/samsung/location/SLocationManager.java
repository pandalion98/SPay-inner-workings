package com.samsung.location;

import android.app.PendingIntent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.location.ISCurrentLocListener.Stub;
import java.util.HashMap;
import java.util.List;

public class SLocationManager {
    public static final String ACTION_SERVICE_READY = "com.samsung.location.SERVICE_READY";
    public static final int CURRENTLOCATION_POI_CATEGORY_ALL = 1;
    public static final int CURRENTLOCATION_POI_CATEGORY_ART_ENTERTAINMENT = 2;
    public static final int CURRENTLOCATION_POI_CATEGORY_COLLEGE_UNIVERSITY = 4;
    public static final int CURRENTLOCATION_POI_CATEGORY_FOOD = 8;
    public static final int CURRENTLOCATION_POI_CATEGORY_NONE = 0;
    public static final int CURRENTLOCATION_POI_CATEGORY_OUTDOORS_RECREATION = 16;
    public static final int CURRENTLOCATION_POI_CATEGORY_PROFESSIONAL = 32;
    public static final int CURRENTLOCATION_POI_CATEGORY_SHOP_SERVICE = 64;
    public static final int CURRENTLOCATION_POI_CATEGORY_TRAVEL_TRANSPORT = 128;
    public static final String CURRENT_LOCATION = "currentlocation";
    public static final String CURRENT_LOCATION_ADDRESS = "currentlocationaddress";
    public static final String CURRENT_LOCATION_POI = "currentlocationpoi";
    public static final int ERROR_ALREADY_STARTED = -5;
    public static final int ERROR_EXCEPTION = -4;
    public static final int ERROR_ID_NOT_EXIST = -3;
    public static final int ERROR_ILLEGAL_ARGUMENT = -2;
    public static final int ERROR_LOCATION_CURRENTLY_UNAVAILABLE = -100;
    public static final int ERROR_NOT_INITIALIZED = -1;
    public static final int ERROR_TOO_MANY_GEOFENCE = -6;
    public static final int GEOFENCE_ENTER = 1;
    public static final int GEOFENCE_EXIT = 2;
    public static final String GEOFENCE_LOCATION = "location";
    public static final String GEOFENCE_TRANSITION = "transition";
    public static final int GEOFENCE_TYPE_BT = 3;
    public static final int GEOFENCE_TYPE_GEOPOINT = 1;
    public static final int GEOFENCE_TYPE_WIFI = 2;
    public static final int GEOFENCE_UNKNOWN = 0;
    public static final int OPERATION_SUCCESS = 0;
    public static final String PERMISSION_ALWAYS_SCAN = "permissionalwaysscan";
    private static final String TAG = "SLocationManager";
    private HashMap<SCurrentLocListener, CurrentLocListenerTransport> mCurrentLocListeners = new HashMap();
    private HashMap<SLocationListener, LocListenerTransport> mLocListeners = new HashMap();
    private final ISLocationManager mService;

    private class CurrentLocListenerTransport extends Stub {
        public static final int TYPE_CURRENT_LOCATION = 1;
        private SCurrentLocListener mListener;
        private final Handler mListenerHandler;

        CurrentLocListenerTransport(SCurrentLocListener listener) {
            this.mListener = listener;
            this.mListenerHandler = new Handler(SLocationManager.this) {
                public void handleMessage(Message msg) {
                    CurrentLocListenerTransport.this._handleMessage(msg);
                }
            };
        }

        public void onCurrentLocation(Location location) {
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = location;
            this.mListenerHandler.sendMessage(msg);
        }

        private void _handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    this.mListener.onCurrentLocation((Location) msg.obj);
                    return;
                default:
                    return;
            }
        }
    }

    private class LocListenerTransport extends ISLocationListener.Stub {
        public static final int TYPE_LOCATION_AVAILABLE = 1;
        public static final int TYPE_LOCATION_AVAILABLE_POI = 2;
        private SLocationListener mListener;
        private final Handler mListenerHandler;

        LocListenerTransport(SLocationListener listener) {
            this.mListener = listener;
            this.mListenerHandler = new Handler(SLocationManager.this) {
                public void handleMessage(Message msg) {
                    LocListenerTransport.this._handleMessage(msg);
                }
            };
        }

        public void onLocationAvailable(Location[] locations) {
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = locations;
            this.mListenerHandler.sendMessage(msg);
        }

        public void onLocationChanged(Location location, Address address, String[] poi) {
            if (location == null) {
                Log.e(SLocationManager.TAG, "onLocationChanged location is null");
                return;
            }
            Message msg = Message.obtain();
            msg.what = 2;
            Bundle extras = new Bundle();
            extras.putParcelable(SLocationManager.CURRENT_LOCATION_ADDRESS, address);
            extras.putStringArray(SLocationManager.CURRENT_LOCATION_POI, poi);
            location.setExtras(extras);
            msg.obj = location;
            this.mListenerHandler.sendMessage(msg);
        }

        public void onLocationChanged(Location location, String address, String[] poi) {
        }

        private void _handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    this.mListener.onLocationAvailable((Location[]) msg.obj);
                    return;
                case 2:
                    Bundle extras = msg.obj.getExtras();
                    this.mListener.onLocationChanged((Location) msg.obj, (Address) extras.get(SLocationManager.CURRENT_LOCATION_ADDRESS), extras.getStringArray(SLocationManager.CURRENT_LOCATION_POI));
                    return;
                default:
                    return;
            }
        }
    }

    public SLocationManager(ISLocationManager service) {
        this.mService = service;
    }

    public int syncGeofence(List<Integer> geofenceIdList) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (geofenceIdList == null) {
            Log.e(TAG, "geofenceIdList is null");
            return -2;
        } else {
            int[] list = new int[geofenceIdList.size()];
            for (int i = 0; i < geofenceIdList.size(); i++) {
                list[i] = ((Integer) geofenceIdList.get(i)).intValue();
            }
            try {
                return this.mService.syncGeofence(list);
            } catch (RemoteException ex) {
                Log.e(TAG, "syncGeofence: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int addGeofence(SLocationParameter parameter) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (!isArgumentsValid(parameter)) {
            return -2;
        } else {
            try {
                return this.mService.addGeofence(parameter);
            } catch (RemoteException ex) {
                Log.e(TAG, "getGeofenceId : RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int removeGeofence(int geofenceId) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        }
        try {
            return this.mService.removeGeofence(geofenceId);
        } catch (RemoteException ex) {
            Log.e(TAG, "removeGeofence: RemoteException " + ex.toString());
            return -4;
        }
    }

    public int startGeofence(int geofenceId, PendingIntent intent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (intent == null) {
            Log.e(TAG, "intent is null");
            return -2;
        } else {
            try {
                return this.mService.startGeofence(geofenceId, intent);
            } catch (RemoteException ex) {
                Log.e(TAG, "startGeofence : RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int stopGeofence(int geofenceId, PendingIntent intent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (intent == null) {
            Log.e(TAG, "intent is null");
            return -2;
        } else {
            try {
                return this.mService.stopGeofence(geofenceId, intent);
            } catch (RemoteException ex) {
                Log.e(TAG, "stopGeofence: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    private boolean isArgumentsValid(SLocationParameter parameter) {
        int type = parameter.getType();
        if (type == 1 || type == 2 || type == 3) {
            if (type == 1) {
                double latitude = parameter.getLatitude();
                double longitude = parameter.getLongitude();
                int radius = parameter.getRadius();
                if (latitude < -90.0d || latitude > 90.0d) {
                    Log.e(TAG, "latitude is not correct");
                    return false;
                } else if (longitude < -180.0d || longitude > 180.0d) {
                    Log.e(TAG, "longitude is not correct");
                    return false;
                } else if (radius < 100) {
                    Log.e(TAG, "radius is not correct");
                    return false;
                }
            }
            if ((type != 2 && type != 3) || parameter.getBssid() != null) {
                return true;
            }
            Log.e(TAG, "bssid is null");
            return false;
        }
        Log.e(TAG, "geofenceType is not correct");
        return false;
    }

    public int startBatching(int period, SLocationListener listener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (listener == null || period <= 0) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                int startLocationBatching;
                synchronized (this.mLocListeners) {
                    LocListenerTransport transport = (LocListenerTransport) this.mLocListeners.get(listener);
                    if (transport == null) {
                        transport = new LocListenerTransport(listener);
                    }
                    this.mLocListeners.put(listener, transport);
                    startLocationBatching = this.mService.startLocationBatching(period, transport);
                }
                return startLocationBatching;
            } catch (RemoteException ex) {
                Log.e(TAG, "startBatching: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int stopBatching(int requestId, SLocationListener listener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (listener == null || requestId <= 0) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                if (((LocListenerTransport) this.mLocListeners.remove(listener)) != null) {
                    return this.mService.stopLocationBatching(requestId);
                }
                Log.e(TAG, "Already stopped geofence");
                return -3;
            } catch (RemoteException ex) {
                Log.e(TAG, "stopBatching: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int startLocationBatching(int period, SLocationListener listener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (listener == null || period <= 0) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                int startLocationBatching;
                synchronized (this.mLocListeners) {
                    LocListenerTransport transport = (LocListenerTransport) this.mLocListeners.get(listener);
                    if (transport == null) {
                        transport = new LocListenerTransport(listener);
                    }
                    this.mLocListeners.put(listener, transport);
                    startLocationBatching = this.mService.startLocationBatching(period, transport);
                }
                return startLocationBatching;
            } catch (RemoteException ex) {
                Log.e(TAG, "startBatching: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int stopLocationBatching(int requestId, SLocationListener listener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (listener == null || requestId <= 0) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                if (((LocListenerTransport) this.mLocListeners.remove(listener)) != null) {
                    return this.mService.stopLocationBatching(requestId);
                }
                Log.e(TAG, "Already stopped geofence");
                return -3;
            } catch (RemoteException ex) {
                Log.e(TAG, "stopBatching: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int requestBatchOfLocations() {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        }
        try {
            Log.e(TAG, "requestBatchOfLocations ");
            return this.mService.requestBatchOfLocations();
        } catch (RemoteException ex) {
            Log.e(TAG, "requestBatchOfLocations: RemoteException " + ex.toString());
            return -4;
        }
    }

    public int updateBatchingOptions(int requestId, int period) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (requestId <= 0 || period <= 0) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                return this.mService.updateBatchingOptions(requestId, period);
            } catch (RemoteException ex) {
                Log.e(TAG, "updateBatchingOptions: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int requestSingleLocation(int accuracy, int timeout, PendingIntent intent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (intent == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                return this.mService.requestSingleLocation(accuracy, timeout, 0, false, intent);
            } catch (RemoteException ex) {
                Log.e(TAG, "requestSingleLocation: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int requestSingleLocation(int accuracy, int timeout, int poicategory, boolean isAddress, PendingIntent intent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (intent == null || poicategory < 0) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                return this.mService.requestSingleLocation(accuracy, timeout, poicategory, isAddress, intent);
            } catch (RemoteException ex) {
                Log.e(TAG, "requestSingleLocation: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int removeSingleLocation(PendingIntent intent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (intent == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                return this.mService.removeSingleLocation(intent);
            } catch (RemoteException ex) {
                Log.e(TAG, "removeSingleLocation: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int requestLocation(boolean isAddress, boolean isPoi, SLocationListener listener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (listener == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                int requestLocation;
                synchronized (this.mLocListeners) {
                    LocListenerTransport transport = (LocListenerTransport) this.mLocListeners.get(listener);
                    if (transport == null) {
                        transport = new LocListenerTransport(listener);
                    }
                    this.mLocListeners.put(listener, transport);
                    requestLocation = this.mService.requestLocation(isAddress, isPoi, transport);
                }
                return requestLocation;
            } catch (RemoteException ex) {
                Log.e(TAG, "requestSingleLocation: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int removeLocation(SLocationListener listener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (listener == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                LocListenerTransport transport = (LocListenerTransport) this.mLocListeners.remove(listener);
                if (transport != null) {
                    return this.mService.removeLocation(transport);
                }
                Log.e(TAG, "Already stopped location");
                return -3;
            } catch (RemoteException ex) {
                Log.e(TAG, "removeLocation: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int requestLocationToPoi(double[] latitude, double[] longitude, PendingIntent intent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (intent == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                return this.mService.requestLocationToPoi(latitude, longitude, intent);
            } catch (RemoteException ex) {
                Log.e(TAG, "requestLocationToPoi: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int requestLocationToPoi(double latitude, double longitude, PendingIntent intent) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (intent == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                return this.mService.requestLocationToPoi(new double[]{latitude}, new double[]{longitude}, intent);
            } catch (RemoteException ex) {
                Log.e(TAG, "requestLocationToPoi: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int requestCurrentLocation(SCurrentLocListener listener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (listener == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                int requestCurrentLocation;
                synchronized (this.mCurrentLocListeners) {
                    CurrentLocListenerTransport transport = (CurrentLocListenerTransport) this.mCurrentLocListeners.get(listener);
                    if (transport == null) {
                        transport = new CurrentLocListenerTransport(listener);
                    }
                    this.mCurrentLocListeners.put(listener, transport);
                    Log.e(TAG, "requestCurrentLocation ");
                    requestCurrentLocation = this.mService.requestCurrentLocation(transport);
                }
                return requestCurrentLocation;
            } catch (RemoteException ex) {
                Log.e(TAG, "requestCurrentLocation: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public int removeCurrentLocation(int requestId, SCurrentLocListener listener) {
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
            return -1;
        } else if (listener == null) {
            Log.e(TAG, "parameters are not vaild");
            return -2;
        } else {
            try {
                synchronized (this.mCurrentLocListeners) {
                    if (((CurrentLocListenerTransport) this.mCurrentLocListeners.remove(listener)) == null) {
                        Log.e(TAG, "already removeCurrentLocation");
                        return -4;
                    }
                    Log.e(TAG, "removeCurrentLocation : " + requestId);
                    int removeCurrentLocation = this.mService.removeCurrentLocation(requestId);
                    return removeCurrentLocation;
                }
            } catch (RemoteException ex) {
                Log.e(TAG, "removeCurrentLocation: RemoteException " + ex.toString());
                return -4;
            }
        }
    }

    public boolean checkPassiveLocation() {
        boolean z = false;
        if (this.mService == null) {
            Log.e(TAG, "SLocationService is not supported");
        } else {
            try {
                Log.e(TAG, "checkPassiveLocation");
                z = this.mService.checkPassiveLocation();
            } catch (RemoteException ex) {
                Log.e(TAG, "checkPassiveLocation: RemoteException " + ex.toString());
            }
        }
        return z;
    }
}
