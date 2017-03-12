package com.samsung.android.sensorhub;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SensorHubManager {
    public static final int CONTEXT_DELAY = 0;
    public static final int GESTURE_SENSOR_DELAY = 1;
    private static final int SENSORHUB_DISABLE = -1;
    private static final int SENSORHUB_DISABLE_FOR_DELAY = -1;
    private static final int SENSORHUB_ENABLE = 1;
    private static final int SENSORHUB_EVENT_SIZE = 16384;
    private static final String TAG = "SensorHubManager";
    private static ArrayList<SensorHub> sFullSensorHubsList = new ArrayList();
    static SparseArray<SensorHub> sHandleToSensorHub = new SparseArray();
    static final ArrayList<ListenerDelegate> sListeners = new ArrayList();
    private static SensorHubEventPool sPool;
    private static long sQueue;
    private static SparseArray<List<SensorHub>> sSensorHubListByType = new SparseArray();
    private static boolean sSensorHubModuleInitialized = false;
    private static SensorHubThread sSensorHubThread;
    Looper mMainLooper;

    private class ListenerDelegate {
        private final Handler mHandler;
        private final SensorHubEventListener mSensorHubEventListener;
        private final ArrayList<SensorHub> mSensorHubList = new ArrayList();
        public SparseBooleanArray mSensorHubs = new SparseBooleanArray();

        ListenerDelegate(SensorHubEventListener listener, SensorHub sensorhub, Handler handler) {
            this.mSensorHubEventListener = listener;
            this.mHandler = new Handler(handler != null ? handler.getLooper() : SensorHubManager.this.mMainLooper, SensorHubManager.this) {
                public void handleMessage(Message msg) {
                    SensorHubEvent t = msg.obj;
                    ListenerDelegate.this.mSensorHubEventListener.onGetSensorHubData(t);
                    SensorHubManager.sPool.returnToPool(t);
                }
            };
            addSensorHub(sensorhub);
        }

        Object getListener() {
            return this.mSensorHubEventListener;
        }

        void addSensorHub(SensorHub sensorhub) {
            this.mSensorHubs.put(sensorhub.getHandle(), true);
            this.mSensorHubList.add(sensorhub);
        }

        int removeSensorHub(SensorHub sensorhub) {
            this.mSensorHubs.delete(sensorhub.getHandle());
            this.mSensorHubList.remove(sensorhub);
            return this.mSensorHubs.size();
        }

        boolean hasSensorHub(SensorHub sensorhub) {
            return this.mSensorHubs.get(sensorhub.getHandle());
        }

        List<SensorHub> getSensorHubs() {
            return this.mSensorHubList;
        }

        void onGetSensorHubDataLocked(SensorHub sensorhub, byte[] buffer, int length, float[] values, long[] timestamp) {
            SensorHubEvent t = SensorHubManager.sPool.getFromPool();
            t.sensorhub = sensorhub;
            StringBuffer log = new StringBuffer("onGetSensorHubDataLocked: ");
            int i;
            if (length > 0) {
                t.buffer = new byte[length];
                t.length = length;
                log.append("library(" + length + ") = ");
                t.buffer[0] = buffer[0];
                log.append(t.buffer[0]);
                i = 1;
                while (i < length) {
                    t.buffer[i] = buffer[i];
                    if (length < 256 || i < 6 || i >= length - 6) {
                        log.append(", ");
                        log.append(t.buffer[i]);
                    }
                    if (length > 256 && i == 6) {
                        log.append(" ...");
                    }
                    i++;
                }
            } else {
                t.values = values;
                t.timestamp = timestamp[0];
                log.append("gesture = ");
                t.values[0] = values[0];
                log.append(t.values[0]);
                for (i = 1; i < values.length; i++) {
                    t.values[i] = values[i];
                    log.append(", ");
                    log.append(t.values[i]);
                }
            }
            Log.d(SensorHubManager.TAG, log.toString());
            Message msg = Message.obtain();
            msg.what = 0;
            msg.obj = t;
            this.mHandler.sendMessage(msg);
        }
    }

    private static class SensorHubEventPool {
        private int mNumItemsInPool;
        private final SensorHubEvent[] mPool;
        private final int mPoolSize;

        private SensorHubEvent createSensorHubEvent() {
            return new SensorHubEvent(16384);
        }

        SensorHubEventPool(int poolSize) {
            this.mPoolSize = poolSize;
            this.mNumItemsInPool = poolSize;
            this.mPool = new SensorHubEvent[poolSize];
        }

        SensorHubEvent getFromPool() {
            SensorHubEvent t = null;
            synchronized (this) {
                if (this.mNumItemsInPool > 0) {
                    int index = this.mPoolSize - this.mNumItemsInPool;
                    t = this.mPool[index];
                    this.mPool[index] = null;
                    this.mNumItemsInPool--;
                }
            }
            if (t == null) {
                return createSensorHubEvent();
            }
            return t;
        }

        void returnToPool(SensorHubEvent t) {
            synchronized (this) {
                if (this.mNumItemsInPool < this.mPoolSize) {
                    this.mNumItemsInPool++;
                    this.mPool[this.mPoolSize - this.mNumItemsInPool] = t;
                }
            }
        }
    }

    private static class SensorHubThread {
        boolean mSensorHubsReady;
        Thread mThread;

        private class SensorHubThreadRunnable implements Runnable {
            SensorHubThreadRunnable() {
            }

            private boolean open() {
                SensorHubManager.sQueue = SensorHubManager.sensorhubs_create_queue();
                return true;
            }

            public void run() {
                byte[] buffer = new byte[16384];
                float[] values = new float[9];
                int[] version = new int[1];
                int[] type = new int[1];
                int[] sizeofdata = new int[1];
                long[] timestamp = new long[1];
                Process.setThreadPriority(-8);
                Log.d(SensorHubManager.TAG, "=======>>> SensorHubManager Thread RUNNING <<<=======");
                if (open()) {
                    synchronized (this) {
                        SensorHubThread.this.mSensorHubsReady = true;
                        notify();
                    }
                    while (true) {
                        int sensorhub = SensorHubManager.sensorhubs_data_poll(SensorHubManager.sQueue, buffer, version, type, sizeofdata, values, timestamp, 16384);
                        int length = sizeofdata[0];
                        synchronized (SensorHubManager.sListeners) {
                            if (sensorhub != -1) {
                                if (!SensorHubManager.sListeners.isEmpty()) {
                                    SensorHub sensorhubObject = (SensorHub) SensorHubManager.sHandleToSensorHub.get(sensorhub);
                                    if (sensorhubObject != null) {
                                        int size = SensorHubManager.sListeners.size();
                                        for (int i = 0; i < size; i++) {
                                            ListenerDelegate listener = (ListenerDelegate) SensorHubManager.sListeners.get(i);
                                            if (listener.hasSensorHub(sensorhubObject)) {
                                                listener.onGetSensorHubDataLocked(sensorhubObject, buffer, length, values, timestamp);
                                            }
                                        }
                                    }
                                }
                            }
                            if (sensorhub != -1 || SensorHubManager.sListeners.isEmpty()) {
                                break;
                            }
                            Log.e(SensorHubManager.TAG, "sensorhubs_data_poll() failed, we bail out: sensorHub=" + sensorhub);
                        }
                    }
                    SensorHubManager.sensorhubs_destroy_queue(SensorHubManager.sQueue);
                    SensorHubManager.sQueue = 0;
                    SensorHubThread.this.mThread = null;
                }
            }
        }

        SensorHubThread() {
        }

        protected void finalize() {
        }

        boolean startLocked() {
            try {
                if (this.mThread == null) {
                    this.mSensorHubsReady = false;
                    SensorHubThreadRunnable runnable = new SensorHubThreadRunnable();
                    Thread thread = new Thread(runnable, SensorHubThread.class.getName());
                    thread.start();
                    synchronized (runnable) {
                        while (!this.mSensorHubsReady) {
                            runnable.wait();
                        }
                    }
                    this.mThread = thread;
                }
            } catch (InterruptedException e) {
            }
            if (this.mThread == null) {
                return false;
            }
            return true;
        }
    }

    private static native void nativeClassInit();

    static native long sensorhubs_create_queue();

    static native int sensorhubs_data_poll(long j, byte[] bArr, int[] iArr, int[] iArr2, int[] iArr3, float[] fArr, long[] jArr, int i);

    static native void sensorhubs_destroy_queue(long j);

    static native boolean sensorhubs_enabledisable(long j, int i, int i2, int i3);

    private static native int sensorhubs_get_next_module(SensorHub sensorHub, int i);

    private static native int sensorhubs_module_init();

    static native int sensorhubs_send_data(long j, int i, int i2, byte[] bArr);

    static native boolean sensorhubs_send_delay(long j, int i, int i2);

    public SensorHubManager(Context context, Looper mainLooper) {
        this.mMainLooper = mainLooper;
        synchronized (sListeners) {
            if (!sSensorHubModuleInitialized) {
                sSensorHubModuleInitialized = true;
                nativeClassInit();
                sensorhubs_module_init();
                Log.d(TAG, "sensorhubs_module_init()");
                ArrayList<SensorHub> fullList = sFullSensorHubsList;
                int i = 0;
                do {
                    SensorHub sensorhub = new SensorHub();
                    i = sensorhubs_get_next_module(sensorhub, i);
                    Log.d(TAG, "Num SensorHub= " + i);
                    if (i >= 0) {
                        Log.d(TAG, "found sensorhub= " + sensorhub.getName() + ", handle=" + sensorhub.getHandle());
                        fullList.add(sensorhub);
                        sHandleToSensorHub.append(sensorhub.getHandle(), sensorhub);
                        continue;
                    }
                } while (i > 0);
                sPool = new SensorHubEventPool(sFullSensorHubsList.size() * 2);
                sSensorHubThread = new SensorHubThread();
            }
        }
    }

    public List<SensorHub> getSensorHubList(int type) {
        List<SensorHub> list;
        ArrayList<SensorHub> fullList = sFullSensorHubsList;
        synchronized (fullList) {
            list = (List) sSensorHubListByType.get(type);
            if (list == null) {
                list = new ArrayList();
                Iterator i$ = fullList.iterator();
                while (i$.hasNext()) {
                    SensorHub i = (SensorHub) i$.next();
                    if (i.getType() == type) {
                        list.add(i);
                    }
                }
                list = Collections.unmodifiableList(list);
                sSensorHubListByType.append(type, list);
            }
        }
        return list;
    }

    public SensorHub getDefaultSensorHub(int type) {
        List<SensorHub> l = getSensorHubList(type);
        return l.isEmpty() ? null : (SensorHub) l.get(0);
    }

    public void unregisterListener(SensorHubEventListener listener, SensorHub sensorhub) {
        unregisterListener((Object) listener, sensorhub);
    }

    public void unregisterListener(SensorHubEventListener listener) {
        unregisterListener((Object) listener);
    }

    public boolean registerListener(SensorHubEventListener listener, SensorHub sensorhub, int rate) {
        return registerListener(listener, sensorhub, rate, null);
    }

    private boolean enableSensorHubLocked(SensorHub sensorhub, int delay) {
        Iterator i$ = sListeners.iterator();
        while (i$.hasNext()) {
            if (((ListenerDelegate) i$.next()).hasSensorHub(sensorhub)) {
                return sensorhubs_enabledisable(sQueue, sensorhub.getHandle(), 1, delay);
            }
        }
        return false;
    }

    private boolean disableSensorHubLocked(SensorHub sensorhub) {
        return sensorhubs_enabledisable(sQueue, sensorhub.getHandle(), -1, -1);
    }

    public boolean registerListener(SensorHubEventListener listener, SensorHub sensorhub, int rate, Handler handler) {
        if (rate >= 0) {
            return registerListener(listener, sensorhub, rate, handler, 0);
        }
        throw new IllegalArgumentException("rate must be >=0");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean registerListener(com.samsung.android.sensorhub.SensorHubEventListener r11, com.samsung.android.sensorhub.SensorHub r12, int r13, android.os.Handler r14, int r15) {
        /*
        r10 = this;
        if (r11 == 0) goto L_0x0004;
    L_0x0002:
        if (r12 != 0) goto L_0x0006;
    L_0x0004:
        r5 = 0;
    L_0x0005:
        return r5;
    L_0x0006:
        if (r13 >= 0) goto L_0x0011;
    L_0x0008:
        r6 = new java.lang.IllegalArgumentException;
        r7 = "rate must be >=0";
        r6.<init>(r7);
        throw r6;
    L_0x0011:
        r5 = 1;
        r0 = -1;
        switch(r13) {
            case 0: goto L_0x009c;
            case 1: goto L_0x009f;
            default: goto L_0x0016;
        };
    L_0x0016:
        r0 = r13;
    L_0x0017:
        r7 = sListeners;
        monitor-enter(r7);
        r3 = 0;
        r6 = sListeners;	 Catch:{ all -> 0x0099 }
        r2 = r6.iterator();	 Catch:{ all -> 0x0099 }
    L_0x0021:
        r6 = r2.hasNext();	 Catch:{ all -> 0x0099 }
        if (r6 == 0) goto L_0x00c6;
    L_0x0027:
        r1 = r2.next();	 Catch:{ all -> 0x0099 }
        r1 = (com.samsung.android.sensorhub.SensorHubManager.ListenerDelegate) r1;	 Catch:{ all -> 0x0099 }
        r6 = r1.getListener();	 Catch:{ all -> 0x0099 }
        if (r6 != r11) goto L_0x0021;
    L_0x0033:
        r3 = r1;
        r4 = r3;
    L_0x0035:
        r6 = "SensorHubManager";
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00c3 }
        r8.<init>();	 Catch:{ all -> 0x00c3 }
        r9 = "registerListener: handle= ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x00c3 }
        r9 = r12.getHandle();	 Catch:{ all -> 0x00c3 }
        r8 = r8.append(r9);	 Catch:{ all -> 0x00c3 }
        r9 = " delay= ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x00c3 }
        r8 = r8.append(r0);	 Catch:{ all -> 0x00c3 }
        r9 = " Listener= ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x00c3 }
        r8 = r8.append(r11);	 Catch:{ all -> 0x00c3 }
        r8 = r8.toString();	 Catch:{ all -> 0x00c3 }
        android.util.Log.d(r6, r8);	 Catch:{ all -> 0x00c3 }
        if (r4 != 0) goto L_0x00ac;
    L_0x0068:
        r3 = new com.samsung.android.sensorhub.SensorHubManager$ListenerDelegate;	 Catch:{ all -> 0x00c3 }
        r3.<init>(r11, r12, r14);	 Catch:{ all -> 0x00c3 }
        r6 = sListeners;	 Catch:{ all -> 0x0099 }
        r6.add(r3);	 Catch:{ all -> 0x0099 }
        r6 = sListeners;	 Catch:{ all -> 0x0099 }
        r6 = r6.isEmpty();	 Catch:{ all -> 0x0099 }
        if (r6 != 0) goto L_0x00aa;
    L_0x007a:
        r6 = sSensorHubThread;	 Catch:{ all -> 0x0099 }
        r6 = r6.startLocked();	 Catch:{ all -> 0x0099 }
        if (r6 == 0) goto L_0x00a3;
    L_0x0082:
        r6 = r10.enableSensorHubLocked(r12, r0);	 Catch:{ all -> 0x0099 }
        if (r6 != 0) goto L_0x0096;
    L_0x0088:
        r6 = sListeners;	 Catch:{ all -> 0x0099 }
        r6.remove(r3);	 Catch:{ all -> 0x0099 }
        r5 = 0;
        r6 = "SensorHubManager";
        r8 = "registerListener: enableSensorHubLocked fail 1";
        android.util.Log.d(r6, r8);	 Catch:{ all -> 0x0099 }
    L_0x0096:
        monitor-exit(r7);	 Catch:{ all -> 0x0099 }
        goto L_0x0005;
    L_0x0099:
        r6 = move-exception;
    L_0x009a:
        monitor-exit(r7);	 Catch:{ all -> 0x0099 }
        throw r6;
    L_0x009c:
        r0 = 0;
        goto L_0x0017;
    L_0x009f:
        r0 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        goto L_0x0017;
    L_0x00a3:
        r6 = sListeners;	 Catch:{ all -> 0x0099 }
        r6.remove(r3);	 Catch:{ all -> 0x0099 }
        r5 = 0;
        goto L_0x0096;
    L_0x00aa:
        r5 = 0;
        goto L_0x0096;
    L_0x00ac:
        r4.addSensorHub(r12);	 Catch:{ all -> 0x00c3 }
        r6 = r10.enableSensorHubLocked(r12, r0);	 Catch:{ all -> 0x00c3 }
        if (r6 != 0) goto L_0x00c1;
    L_0x00b5:
        r4.removeSensorHub(r12);	 Catch:{ all -> 0x00c3 }
        r5 = 0;
        r6 = "SensorHubManager";
        r8 = "registerListener: enableSensorHubLocked fail 2";
        android.util.Log.d(r6, r8);	 Catch:{ all -> 0x00c3 }
    L_0x00c1:
        r3 = r4;
        goto L_0x0096;
    L_0x00c3:
        r6 = move-exception;
        r3 = r4;
        goto L_0x009a;
    L_0x00c6:
        r4 = r3;
        goto L_0x0035;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.sensorhub.SensorHubManager.registerListener(com.samsung.android.sensorhub.SensorHubEventListener, com.samsung.android.sensorhub.SensorHub, int, android.os.Handler, int):boolean");
    }

    private void unregisterListener(Object listener, SensorHub sensorhub) {
        if (listener != null && sensorhub != null) {
            synchronized (sListeners) {
                int size = sListeners.size();
                int i = 0;
                while (i < size) {
                    ListenerDelegate l = (ListenerDelegate) sListeners.get(i);
                    if (l.getListener() == listener) {
                        if (l.removeSensorHub(sensorhub) == 0) {
                            sListeners.remove(i);
                        }
                        disableSensorHubLocked(sensorhub);
                        Log.d(TAG, "unregisterListener: handle= " + sensorhub.getHandle() + " Listener= " + listener);
                    } else {
                        i++;
                    }
                }
                disableSensorHubLocked(sensorhub);
                Log.d(TAG, "unregisterListener: handle= " + sensorhub.getHandle() + " Listener= " + listener);
            }
        }
    }

    private void unregisterListener(Object listener) {
        if (listener != null) {
            synchronized (sListeners) {
                int size = sListeners.size();
                int i = 0;
                while (i < size) {
                    ListenerDelegate l = (ListenerDelegate) sListeners.get(i);
                    if (l.getListener() == listener) {
                        sListeners.remove(i);
                        for (SensorHub sensorhub : l.getSensorHubs()) {
                            disableSensorHubLocked(sensorhub);
                            Log.d(TAG, "unregisterListener: disable all sensorhubs for this listener, name=  listener= " + listener);
                        }
                    } else {
                        i++;
                    }
                }
            }
        }
    }

    public int SendSensorHubData(SensorHub sensorhub, int datasize, byte[] data) {
        return SendSensorHubData(datasize, data);
    }

    private static int SendSensorHubData(int datasize, byte[] data) {
        StringBuffer log = new StringBuffer();
        int i = 0;
        while (i < datasize) {
            if (i == 0) {
                log.append("send data = ");
            } else if (datasize < 256 || i < 6 || i >= datasize - 6) {
                log.append(", ");
            }
            if (datasize < 256 || i < 6 || i >= datasize - 6) {
                log.append(data[i]);
            }
            if (datasize > 256 && i == 6) {
                log.append(" ...");
            }
            i++;
        }
        Log.d(TAG, "SendSensorHubData: " + log.toString());
        int res = sensorhubs_send_data(sQueue, 0, datasize, data);
        if (res < 0) {
            Log.e(TAG, "SendSensorHubData: error(" + res + ")");
        }
        return res;
    }
}
