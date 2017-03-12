package android.location;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus.Listener;
import android.location.GpsStatus.NmeaListener;
import android.location.IExerciseLocationListener.Stub;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.location.ProviderProperties;
import com.kddi.android.internal.pdg.PdgLocationAccessChecker;
import com.sec.android.app.CscFeature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocationManager {
    public static final String EXTRA_GPS_ENABLED = "enabled";
    public static final String FUSED_PROVIDER = "fused";
    public static final String GPS_ENABLED_CHANGE_ACTION = "android.location.GPS_ENABLED_CHANGE";
    public static final String GPS_FIX_CHANGE_ACTION = "android.location.GPS_FIX_CHANGE";
    public static final String GPS_PROVIDER = "gps";
    public static final String HIGH_POWER_REQUEST_CHANGE_ACTION = "android.location.HIGH_POWER_REQUEST_CHANGE";
    public static final String KEY_LOCATION_CHANGED = "location";
    public static final String KEY_PROVIDER_ENABLED = "providerEnabled";
    public static final String KEY_PROXIMITY_ENTERING = "entering";
    public static final String KEY_STATUS_CHANGED = "status";
    public static final String MODE_CHANGED_ACTION = "android.location.MODE_CHANGED";
    public static final String NETWORK_PROVIDER = "network";
    public static final String PASSIVE_PROVIDER = "passive";
    public static final String PROVIDERS_CHANGED_ACTION = "android.location.PROVIDERS_CHANGED";
    private static final String TAG = "LocationManager";
    private final Context mContext;
    private final HashMap<ExerciseLocationListener, ExerciseLocationListenerTransport> mExerciseLocationListeners = new HashMap();
    private final GpsMeasurementListenerTransport mGpsMeasurementListenerTransport;
    private final GpsNavigationMessageListenerTransport mGpsNavigationMessageListenerTransport;
    private final GpsStatus mGpsStatus = new GpsStatus();
    private final HashMap<Listener, GpsStatusListenerTransport> mGpsStatusListeners = new HashMap();
    private HashMap<LocationListener, ListenerTransport> mListeners = new HashMap();
    private final HashMap<NmeaListener, GpsStatusListenerTransport> mNmeaListeners = new HashMap();
    private final ILocationManager mService;

    private class ExerciseLocationListenerTransport extends Stub {
        private static final int TYPE_LOCATION_CHANGED = 1;
        private final ExerciseLocationListener mListener;

        ExerciseLocationListenerTransport(ExerciseLocationListener listener) {
            this.mListener = listener;
        }

        public void onLocationChanged(long[] timestamp, double[] latitude, double[] longtitude, float[] altitude, float[] pressure, float[] speed, double[] pedoDistance, double[] pedoSpeed, long[] pedoStepCount, int numOfBatch) {
            if (this.mListener != null) {
                this.mListener.onLocationChanged(timestamp, latitude, longtitude, altitude, pressure, speed, pedoDistance, pedoSpeed, pedoStepCount, numOfBatch);
            }
        }

        public void onStatusChanged(int exercise_status) {
            if (this.mListener != null) {
                this.mListener.onStatusChanged(exercise_status);
            }
        }
    }

    private class GpsStatusListenerTransport extends IGpsStatusListener.Stub {
        private static final int NMEA_RECEIVED = 1000;
        private final Handler mGpsHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 1000) {
                    synchronized (GpsStatusListenerTransport.this.mNmeaBuffer) {
                        int length = GpsStatusListenerTransport.this.mNmeaBuffer.size();
                        for (int i = 0; i < length; i++) {
                            Nmea nmea = (Nmea) GpsStatusListenerTransport.this.mNmeaBuffer.get(i);
                            GpsStatusListenerTransport.this.mNmeaListener.onNmeaReceived(nmea.mTimestamp, nmea.mNmea);
                        }
                        GpsStatusListenerTransport.this.mNmeaBuffer.clear();
                    }
                    return;
                }
                synchronized (LocationManager.this.mGpsStatus) {
                    GpsStatusListenerTransport.this.mListener.onGpsStatusChanged(msg.what);
                }
            }
        };
        private final Listener mListener;
        private ArrayList<Nmea> mNmeaBuffer;
        private final NmeaListener mNmeaListener;

        private class Nmea {
            String mNmea;
            long mTimestamp;

            Nmea(long timestamp, String nmea) {
                this.mTimestamp = timestamp;
                this.mNmea = nmea;
            }
        }

        GpsStatusListenerTransport(Listener listener) {
            this.mListener = listener;
            this.mNmeaListener = null;
        }

        GpsStatusListenerTransport(NmeaListener listener) {
            this.mNmeaListener = listener;
            this.mListener = null;
            this.mNmeaBuffer = new ArrayList();
        }

        public void onGpsStarted() {
            if (this.mListener != null) {
                Message msg = Message.obtain();
                msg.what = 1;
                this.mGpsHandler.sendMessage(msg);
            }
        }

        public void onGpsStopped() {
            if (this.mListener != null) {
                Message msg = Message.obtain();
                msg.what = 2;
                this.mGpsHandler.sendMessage(msg);
            }
        }

        public void onFirstFix(int ttff) {
            if (this.mListener != null) {
                LocationManager.this.mGpsStatus.setTimeToFirstFix(ttff);
                Message msg = Message.obtain();
                msg.what = 3;
                this.mGpsHandler.sendMessage(msg);
            }
        }

        public void onSvStatusChanged(int svCount, int[] prns, float[] snrs, float[] elevations, float[] azimuths, int ephemerisMask, int almanacMask, int usedInFixMask, int[] used) {
            if (this.mListener != null) {
                LocationManager.this.mGpsStatus.setStatus(svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask, used);
                Message msg = Message.obtain();
                msg.what = 4;
                this.mGpsHandler.removeMessages(4);
                this.mGpsHandler.sendMessage(msg);
            }
        }

        public void onNmeaReceived(long timestamp, String nmea) {
            if (this.mNmeaListener != null) {
                synchronized (this.mNmeaBuffer) {
                    this.mNmeaBuffer.add(new Nmea(timestamp, nmea));
                }
                Message msg = Message.obtain();
                msg.what = 1000;
                this.mGpsHandler.removeMessages(1000);
                this.mGpsHandler.sendMessage(msg);
            }
        }
    }

    private class ListenerTransport extends ILocationListener.Stub {
        private static final int TYPE_LOCATION_CHANGED = 1;
        private static final int TYPE_PROVIDER_DISABLED = 4;
        private static final int TYPE_PROVIDER_ENABLED = 3;
        private static final int TYPE_STATUS_CHANGED = 2;
        private LocationListener mListener;
        private final Handler mListenerHandler;

        ListenerTransport(LocationListener listener, Looper looper) {
            this.mListener = listener;
            if (looper == null) {
                this.mListenerHandler = new Handler(LocationManager.this) {
                    public void handleMessage(Message msg) {
                        ListenerTransport.this._handleMessage(msg);
                    }
                };
            } else {
                this.mListenerHandler = new Handler(looper, LocationManager.this) {
                    public void handleMessage(Message msg) {
                        ListenerTransport.this._handleMessage(msg);
                    }
                };
            }
        }

        public void onLocationChanged(Location location) {
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = location;
            this.mListenerHandler.sendMessage(msg);
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            Message msg = Message.obtain();
            msg.what = 2;
            Bundle b = new Bundle();
            b.putString("provider", provider);
            b.putInt("status", status);
            if (extras != null) {
                b.putBundle("extras", extras);
            }
            msg.obj = b;
            this.mListenerHandler.sendMessage(msg);
        }

        public void onProviderEnabled(String provider) {
            Message msg = Message.obtain();
            msg.what = 3;
            msg.obj = provider;
            this.mListenerHandler.sendMessage(msg);
        }

        public void onProviderDisabled(String provider) {
            Message msg = Message.obtain();
            msg.what = 4;
            msg.obj = provider;
            this.mListenerHandler.sendMessage(msg);
        }

        private void _handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    this.mListener.onLocationChanged(new Location((Location) msg.obj));
                    break;
                case 2:
                    Bundle b = msg.obj;
                    this.mListener.onStatusChanged(b.getString("provider"), b.getInt("status"), b.getBundle("extras"));
                    break;
                case 3:
                    this.mListener.onProviderEnabled((String) msg.obj);
                    break;
                case 4:
                    this.mListener.onProviderDisabled((String) msg.obj);
                    break;
            }
            try {
                LocationManager.this.mService.locationCallbackFinished(this);
            } catch (RemoteException e) {
                Log.e(LocationManager.TAG, "locationCallbackFinished: RemoteException", e);
            }
        }
    }

    public LocationManager(Context context, ILocationManager service) {
        this.mService = service;
        this.mContext = context;
        this.mGpsMeasurementListenerTransport = new GpsMeasurementListenerTransport(this.mContext, this.mService);
        this.mGpsNavigationMessageListenerTransport = new GpsNavigationMessageListenerTransport(this.mContext, this.mService);
    }

    private LocationProvider createProvider(String name, ProviderProperties properties) {
        return new LocationProvider(name, properties);
    }

    public List<String> getAllProviders() {
        try {
            return this.mService.getAllProviders();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
            return null;
        }
    }

    public List<String> getProviders(boolean enabledOnly) {
        List<String> list = null;
        try {
            list = this.mService.getProviders(null, enabledOnly);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
        return list;
    }

    public LocationProvider getProvider(String name) {
        LocationProvider locationProvider = null;
        checkProvider(name);
        try {
            ProviderProperties properties = this.mService.getProviderProperties(name);
            if (properties != null) {
                locationProvider = createProvider(name, properties);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
        return locationProvider;
    }

    public List<String> getProviders(Criteria criteria, boolean enabledOnly) {
        checkCriteria(criteria);
        try {
            return this.mService.getProviders(criteria, enabledOnly);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
            return null;
        }
    }

    public String getBestProvider(Criteria criteria, boolean enabledOnly) {
        checkCriteria(criteria);
        try {
            return this.mService.getBestProvider(criteria, enabledOnly);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
            return null;
        }
    }

    public void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener) {
        checkProvider(provider);
        checkListener(listener);
        requestLocationUpdates(LocationRequest.createFromDeprecatedProvider(provider, minTime, minDistance, false), listener, null, null);
    }

    public void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener, Looper looper) {
        checkProvider(provider);
        checkListener(listener);
        requestLocationUpdates(LocationRequest.createFromDeprecatedProvider(provider, minTime, minDistance, false), listener, looper, null);
    }

    public void requestLocationUpdates(long minTime, float minDistance, Criteria criteria, LocationListener listener, Looper looper) {
        checkCriteria(criteria);
        checkListener(listener);
        requestLocationUpdates(LocationRequest.createFromDeprecatedCriteria(criteria, minTime, minDistance, false), listener, looper, null);
    }

    public void requestLocationUpdates(String provider, long minTime, float minDistance, PendingIntent intent) {
        checkProvider(provider);
        checkPendingIntent(intent);
        requestLocationUpdates(LocationRequest.createFromDeprecatedProvider(provider, minTime, minDistance, false), null, null, intent);
    }

    public void requestLocationUpdates(long minTime, float minDistance, Criteria criteria, PendingIntent intent) {
        checkCriteria(criteria);
        checkPendingIntent(intent);
        requestLocationUpdates(LocationRequest.createFromDeprecatedCriteria(criteria, minTime, minDistance, false), null, null, intent);
    }

    public void requestSingleUpdate(String provider, LocationListener listener, Looper looper) {
        checkProvider(provider);
        checkListener(listener);
        requestLocationUpdates(LocationRequest.createFromDeprecatedProvider(provider, 0, 0.0f, true), listener, looper, null);
    }

    public void requestSingleUpdate(Criteria criteria, LocationListener listener, Looper looper) {
        checkCriteria(criteria);
        checkListener(listener);
        requestLocationUpdates(LocationRequest.createFromDeprecatedCriteria(criteria, 0, 0.0f, true), listener, looper, null);
    }

    public void requestSingleUpdate(String provider, PendingIntent intent) {
        checkProvider(provider);
        checkPendingIntent(intent);
        requestLocationUpdates(LocationRequest.createFromDeprecatedProvider(provider, 0, 0.0f, true), null, null, intent);
    }

    public void requestSingleUpdate(Criteria criteria, PendingIntent intent) {
        checkCriteria(criteria);
        checkPendingIntent(intent);
        requestLocationUpdates(LocationRequest.createFromDeprecatedCriteria(criteria, 0, 0.0f, true), null, null, intent);
    }

    public void requestLocationUpdates(LocationRequest request, LocationListener listener, Looper looper) {
        checkListener(listener);
        requestLocationUpdates(request, listener, looper, null);
    }

    public void requestLocationUpdates(LocationRequest request, PendingIntent intent) {
        checkPendingIntent(intent);
        requestLocationUpdates(request, null, null, intent);
    }

    private ListenerTransport wrapListener(LocationListener listener, Looper looper) {
        if (listener == null) {
            return null;
        }
        ListenerTransport transport;
        synchronized (this.mListeners) {
            transport = (ListenerTransport) this.mListeners.get(listener);
            if (transport == null) {
                transport = new ListenerTransport(listener, looper);
            }
            this.mListeners.put(listener, transport);
        }
        return transport;
    }

    private void requestLocationUpdates(LocationRequest request, LocationListener listener, Looper looper, PendingIntent intent) {
        String packageName = this.mContext.getPackageName();
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_Common_EnablePrivacyDataGuard") || PdgLocationAccessChecker.checkPrivacy(this.mContext, packageName)) {
            try {
                this.mService.requestLocationUpdates(request, wrapListener(listener, looper), intent, packageName);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException", e);
            }
        } else if (listener != null) {
            listener.onStatusChanged(request.getProvider(), 0, new Bundle());
        } else if (intent != null) {
            Intent statusChanged = new Intent();
            statusChanged.putExtras(new Bundle());
            statusChanged.putExtra("status", 0);
            try {
                synchronized (this) {
                    intent.send(this.mContext, 0, statusChanged);
                }
            } catch (CanceledException e2) {
            }
        }
    }

    public void removeUpdates(LocationListener listener) {
        checkListener(listener);
        String packageName = this.mContext.getPackageName();
        synchronized (this.mListeners) {
            ListenerTransport transport = (ListenerTransport) this.mListeners.remove(listener);
        }
        if (transport != null) {
            try {
                this.mService.removeUpdates(transport, null, packageName);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException", e);
            }
        }
    }

    public void removeUpdates(PendingIntent intent) {
        checkPendingIntent(intent);
        try {
            this.mService.removeUpdates(null, intent, this.mContext.getPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public void addProximityAlert(double latitude, double longitude, float radius, long expiration, PendingIntent intent) {
        checkPendingIntent(intent);
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_Common_EnablePrivacyDataGuard") || PdgLocationAccessChecker.checkPrivacy(this.mContext, this.mContext.getPackageName())) {
            if (expiration < 0) {
                expiration = Long.MAX_VALUE;
            }
            Geofence fence = Geofence.createCircle(latitude, longitude, radius);
            try {
                this.mService.requestGeofence(new LocationRequest().setExpireIn(expiration), fence, intent, this.mContext.getPackageName());
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException", e);
            }
        }
    }

    public void addGeofence(LocationRequest request, Geofence fence, PendingIntent intent) {
        checkPendingIntent(intent);
        checkGeofence(fence);
        try {
            this.mService.requestGeofence(request, fence, intent, this.mContext.getPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public void removeProximityAlert(PendingIntent intent) {
        checkPendingIntent(intent);
        try {
            this.mService.removeGeofence(null, intent, this.mContext.getPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public void removeGeofence(Geofence fence, PendingIntent intent) {
        checkPendingIntent(intent);
        checkGeofence(fence);
        try {
            this.mService.removeGeofence(fence, intent, this.mContext.getPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public void removeAllGeofences(PendingIntent intent) {
        checkPendingIntent(intent);
        try {
            this.mService.removeGeofence(null, intent, this.mContext.getPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public boolean isProviderEnabled(String provider) {
        checkProvider(provider);
        try {
            return this.mService.isProviderEnabled(provider);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
            return false;
        }
    }

    public Location getLastLocation() {
        Location location = null;
        try {
            location = this.mService.getLastLocation(null, this.mContext.getPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
        return location;
    }

    public Location getLastKnownLocation(String provider) {
        Location location = null;
        checkProvider(provider);
        String packageName = this.mContext.getPackageName();
        if (!CscFeature.getInstance().getEnableStatus("CscFeature_Common_EnablePrivacyDataGuard") || PdgLocationAccessChecker.checkPrivacy(this.mContext, packageName)) {
            try {
                location = this.mService.getLastLocation(LocationRequest.createFromDeprecatedProvider(provider, 0, 0.0f, true), packageName);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException", e);
            }
        }
        return location;
    }

    public void addTestProvider(String name, boolean requiresNetwork, boolean requiresSatellite, boolean requiresCell, boolean hasMonetaryCost, boolean supportsAltitude, boolean supportsSpeed, boolean supportsBearing, int powerRequirement, int accuracy) {
        ProviderProperties properties = new ProviderProperties(requiresNetwork, requiresSatellite, requiresCell, hasMonetaryCost, supportsAltitude, supportsSpeed, supportsBearing, powerRequirement, accuracy);
        if (name.matches(LocationProvider.BAD_CHARS_REGEX)) {
            throw new IllegalArgumentException("provider name contains illegal character: " + name);
        }
        try {
            this.mService.addTestProvider(name, properties, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public void removeTestProvider(String provider) {
        try {
            this.mService.removeTestProvider(provider, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public void setTestProviderLocation(String provider, Location loc) {
        if (!loc.isComplete()) {
            IllegalArgumentException e = new IllegalArgumentException("Incomplete location object, missing timestamp or accuracy? " + loc);
            if (this.mContext.getApplicationInfo().targetSdkVersion <= 16) {
                Log.w(TAG, e);
                loc.makeComplete();
            } else {
                throw e;
            }
        }
        try {
            this.mService.setTestProviderLocation(provider, loc, this.mContext.getOpPackageName());
        } catch (RemoteException e2) {
            Log.e(TAG, "RemoteException", e2);
        }
    }

    public void clearTestProviderLocation(String provider) {
        try {
            this.mService.clearTestProviderLocation(provider, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public void setTestProviderEnabled(String provider, boolean enabled) {
        try {
            this.mService.setTestProviderEnabled(provider, enabled, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public void clearTestProviderEnabled(String provider) {
        try {
            this.mService.clearTestProviderEnabled(provider, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public void setTestProviderStatus(String provider, int status, Bundle extras, long updateTime) {
        try {
            this.mService.setTestProviderStatus(provider, status, extras, updateTime, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public void clearTestProviderStatus(String provider) {
        try {
            this.mService.clearTestProviderStatus(provider, this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException", e);
        }
    }

    public boolean addGpsStatusListener(Listener listener) {
        if (this.mGpsStatusListeners.get(listener) != null) {
            return true;
        }
        try {
            GpsStatusListenerTransport transport = new GpsStatusListenerTransport(listener);
            boolean result = this.mService.addGpsStatusListener(transport, this.mContext.getPackageName());
            if (!result) {
                return result;
            }
            this.mGpsStatusListeners.put(listener, transport);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in registerGpsStatusListener: ", e);
            return false;
        }
    }

    public void removeGpsStatusListener(Listener listener) {
        try {
            GpsStatusListenerTransport transport = (GpsStatusListenerTransport) this.mGpsStatusListeners.remove(listener);
            if (transport != null) {
                this.mService.removeGpsStatusListener(transport);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in unregisterGpsStatusListener: ", e);
        }
    }

    public boolean addNmeaListener(NmeaListener listener) {
        if (CscFeature.getInstance().getEnableStatus("CscFeature_Common_EnablePrivacyDataGuard") && !PdgLocationAccessChecker.checkPrivacy(this.mContext, this.mContext.getPackageName())) {
            return false;
        }
        if (this.mNmeaListeners.get(listener) != null) {
            return true;
        }
        try {
            GpsStatusListenerTransport transport = new GpsStatusListenerTransport(listener);
            boolean result = this.mService.addGpsStatusListener(transport, this.mContext.getPackageName());
            if (!result) {
                return result;
            }
            this.mNmeaListeners.put(listener, transport);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in registerGpsStatusListener: ", e);
            return false;
        }
    }

    public void removeNmeaListener(NmeaListener listener) {
        try {
            GpsStatusListenerTransport transport = (GpsStatusListenerTransport) this.mNmeaListeners.remove(listener);
            if (transport != null) {
                this.mService.removeGpsStatusListener(transport);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in unregisterGpsStatusListener: ", e);
        }
    }

    public boolean addGpsMeasurementListener(GpsMeasurementsEvent.Listener listener) {
        return this.mGpsMeasurementListenerTransport.add(listener);
    }

    public void removeGpsMeasurementListener(GpsMeasurementsEvent.Listener listener) {
        this.mGpsMeasurementListenerTransport.remove(listener);
    }

    public boolean addGpsNavigationMessageListener(GpsNavigationMessageEvent.Listener listener) {
        return this.mGpsNavigationMessageListenerTransport.add(listener);
    }

    public void removeGpsNavigationMessageListener(GpsNavigationMessageEvent.Listener listener) {
        this.mGpsNavigationMessageListenerTransport.remove(listener);
    }

    public GpsStatus getGpsStatus(GpsStatus status) {
        if (status == null) {
            status = new GpsStatus();
        }
        status.setStatus(this.mGpsStatus);
        return status;
    }

    public boolean sendExtraCommand(String provider, String command, Bundle extras) {
        try {
            return this.mService.sendExtraCommand(provider, command, extras);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in sendExtraCommand: ", e);
            return false;
        }
    }

    public boolean sendNiResponse(int notifId, int userResponse) {
        try {
            return this.mService.sendNiResponse(notifId, userResponse);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in sendNiResponse: ", e);
            return false;
        }
    }

    private static void checkProvider(String provider) {
        if (provider == null) {
            throw new IllegalArgumentException("invalid provider: " + provider);
        }
    }

    private static void checkCriteria(Criteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("invalid criteria: " + criteria);
        }
    }

    private static void checkListener(LocationListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("invalid listener: " + listener);
        }
    }

    private void checkPendingIntent(PendingIntent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("invalid pending intent: " + intent);
        } else if (!intent.isTargetedToPackage()) {
            IllegalArgumentException e = new IllegalArgumentException("pending intent must be targeted to package");
            if (this.mContext.getApplicationInfo().targetSdkVersion > 16) {
                throw e;
            }
            Log.w(TAG, e);
        }
    }

    private static void checkGeofence(Geofence fence) {
        if (fence == null) {
            throw new IllegalArgumentException("invalid geofence: " + fence);
        }
    }

    public void requestExerciseLocationUpdates(int flags, int updateRateMs, ExerciseLocationListener listener) {
        if (listener == null || addExerciseLocationListener(listener)) {
            if (updateRateMs < 0) {
                updateRateMs = 0;
            }
            try {
                this.mService.requestExerciseLocationUpdates(flags, updateRateMs);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException", e);
            }
        }
    }

    public void removeExerciseLocationUpdates(ExerciseLocationListener listener) {
        try {
            ExerciseLocationListenerTransport transport = (ExerciseLocationListenerTransport) this.mExerciseLocationListeners.remove(listener);
            if (transport != null) {
                this.mService.removeExerciseLocationListener(transport);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in unregisterExerciseLocationListener: ", e);
        }
    }

    private boolean addExerciseLocationListener(ExerciseLocationListener listener) {
        if (this.mExerciseLocationListeners.get(listener) != null) {
            return true;
        }
        try {
            ExerciseLocationListenerTransport transport = new ExerciseLocationListenerTransport(listener);
            boolean result = this.mService.addExerciseLocationListener(transport, this.mContext.getPackageName());
            if (!result) {
                return result;
            }
            this.mExerciseLocationListeners.put(listener, transport);
            return result;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in registerExerciseLocationListeners: ", e);
            return false;
        }
    }

    private void removeExerciseLocationListener(ExerciseLocationListener listener) {
        try {
            ExerciseLocationListenerTransport transport = (ExerciseLocationListenerTransport) this.mExerciseLocationListeners.remove(listener);
            if (transport != null) {
                this.mService.removeExerciseLocationListener(transport);
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in unregisterExerciseLocationListener: ", e);
        }
    }

    public void requestFlushExerciseLocation() {
        try {
            this.mService.requestFlushExerciseLocation();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in requestFlushExcersiceLocation: ", e);
        }
    }

    public boolean notifyNSFLP(Message msg) {
        boolean z = false;
        try {
            if (!(this.mService == null || msg == null)) {
                z = this.mService.notifyNSFLP(msg);
            }
        } catch (RemoteException e) {
        }
        return z;
    }
}
