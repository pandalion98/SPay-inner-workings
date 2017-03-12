package com.android.server;

import android.os.ConditionVariable;
import android.os.SystemClock;

abstract class ResettableTimeout {
    private ConditionVariable mLock = new ConditionVariable();
    private volatile long mOffAt;
    private volatile boolean mOffCalled;
    private Thread mThread;

    private class T extends Thread {
        private T() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r6 = this;
            r2 = com.android.server.ResettableTimeout.this;
            r2 = r2.mLock;
            r2.open();
        L_0x0009:
            monitor-enter(r6);
            r2 = com.android.server.ResettableTimeout.this;	 Catch:{ all -> 0x0036 }
            r2 = r2.mOffAt;	 Catch:{ all -> 0x0036 }
            r4 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x0036 }
            r0 = r2 - r4;
            r2 = 0;
            r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
            if (r2 > 0) goto L_0x002f;
        L_0x001c:
            r2 = com.android.server.ResettableTimeout.this;	 Catch:{ all -> 0x0036 }
            r3 = 1;
            r2.mOffCalled = r3;	 Catch:{ all -> 0x0036 }
            r2 = com.android.server.ResettableTimeout.this;	 Catch:{ all -> 0x0036 }
            r2.off();	 Catch:{ all -> 0x0036 }
            r2 = com.android.server.ResettableTimeout.this;	 Catch:{ all -> 0x0036 }
            r3 = 0;
            r2.mThread = r3;	 Catch:{ all -> 0x0036 }
            monitor-exit(r6);	 Catch:{ all -> 0x0036 }
            return;
        L_0x002f:
            monitor-exit(r6);	 Catch:{ all -> 0x0036 }
            sleep(r0);	 Catch:{ InterruptedException -> 0x0034 }
            goto L_0x0009;
        L_0x0034:
            r2 = move-exception;
            goto L_0x0009;
        L_0x0036:
            r2 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x0036 }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.ResettableTimeout.T.run():void");
        }
    }

    public abstract void off();

    public abstract void on(boolean z);

    ResettableTimeout() {
    }

    public void go(long milliseconds) {
        synchronized (this) {
            boolean alreadyOn;
            this.mOffAt = SystemClock.uptimeMillis() + milliseconds;
            if (this.mThread == null) {
                alreadyOn = false;
                this.mLock.close();
                this.mThread = new T();
                this.mThread.start();
                this.mLock.block();
                this.mOffCalled = false;
            } else {
                alreadyOn = true;
                this.mThread.interrupt();
            }
            on(alreadyOn);
        }
    }

    public void cancel() {
        synchronized (this) {
            this.mOffAt = 0;
            if (this.mThread != null) {
                this.mThread.interrupt();
                this.mThread = null;
            }
            if (!this.mOffCalled) {
                this.mOffCalled = true;
                off();
            }
        }
    }
}
