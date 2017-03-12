package android.hardware;

import android.content.Context;
import android.net.ProxyInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.SystemProperties;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import com.sec.enterprise.knoxcustom.KnoxCustomManager;
import dalvik.system.CloseGuard;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SystemSensorManager extends SensorManager {
    private static final int DEBUG_LEVEL_HIGH = 18760;
    private static final int DEBUG_LEVEL_LOW = 20300;
    private static final int DEBUG_LEVEL_MID = 18765;
    private static boolean IS_DEBUG = false;
    private static InjectEventQueue mInjectEventQueue = null;
    private static boolean sSensorModuleInitialized = false;
    private final Context mContext;
    private final ArrayList<Sensor> mFullSensorsList = new ArrayList();
    private final SparseArray<Sensor> mHandleToSensor = new SparseArray();
    private final Object mLock = new Object();
    private final Looper mMainLooper;
    private final long mNativeInstance;
    private final HashMap<SensorEventListener, SensorEventQueue> mSensorListeners = new HashMap();
    private final int mTargetSdkLevel;
    private final HashMap<TriggerEventListener, TriggerEventQueue> mTriggerListeners = new HashMap();

    private static abstract class BaseEventQueue {
        protected static final int OPERATING_MODE_DATA_INJECTION = 1;
        protected static final int OPERATING_MODE_NORMAL = 0;
        private final SparseBooleanArray mActiveSensors = new SparseBooleanArray();
        private final CloseGuard mCloseGuard = CloseGuard.get();
        protected final SparseBooleanArray mFirstEvent = new SparseBooleanArray();
        protected final SystemSensorManager mManager;
        private final float[] mScratch = new float[16];
        protected final SparseIntArray mSensorAccuracies = new SparseIntArray();
        private long nSensorEventQueue;

        private static native void nativeDestroySensorEventQueue(long j);

        private static native int nativeDisableSensor(long j, int i);

        private static native int nativeEnableSensor(long j, int i, int i2, int i3);

        private static native int nativeFlushSensor(long j);

        private static native long nativeInitBaseEventQueue(long j, WeakReference<BaseEventQueue> weakReference, MessageQueue messageQueue, float[] fArr, String str, int i, String str2);

        private static native int nativeInjectSensorData(long j, int i, float[] fArr, int i2, long j2);

        protected abstract void addSensorEvent(Sensor sensor);

        protected abstract void dispatchFlushCompleteEvent(int i);

        protected abstract void dispatchSensorEvent(int i, float[] fArr, int i2, long j);

        protected abstract void removeSensorEvent(Sensor sensor);

        BaseEventQueue(Looper looper, SystemSensorManager manager, int mode, String packageName) {
            if (packageName == null) {
                packageName = ProxyInfo.LOCAL_EXCL_LIST;
            }
            this.nSensorEventQueue = nativeInitBaseEventQueue(manager.mNativeInstance, new WeakReference(this), looper.getQueue(), this.mScratch, packageName, mode, manager.mContext.getOpPackageName());
            this.mCloseGuard.open("dispose");
            this.mManager = manager;
        }

        public void dispose() {
            dispose(false);
        }

        public boolean addSensor(Sensor sensor, int delayUs, int maxBatchReportLatencyUs) {
            int handle = sensor.getHandle();
            if (this.mActiveSensors.get(handle)) {
                return false;
            }
            this.mActiveSensors.put(handle, true);
            addSensorEvent(sensor);
            if (enableSensor(sensor, delayUs, maxBatchReportLatencyUs) == 0 || (maxBatchReportLatencyUs != 0 && (maxBatchReportLatencyUs <= 0 || enableSensor(sensor, delayUs, 0) == 0))) {
                return true;
            }
            removeSensor(sensor, false);
            return false;
        }

        public boolean removeAllSensors() {
            for (int i = 0; i < this.mActiveSensors.size(); i++) {
                if (this.mActiveSensors.valueAt(i)) {
                    int handle = this.mActiveSensors.keyAt(i);
                    Sensor sensor = (Sensor) this.mManager.mHandleToSensor.get(handle);
                    if (sensor != null) {
                        disableSensor(sensor);
                        this.mActiveSensors.put(handle, false);
                        removeSensorEvent(sensor);
                    }
                }
            }
            return true;
        }

        public boolean removeSensor(Sensor sensor, boolean disable) {
            if (!this.mActiveSensors.get(sensor.getHandle())) {
                return false;
            }
            if (disable) {
                disableSensor(sensor);
            }
            this.mActiveSensors.put(sensor.getHandle(), false);
            removeSensorEvent(sensor);
            return true;
        }

        public int flush() {
            if (this.nSensorEventQueue != 0) {
                return nativeFlushSensor(this.nSensorEventQueue);
            }
            throw new NullPointerException();
        }

        public boolean hasSensors() {
            return this.mActiveSensors.indexOfValue(true) >= 0;
        }

        protected void finalize() throws Throwable {
            try {
                dispose(true);
            } finally {
                super.finalize();
            }
        }

        private void dispose(boolean finalized) {
            if (this.mCloseGuard != null) {
                if (finalized) {
                    this.mCloseGuard.warnIfOpen();
                }
                this.mCloseGuard.close();
            }
            if (this.nSensorEventQueue != 0) {
                nativeDestroySensorEventQueue(this.nSensorEventQueue);
                this.nSensorEventQueue = 0;
            }
        }

        private int enableSensor(Sensor sensor, int rateUs, int maxBatchReportLatencyUs) {
            if (this.nSensorEventQueue == 0) {
                throw new NullPointerException();
            } else if (sensor != null) {
                return nativeEnableSensor(this.nSensorEventQueue, sensor.getHandle(), rateUs, maxBatchReportLatencyUs);
            } else {
                throw new NullPointerException();
            }
        }

        protected int injectSensorDataBase(int handle, float[] values, int accuracy, long timestamp) {
            return nativeInjectSensorData(this.nSensorEventQueue, handle, values, accuracy, timestamp);
        }

        private int disableSensor(Sensor sensor) {
            if (this.nSensorEventQueue == 0) {
                throw new NullPointerException();
            } else if (sensor != null) {
                return nativeDisableSensor(this.nSensorEventQueue, sensor.getHandle());
            } else {
                throw new NullPointerException();
            }
        }
    }

    final class InjectEventQueue extends BaseEventQueue {
        public InjectEventQueue(Looper looper, SystemSensorManager manager, String packageName) {
            super(looper, manager, 1, packageName);
        }

        int injectSensorData(int handle, float[] values, int accuracy, long timestamp) {
            return injectSensorDataBase(handle, values, accuracy, timestamp);
        }

        protected void dispatchSensorEvent(int handle, float[] values, int accuracy, long timestamp) {
        }

        protected void dispatchFlushCompleteEvent(int handle) {
        }

        protected void addSensorEvent(Sensor sensor) {
        }

        protected void removeSensorEvent(Sensor sensor) {
        }
    }

    static final class SensorEventQueue extends BaseEventQueue {
        private final SensorEventListener mListener;
        private final SparseArray<SensorEvent> mSensorsEvents = new SparseArray();

        public SensorEventQueue(SensorEventListener listener, Looper looper, SystemSensorManager manager, String packageName) {
            super(looper, manager, 0, packageName);
            this.mListener = listener;
        }

        public void addSensorEvent(Sensor sensor) {
            SensorEvent t = new SensorEvent(Sensor.getMaxLengthValuesArray(sensor, this.mManager.mTargetSdkLevel));
            synchronized (this.mSensorsEvents) {
                this.mSensorsEvents.put(sensor.getHandle(), t);
            }
        }

        public void removeSensorEvent(Sensor sensor) {
            synchronized (this.mSensorsEvents) {
                this.mSensorsEvents.delete(sensor.getHandle());
            }
        }

        protected void dispatchSensorEvent(int handle, float[] values, int inAccuracy, long timestamp) {
            Sensor sensor = (Sensor) this.mManager.mHandleToSensor.get(handle);
            synchronized (this.mSensorsEvents) {
                SensorEvent t = (SensorEvent) this.mSensorsEvents.get(handle);
            }
            if (t != null) {
                System.arraycopy(values, 0, t.values, 0, t.values.length);
                t.timestamp = timestamp;
                t.accuracy = inAccuracy;
                t.sensor = sensor;
                switch (t.sensor.getType()) {
                    case 8:
                        if (((int) values[0]) <= 0) {
                            Log.d("SensorManager", "Proximity, val = " + values[0] + "  [close]");
                            break;
                        } else {
                            Log.d("SensorManager", "Proximity, val = " + values[0] + "  [far]");
                            break;
                        }
                }
                int accuracy = this.mSensorAccuracies.get(handle);
                if (t.accuracy >= 0 && accuracy != t.accuracy) {
                    this.mSensorAccuracies.put(handle, t.accuracy);
                    this.mListener.onAccuracyChanged(t.sensor, t.accuracy);
                }
                this.mListener.onSensorChanged(t);
            }
        }

        protected void dispatchFlushCompleteEvent(int handle) {
            if (this.mListener instanceof SensorEventListener2) {
                ((SensorEventListener2) this.mListener).onFlushCompleted((Sensor) this.mManager.mHandleToSensor.get(handle));
            }
        }
    }

    static final class TriggerEventQueue extends BaseEventQueue {
        private final TriggerEventListener mListener;
        private final SparseArray<TriggerEvent> mTriggerEvents = new SparseArray();

        public TriggerEventQueue(TriggerEventListener listener, Looper looper, SystemSensorManager manager, String packageName) {
            super(looper, manager, 0, packageName);
            this.mListener = listener;
        }

        public void addSensorEvent(Sensor sensor) {
            TriggerEvent t = new TriggerEvent(Sensor.getMaxLengthValuesArray(sensor, this.mManager.mTargetSdkLevel));
            synchronized (this.mTriggerEvents) {
                this.mTriggerEvents.put(sensor.getHandle(), t);
            }
        }

        public void removeSensorEvent(Sensor sensor) {
            synchronized (this.mTriggerEvents) {
                this.mTriggerEvents.delete(sensor.getHandle());
            }
        }

        protected void dispatchSensorEvent(int handle, float[] values, int accuracy, long timestamp) {
            Sensor sensor = (Sensor) this.mManager.mHandleToSensor.get(handle);
            synchronized (this.mTriggerEvents) {
                TriggerEvent t = (TriggerEvent) this.mTriggerEvents.get(handle);
            }
            if (t == null) {
                Log.e("SensorManager", "Error: Trigger Event is null for Sensor: " + sensor);
                return;
            }
            System.arraycopy(values, 0, t.values, 0, t.values.length);
            t.timestamp = timestamp;
            t.sensor = sensor;
            this.mManager.cancelTriggerSensorImpl(this.mListener, sensor, false);
            this.mListener.onTrigger(t);
        }

        protected void dispatchFlushCompleteEvent(int handle) {
        }
    }

    private static native void nativeClassInit();

    private static native long nativeCreate(String str);

    private static native boolean nativeGetSensorAtIndex(long j, Sensor sensor, int i);

    private static native boolean nativeIsDataInjectionEnabled(long j);

    public SystemSensorManager(Context context, Looper mainLooper) {
        this.mMainLooper = mainLooper;
        this.mTargetSdkLevel = context.getApplicationInfo().targetSdkVersion;
        this.mContext = context;
        this.mNativeInstance = nativeCreate(context.getOpPackageName());
        synchronized (this.mLock) {
            if (!sSensorModuleInitialized) {
                sSensorModuleInitialized = true;
                nativeClassInit();
            }
        }
        int knoxCustomSensorDisabled = 0;
        KnoxCustomManager knoxCustomManager = EnterpriseDeviceManager.getInstance().getKnoxCustomManager();
        if (knoxCustomManager != null) {
            knoxCustomSensorDisabled = knoxCustomManager.getSensorDisabled();
        }
        int index = 0;
        while (true) {
            Sensor sensor = new Sensor();
            if (nativeGetSensorAtIndex(this.mNativeInstance, sensor, index)) {
                if ((getKnoxCustomSensorMask(sensor.getType()) & knoxCustomSensorDisabled) != 0) {
                    Log.d("SensorManager", "disable the sensor index = " + index);
                } else {
                    this.mFullSensorsList.add(sensor);
                    this.mHandleToSensor.append(sensor.getHandle(), sensor);
                }
                index++;
            } else {
                IS_DEBUG = isDebug();
                return;
            }
        }
    }

    protected List<Sensor> getFullSensorList() {
        return this.mFullSensorsList;
    }

    protected boolean registerListenerImpl(SensorEventListener listener, Sensor sensor, int delayUs, Handler handler, int maxBatchReportLatencyUs, int reservedFlags) {
        boolean z = false;
        if (listener == null || sensor == null) {
            Log.e("SensorManager", "sensor or listener is null");
        } else if (sensor.getReportingMode() == 2) {
            Log.e("SensorManager", "Trigger Sensors should use the requestTriggerSensor.");
        } else if (maxBatchReportLatencyUs < 0 || delayUs < 0) {
            Log.e("SensorManager", "maxBatchReportLatencyUs and delayUs should be non-negative");
        } else {
            String strlistener = EncodeLog(" listener = " + listener);
            synchronized (this.mSensorListeners) {
                SensorEventQueue queue = (SensorEventQueue) this.mSensorListeners.get(listener);
                if (queue == null) {
                    queue = new SensorEventQueue(listener, handler != null ? handler.getLooper() : this.mMainLooper, this, listener.getClass().getEnclosingClass() != null ? listener.getClass().getEnclosingClass().getName() : listener.getClass().getName());
                    if (queue.addSensor(sensor, delayUs, maxBatchReportLatencyUs)) {
                        this.mSensorListeners.put(listener, queue);
                        Log.d("SensorManager", "registerListener :: " + sensor.getHandle() + ", " + sensor.getName() + ", " + delayUs + ", " + maxBatchReportLatencyUs + ", " + strlistener);
                        z = true;
                    } else {
                        queue.dispose();
                    }
                } else {
                    z = queue.addSensor(sensor, delayUs, maxBatchReportLatencyUs);
                }
            }
        }
        return z;
    }

    protected void unregisterListenerImpl(SensorEventListener listener, Sensor sensor) {
        if (sensor == null || sensor.getReportingMode() != 2) {
            String strlistener = EncodeLog(" listener = " + listener);
            synchronized (this.mSensorListeners) {
                SensorEventQueue queue = (SensorEventQueue) this.mSensorListeners.get(listener);
                if (queue != null) {
                    boolean result;
                    if (sensor == null) {
                        result = queue.removeAllSensors();
                    } else {
                        result = queue.removeSensor(sensor, true);
                    }
                    if (result && !queue.hasSensors()) {
                        this.mSensorListeners.remove(listener);
                        queue.dispose();
                    }
                    Log.d("SensorManager", "unregisterListener ::  " + strlistener);
                }
            }
        }
    }

    protected boolean requestTriggerSensorImpl(TriggerEventListener listener, Sensor sensor) {
        boolean z = false;
        if (sensor == null) {
            throw new IllegalArgumentException("sensor cannot be null");
        } else if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        } else {
            if (sensor.getReportingMode() == 2) {
                String strlistener = EncodeLog(" listener = " + listener);
                synchronized (this.mTriggerListeners) {
                    TriggerEventQueue queue = (TriggerEventQueue) this.mTriggerListeners.get(listener);
                    if (queue == null) {
                        String fullClassName;
                        if (listener.getClass().getEnclosingClass() != null) {
                            fullClassName = listener.getClass().getEnclosingClass().getName();
                        } else {
                            fullClassName = listener.getClass().getName();
                        }
                        queue = new TriggerEventQueue(listener, this.mMainLooper, this, fullClassName);
                        if (queue.addSensor(sensor, 0, 0)) {
                            this.mTriggerListeners.put(listener, queue);
                            Log.d("SensorManager", "requestTrigger :: " + sensor.getHandle() + ", " + sensor.getName() + ", " + strlistener);
                            z = true;
                        } else {
                            queue.dispose();
                            Log.d("SensorManager", "requestTrigger :: fail");
                        }
                    } else {
                        z = queue.addSensor(sensor, 0, 0);
                    }
                }
            }
            return z;
        }
    }

    protected boolean cancelTriggerSensorImpl(TriggerEventListener listener, Sensor sensor, boolean disable) {
        boolean z = false;
        if (sensor == null || sensor.getReportingMode() == 2) {
            synchronized (this.mTriggerListeners) {
                TriggerEventQueue queue = (TriggerEventQueue) this.mTriggerListeners.get(listener);
                if (queue != null) {
                    if (sensor == null) {
                        z = queue.removeAllSensors();
                    } else {
                        z = queue.removeSensor(sensor, disable);
                    }
                    if (z && !queue.hasSensors()) {
                        this.mTriggerListeners.remove(listener);
                        queue.dispose();
                    }
                    if (listener != null) {
                        Log.d("SensorManager", "cancelTrigger :: " + EncodeLog(" listener = " + listener));
                    }
                }
            }
        }
        return z;
    }

    protected boolean flushImpl(SensorEventListener listener) {
        boolean z = false;
        if (listener == null) {
            throw new IllegalArgumentException("listener cannot be null");
        }
        synchronized (this.mSensorListeners) {
            SensorEventQueue queue = (SensorEventQueue) this.mSensorListeners.get(listener);
            if (queue == null) {
            } else {
                if (queue.flush() == 0) {
                    z = true;
                }
            }
        }
        return z;
    }

    protected boolean initDataInjectionImpl(boolean enable) {
        boolean z;
        synchronized (this.mLock) {
            if (!enable) {
                if (mInjectEventQueue != null) {
                    mInjectEventQueue.dispose();
                    mInjectEventQueue = null;
                }
                z = true;
            } else if (nativeIsDataInjectionEnabled(this.mNativeInstance)) {
                if (mInjectEventQueue == null) {
                    mInjectEventQueue = new InjectEventQueue(this.mMainLooper, this, this.mContext.getPackageName());
                }
                z = true;
            } else {
                Log.e("SensorManager", "Data Injection mode not enabled");
                z = false;
            }
        }
        return z;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected boolean injectSensorDataImpl(android.hardware.Sensor r11, float[] r12, int r13, long r14) {
        /*
        r10 = this;
        r7 = 0;
        r8 = r10.mLock;
        monitor-enter(r8);
        r0 = mInjectEventQueue;	 Catch:{ all -> 0x002e }
        if (r0 != 0) goto L_0x0012;
    L_0x0008:
        r0 = "SensorManager";
        r1 = "Data injection mode not activated before calling injectSensorData";
        android.util.Log.e(r0, r1);	 Catch:{ all -> 0x002e }
        monitor-exit(r8);	 Catch:{ all -> 0x002e }
        r0 = r7;
    L_0x0011:
        return r0;
    L_0x0012:
        r0 = mInjectEventQueue;	 Catch:{ all -> 0x002e }
        r1 = r11.getHandle();	 Catch:{ all -> 0x002e }
        r2 = r12;
        r3 = r13;
        r4 = r14;
        r6 = r0.injectSensorData(r1, r2, r3, r4);	 Catch:{ all -> 0x002e }
        if (r6 == 0) goto L_0x0029;
    L_0x0021:
        r0 = mInjectEventQueue;	 Catch:{ all -> 0x002e }
        r0.dispose();	 Catch:{ all -> 0x002e }
        r0 = 0;
        mInjectEventQueue = r0;	 Catch:{ all -> 0x002e }
    L_0x0029:
        if (r6 != 0) goto L_0x0031;
    L_0x002b:
        r0 = 1;
    L_0x002c:
        monitor-exit(r8);	 Catch:{ all -> 0x002e }
        goto L_0x0011;
    L_0x002e:
        r0 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x002e }
        throw r0;
    L_0x0031:
        r0 = r7;
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.SystemSensorManager.injectSensorDataImpl(android.hardware.Sensor, float[], int, long):boolean");
    }

    private static int getKnoxCustomSensorMask(int id) {
        switch (id) {
            case 1:
                return 2;
            case 2:
            case 14:
                return 32;
            case 3:
                return 8;
            case 4:
            case 16:
                return 1;
            case 5:
                return 4;
            case 8:
                return 16;
            default:
                return 0;
        }
    }

    protected static String EncodeLog(String path) {
        return IS_DEBUG ? path : " ";
    }

    private boolean isDebug() {
        String state = SystemProperties.get("ro.debug_level", "Unknown");
        try {
            if (state.equalsIgnoreCase("Unknown")) {
                return false;
            }
            int debugLevel = Integer.parseInt(state.substring(2), 16);
            if (debugLevel == 20300) {
                return false;
            }
            if (debugLevel == 18765 || debugLevel == 18760) {
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
