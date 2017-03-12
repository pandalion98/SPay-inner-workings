package android.os;

public abstract class CountDownTimer {
    private static final int MSG = 1;
    private boolean mCancelled = false;
    private final long mCountdownInterval;
    private Handler mHandler = new Handler() {
        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r15) {
            /*
            r14 = this;
            r12 = 0;
            r7 = android.os.CountDownTimer.this;
            monitor-enter(r7);
            r6 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0026 }
            r6 = r6.mCancelled;	 Catch:{ all -> 0x0026 }
            if (r6 == 0) goto L_0x000f;
        L_0x000d:
            monitor-exit(r7);	 Catch:{ all -> 0x0026 }
        L_0x000e:
            return;
        L_0x000f:
            r6 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0026 }
            r8 = r6.mStopTimeInFuture;	 Catch:{ all -> 0x0026 }
            r10 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x0026 }
            r4 = r8 - r10;
            r6 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1));
            if (r6 > 0) goto L_0x0029;
        L_0x001f:
            r6 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0026 }
            r6.onFinish();	 Catch:{ all -> 0x0026 }
        L_0x0024:
            monitor-exit(r7);	 Catch:{ all -> 0x0026 }
            goto L_0x000e;
        L_0x0026:
            r6 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0026 }
            throw r6;
        L_0x0029:
            r6 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0026 }
            r8 = r6.mCountdownInterval;	 Catch:{ all -> 0x0026 }
            r6 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
            if (r6 >= 0) goto L_0x003c;
        L_0x0033:
            r6 = 1;
            r6 = r14.obtainMessage(r6);	 Catch:{ all -> 0x0026 }
            r14.sendMessageDelayed(r6, r4);	 Catch:{ all -> 0x0026 }
            goto L_0x0024;
        L_0x003c:
            r2 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x0026 }
            r6 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0026 }
            r6.onTick(r4);	 Catch:{ all -> 0x0026 }
            r6 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0026 }
            r8 = r6.mCountdownInterval;	 Catch:{ all -> 0x0026 }
            r8 = r8 + r2;
            r10 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x0026 }
            r0 = r8 - r10;
        L_0x0052:
            r6 = (r0 > r12 ? 1 : (r0 == r12 ? 0 : -1));
            if (r6 >= 0) goto L_0x005e;
        L_0x0056:
            r6 = android.os.CountDownTimer.this;	 Catch:{ all -> 0x0026 }
            r8 = r6.mCountdownInterval;	 Catch:{ all -> 0x0026 }
            r0 = r0 + r8;
            goto L_0x0052;
        L_0x005e:
            r6 = 1;
            r6 = r14.obtainMessage(r6);	 Catch:{ all -> 0x0026 }
            r14.sendMessageDelayed(r6, r0);	 Catch:{ all -> 0x0026 }
            goto L_0x0024;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.os.CountDownTimer.1.handleMessage(android.os.Message):void");
        }
    };
    private final long mMillisInFuture;
    private long mStopTimeInFuture;

    public abstract void onFinish();

    public abstract void onTick(long j);

    public CountDownTimer(long millisInFuture, long countDownInterval) {
        this.mMillisInFuture = millisInFuture;
        this.mCountdownInterval = countDownInterval;
    }

    public final synchronized void cancel() {
        this.mCancelled = true;
        this.mHandler.removeMessages(1);
    }

    public final synchronized CountDownTimer start() {
        CountDownTimer this;
        this.mCancelled = false;
        if (this.mMillisInFuture <= 0) {
            onFinish();
            this = this;
        } else {
            this.mStopTimeInFuture = SystemClock.elapsedRealtime() + this.mMillisInFuture;
            this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
            this = this;
        }
        return this;
    }
}
