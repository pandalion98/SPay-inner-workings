package android.os;

import android.content.Context;
import android.os.IUpdateLock.Stub;
import android.util.Log;

public class UpdateLock {
    private static final boolean DEBUG = false;
    public static final String NOW_IS_CONVENIENT = "nowisconvenient";
    private static final String TAG = "UpdateLock";
    public static final String TIMESTAMP = "timestamp";
    public static final String UPDATE_LOCK_CHANGED = "android.os.UpdateLock.UPDATE_LOCK_CHANGED";
    private static IUpdateLock sService;
    int mCount = 0;
    boolean mHeld = false;
    boolean mRefCounted = true;
    final String mTag;
    IBinder mToken;

    private static void checkService() {
        if (sService == null) {
            sService = Stub.asInterface(ServiceManager.getService(Context.UPDATE_LOCK_SERVICE));
        }
    }

    public UpdateLock(String tag) {
        this.mTag = tag;
        this.mToken = new Binder();
    }

    public void setReferenceCounted(boolean isRefCounted) {
        this.mRefCounted = isRefCounted;
    }

    public boolean isHeld() {
        boolean z;
        synchronized (this.mToken) {
            z = this.mHeld;
        }
        return z;
    }

    public void acquire() {
        checkService();
        synchronized (this.mToken) {
            acquireLocked();
        }
    }

    private void acquireLocked() {
        if (this.mRefCounted) {
            int i = this.mCount;
            this.mCount = i + 1;
            if (i != 0) {
                return;
            }
        }
        if (sService != null) {
            try {
                sService.acquireUpdateLock(this.mToken, this.mTag);
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to contact service to acquire");
            }
        }
        this.mHeld = true;
    }

    public void release() {
        checkService();
        synchronized (this.mToken) {
            releaseLocked();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void releaseLocked() {
        /*
        r3 = this;
        r1 = r3.mRefCounted;
        if (r1 == 0) goto L_0x000c;
    L_0x0004:
        r1 = r3.mCount;
        r1 = r1 + -1;
        r3.mCount = r1;
        if (r1 != 0) goto L_0x001a;
    L_0x000c:
        r1 = sService;
        if (r1 == 0) goto L_0x0017;
    L_0x0010:
        r1 = sService;	 Catch:{ RemoteException -> 0x0026 }
        r2 = r3.mToken;	 Catch:{ RemoteException -> 0x0026 }
        r1.releaseUpdateLock(r2);	 Catch:{ RemoteException -> 0x0026 }
    L_0x0017:
        r1 = 0;
        r3.mHeld = r1;
    L_0x001a:
        r1 = r3.mCount;
        if (r1 >= 0) goto L_0x002f;
    L_0x001e:
        r1 = new java.lang.RuntimeException;
        r2 = "UpdateLock under-locked";
        r1.<init>(r2);
        throw r1;
    L_0x0026:
        r0 = move-exception;
        r1 = "UpdateLock";
        r2 = "Unable to contact service to release";
        android.util.Log.e(r1, r2);
        goto L_0x0017;
    L_0x002f:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.UpdateLock.releaseLocked():void");
    }

    protected void finalize() throws Throwable {
        synchronized (this.mToken) {
            if (this.mHeld) {
                Log.wtf(TAG, "UpdateLock finalized while still held");
                try {
                    sService.releaseUpdateLock(this.mToken);
                } catch (RemoteException e) {
                    Log.e(TAG, "Unable to contact service to release");
                }
            }
        }
    }
}
