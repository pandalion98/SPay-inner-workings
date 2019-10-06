package com.synaptics.bpd.fingerprint;

import android.content.Context;
import android.util.Log;

public class FingerprintCore extends VcsEvents {
    public static final String API_VERSION = "2.0";
    protected static final boolean DBG = false;
    protected static final int ENROLL = 151;
    protected static final int GETPRINT = 156;
    protected static final int IDENTIFY = 152;
    protected static final int IDLE = 150;
    protected static final String TAG = "Fingerprint";
    protected EventListener mAlipayEventListener = null;
    protected Context mContext = null;
    private EventListener mEventListener = null;
    protected int mOperation = IDLE;
    protected EventListener mQcafEventListener = null;

    public interface EventListener {
        void onEvent(FingerprintEvent fingerprintEvent);
    }

    private native int jniCancelOp();

    private native int jniCleanupVcs();

    private native int jniGetEnrolledFingerList(String str, VcsInt vcsInt);

    private native int jniGetFingerprintImage();

    private native Object[] jniGetUserList(VcsInt vcsInt);

    private native String jniGetVersion();

    private native int jniIdentify(String str);

    private native int jniIdentifyEx(String str, Object obj);

    private native int jniInitVcs(FingerprintCore fingerprintCore);

    public FingerprintCore(Context ctx) {
        this.mContext = ctx;
        int result = jniInitVcs(this);
        if (result != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Initialization failed, result:");
            sb.append(result);
            Log.e(str, sb.toString());
        }
    }

    public FingerprintCore(Context ctx, EventListener listener) {
        this.mContext = ctx;
        int result = jniInitVcs(this);
        if (result != 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Initialization failed, result:");
            sb.append(result);
            Log.e(str, sb.toString());
        }
        this.mEventListener = listener;
    }

    public String getVersion() {
        if (this.mOperation == IDLE) {
            return jniGetVersion();
        }
        Log.e(TAG, "Other operation is in progress, cancelling request");
        return null;
    }

    public int registerListener(EventListener listener) {
        if (this.mOperation != IDLE) {
            return 102;
        }
        this.mEventListener = listener;
        return 0;
    }

    public int identify(String userId) {
        if (this.mOperation != IDLE) {
            return 102;
        }
        if (userId == null) {
            userId = "";
        }
        int ret = jniIdentify(userId);
        if (ret == 0) {
            this.mOperation = IDENTIFY;
        }
        return ret;
    }

    public int identify(String userId, Object additionalData) {
        if (this.mOperation != IDLE) {
            return 102;
        }
        if (userId == null) {
            userId = "";
        }
        int ret = jniIdentifyEx(userId, additionalData);
        if (ret == 0) {
            this.mOperation = IDENTIFY;
        }
        return ret;
    }

    public int getFingerprintImage() {
        if (this.mOperation != IDLE) {
            return 102;
        }
        int ret = jniGetFingerprintImage();
        if (ret == 0) {
            this.mOperation = GETPRINT;
        }
        return ret;
    }

    public int getUserList(VcsStringArray userList) {
        VcsInt retCode = new VcsInt();
        Object[] objects = jniGetUserList(retCode);
        int ret = retCode.num;
        if (ret != 0) {
            return ret;
        }
        if (userList == null) {
            userList = new VcsStringArray();
        }
        if (objects != null && (objects instanceof String[])) {
            userList.strlist = (String[]) objects;
        }
        if (userList.strlist == null) {
            return retCode.num;
        }
        return 0;
    }

    public int getUserList(VcsObjectArray userList) {
        VcsInt retCode = new VcsInt();
        Object[] objects = jniGetUserList(retCode);
        int ret = retCode.num;
        if (ret != 0) {
            return ret;
        }
        if (objects == null || objects.length == 0) {
            return 118;
        }
        if (userList == null) {
            userList = new VcsObjectArray();
        }
        for (Object object : objects) {
            if (object == null || !(object instanceof byte[])) {
                Log.w(TAG, "not byte array");
            } else {
                userList.list.add((byte[]) object);
            }
        }
        if (userList == null || userList.list == null) {
            return retCode.num;
        }
        return 0;
    }

    public int getEnrolledFingerList(String userId, VcsInt fingermask) {
        if (userId == null) {
            userId = "";
        }
        return jniGetEnrolledFingerList(userId, fingermask);
    }

    public int cancel() {
        int ret = jniCancelOp();
        this.mOperation = IDLE;
        return ret;
    }

    public int cleanUp() {
        this.mOperation = IDLE;
        this.mEventListener = null;
        this.mAlipayEventListener = null;
        this.mQcafEventListener = null;
        return jniCleanupVcs();
    }

    public synchronized void FingerprintEventCallback(FingerprintEvent event) {
        if (event == null) {
            Log.e(TAG, "FP - EventsCB()::Invalid event data!");
            return;
        }
        if (isOperationComplete(event.eventId)) {
            Log.i(TAG, "Operation complete, setting to IDLE");
            this.mOperation = IDLE;
        }
        if (this.mOperation == GETPRINT && event.eventId == 401) {
            this.mOperation = IDLE;
        }
        if (this.mQcafEventListener != null) {
            this.mQcafEventListener.onEvent(event);
        }
        if (this.mAlipayEventListener != null) {
            this.mAlipayEventListener.onEvent(event);
        }
        if (this.mEventListener != null) {
            this.mEventListener.onEvent(event);
        }
        doHandleIfNavigationEvent(event);
    }

    private boolean isOperationComplete(int eventId) {
        return eventId == 402 || eventId == 403 || eventId == 404;
    }

    private void doHandleIfNavigationEvent(FingerprintEvent fpEvent) {
        NavigationEvent[] navigationEventArr;
        if (fpEvent != null && fpEvent.eventId == 410) {
            Log.i(TAG, "Navigation event recd!");
            if (fpEvent.eventData == null || !(fpEvent.eventData instanceof NavigationReport)) {
                Log.e(TAG, "Invalid navigation report!");
                return;
            }
            int navGesture = 0;
            NavigationReport navReport = (NavigationReport) fpEvent.eventData;
            Log.i(TAG, String.format("Navigation Report: Device[%d], Target[%d], Event#[%d]", new Object[]{Integer.valueOf(navReport.device), Integer.valueOf(navReport.target), Integer.valueOf(navReport.events)}));
            for (NavigationEvent navEvent : navReport.navEvents) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Navigation Event: eventMaskMotionCurrent[");
                sb.append(navEvent.eventMaskMotionCurrent);
                sb.append("], eventMaskGesturesCurrent[");
                sb.append(navEvent.eventMaskGesturesCurrent);
                sb.append("], eventMaskMotionPrev[");
                sb.append(navEvent.eventMaskMotionPrev);
                sb.append("], eventMaskGesturesPrev[");
                sb.append(navEvent.eventMaskGesturesPrev);
                sb.append("], eventTime[");
                sb.append(navEvent.eventTime);
                sb.append("], deltaX[");
                sb.append(navEvent.deltaX);
                sb.append("], deltaY[");
                sb.append(navEvent.deltaY);
                sb.append("], fingerOnSensor[");
                sb.append(navEvent.fingerOnSensor);
                sb.append("], eventTimeOutOfQueue[");
                sb.append(navEvent.eventTimeOutOfQueue);
                sb.append("]");
                Log.i(str, sb.toString());
                if ((navEvent.eventMaskGesturesCurrent & 4) == 4) {
                    navGesture |= 4;
                } else if ((navEvent.eventMaskGesturesCurrent & 8) == 8) {
                    navGesture |= 8;
                } else if ((navEvent.eventMaskGesturesCurrent & 1) == 1) {
                    navGesture |= 1;
                } else if ((navEvent.eventMaskGesturesCurrent & 2) == 2) {
                    navGesture |= 2;
                } else if ((navEvent.eventMaskGesturesCurrent & 32768) == 32768) {
                    navGesture |= 32768;
                } else if ((navEvent.eventMaskGesturesCurrent & 16384) == 16384) {
                    navGesture |= 16384;
                } else if ((navEvent.eventMaskGesturesCurrent & 16) == 16) {
                    navGesture |= 16;
                }
                FingerprintEvent fpEvt = new FingerprintEvent();
                fpEvt.eventId = 501;
                fpEvt.eventData = Integer.valueOf(navGesture);
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Navigation Event: ");
                sb2.append(Integer.toHexString(navGesture));
                Log.i(str2, sb2.toString());
                if (this.mEventListener != null) {
                    this.mEventListener.onEvent(fpEvt);
                }
            }
        }
    }

    static {
        try {
            System.loadLibrary("vcsfp");
        } catch (Throwable e) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Error loading library libvcsfp: ");
            sb.append(e);
            Log.e(str, sb.toString());
        }
    }
}
