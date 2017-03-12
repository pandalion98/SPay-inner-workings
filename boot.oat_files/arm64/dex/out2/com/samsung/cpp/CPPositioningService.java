package com.samsung.cpp;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import com.samsung.cpp.ICPPositioningService.Stub;
import com.samsung.location.SLocationManager;

public class CPPositioningService extends Stub {
    public static final int ERROR_EXCEPTION = -4;
    public static final int ERROR_ILLEGAL_ARGUMENT = -2;
    private static final int MSG_DEREGISTER_ANDROID_GEO_FENCE_UPDATE = 8;
    private static final int MSG_DEREGISTER_CP_GEO_FENCE_UPDATE = 6;
    private static final int MSG_REGISTER_ANDROID_GEO_FENCE_UPDATE = 7;
    private static final int MSG_REGISTER_CP_GEO_FENCE_UPDATE = 5;
    private static final int MSG_REQUEST_ANDROID_LOCATION_UPDATE = 1;
    private static final int MSG_REQUEST_CP_LOCATION_UPDATE = 3;
    private static final int MSG_STOP_ANDROID_LOCATION_UPDATE = 2;
    private static final int MSG_STOP_CP_LOCATION_UPDATE = 4;
    public static final int OPERATION_SUCCESS = 0;
    private static final String TAG = "CPPositioningService";
    private CPPAndroidLocProvider mAndroidLocProvider;
    private Context mContext;
    private CPPProvider mCpLocationProvider;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ICPPLocationListener tempCppLocListener = msg.obj;
                    Log.d(CPPositioningService.TAG, "MSG_REQUEST_ANDROID_LOCATION_UPDATE");
                    Log.d(CPPositioningService.TAG, "requestLocationUpdates Interval : " + msg.arg1 + " Provider(0-GPS, 1-google NLP) : " + msg.arg2 + " listener : " + tempCppLocListener.hashCode());
                    CPPositioningService.this.mAndroidLocProvider.requestLocationUpdates(msg.arg1, msg.arg2, tempCppLocListener, "");
                    return;
                case 2:
                    ICPPLocationListener tempStopCppLocListener = msg.obj;
                    Log.d(CPPositioningService.TAG, "MSG_STOP_ANDROID_LOCATION_UPDATE");
                    Log.d(CPPositioningService.TAG, "stopLocationUpdates listener : " + tempStopCppLocListener);
                    CPPositioningService.this.mAndroidLocProvider.stopLocationUpdates(tempStopCppLocListener);
                    return;
                case 3:
                    RequestLocationInput input = msg.obj;
                    Log.d(CPPositioningService.TAG, "MSG_REQUEST_CP_LOCATION_UPDATE");
                    CPPositioningService.this.mCpLocationProvider.requestCPLocationUpdates(input);
                    return;
                case 4:
                    ICPPLocationListener tempStopCpLocListener = msg.obj;
                    Log.d(CPPositioningService.TAG, "MSG_STOP_CP_LOCATION_UPDATE");
                    Log.d(CPPositioningService.TAG, "requestLocationUpdates listener : " + tempStopCpLocListener);
                    CPPositioningService.this.mCpLocationProvider.stopCPLocationUpdates((ICPPLocationListener) msg.obj);
                    return;
                case 5:
                    Log.d(CPPositioningService.TAG, "MSG_REGISTER_CP_GEO_FENCE_UPDATE");
                    CPPositioningService.this.mCpLocationProvider.registerCPGeoFence((RequestCPGeoFenceRegister) msg.obj);
                    return;
                case 6:
                    Log.d(CPPositioningService.TAG, "MSG_DEREGISTER_CP_GEO_FENCE_UPDATE");
                    CPPositioningService.this.mCpLocationProvider.deRegisterCPGeoFence(msg.arg1);
                    return;
                case 7:
                    Log.d(CPPositioningService.TAG, "MSG_REGISTER_ANDROID_GEO_FENCE_UPDATE");
                    return;
                case 8:
                    Log.d(CPPositioningService.TAG, "MSG_DEREGISTER_ANDROID_GEO_FENCE_UPDATE");
                    return;
                default:
                    Log.d(CPPositioningService.TAG, "Invalid Message");
                    return;
            }
        }
    };
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;

    public static class RequestCPGeoFenceRegister {
        int mGeoMode;
        int mKey;
        double mLatitude;
        double mLongitude;
        int mPeriod;
        int mRadius;
    }

    public static class RequestLocationInput {
        public ICPPLocationListener mCppLocListener;
        public int mInterval;
        public int mMinDist;
        public int mMode;
    }

    protected void finalize() throws Throwable {
        super.finalize();
        Log.d(TAG, "finalized");
    }

    public CPPositioningService(Context context) {
        Log.d(TAG, "CPPositioningService : constructed");
        this.mContext = context;
        this.mLocationManager = (LocationManager) this.mContext.getSystemService(SLocationManager.GEOFENCE_LOCATION);
        this.mCpLocationProvider = new CPPProvider(context);
        this.mAndroidLocProvider = new CPPAndroidLocProvider(context);
    }

    public void systemReady() {
        Log.d(TAG, "systemReady");
    }

    public int requestLocationUpdates(int interval, int typeOfLoc, ICPPLocationListener cppLocListener, String pkgName) {
        Log.d(TAG, "requestLocationUpdates... ");
        if (!this.mCpLocationProvider.isEnabled()) {
            Log.e(TAG, "requestLocationUpdates : CPP Service is not enabled yet. ERROR_EXCEPTION");
            return -4;
        } else if (cppLocListener == null) {
            Log.e(TAG, "requestLocationUpdates: parameters are not valid");
            return -2;
        } else {
            Log.d(TAG, "requestLocationUpdates called from App : " + pkgName + "Interval : " + interval + "Provider(0-GPS, 1-google NLP) : " + typeOfLoc + "Listener :" + cppLocListener.hashCode());
            Message msg = this.mHandler.obtainMessage(1);
            msg.obj = cppLocListener;
            msg.arg1 = interval;
            msg.arg2 = typeOfLoc;
            this.mHandler.sendMessage(msg);
            return 0;
        }
    }

    public int stopLocationUpdates(ICPPLocationListener cppLocListener) {
        Log.d(TAG, "stopLocationUpdates... ");
        if (!this.mCpLocationProvider.isEnabled()) {
            Log.e(TAG, "stopLocationUpdates : CPP Service is not enabled yet. ERROR_EXCEPTION");
            return -4;
        } else if (cppLocListener == null) {
            Log.e(TAG, "stopLocationUpdates : parameters are not valid");
            return -2;
        } else {
            Message msg = this.mHandler.obtainMessage(2);
            msg.obj = cppLocListener;
            this.mHandler.sendMessage(msg);
            return 0;
        }
    }

    public int requestCPLocationUpdates(int interval, int minDist, int mode, ICPPLocationListener cppLocListener) {
        Log.d(TAG, "requestCPLocationUpdates... ");
        if (!this.mCpLocationProvider.isEnabled()) {
            Log.e(TAG, "requestCPLocationUpdates : CPP Service is not enabled yet. ERROR_EXCEPTION");
            return -4;
        } else if (cppLocListener == null) {
            Log.e(TAG, "requestCPLocationUpdates: parameters are not valid");
            return -2;
        } else {
            Log.d(TAG, "requestCPLocationUpdates Interval: " + interval + "Listener:" + cppLocListener.hashCode());
            RequestLocationInput input = new RequestLocationInput();
            input.mInterval = interval;
            input.mMinDist = minDist;
            input.mMode = mode;
            input.mCppLocListener = cppLocListener;
            this.mHandler.obtainMessage(3, input).sendToTarget();
            return 0;
        }
    }

    public int stopCPLocationUpdates(ICPPLocationListener cppLocListener) {
        Log.d(TAG, "stopCPLocationUpdates... ");
        if (!this.mCpLocationProvider.isEnabled()) {
            Log.e(TAG, "stopCPLocationUpdates : CPP Service is not enabled yet. ERROR_EXCEPTION");
            return -4;
        } else if (cppLocListener == null) {
            Log.e(TAG, "stopCPLocationUpdates: parameters are not valid");
            return -2;
        } else {
            Log.d(TAG, "stopCPLocationUpdates: " + cppLocListener.hashCode());
            Message msg = this.mHandler.obtainMessage(4);
            msg.obj = cppLocListener;
            this.mHandler.sendMessage(msg);
            return 0;
        }
    }

    public int registerGeoFence(double latitude, double longitude, int radius, int typeOfEvents, IGeoFenceListener listener) {
        Log.d(TAG, "registerGeoFence... ");
        if (!this.mCpLocationProvider.isEnabled()) {
            Log.e(TAG, "registerGeoFence : CPP Service is not enabled yet. ERROR_EXCEPTION");
            return -4;
        } else if (listener == null) {
            Log.e(TAG, "registerGeoFence : parameters are not valid");
            return -2;
        } else {
            Log.d(TAG, "registerGeoFence latitude : " + latitude + " longitude:" + longitude + " radius: " + radius + " typeOfEvents: " + typeOfEvents);
            Message msg = this.mHandler.obtainMessage(7);
            Bundle b = new Bundle();
            b.putDouble("latitude", latitude);
            b.putDouble("longitude", longitude);
            b.putInt("radius", radius);
            b.putInt("typeOfEvents", typeOfEvents);
            b.putParcelable("IGeoFenceListener", (Parcelable) listener);
            msg.setData(b);
            this.mHandler.sendMessage(msg);
            return 0;
        }
    }

    public int deRegisterGeoFence(IGeoFenceListener listener) {
        Log.d(TAG, "deRegisterGeoFence... ");
        if (!this.mCpLocationProvider.isEnabled()) {
            Log.e(TAG, "deRegisterGeoFence : CPP Service is not enabled yet. ERROR_EXCEPTION");
            return -4;
        } else if (listener == null) {
            Log.e(TAG, "deRegisterGeoFence: parameters are not valid");
            return -2;
        } else {
            Message msg = this.mHandler.obtainMessage(8);
            msg.obj = listener;
            this.mHandler.sendMessage(msg);
            return 0;
        }
    }

    public int registerCPGeoFence(double latitude, double longitude, int geoMode, int radius, int period) throws RemoteException {
        Log.d(TAG, "registerCPGeoFence... ");
        if (!this.mCpLocationProvider.isEnabled()) {
            Log.e(TAG, "registerCPGeoFence : CPP Service is not enabled yet. ERROR_EXCEPTION");
            return -4;
        } else if (latitude == 0.0d && longitude == 0.0d && geoMode == 0 && radius == 0 && period == 0) {
            Log.e(TAG, "registerCPGeoFence : parameters are not valid");
            return -2;
        } else {
            Log.d(TAG, "registerCPGeoFence() latitude : " + latitude + ", longitude : " + longitude + ", geoMode : " + geoMode + ", radius : " + radius + ", period : " + period);
            RequestCPGeoFenceRegister input = new RequestCPGeoFenceRegister();
            input.mGeoMode = geoMode;
            input.mRadius = radius;
            input.mPeriod = period;
            input.mLongitude = longitude;
            input.mLatitude = latitude;
            input.mKey = this.mCpLocationProvider.insertGeoFencePOI(input);
            this.mHandler.obtainMessage(5, input).sendToTarget();
            return input.mKey;
        }
    }

    public int deRegisterCPGeoFence(int clientID) throws RemoteException {
        Log.d(TAG, "registerCPGeoFence... (clientID : " + clientID);
        if (!this.mCpLocationProvider.isEnabled()) {
            Log.e(TAG, "deRegisterGeoFence : CPP Service is not enabled yet. ERROR_EXCEPTION");
            return -4;
        } else if (clientID == 0) {
            Log.e(TAG, "deRegisterGeoFence : parameter is not valid");
            return -2;
        } else {
            Message msg = this.mHandler.obtainMessage(6);
            msg.arg1 = clientID;
            this.mHandler.sendMessage(msg);
            return 0;
        }
    }
}
