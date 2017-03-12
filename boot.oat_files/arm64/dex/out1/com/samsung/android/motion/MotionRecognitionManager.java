package com.samsung.android.motion;

import android.app.ActivityThread;
import android.hardware.scontext.ISContextService;
import android.hardware.scontext.ISContextService.Stub;
import android.hardware.scontext.SContext;
import android.hardware.scontext.SContextEvent;
import android.hardware.scontext.SContextListener;
import android.hardware.scontext.SContextManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.Log;
import java.util.ArrayList;

public class MotionRecognitionManager {
    public static final String ACTION_MOTION_RECOGNITION_EVENT = "com.samsung.android.motion.MOTION_RECOGNITION_EVENT";
    private static final int DEBUG_LEVEL_HIGH = 18760;
    private static final int DEBUG_LEVEL_LOW = 20300;
    private static final int DEBUG_LEVEL_MID = 18765;
    public static final int EVENT_ALL = 2097151;
    public static final int EVENT_CALL_POSE = 262144;
    public static final int EVENT_DIRECT_CALLING = 1024;
    public static final int EVENT_DOUBLE_TAP = 8;
    public static final int EVENT_FLAT = 8192;
    public static final int EVENT_LCD_UP_STEADY = 65536;
    public static final int EVENT_LOCK_EXECUTE_L = 128;
    public static final int EVENT_LOCK_EXECUTE_R = 256;
    public static final int EVENT_NUM = 21;
    public static final int EVENT_OVER_TURN = 1;
    public static final int EVENT_OVER_TURN_LOW_POWER = 131072;
    public static final int EVENT_PALM_SWIPE = 4194304;
    public static final int EVENT_PALM_TOUCH = 2097152;
    public static final int EVENT_PANNING_GALLERY = 32;
    public static final int EVENT_PANNING_HOME = 64;
    public static final int EVENT_REACTIVE_ALERT = 4;
    public static final int EVENT_SHAKE = 2;
    public static final int EVENT_SMART_ALERT_SETTING = 32768;
    public static final int EVENT_SMART_RELAY = 1048576;
    public static final int EVENT_SMART_SCROLL = 524288;
    public static final int EVENT_TILT = 16;
    public static final int EVENT_TILT_LEVEL_ZERO = 4096;
    public static final int EVENT_TILT_LEVEL_ZERO_LAND = 16384;
    public static final int EVENT_TILT_TO_UNLOCK = 2048;
    public static final int EVENT_VOLUME_DOWN = 512;
    public static final int MOTION_SENSOR_NUM = 4;
    public static final int MOTION_USE_ACC = 1;
    public static final int MOTION_USE_ALL = 15;
    public static final int MOTION_USE_ALWAYS = 1073741824;
    public static final int MOTION_USE_GYRO = 2;
    public static final int MOTION_USE_LIGHT = 8;
    public static final int MOTION_USE_PROX = 4;
    protected static final String TAG = "MotionRecognitionManager";
    private static final boolean localLOGV = false;
    private static final int mMotionVersion = 1;
    private Looper mMainLooper;
    private int mMovementCnt;
    private SContextManager mSContextManager;
    private ISContextService mSContextService = Stub.asInterface(ServiceManager.getService("scontext"));
    private boolean mSSPEnabled;
    private IMotionRecognitionService motionService;
    private final SContextListener mySContextMotionListener = new SContextListener() {
        public void onSContextChanged(SContextEvent event) {
            SContext scontext = event.scontext;
            MREvent mrevent = new MREvent();
            boolean isEnabledPickUp = false;
            switch (scontext.getType()) {
                case 5:
                    if (event.getMovementContext().getAction() == 1) {
                        try {
                            isEnabledPickUp = MotionRecognitionManager.this.motionService.getPickUpMotionStatus();
                            Log.d(MotionRecognitionManager.TAG, "  >> check setting smart alert enabled : " + isEnabledPickUp);
                        } catch (RemoteException e) {
                            Log.e(MotionRecognitionManager.TAG, "RemoteException in getPickUpMotionStatus: ", e);
                        }
                        if (isEnabledPickUp) {
                            mrevent.setMotion(67);
                            Log.e(MotionRecognitionManager.TAG, "mySContextMotionListener : Send Smart alert event");
                            synchronized (MotionRecognitionManager.this.sListenerDelegates) {
                                int size = MotionRecognitionManager.this.sListenerDelegates.size();
                                for (int i = 0; i < size; i++) {
                                    ((MRListenerDelegate) MotionRecognitionManager.this.sListenerDelegates.get(i)).motionCallback(mrevent);
                                }
                            }
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private final ArrayList<MRListenerDelegate> sListenerDelegates = new ArrayList();
    private final ArrayList<String> sListenerwithSSP = new ArrayList();

    private class MRListenerDelegate extends IMotionRecognitionCallback.Stub {
        private final int EVENT_FROM_SERVICE = 53;
        private final Handler mHandler;
        private final MRListener mListener;
        private String mListenerPackageName = null;
        private int mMotionEvents;

        MRListenerDelegate(MRListener listener, int motion_sensors, Handler handler) {
            this.mListener = listener;
            Looper looper = handler != null ? handler.getLooper() : MotionRecognitionManager.this.mMainLooper;
            this.mMotionEvents = motion_sensors;
            this.mListenerPackageName = ActivityThread.currentPackageName();
            this.mHandler = new Handler(looper, MotionRecognitionManager.this) {
                public void handleMessage(Message msg) {
                    try {
                        if (MRListenerDelegate.this.mListener != null && msg != null && msg.what == 53) {
                            MRListenerDelegate.this.mListener.onMotionListener(msg.obj);
                        }
                    } catch (ClassCastException e) {
                        Log.e(MotionRecognitionManager.TAG, "ClassCastException in handleMessage: msg.obj = " + msg.obj, e);
                    }
                }
            };
        }

        public MRListener getListener() {
            return this.mListener;
        }

        public int getMotionEvents() {
            return this.mMotionEvents;
        }

        public void setMotionEvents(int motionevents) {
            this.mMotionEvents = motionevents;
        }

        public void motionCallback(MREvent motionEvent) {
            Message msg = Message.obtain();
            msg.what = 53;
            msg.obj = motionEvent;
            this.mHandler.sendMessage(msg);
        }

        public String getListenerInfo() {
            return this.mListener.toString();
        }

        public String getListenerPackageName() {
            return this.mListenerPackageName;
        }
    }

    public MotionRecognitionManager(Looper mainLooper) {
        int i = 0;
        Log.e(TAG, "mSContextService = " + this.mSContextService);
        this.mMainLooper = mainLooper;
        this.mSContextManager = new SContextManager(this.mMainLooper);
        this.mMovementCnt = 0;
        if (this.mSContextService != null) {
            try {
                this.motionService = IMotionRecognitionService.Stub.asInterface(this.mSContextService.getMotionRecognitionService());
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in motionService: ", e);
            } finally {
                String str = TAG;
                StringBuilder append = new StringBuilder().append("motionService = ");
                i = this.motionService;
                Log.e(str, append.append(i).toString());
            }
        } else {
            this.motionService = IMotionRecognitionService.Stub.asInterface(ServiceManager.getService("motion_recognition"));
        }
        Log.e(TAG, "motionService = " + this.motionService);
        this.mMovementCnt = i;
        try {
            if (this.motionService != null) {
                this.mSSPEnabled = this.motionService.getSSPstatus();
            }
        } catch (RemoteException e2) {
            Log.e(TAG, "RemoteException in getSSPstatus: ", e2);
        }
    }

    @Deprecated
    public void registerListener(MRListener listener) {
        registerListener(listener, 0, null);
    }

    @Deprecated
    public void registerListener(MRListener listener, Handler handler) {
        registerListener(listener, 0, handler);
    }

    @Deprecated
    public void registerListener(MRListener listener, int motion_sensors) {
        registerListener(listener, motion_sensors, null);
    }

    @Deprecated
    public void registerListener(MRListener listener, int motion_sensors, Handler handler) {
        int motion_events = 0;
        if ((motion_sensors & 1) != 0) {
            motion_events = 0 | 237570;
        }
        if ((motion_sensors & 2) != 0) {
            motion_events |= 20985;
        }
        if ((motion_sensors & 4) != 0) {
            motion_events |= 3072;
        }
        registerListenerEvent(listener, motion_events, handler);
    }

    public void registerListenerEvent(MRListener listener, int motion_events) {
        registerListenerEvent(listener, motion_events, null);
    }

    public void registerListenerEvent(MRListener listener, int motion_sensors, int motion_events, Handler handler) {
        Throwable th;
        if (listener != null && this.motionService != null) {
            synchronized (this.sListenerDelegates) {
                MRListenerDelegate mrlistener = null;
                int size = this.sListenerDelegates.size();
                boolean bregisterd = false;
                for (int i = 0; i < size; i++) {
                    MRListenerDelegate l = (MRListenerDelegate) this.sListenerDelegates.get(i);
                    if (l.getListener() == listener) {
                        String strlistener = EncodeLog("name :" + listener);
                        if ((l.getMotionEvents() & motion_events) != 0) {
                            Log.d(TAG, "  .registerListenerEvent : fail. already registered / listener count = " + this.sListenerDelegates.size() + ", " + strlistener);
                            bregisterd = true;
                        } else {
                            mrlistener = l;
                            Log.d(TAG, "  .registerListenerEvent : already registered but need to update motion events / listener count = " + this.sListenerDelegates.size() + ", " + strlistener);
                        }
                    }
                }
                if (bregisterd) {
                    return;
                }
                if (mrlistener != null) {
                    motion_events |= mrlistener.getMotionEvents();
                    mrlistener = null;
                    unregisterListener(listener);
                }
                MRListenerDelegate mrlistener2 = mrlistener;
                if (mrlistener2 == null) {
                    try {
                        mrlistener = new MRListenerDelegate(listener, motion_events, handler);
                    } catch (Throwable th2) {
                        th = th2;
                        mrlistener = mrlistener2;
                        throw th;
                    }
                }
                mrlistener = mrlistener2;
                try {
                    this.sListenerDelegates.add(mrlistener);
                    if ((motion_events & 4) != 0) {
                        if (this.mSSPEnabled) {
                            if (this.mySContextMotionListener == null || this.mMovementCnt != 0) {
                                Log.e(TAG, " [MOVEMENT_SERVICE] registerListener : fail. already registered ");
                            } else {
                                Log.d(TAG, " [MOVEMENT_SERVICE] registerListener ");
                                this.mSContextManager.registerListener(this.mySContextMotionListener, 5);
                            }
                            this.mMovementCnt++;
                            motion_events &= -5;
                        } else {
                            try {
                                this.mSSPEnabled = this.motionService.getSSPstatus();
                            } catch (RemoteException e) {
                                Log.e(TAG, "RemoteException in getSSPstatus: ", e);
                            }
                            Log.d(TAG, "SSP disabled : " + this.mSSPEnabled);
                        }
                    }
                    if (motion_events != 0) {
                        this.motionService.registerCallback(mrlistener, motion_sensors, motion_events);
                    }
                } catch (RemoteException e2) {
                    Log.e(TAG, "RemoteException in registerListenerEvent : ", e2);
                } catch (Throwable th3) {
                    th = th3;
                    throw th;
                }
                Log.v(TAG, "  .registerListenerEvent : success. listener count = " + size + "->" + this.sListenerDelegates.size() + ", motion_events=" + motion_events + ", " + EncodeLog("name :" + listener));
            }
        }
    }

    public void registerListenerEvent(MRListener listener, int motion_events, Handler handler) {
        registerListenerEvent(listener, 0, motion_events, handler);
    }

    public void unregisterListener(MRListener listener, int motion_events) {
        if (this.motionService != null) {
            synchronized (this.sListenerDelegates) {
                int size = this.sListenerDelegates.size();
                int motionevents = 0;
                for (int i = 0; i < size; i++) {
                    MRListenerDelegate l = (MRListenerDelegate) this.sListenerDelegates.get(i);
                    if (l.getListener() == listener) {
                        motionevents = l.getMotionEvents() & (motion_events ^ -1);
                        Log.d(TAG, "update listener " + i + " = " + EncodeLog("name :" + listener) + ",  motionevents = " + motionevents);
                        break;
                    }
                }
                unregisterListener(listener);
                if (motionevents != 0) {
                    registerListenerEvent(listener, motionevents);
                }
            }
        }
    }

    public void unregisterListener(MRListener listener) {
        if (this.motionService != null) {
            synchronized (this.sListenerDelegates) {
                int i;
                int size = this.sListenerDelegates.size();
                for (i = 0; i < size; i++) {
                    Log.d(TAG, "@ member " + i + " = " + EncodeLog(((MRListenerDelegate) this.sListenerDelegates.get(i)).getListener().toString()));
                }
                i = 0;
                while (i < size) {
                    MRListenerDelegate l = (MRListenerDelegate) this.sListenerDelegates.get(i);
                    if (l.getListener() == listener) {
                        this.sListenerDelegates.remove(i);
                        boolean bdisable = false;
                        try {
                            if ((l.getMotionEvents() & 4) != 0) {
                                if (this.mSSPEnabled) {
                                    this.mMovementCnt--;
                                    if (this.mMovementCnt <= 0) {
                                        Log.d(TAG, " [MOVEMENT_SERVICE] unregisterListener ");
                                        this.mMovementCnt = 0;
                                        this.mSContextManager.unregisterListener(this.mySContextMotionListener, 5);
                                    }
                                    Log.d(TAG, "unregisterListener - mMovementCnt : " + this.mMovementCnt);
                                } else {
                                    try {
                                        this.mSSPEnabled = this.motionService.getSSPstatus();
                                    } catch (RemoteException e) {
                                        Log.e(TAG, "RemoteException in getSSPstatus: ", e);
                                    }
                                    Log.d(TAG, "SSP disabled : " + this.mSSPEnabled);
                                    this.motionService.unregisterCallback(l);
                                    bdisable = true;
                                }
                            }
                            if (!((l.getMotionEvents() & -5) == 0 || bdisable)) {
                                this.motionService.unregisterCallback(l);
                            }
                        } catch (RemoteException e2) {
                            Log.e(TAG, "RemoteException in unregisterListener: ", e2);
                        }
                    } else {
                        i++;
                    }
                }
                Log.i(TAG, "  .unregisterListener : / listener count = " + size + "->" + this.sListenerDelegates.size() + ", " + EncodeLog("name :" + listener));
            }
        }
    }

    @Deprecated
    public void useMotionAlways(MRListener listener, boolean bUseAlways) {
    }

    public void setMotionAngle(MRListener listener, int status) {
    }

    public void setSmartMotionAngle(MRListener listener, int status) {
        if (this.motionService != null) {
            synchronized (this.sListenerDelegates) {
                int size = this.sListenerDelegates.size();
                for (int i = 0; i < size; i++) {
                    MRListenerDelegate l = (MRListenerDelegate) this.sListenerDelegates.get(i);
                    if (l.getListener() == listener) {
                        try {
                            this.motionService.setMotionAngle(l, status);
                        } catch (RemoteException e) {
                            Log.e(TAG, "RemoteException in setSmartMotionAngle: ", e);
                        }
                        return;
                    }
                }
                Log.d(TAG, "  .setSmartMotionAngle : listener has to be registered first");
            }
        }
    }

    @Deprecated
    public void setMotionTiltLevel(int stopUp, int level1Up, int level2Up, int stopDown, int level1Down, int level2Down) {
        if (this.motionService != null) {
            try {
                this.motionService.setMotionTiltLevel(stopUp, level1Up, level2Up, stopDown, level1Down, level2Down);
            } catch (RemoteException e) {
                Log.e(TAG, "RemoteException in setMotionTiltLevel: ", e);
            }
            Log.d(TAG, "  .setMotionTiltLevel : 1");
        }
    }

    public int resetMotionEngine() {
        if (this.motionService == null) {
            return -1;
        }
        try {
            return this.motionService.resetMotionEngine();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in resetMotionEngine: ", e);
            return 0;
        }
    }

    @Deprecated
    public static boolean isValidMotionSensor(int motion_sensor) {
        return motion_sensor == 1 || motion_sensor == 2 || motion_sensor == 4 || motion_sensor == 8;
    }

    public static int getMotionVersion() {
        return 1;
    }

    private static String EncodeLog(String path) {
        String state = SystemProperties.get("ro.debug_level", "Unknown");
        if (state.equals("Unknown")) {
            return " ";
        }
        try {
            int debugLevel = Integer.parseInt(state.substring(2), 16);
            if (debugLevel == DEBUG_LEVEL_LOW) {
                return " ";
            }
            return (debugLevel == DEBUG_LEVEL_MID || debugLevel == DEBUG_LEVEL_HIGH) ? path : " ";
        } catch (NumberFormatException e) {
            return " ";
        }
    }

    public boolean isAvailable(int type) {
        boolean ret = false;
        if (this.motionService == null) {
            return ret;
        }
        switch (type) {
            case 1:
            case 4:
            case 1024:
            case 2097152:
            case 4194304:
                try {
                    ret = this.motionService.isAvailable(type);
                    break;
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException in getSSPstatus: ", e);
                    break;
                }
            default:
                ret = false;
                break;
        }
        return ret;
    }

    public boolean setTestSensor() {
        try {
            return this.motionService.setTestSensor();
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in setTestSensor");
            return false;
        }
    }
}
