package android.hardware.location;

import android.Manifest.permission;
import android.content.Context;
import android.location.IFusedGeofenceHardware;
import android.location.IGpsGeofenceHardware;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IInterface;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Iterator;

public final class GeofenceHardwareImpl {
    private static final int ADD_GEOFENCE_CALLBACK = 2;
    private static final int CALLBACK_ADD = 2;
    private static final int CALLBACK_REMOVE = 3;
    private static final int CAPABILITY_GNSS = 1;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int FIRST_VERSION_WITH_CAPABILITIES = 2;
    private static final int GEOFENCE_CALLBACK_BINDER_DIED = 6;
    private static final int GEOFENCE_STATUS = 1;
    private static final int GEOFENCE_TRANSITION_CALLBACK = 1;
    private static final int LOCATION_HAS_ACCURACY = 16;
    private static final int LOCATION_HAS_ALTITUDE = 2;
    private static final int LOCATION_HAS_BEARING = 8;
    private static final int LOCATION_HAS_LAT_LONG = 1;
    private static final int LOCATION_HAS_SPEED = 4;
    private static final int LOCATION_INVALID = 0;
    private static final int MONITOR_CALLBACK_BINDER_DIED = 4;
    private static final int PAUSE_GEOFENCE_CALLBACK = 4;
    private static final int REAPER_GEOFENCE_ADDED = 1;
    private static final int REAPER_MONITOR_CALLBACK_ADDED = 2;
    private static final int REAPER_REMOVED = 3;
    private static final int REMOVE_GEOFENCE_CALLBACK = 3;
    private static final int RESOLUTION_LEVEL_COARSE = 2;
    private static final int RESOLUTION_LEVEL_FINE = 3;
    private static final int RESOLUTION_LEVEL_NONE = 1;
    private static final int RESUME_GEOFENCE_CALLBACK = 5;
    private static final String TAG = "GeofenceHardwareImpl";
    private static GeofenceHardwareImpl sInstance;
    private final ArrayList<IGeofenceHardwareMonitorCallback>[] mCallbacks = new ArrayList[2];
    private Handler mCallbacksHandler = new Handler() {
        public void handleMessage(Message msg) {
            ArrayList<IGeofenceHardwareMonitorCallback> callbackList;
            IGeofenceHardwareMonitorCallback callback;
            switch (msg.what) {
                case 1:
                    GeofenceHardwareMonitorEvent event = msg.obj;
                    callbackList = GeofenceHardwareImpl.this.mCallbacks[event.getMonitoringType()];
                    if (callbackList != null) {
                        if (GeofenceHardwareImpl.DEBUG) {
                            Log.d(GeofenceHardwareImpl.TAG, "MonitoringSystemChangeCallback: " + event);
                        }
                        Iterator i$ = callbackList.iterator();
                        while (i$.hasNext()) {
                            try {
                                ((IGeofenceHardwareMonitorCallback) i$.next()).onMonitoringSystemChange(event);
                            } catch (RemoteException e) {
                                Log.d(GeofenceHardwareImpl.TAG, "Error reporting onMonitoringSystemChange.", e);
                            }
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 2:
                    int monitoringType = msg.arg1;
                    callback = msg.obj;
                    callbackList = GeofenceHardwareImpl.this.mCallbacks[monitoringType];
                    if (callbackList == null) {
                        callbackList = new ArrayList();
                        GeofenceHardwareImpl.this.mCallbacks[monitoringType] = callbackList;
                    }
                    if (!callbackList.contains(callback)) {
                        callbackList.add(callback);
                        return;
                    }
                    return;
                case 3:
                    callback = (IGeofenceHardwareMonitorCallback) msg.obj;
                    callbackList = GeofenceHardwareImpl.this.mCallbacks[msg.arg1];
                    if (callbackList != null) {
                        callbackList.remove(callback);
                        return;
                    }
                    return;
                case 4:
                    callback = (IGeofenceHardwareMonitorCallback) msg.obj;
                    if (GeofenceHardwareImpl.DEBUG) {
                        Log.d(GeofenceHardwareImpl.TAG, "Monitor callback reaped:" + callback);
                    }
                    callbackList = GeofenceHardwareImpl.this.mCallbacks[msg.arg1];
                    if (callbackList != null && callbackList.contains(callback)) {
                        callbackList.remove(callback);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private int mCapabilities;
    private final Context mContext;
    private IFusedGeofenceHardware mFusedService;
    private Handler mGeofenceHandler = new Handler() {
        public void handleMessage(Message msg) {
            IGeofenceHardwareCallback callback;
            int geofenceId;
            int i;
            switch (msg.what) {
                case 1:
                    GeofenceTransition geofenceTransition = (GeofenceTransition) msg.obj;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceTransition.mGeofenceId);
                        if (GeofenceHardwareImpl.DEBUG) {
                            Log.d(GeofenceHardwareImpl.TAG, "GeofenceTransistionCallback: GPS : GeofenceId: " + geofenceTransition.mGeofenceId + " Transition: " + geofenceTransition.mTransition + " Location: " + geofenceTransition.mLocation + ":" + GeofenceHardwareImpl.this.mGeofences);
                        }
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceTransition(geofenceTransition.mGeofenceId, geofenceTransition.mTransition, geofenceTransition.mLocation, geofenceTransition.mTimestamp, geofenceTransition.mMonitoringType);
                        } catch (RemoteException e) {
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 2:
                    geofenceId = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId);
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceAdd(geofenceId, msg.arg2);
                        } catch (RemoteException e2) {
                            Log.i(GeofenceHardwareImpl.TAG, "Remote Exception:" + e2);
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 3:
                    geofenceId = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId);
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceRemove(geofenceId, msg.arg2);
                        } catch (RemoteException e3) {
                        }
                        IBinder callbackBinder = callback.asBinder();
                        boolean callbackInUse = false;
                        synchronized (GeofenceHardwareImpl.this.mGeofences) {
                            GeofenceHardwareImpl.this.mGeofences.remove(geofenceId);
                            i = 0;
                            while (i < GeofenceHardwareImpl.this.mGeofences.size()) {
                                if (((IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.valueAt(i)).asBinder() == callbackBinder) {
                                    callbackInUse = true;
                                } else {
                                    i++;
                                }
                            }
                        }
                        if (!callbackInUse) {
                            Iterator<Reaper> iterator = GeofenceHardwareImpl.this.mReapers.iterator();
                            while (iterator.hasNext()) {
                                Reaper reaper = (Reaper) iterator.next();
                                if (reaper.mCallback != null && reaper.mCallback.asBinder() == callbackBinder) {
                                    iterator.remove();
                                    reaper.unlinkToDeath();
                                    if (GeofenceHardwareImpl.DEBUG) {
                                        Log.d(GeofenceHardwareImpl.TAG, String.format("Removed reaper %s because binder %s is no longer needed.", new Object[]{reaper, callbackBinder}));
                                    }
                                }
                            }
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 4:
                    geofenceId = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId);
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofencePause(geofenceId, msg.arg2);
                        } catch (RemoteException e4) {
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 5:
                    geofenceId = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        callback = (IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.get(geofenceId);
                    }
                    if (callback != null) {
                        try {
                            callback.onGeofenceResume(geofenceId, msg.arg2);
                        } catch (RemoteException e5) {
                        }
                    }
                    GeofenceHardwareImpl.this.releaseWakeLock();
                    return;
                case 6:
                    callback = (IGeofenceHardwareCallback) msg.obj;
                    if (GeofenceHardwareImpl.DEBUG) {
                        Log.d(GeofenceHardwareImpl.TAG, "Geofence callback reaped:" + callback);
                    }
                    int monitoringType = msg.arg1;
                    synchronized (GeofenceHardwareImpl.this.mGeofences) {
                        for (i = 0; i < GeofenceHardwareImpl.this.mGeofences.size(); i++) {
                            if (((IGeofenceHardwareCallback) GeofenceHardwareImpl.this.mGeofences.valueAt(i)).equals(callback)) {
                                geofenceId = GeofenceHardwareImpl.this.mGeofences.keyAt(i);
                                GeofenceHardwareImpl.this.removeGeofence(GeofenceHardwareImpl.this.mGeofences.keyAt(i), monitoringType);
                                GeofenceHardwareImpl.this.mGeofences.remove(geofenceId);
                            }
                        }
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private final SparseArray<IGeofenceHardwareCallback> mGeofences = new SparseArray();
    private IGpsGeofenceHardware mGpsService;
    private Handler mReaperHandler = new Handler() {
        public void handleMessage(Message msg) {
            Reaper r;
            switch (msg.what) {
                case 1:
                    IGeofenceHardwareCallback callback = msg.obj;
                    r = new Reaper(callback, msg.arg1);
                    if (!GeofenceHardwareImpl.this.mReapers.contains(r)) {
                        GeofenceHardwareImpl.this.mReapers.add(r);
                        try {
                            callback.asBinder().linkToDeath(r, 0);
                            return;
                        } catch (RemoteException e) {
                            return;
                        }
                    }
                    return;
                case 2:
                    IGeofenceHardwareMonitorCallback monitorCallback = msg.obj;
                    r = new Reaper(monitorCallback, msg.arg1);
                    if (!GeofenceHardwareImpl.this.mReapers.contains(r)) {
                        GeofenceHardwareImpl.this.mReapers.add(r);
                        try {
                            monitorCallback.asBinder().linkToDeath(r, 0);
                            return;
                        } catch (RemoteException e2) {
                            return;
                        }
                    }
                    return;
                case 3:
                    GeofenceHardwareImpl.this.mReapers.remove((Reaper) msg.obj);
                    return;
                default:
                    return;
            }
        }
    };
    private final ArrayList<Reaper> mReapers = new ArrayList();
    private int[] mSupportedMonitorTypes = new int[2];
    private int mVersion = 1;
    private WakeLock mWakeLock;

    private class GeofenceTransition {
        private int mGeofenceId;
        private Location mLocation;
        private int mMonitoringType;
        private int mSourcesUsed;
        private long mTimestamp;
        private int mTransition;

        GeofenceTransition(int geofenceId, int transition, long timestamp, Location location, int monitoringType, int sourcesUsed) {
            this.mGeofenceId = geofenceId;
            this.mTransition = transition;
            this.mTimestamp = timestamp;
            this.mLocation = location;
            this.mMonitoringType = monitoringType;
            this.mSourcesUsed = sourcesUsed;
        }
    }

    class Reaper implements DeathRecipient {
        private IGeofenceHardwareCallback mCallback;
        private IGeofenceHardwareMonitorCallback mMonitorCallback;
        private int mMonitoringType;

        Reaper(IGeofenceHardwareCallback c, int monitoringType) {
            this.mCallback = c;
            this.mMonitoringType = monitoringType;
        }

        Reaper(IGeofenceHardwareMonitorCallback c, int monitoringType) {
            this.mMonitorCallback = c;
            this.mMonitoringType = monitoringType;
        }

        public void binderDied() {
            Message m;
            if (this.mCallback != null) {
                m = GeofenceHardwareImpl.this.mGeofenceHandler.obtainMessage(6, this.mCallback);
                m.arg1 = this.mMonitoringType;
                GeofenceHardwareImpl.this.mGeofenceHandler.sendMessage(m);
            } else if (this.mMonitorCallback != null) {
                m = GeofenceHardwareImpl.this.mCallbacksHandler.obtainMessage(4, this.mMonitorCallback);
                m.arg1 = this.mMonitoringType;
                GeofenceHardwareImpl.this.mCallbacksHandler.sendMessage(m);
            }
            GeofenceHardwareImpl.this.mReaperHandler.sendMessage(GeofenceHardwareImpl.this.mReaperHandler.obtainMessage(3, this));
        }

        public int hashCode() {
            int hashCode;
            int i = 0;
            if (this.mCallback != null) {
                hashCode = this.mCallback.asBinder().hashCode();
            } else {
                hashCode = 0;
            }
            hashCode = (hashCode + 527) * 31;
            if (this.mMonitorCallback != null) {
                i = this.mMonitorCallback.asBinder().hashCode();
            }
            return ((hashCode + i) * 31) + this.mMonitoringType;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            Reaper rhs = (Reaper) obj;
            if (binderEquals(rhs.mCallback, this.mCallback) && binderEquals(rhs.mMonitorCallback, this.mMonitorCallback) && rhs.mMonitoringType == this.mMonitoringType) {
                return true;
            }
            return false;
        }

        private boolean binderEquals(IInterface left, IInterface right) {
            boolean z = false;
            if (left != null) {
                if (right != null && left.asBinder() == right.asBinder()) {
                    z = true;
                }
                return z;
            } else if (right == null) {
                return true;
            } else {
                return false;
            }
        }

        private boolean unlinkToDeath() {
            if (this.mMonitorCallback != null) {
                return this.mMonitorCallback.asBinder().unlinkToDeath(this, 0);
            }
            if (this.mCallback != null) {
                return this.mCallback.asBinder().unlinkToDeath(this, 0);
            }
            return true;
        }

        private boolean callbackEquals(IGeofenceHardwareCallback cb) {
            return this.mCallback != null && this.mCallback.asBinder() == cb.asBinder();
        }
    }

    public static synchronized GeofenceHardwareImpl getInstance(Context context) {
        GeofenceHardwareImpl geofenceHardwareImpl;
        synchronized (GeofenceHardwareImpl.class) {
            if (sInstance == null) {
                sInstance = new GeofenceHardwareImpl(context);
            }
            geofenceHardwareImpl = sInstance;
        }
        return geofenceHardwareImpl;
    }

    private GeofenceHardwareImpl(Context context) {
        this.mContext = context;
        setMonitorAvailability(0, 2);
        setMonitorAvailability(1, 2);
    }

    private void acquireWakeLock() {
        if (this.mWakeLock == null) {
            this.mWakeLock = ((PowerManager) this.mContext.getSystemService(Context.POWER_SERVICE)).newWakeLock(1, TAG);
        }
        this.mWakeLock.acquire();
    }

    private void releaseWakeLock() {
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
        }
    }

    private void updateGpsHardwareAvailability() {
        boolean gpsSupported;
        try {
            gpsSupported = this.mGpsService.isHardwareGeofenceSupported();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote Exception calling LocationManagerService");
            gpsSupported = false;
        }
        if (gpsSupported) {
            setMonitorAvailability(0, 0);
        }
    }

    private void updateFusedHardwareAvailability() {
        boolean fusedSupported;
        try {
            boolean hasGnnsCapabilities;
            if (this.mVersion < 2 || (this.mCapabilities & 1) != 0) {
                hasGnnsCapabilities = true;
            } else {
                hasGnnsCapabilities = false;
            }
            fusedSupported = this.mFusedService != null ? this.mFusedService.isSupported() && hasGnnsCapabilities : false;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException calling LocationManagerService");
            fusedSupported = false;
        }
        if (fusedSupported) {
            setMonitorAvailability(1, 0);
        }
    }

    public void setGpsHardwareGeofence(IGpsGeofenceHardware service) {
        if (this.mGpsService == null) {
            this.mGpsService = service;
            updateGpsHardwareAvailability();
        } else if (service == null) {
            this.mGpsService = null;
            Log.w(TAG, "GPS Geofence Hardware service seems to have crashed");
        } else {
            Log.e(TAG, "Error: GpsService being set again.");
        }
    }

    public void onCapabilities(int capabilities) {
        this.mCapabilities = capabilities;
        updateFusedHardwareAvailability();
    }

    public void setVersion(int version) {
        this.mVersion = version;
        updateFusedHardwareAvailability();
    }

    public void setFusedGeofenceHardware(IFusedGeofenceHardware service) {
        if (this.mFusedService == null) {
            this.mFusedService = service;
            updateFusedHardwareAvailability();
        } else if (service == null) {
            this.mFusedService = null;
            Log.w(TAG, "Fused Geofence Hardware service seems to have crashed");
        } else {
            Log.e(TAG, "Error: FusedService being set again");
        }
    }

    public int[] getMonitoringTypes() {
        boolean gpsSupported;
        boolean fusedSupported;
        synchronized (this.mSupportedMonitorTypes) {
            if (this.mSupportedMonitorTypes[0] != 2) {
                gpsSupported = true;
            } else {
                gpsSupported = false;
            }
            if (this.mSupportedMonitorTypes[1] != 2) {
                fusedSupported = true;
            } else {
                fusedSupported = false;
            }
        }
        if (gpsSupported) {
            if (fusedSupported) {
                return new int[]{0, 1};
            }
            return new int[]{0};
        } else if (!fusedSupported) {
            return new int[0];
        } else {
            return new int[]{1};
        }
    }

    public int getStatusOfMonitoringType(int monitoringType) {
        int i;
        synchronized (this.mSupportedMonitorTypes) {
            if (monitoringType >= this.mSupportedMonitorTypes.length || monitoringType < 0) {
                throw new IllegalArgumentException("Unknown monitoring type");
            }
            i = this.mSupportedMonitorTypes[monitoringType];
        }
        return i;
    }

    public int getCapabilitiesForMonitoringType(int monitoringType) {
        switch (this.mSupportedMonitorTypes[monitoringType]) {
            case 0:
                switch (monitoringType) {
                    case 0:
                        return 1;
                    case 1:
                        if (this.mVersion >= 2) {
                            return this.mCapabilities;
                        }
                        return 1;
                    default:
                        break;
                }
        }
        return 0;
    }

    public boolean addCircularFence(int monitoringType, GeofenceHardwareRequestParcelable request, IGeofenceHardwareCallback callback) {
        boolean result;
        int geofenceId = request.getId();
        if (DEBUG) {
            Log.d(TAG, String.format("addCircularFence: monitoringType=%d, %s", new Object[]{Integer.valueOf(monitoringType), request}));
        }
        synchronized (this.mGeofences) {
            this.mGeofences.put(geofenceId, callback);
        }
        switch (monitoringType) {
            case 0:
                if (this.mGpsService != null) {
                    try {
                        result = this.mGpsService.addCircularHardwareGeofence(request.getId(), request.getLatitude(), request.getLongitude(), request.getRadius(), request.getLastTransition(), request.getMonitorTransitions(), request.getNotificationResponsiveness(), request.getUnknownTimer());
                        break;
                    } catch (RemoteException e) {
                        Log.e(TAG, "AddGeofence: Remote Exception calling LocationManagerService");
                        result = false;
                        break;
                    }
                }
                return false;
            case 1:
                if (this.mFusedService != null) {
                    try {
                        this.mFusedService.addGeofences(new GeofenceHardwareRequestParcelable[]{request});
                        result = true;
                        break;
                    } catch (RemoteException e2) {
                        Log.e(TAG, "AddGeofence: RemoteException calling LocationManagerService");
                        result = false;
                        break;
                    }
                }
                return false;
            default:
                result = false;
                break;
        }
        if (result) {
            Message m = this.mReaperHandler.obtainMessage(1, callback);
            m.arg1 = monitoringType;
            this.mReaperHandler.sendMessage(m);
        } else {
            synchronized (this.mGeofences) {
                this.mGeofences.remove(geofenceId);
            }
        }
        if (!DEBUG) {
            return result;
        }
        Log.d(TAG, "addCircularFence: Result is: " + result);
        return result;
    }

    public boolean removeGeofence(int geofenceId, int monitoringType) {
        boolean result;
        if (DEBUG) {
            Log.d(TAG, "Remove Geofence: GeofenceId: " + geofenceId);
        }
        synchronized (this.mGeofences) {
            if (this.mGeofences.get(geofenceId) == null) {
                throw new IllegalArgumentException("Geofence " + geofenceId + " not registered.");
            }
        }
        switch (monitoringType) {
            case 0:
                if (this.mGpsService != null) {
                    try {
                        result = this.mGpsService.removeHardwareGeofence(geofenceId);
                        break;
                    } catch (RemoteException e) {
                        Log.e(TAG, "RemoveGeofence: Remote Exception calling LocationManagerService");
                        result = false;
                        break;
                    }
                }
                return false;
            case 1:
                if (this.mFusedService != null) {
                    try {
                        this.mFusedService.removeGeofences(new int[]{geofenceId});
                        result = true;
                        break;
                    } catch (RemoteException e2) {
                        Log.e(TAG, "RemoveGeofence: RemoteException calling LocationManagerService");
                        result = false;
                        break;
                    }
                }
                return false;
            default:
                result = false;
                break;
        }
        if (DEBUG) {
            Log.d(TAG, "removeGeofence: Result is: " + result);
        }
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean pauseGeofence(int r7, int r8) {
        /*
        r6 = this;
        r1 = 0;
        r2 = DEBUG;
        if (r2 == 0) goto L_0x001d;
    L_0x0005:
        r2 = "GeofenceHardwareImpl";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Pause Geofence: GeofenceId: ";
        r3 = r3.append(r4);
        r3 = r3.append(r7);
        r3 = r3.toString();
        android.util.Log.d(r2, r3);
    L_0x001d:
        r3 = r6.mGeofences;
        monitor-enter(r3);
        r2 = r6.mGeofences;	 Catch:{ all -> 0x0047 }
        r2 = r2.get(r7);	 Catch:{ all -> 0x0047 }
        if (r2 != 0) goto L_0x004a;
    L_0x0028:
        r2 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0047 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0047 }
        r4.<init>();	 Catch:{ all -> 0x0047 }
        r5 = "Geofence ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0047 }
        r4 = r4.append(r7);	 Catch:{ all -> 0x0047 }
        r5 = " not registered.";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0047 }
        r4 = r4.toString();	 Catch:{ all -> 0x0047 }
        r2.<init>(r4);	 Catch:{ all -> 0x0047 }
        throw r2;	 Catch:{ all -> 0x0047 }
    L_0x0047:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0047 }
        throw r2;
    L_0x004a:
        monitor-exit(r3);	 Catch:{ all -> 0x0047 }
        switch(r8) {
            case 0: goto L_0x006d;
            case 1: goto L_0x0082;
            default: goto L_0x004e;
        };
    L_0x004e:
        r1 = 0;
    L_0x004f:
        r2 = DEBUG;
        if (r2 == 0) goto L_0x006c;
    L_0x0053:
        r2 = "GeofenceHardwareImpl";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "pauseGeofence: Result is: ";
        r3 = r3.append(r4);
        r3 = r3.append(r1);
        r3 = r3.toString();
        android.util.Log.d(r2, r3);
    L_0x006c:
        return r1;
    L_0x006d:
        r2 = r6.mGpsService;
        if (r2 == 0) goto L_0x006c;
    L_0x0071:
        r2 = r6.mGpsService;	 Catch:{ RemoteException -> 0x0078 }
        r1 = r2.pauseHardwareGeofence(r7);	 Catch:{ RemoteException -> 0x0078 }
        goto L_0x004f;
    L_0x0078:
        r0 = move-exception;
        r2 = "GeofenceHardwareImpl";
        r3 = "PauseGeofence: Remote Exception calling LocationManagerService";
        android.util.Log.e(r2, r3);
        r1 = 0;
        goto L_0x004f;
    L_0x0082:
        r2 = r6.mFusedService;
        if (r2 == 0) goto L_0x006c;
    L_0x0086:
        r2 = r6.mFusedService;	 Catch:{ RemoteException -> 0x008d }
        r2.pauseMonitoringGeofence(r7);	 Catch:{ RemoteException -> 0x008d }
        r1 = 1;
        goto L_0x004f;
    L_0x008d:
        r0 = move-exception;
        r2 = "GeofenceHardwareImpl";
        r3 = "PauseGeofence: RemoteException calling LocationManagerService";
        android.util.Log.e(r2, r3);
        r1 = 0;
        goto L_0x004f;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.location.GeofenceHardwareImpl.pauseGeofence(int, int):boolean");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean resumeGeofence(int r7, int r8, int r9) {
        /*
        r6 = this;
        r1 = 0;
        r2 = DEBUG;
        if (r2 == 0) goto L_0x001d;
    L_0x0005:
        r2 = "GeofenceHardwareImpl";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Resume Geofence: GeofenceId: ";
        r3 = r3.append(r4);
        r3 = r3.append(r7);
        r3 = r3.toString();
        android.util.Log.d(r2, r3);
    L_0x001d:
        r3 = r6.mGeofences;
        monitor-enter(r3);
        r2 = r6.mGeofences;	 Catch:{ all -> 0x0047 }
        r2 = r2.get(r7);	 Catch:{ all -> 0x0047 }
        if (r2 != 0) goto L_0x004a;
    L_0x0028:
        r2 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0047 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0047 }
        r4.<init>();	 Catch:{ all -> 0x0047 }
        r5 = "Geofence ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0047 }
        r4 = r4.append(r7);	 Catch:{ all -> 0x0047 }
        r5 = " not registered.";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0047 }
        r4 = r4.toString();	 Catch:{ all -> 0x0047 }
        r2.<init>(r4);	 Catch:{ all -> 0x0047 }
        throw r2;	 Catch:{ all -> 0x0047 }
    L_0x0047:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0047 }
        throw r2;
    L_0x004a:
        monitor-exit(r3);	 Catch:{ all -> 0x0047 }
        switch(r8) {
            case 0: goto L_0x006d;
            case 1: goto L_0x0082;
            default: goto L_0x004e;
        };
    L_0x004e:
        r1 = 0;
    L_0x004f:
        r2 = DEBUG;
        if (r2 == 0) goto L_0x006c;
    L_0x0053:
        r2 = "GeofenceHardwareImpl";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "resumeGeofence: Result is: ";
        r3 = r3.append(r4);
        r3 = r3.append(r1);
        r3 = r3.toString();
        android.util.Log.d(r2, r3);
    L_0x006c:
        return r1;
    L_0x006d:
        r2 = r6.mGpsService;
        if (r2 == 0) goto L_0x006c;
    L_0x0071:
        r2 = r6.mGpsService;	 Catch:{ RemoteException -> 0x0078 }
        r1 = r2.resumeHardwareGeofence(r7, r9);	 Catch:{ RemoteException -> 0x0078 }
        goto L_0x004f;
    L_0x0078:
        r0 = move-exception;
        r2 = "GeofenceHardwareImpl";
        r3 = "ResumeGeofence: Remote Exception calling LocationManagerService";
        android.util.Log.e(r2, r3);
        r1 = 0;
        goto L_0x004f;
    L_0x0082:
        r2 = r6.mFusedService;
        if (r2 == 0) goto L_0x006c;
    L_0x0086:
        r2 = r6.mFusedService;	 Catch:{ RemoteException -> 0x008d }
        r2.resumeMonitoringGeofence(r7, r9);	 Catch:{ RemoteException -> 0x008d }
        r1 = 1;
        goto L_0x004f;
    L_0x008d:
        r0 = move-exception;
        r2 = "GeofenceHardwareImpl";
        r3 = "ResumeGeofence: RemoteException calling LocationManagerService";
        android.util.Log.e(r2, r3);
        r1 = 0;
        goto L_0x004f;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.location.GeofenceHardwareImpl.resumeGeofence(int, int, int):boolean");
    }

    public boolean registerForMonitorStateChangeCallback(int monitoringType, IGeofenceHardwareMonitorCallback callback) {
        Message reaperMessage = this.mReaperHandler.obtainMessage(2, callback);
        reaperMessage.arg1 = monitoringType;
        this.mReaperHandler.sendMessage(reaperMessage);
        Message m = this.mCallbacksHandler.obtainMessage(2, callback);
        m.arg1 = monitoringType;
        this.mCallbacksHandler.sendMessage(m);
        return true;
    }

    public boolean unregisterForMonitorStateChangeCallback(int monitoringType, IGeofenceHardwareMonitorCallback callback) {
        Message m = this.mCallbacksHandler.obtainMessage(3, callback);
        m.arg1 = monitoringType;
        this.mCallbacksHandler.sendMessage(m);
        return true;
    }

    public void reportGeofenceTransition(int geofenceId, Location location, int transition, long transitionTimestamp, int monitoringType, int sourcesUsed) {
        if (location == null) {
            Log.e(TAG, String.format("Invalid Geofence Transition: location=null", new Object[0]));
            return;
        }
        if (DEBUG) {
            Log.d(TAG, "GeofenceTransition| " + location + ", transition:" + transition + ", transitionTimestamp:" + transitionTimestamp + ", monitoringType:" + monitoringType + ", sourcesUsed:" + sourcesUsed);
        }
        GeofenceTransition geofenceTransition = new GeofenceTransition(geofenceId, transition, transitionTimestamp, location, monitoringType, sourcesUsed);
        acquireWakeLock();
        this.mGeofenceHandler.obtainMessage(1, geofenceTransition).sendToTarget();
    }

    public void reportGeofenceMonitorStatus(int monitoringType, int monitoringStatus, Location location, int source) {
        setMonitorAvailability(monitoringType, monitoringStatus);
        acquireWakeLock();
        this.mCallbacksHandler.obtainMessage(1, new GeofenceHardwareMonitorEvent(monitoringType, monitoringStatus, source, location)).sendToTarget();
    }

    private void reportGeofenceOperationStatus(int operation, int geofenceId, int operationStatus) {
        acquireWakeLock();
        Message message = this.mGeofenceHandler.obtainMessage(operation);
        message.arg1 = geofenceId;
        message.arg2 = operationStatus;
        message.sendToTarget();
    }

    public void reportGeofenceAddStatus(int geofenceId, int status) {
        if (DEBUG) {
            Log.d(TAG, "AddCallback| id:" + geofenceId + ", status:" + status);
        }
        reportGeofenceOperationStatus(2, geofenceId, status);
    }

    public void reportGeofenceRemoveStatus(int geofenceId, int status) {
        if (DEBUG) {
            Log.d(TAG, "RemoveCallback| id:" + geofenceId + ", status:" + status);
        }
        reportGeofenceOperationStatus(3, geofenceId, status);
    }

    public void reportGeofencePauseStatus(int geofenceId, int status) {
        if (DEBUG) {
            Log.d(TAG, "PauseCallbac| id:" + geofenceId + ", status" + status);
        }
        reportGeofenceOperationStatus(4, geofenceId, status);
    }

    public void reportGeofenceResumeStatus(int geofenceId, int status) {
        if (DEBUG) {
            Log.d(TAG, "ResumeCallback| id:" + geofenceId + ", status:" + status);
        }
        reportGeofenceOperationStatus(5, geofenceId, status);
    }

    private void setMonitorAvailability(int monitor, int val) {
        synchronized (this.mSupportedMonitorTypes) {
            this.mSupportedMonitorTypes[monitor] = val;
        }
    }

    int getMonitoringResolutionLevel(int monitoringType) {
        switch (monitoringType) {
            case 0:
            case 1:
                return 3;
            default:
                return 1;
        }
    }

    int getAllowedResolutionLevel(int pid, int uid) {
        if (this.mContext.checkPermission(permission.ACCESS_FINE_LOCATION, pid, uid) == 0) {
            return 3;
        }
        if (this.mContext.checkPermission(permission.ACCESS_COARSE_LOCATION, pid, uid) == 0) {
            return 2;
        }
        return 1;
    }
}
