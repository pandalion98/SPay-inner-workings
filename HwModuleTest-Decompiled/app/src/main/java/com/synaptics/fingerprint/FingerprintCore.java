package com.synaptics.fingerprint;

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
    private EventListener mEventListener = null;
    protected int mOperation = IDLE;

    public interface EventListener {
        void onEvent(FingerprintEvent fingerprintEvent);
    }

    private native int jniCancelOp();

    private native int jniCleanupVcs();

    private native int jniGetEnrolledFingerList(String str, VcsInt vcsInt);

    private native int jniGetFingerprintImage();

    private native String[] jniGetUserList(VcsInt vcsInt);

    private native String jniGetVersion();

    private native int jniIdentify(String str);

    private native int jniIdentifyEx(String str, int i);

    private native int jniInitVcs(FingerprintCore fingerprintCore);

    private native int jniSetSecurityLevel(int i);

    public FingerprintCore(Context ctx) {
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

    public int setSecurityLevel(int level) {
        if (this.mOperation != IDLE) {
            return 102;
        }
        if (level == 0 || level == 1 || level == 2 || level == 3) {
            return jniSetSecurityLevel(level);
        }
        return 111;
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

    public int identify(String userId, int priority) {
        if (priority != 2 && priority != 1) {
            return 111;
        }
        if (this.mOperation != IDLE) {
            return 102;
        }
        if (userId == null) {
            userId = "";
        }
        int ret = jniIdentifyEx(userId, priority);
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
        userList.strlist = jniGetUserList(retCode);
        if (userList.strlist == null) {
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
        return jniCleanupVcs();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0039, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void FingerprintEventCallback(com.synaptics.fingerprint.FingerprintEvent r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            if (r4 != 0) goto L_0x000e
            java.lang.String r0 = "Fingerprint"
            java.lang.String r1 = "FP - EventsCB()::Invalid event data!"
            android.util.Log.e(r0, r1)     // Catch:{ all -> 0x000c }
            monitor-exit(r3)
            return
        L_0x000c:
            r4 = move-exception
            goto L_0x003a
        L_0x000e:
            int r0 = r4.eventId     // Catch:{ all -> 0x000c }
            boolean r0 = r3.isOperationComplete(r0)     // Catch:{ all -> 0x000c }
            r1 = 150(0x96, float:2.1E-43)
            if (r0 == 0) goto L_0x0021
            java.lang.String r0 = "Fingerprint"
            java.lang.String r2 = "Operation complete, setting to IDLE"
            android.util.Log.i(r0, r2)     // Catch:{ all -> 0x000c }
            r3.mOperation = r1     // Catch:{ all -> 0x000c }
        L_0x0021:
            int r0 = r3.mOperation     // Catch:{ all -> 0x000c }
            r2 = 156(0x9c, float:2.19E-43)
            if (r0 != r2) goto L_0x002f
            int r0 = r4.eventId     // Catch:{ all -> 0x000c }
            r2 = 401(0x191, float:5.62E-43)
            if (r0 != r2) goto L_0x002f
            r3.mOperation = r1     // Catch:{ all -> 0x000c }
        L_0x002f:
            com.synaptics.fingerprint.FingerprintCore$EventListener r0 = r3.mEventListener     // Catch:{ all -> 0x000c }
            if (r0 == 0) goto L_0x0038
            com.synaptics.fingerprint.FingerprintCore$EventListener r0 = r3.mEventListener     // Catch:{ all -> 0x000c }
            r0.onEvent(r4)     // Catch:{ all -> 0x000c }
        L_0x0038:
            monitor-exit(r3)
            return
        L_0x003a:
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.synaptics.fingerprint.FingerprintCore.FingerprintEventCallback(com.synaptics.fingerprint.FingerprintEvent):void");
    }

    private boolean isOperationComplete(int eventId) {
        return eventId == 402 || eventId == 403 || eventId == 404;
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
