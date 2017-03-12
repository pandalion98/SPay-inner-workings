package com.samsung.android.smartface;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import com.samsung.android.smartface.ISmartFaceClient.Stub;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SmartFaceManager {
    public static final String FALSE = "false";
    public static final String FEATURE_SMART_PAUSE = "com.sec.android.smartface.smart_pause";
    public static final String FEATURE_SMART_ROTATION = "com.sec.android.smartface.smart_rotation";
    public static final String FEATURE_SMART_SCROLL = "com.sec.android.smartface.smart_scroll";
    public static final String FEATURE_SMART_STAY = "com.sec.android.smartface.smart_stay";
    public static final int MSG_FACEINFO = 0;
    public static final int MSG_REGISTERED = 1;
    public static final int MSG_UNREGISTERED = 2;
    private static final String NULL_VALUE = "";
    public static final String PAGE_BOTTOM = "1";
    public static final String PAGE_MIDDLE = "0";
    public static final String PAGE_TOP = "-1";
    public static final String PAUSE_THIS_CLIENT = "paused-state";
    public static final int SERVICETYPE_HYBRID = 32;
    public static final int SERVICETYPE_MOTION = 16;
    public static final int SERVICETYPE_PAUSE = 2;
    public static final int SERVICETYPE_ROTATION = 8;
    public static final int SERVICETYPE_SCROLL = 1;
    public static final int SERVICETYPE_STAY = 4;
    public static final String SMARTFACE_SERVICE = "samsung.smartfaceservice";
    public static final String SMART_ROTATION_UI_ORIENTATION = "smart-rotation-ui-orientation";
    public static final String SMART_SCREEN_DUMP_PREVIEW = "smart-screen-dump";
    public static final String SMART_SCROLL_PAGE_STATUS = "smart-scroll-page-status";
    public static final String SMART_STAY_FRAMECOUNT_RESET = "smart-stay-framecount-reset";
    private static final String TAG = "SmartFaceManager";
    public static final String TRUE = "true";
    private final Condition complete = this.lock.newCondition();
    private final Lock lock = new ReentrantLock();
    private int mCallbackData;
    private SmartFaceClient mClient = null;
    private Context mContext = null;
    private EventHandler mEventHandler = null;
    private EventHandler mInternalEventHandler = null;
    private SmartFaceInfoListener mListener = null;
    private ISmartFaceService mService = null;
    private int mSmartStayDelay = Integer.parseInt("2950");

    public interface SmartFaceInfoListener {
        void onInfo(FaceInfo faceInfo, int i);
    }

    private class EventHandler extends Handler {
        private SmartFaceManager mManager = null;

        public EventHandler(SmartFaceManager manager, Looper looper) {
            super(looper);
            this.mManager = manager;
        }

        public void handleMessage(Message msg) {
            if (SmartFaceManager.this.mListener != null) {
                switch (msg.what) {
                    case 0:
                        SmartFaceManager.this.mListener.onInfo((FaceInfo) msg.obj, msg.arg1);
                        return;
                    case 1:
                        if (SmartFaceManager.this.mListener instanceof SmartFaceInfoListener2) {
                            ((SmartFaceInfoListener2) SmartFaceManager.this.mListener).onRegistered(this.mManager, msg.arg1);
                            return;
                        } else {
                            Log.e(SmartFaceManager.TAG, "Listener does not implements SmartFaceInfoListener2");
                            return;
                        }
                    case 2:
                        if (SmartFaceManager.this.mListener instanceof SmartFaceInfoListener2) {
                            ((SmartFaceInfoListener2) SmartFaceManager.this.mListener).onUnregistered(this.mManager, msg.arg1);
                            return;
                        } else {
                            Log.e(SmartFaceManager.TAG, "Listener does not implements SmartFaceInfoListener2");
                            return;
                        }
                    default:
                        return;
                }
            }
            Log.e(SmartFaceManager.TAG, "Listener is null");
        }
    }

    private class SmartFaceClient extends Stub {
        SmartFaceClient() {
            Log.e(SmartFaceManager.TAG, "New SmartFaceClient");
        }

        public void onInfo(int msg_type, FaceInfo data, int service_type) {
            if (SmartFaceManager.this.mInternalEventHandler != null) {
                SmartFaceManager.this.mInternalEventHandler.sendMessage(SmartFaceManager.this.mInternalEventHandler.obtainMessage(msg_type, service_type, 0, data));
            } else if (SmartFaceManager.this.mEventHandler != null) {
                SmartFaceManager.this.mEventHandler.sendMessage(SmartFaceManager.this.mEventHandler.obtainMessage(msg_type, service_type, 0, data));
            } else {
                Log.e(SmartFaceManager.TAG, "EventHandler is null");
            }
        }
    }

    public interface SmartFaceInfoListener2 extends SmartFaceInfoListener {
        void onRegistered(SmartFaceManager smartFaceManager, int i);

        void onUnregistered(SmartFaceManager smartFaceManager, int i);
    }

    public static SmartFaceManager getSmartFaceManager() {
        return null;
    }

    public static SmartFaceManager getSmartFaceManager(Context context) {
        return new SmartFaceManager(context);
    }

    private synchronized boolean ensureServiceConnected() {
        boolean z;
        if (this.mService != null) {
            try {
                this.mService.setValue(this.mClient, "empty key for ping", "empty value");
            } catch (RemoteException e) {
                if (e instanceof DeadObjectException) {
                    this.mService = null;
                }
            }
        }
        if (this.mService == null) {
            startSmartFaceService();
            waitForService();
        }
        if (this.mService != null) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    private void waitForService() {
        for (int count = 1; count <= 3; count++) {
            this.mService = ISmartFaceService.Stub.asInterface(ServiceManager.getService(SMARTFACE_SERVICE));
            if (this.mService != null) {
                Log.v(TAG, "Service connected!");
                return;
            }
            try {
                Thread.sleep(300);
                Log.e(TAG, "Wait for " + (count * 300) + "ms...");
            } catch (InterruptedException e) {
            }
        }
    }

    private void startSmartFaceService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.samsung.android.smartface", "com.samsung.android.smartface.SmartFaceServiceStarter"));
        this.mContext.startServiceAsUser(intent, UserHandle.CURRENT_OR_SELF);
    }

    private SmartFaceManager(Context context) {
        this.mContext = context;
        this.mClient = new SmartFaceClient();
        Looper looper = Looper.myLooper();
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
            return;
        }
        looper = Looper.getMainLooper();
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            this.mEventHandler = null;
        }
    }

    public boolean start(int service_type) {
        if (!ensureServiceConnected()) {
            return false;
        }
        boolean ret = false;
        try {
            return this.mService.register(this.mClient, service_type);
        } catch (RemoteException e) {
            e.printStackTrace();
            return ret;
        }
    }

    public void startAsync(int service_type) {
        if (ensureServiceConnected()) {
            try {
                this.mService.registerAsync(this.mClient, service_type);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        if (ensureServiceConnected()) {
            try {
                this.mService.unregister(this.mClient);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (this.mEventHandler != null) {
                this.mEventHandler.removeCallbacksAndMessages(null);
            }
            if (this.mInternalEventHandler != null) {
                this.mInternalEventHandler.removeCallbacksAndMessages(null);
            }
        }
    }

    public void stopAsync() {
        if (ensureServiceConnected()) {
            try {
                this.mService.unregisterAsync(this.mClient);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (this.mEventHandler != null) {
                this.mEventHandler.removeCallbacksAndMessages(null);
            }
            if (this.mInternalEventHandler != null) {
                this.mInternalEventHandler.removeCallbacksAndMessages(null);
            }
        }
    }

    public void pause() {
        setValue(PAUSE_THIS_CLIENT, TRUE);
        if (this.mEventHandler != null) {
            this.mEventHandler.removeCallbacksAndMessages(null);
        }
        if (this.mInternalEventHandler != null) {
            this.mInternalEventHandler.removeCallbacksAndMessages(null);
        }
    }

    public void resume() {
        setValue(PAUSE_THIS_CLIENT, FALSE);
    }

    public void setValue(String key, int value) {
        setValue(key, Integer.toString(value));
    }

    public void setValue(String key, String value) {
        if (ensureServiceConnected()) {
            Log.d(TAG, "Sending " + key + ":" + value + " to service");
            try {
                this.mService.setValue(this.mClient, key, value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean checkForSmartStay() {
        boolean ret;
        Log.e(TAG, "checkForSmartStay S");
        ret = false;
        HandlerThread thread1 = new HandlerThread("Smart Stay Wait Thread");
        thread1.start();
        this.mInternalEventHandler = new EventHandler(this, thread1.getLooper());
        SmartFaceInfoListener listener = this.mListener;
        setListener(new SmartFaceInfoListener() {
            public void onInfo(FaceInfo data, int service_type) {
                Log.e(SmartFaceManager.TAG, "checkForSmartStay onInfo: " + Integer.toBinaryString(service_type) + ": " + data.needToStay);
                if ((service_type & 4) != 0) {
                    SmartFaceManager.this.lock.lock();
                    SmartFaceManager.this.mCallbackData = data.needToStay;
                    SmartFaceManager.this.complete.signal();
                    SmartFaceManager.this.lock.unlock();
                }
            }
        });
        this.lock.lock();
        try {
            setValue(SMART_STAY_FRAMECOUNT_RESET, "");
            if (start(4)) {
                this.mCallbackData = -1;
                waitForCallback((int) (0.43f * ((float) this.mSmartStayDelay)));
                if (this.mCallbackData > 0) {
                    ret = true;
                }
                this.mCallbackData = -1;
                waitForCallback((int) (0.37f * ((float) this.mSmartStayDelay)));
                if (this.mCallbackData > 0) {
                    ret = true;
                }
            }
            stop();
            thread1.quit();
            this.mInternalEventHandler = null;
            setListener(listener);
            Log.e(TAG, "checkForSmartStay X: " + ret);
        } finally {
            this.lock.unlock();
        }
        return ret;
    }

    public synchronized boolean checkForSmartRotation(int orientation) {
        boolean ret;
        Log.e(TAG, "checkForSmartRotation S: " + orientation);
        ret = false;
        HandlerThread thread1 = new HandlerThread("Smart Rotation Wait Thread");
        thread1.start();
        this.mInternalEventHandler = new EventHandler(this, thread1.getLooper());
        SmartFaceInfoListener listener = this.mListener;
        setListener(new SmartFaceInfoListener() {
            public void onInfo(FaceInfo data, int service_type) {
                Log.e(SmartFaceManager.TAG, "checkForSmartRotation onInfo: " + Integer.toBinaryString(service_type) + ": " + data.needToRotate);
                if ((service_type & 8) != 0) {
                    SmartFaceManager.this.lock.lock();
                    SmartFaceManager.this.mCallbackData = data.needToRotate;
                    SmartFaceManager.this.complete.signal();
                    SmartFaceManager.this.lock.unlock();
                }
            }
        });
        this.lock.lock();
        try {
            setValue(SMART_ROTATION_UI_ORIENTATION, orientation);
            if (start(8)) {
                this.mCallbackData = -1;
                waitForCallback(500);
                if (this.mCallbackData > 0) {
                    ret = true;
                }
                this.mCallbackData = -1;
                waitForCallback(500);
                if (this.mCallbackData > 0) {
                    ret = true;
                }
            }
            stop();
            thread1.quit();
            this.mInternalEventHandler = null;
            setListener(listener);
            Log.e(TAG, "checkForSmartRotation E: " + ret);
        } finally {
            this.lock.unlock();
        }
        return ret;
    }

    public int getSupportedServices() {
        if (!ensureServiceConnected()) {
            return 0;
        }
        try {
            return this.mService.getSupportedServices();
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setListener(SmartFaceInfoListener listener) {
        this.mListener = listener;
    }

    private long waitForCallback(int wait_milli) {
        long ret = -1;
        try {
            ret = this.complete.awaitNanos(((long) (wait_milli * 1000)) * 1000);
            if (ret <= 0) {
                Log.e(TAG, "No Callback!");
            }
        } catch (Exception e) {
        }
        return ret;
    }
}
