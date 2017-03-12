package android.view;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Log;
import android.util.TimeUtils;
import com.samsung.android.smartface.SmartFaceManager;
import java.io.PrintWriter;

public final class Choreographer {
    public static final int CALLBACK_ANIMATION = 1;
    public static final int CALLBACK_COMMIT = 3;
    public static final int CALLBACK_INPUT = 0;
    private static final int CALLBACK_LAST = 3;
    private static final String[] CALLBACK_TRACE_TITLES = new String[]{"input", "animation", "traversal", "commit"};
    public static final int CALLBACK_TRAVERSAL = 2;
    private static final boolean DEBUG_FRAMES = false;
    private static final boolean DEBUG_JANK = false;
    private static final long DEFAULT_FRAME_DELAY = 10;
    private static final Object FRAME_CALLBACK_TOKEN = new Object() {
        public String toString() {
            return "FRAME_CALLBACK_TOKEN";
        }
    };
    private static final int MSG_DO_FRAME = 0;
    private static final int MSG_DO_SCHEDULE_CALLBACK = 2;
    private static final int MSG_DO_SCHEDULE_VSYNC = 1;
    private static final int MSG_DO_SND_CF = 3;
    private static final int REPORTING_BASE = SystemProperties.getInt("debug.reporting.base", 60);
    private static final int SKIPPED_FRAME_REPORTING_BASE = SystemProperties.getInt("sysprof.choreographer.skip", -1);
    private static final int SKIPPED_FRAME_WARNING_LIMIT = SystemProperties.getInt("debug.choreographer.skipwarning", 30);
    private static final String TAG = "Choreographer";
    private static final boolean USE_FRAME_TIME = SystemProperties.getBoolean("debug.choreographer.frametime", true);
    private static final boolean USE_VSYNC = SystemProperties.getBoolean("debug.choreographer.vsync", true);
    private static volatile long sFrameDelay = DEFAULT_FRAME_DELAY;
    private static final ThreadLocal<Choreographer> sThreadInstance = new ThreadLocal<Choreographer>() {
        protected Choreographer initialValue() {
            Looper looper = Looper.myLooper();
            if (looper != null) {
                return new Choreographer(looper);
            }
            throw new IllegalStateException("The current thread must have a looper!");
        }
    };
    private CallbackRecord mCallbackPool;
    private final CallbackQueue[] mCallbackQueues;
    private boolean mCallbacksRunning;
    Context mContext;
    private boolean mDebugPrintNextFrameTimeDelta;
    private final FrameDisplayEventReceiver mDisplayEventReceiver;
    FrameInfo mFrameInfo;
    private long mFrameIntervalNanos;
    private boolean mFrameScheduled;
    private final FrameHandler mHandler;
    private long mLastFrameTimeNanos;
    private final Object mLock;
    private final Looper mLooper;

    private final class CallbackQueue {
        private CallbackRecord mHead;

        private CallbackQueue() {
        }

        public boolean hasDueCallbacksLocked(long now) {
            return this.mHead != null && this.mHead.dueTime <= now;
        }

        public CallbackRecord extractDueCallbacksLocked(long now) {
            CallbackRecord callbacks = this.mHead;
            if (callbacks == null || callbacks.dueTime > now) {
                return null;
            }
            CallbackRecord last = callbacks;
            CallbackRecord next = last.next;
            while (next != null) {
                if (next.dueTime > now) {
                    last.next = null;
                    break;
                }
                last = next;
                next = next.next;
            }
            this.mHead = next;
            return callbacks;
        }

        public void addCallbackLocked(long dueTime, Object action, Object token) {
            CallbackRecord callback = Choreographer.this.obtainCallbackLocked(dueTime, action, token);
            CallbackRecord entry = this.mHead;
            if (entry == null) {
                this.mHead = callback;
            } else if (dueTime < entry.dueTime) {
                callback.next = entry;
                this.mHead = callback;
            } else {
                while (entry.next != null) {
                    if (dueTime < entry.next.dueTime) {
                        callback.next = entry.next;
                        break;
                    }
                    entry = entry.next;
                }
                entry.next = callback;
            }
        }

        public void removeCallbacksLocked(Object action, Object token) {
            CallbackRecord predecessor = null;
            CallbackRecord callback = this.mHead;
            while (callback != null) {
                CallbackRecord next = callback.next;
                if ((action == null || callback.action == action) && (token == null || callback.token == token)) {
                    if (predecessor != null) {
                        predecessor.next = next;
                    } else {
                        this.mHead = next;
                    }
                    Choreographer.this.recycleCallbackLocked(callback);
                } else {
                    predecessor = callback;
                }
                callback = next;
            }
        }
    }

    private static final class CallbackRecord {
        public Object action;
        public long dueTime;
        public CallbackRecord next;
        public Object token;

        private CallbackRecord() {
        }

        public void run(long frameTimeNanos) {
            if (this.token == Choreographer.FRAME_CALLBACK_TOKEN) {
                ((FrameCallback) this.action).doFrame(frameTimeNanos);
            } else {
                ((Runnable) this.action).run();
            }
        }
    }

    public interface FrameCallback {
        void doFrame(long j);
    }

    private final class FrameDisplayEventReceiver extends DisplayEventReceiver implements Runnable {
        private int mFrame;
        private boolean mHavePendingVsync;
        private long mTimestampNanos;

        public FrameDisplayEventReceiver(Looper looper) {
            super(looper);
        }

        public void onVsync(long timestampNanos, int builtInDisplayId, int frame) {
            if (builtInDisplayId != 0) {
                Log.d(Choreographer.TAG, "Received vsync from secondary display, but we don't support this case yet.  Choreographer needs a way to explicitly request vsync for a specific display to ensure it doesn't lose track of its scheduled vsync.");
                scheduleVsync();
                return;
            }
            long now = System.nanoTime();
            if (timestampNanos > now) {
                Log.w(Choreographer.TAG, "Frame time is " + (((float) (timestampNanos - now)) * 1.0E-6f) + " ms in the future!  Check that graphics HAL is generating vsync " + "timestamps using the correct timebase.");
                timestampNanos = now;
            }
            if (this.mHavePendingVsync) {
                Log.w(Choreographer.TAG, "Already have a pending vsync event.  There should only be one at a time.");
            } else {
                this.mHavePendingVsync = true;
            }
            this.mTimestampNanos = timestampNanos;
            this.mFrame = frame;
            Message msg = Message.obtain(Choreographer.this.mHandler, this);
            msg.setAsynchronous(true);
            Choreographer.this.mHandler.sendMessageAtTime(msg, timestampNanos / TimeUtils.NANOS_PER_MS);
        }

        public void run() {
            this.mHavePendingVsync = false;
            Choreographer.this.doFrame(this.mTimestampNanos, this.mFrame);
        }
    }

    private final class FrameHandler extends Handler {
        public FrameHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Choreographer.this.doFrame(System.nanoTime(), 0);
                    return;
                case 1:
                    Choreographer.this.doScheduleVsync();
                    return;
                case 2:
                    Choreographer.this.doScheduleCallback(msg.arg1);
                    return;
                case 3:
                    Choreographer.this.doReport(((Long) msg.obj).longValue());
                    return;
                default:
                    return;
            }
        }
    }

    private Choreographer(Looper looper) {
        FrameDisplayEventReceiver frameDisplayEventReceiver;
        this.mLock = new Object();
        this.mFrameInfo = new FrameInfo();
        this.mLooper = looper;
        this.mHandler = new FrameHandler(looper);
        if (USE_VSYNC) {
            frameDisplayEventReceiver = new FrameDisplayEventReceiver(looper);
        } else {
            frameDisplayEventReceiver = null;
        }
        this.mDisplayEventReceiver = frameDisplayEventReceiver;
        this.mLastFrameTimeNanos = Long.MIN_VALUE;
        this.mFrameIntervalNanos = (long) (1.0E9f / getRefreshRate());
        this.mCallbackQueues = new CallbackQueue[4];
        for (int i = 0; i <= 3; i++) {
            this.mCallbackQueues[i] = new CallbackQueue();
        }
    }

    private static float getRefreshRate() {
        return DisplayManagerGlobal.getInstance().getDisplayInfo(0).getMode().getRefreshRate();
    }

    public static Choreographer getInstance() {
        return (Choreographer) sThreadInstance.get();
    }

    public static long getFrameDelay() {
        return sFrameDelay;
    }

    public static void setFrameDelay(long frameDelay) {
        sFrameDelay = frameDelay;
    }

    public static long subtractFrameDelay(long delayMillis) {
        long frameDelay = sFrameDelay;
        return delayMillis <= frameDelay ? 0 : delayMillis - frameDelay;
    }

    public long getFrameIntervalNanos() {
        return this.mFrameIntervalNanos;
    }

    void dump(String prefix, PrintWriter writer) {
        String innerPrefix = prefix + "  ";
        writer.print(prefix);
        writer.println("Choreographer:");
        writer.print(innerPrefix);
        writer.print("mFrameScheduled=");
        writer.println(this.mFrameScheduled);
        writer.print(innerPrefix);
        writer.print("mLastFrameTime=");
        writer.println(TimeUtils.formatUptime(this.mLastFrameTimeNanos / TimeUtils.NANOS_PER_MS));
    }

    public void postCallback(int callbackType, Runnable action, Object token) {
        postCallbackDelayed(callbackType, action, token, 0);
    }

    public void postCallbackDelayed(int callbackType, Runnable action, Object token, long delayMillis) {
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        } else if (callbackType < 0 || callbackType > 3) {
            throw new IllegalArgumentException("callbackType is invalid");
        } else {
            postCallbackDelayedInternal(callbackType, action, token, delayMillis);
        }
    }

    private void postCallbackDelayedInternal(int callbackType, Object action, Object token, long delayMillis) {
        synchronized (this.mLock) {
            long now = SystemClock.uptimeMillis();
            long dueTime = now + delayMillis;
            this.mCallbackQueues[callbackType].addCallbackLocked(dueTime, action, token);
            if (dueTime <= now) {
                scheduleFrameLocked(now);
            } else {
                Message msg = this.mHandler.obtainMessage(2, action);
                msg.arg1 = callbackType;
                msg.setAsynchronous(true);
                this.mHandler.sendMessageAtTime(msg, dueTime);
            }
        }
    }

    public void removeCallbacks(int callbackType, Runnable action, Object token) {
        if (callbackType < 0 || callbackType > 3) {
            throw new IllegalArgumentException("callbackType is invalid");
        }
        removeCallbacksInternal(callbackType, action, token);
    }

    private void removeCallbacksInternal(int callbackType, Object action, Object token) {
        synchronized (this.mLock) {
            this.mCallbackQueues[callbackType].removeCallbacksLocked(action, token);
            if (action != null && token == null) {
                this.mHandler.removeMessages(2, action);
            }
        }
    }

    public void postFrameCallback(FrameCallback callback) {
        postFrameCallbackDelayed(callback, 0);
    }

    public void postFrameCallbackDelayed(FrameCallback callback, long delayMillis) {
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        postCallbackDelayedInternal(1, callback, FRAME_CALLBACK_TOKEN, delayMillis);
    }

    public void removeFrameCallback(FrameCallback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback must not be null");
        }
        removeCallbacksInternal(1, callback, FRAME_CALLBACK_TOKEN);
    }

    public long getFrameTime() {
        return getFrameTimeNanos() / TimeUtils.NANOS_PER_MS;
    }

    public long getFrameTimeNanos() {
        long nanoTime;
        synchronized (this.mLock) {
            if (this.mCallbacksRunning) {
                nanoTime = USE_FRAME_TIME ? this.mLastFrameTimeNanos : System.nanoTime();
            } else {
                throw new IllegalStateException("This method must only be called as part of a callback while a frame is in progress.");
            }
        }
        return nanoTime;
    }

    private void scheduleFrameLocked(long now) {
        if (!this.mFrameScheduled) {
            this.mFrameScheduled = true;
            Message msg;
            if (!USE_VSYNC) {
                long nextFrameTime = Math.max((this.mLastFrameTimeNanos / TimeUtils.NANOS_PER_MS) + sFrameDelay, now);
                msg = this.mHandler.obtainMessage(0);
                msg.setAsynchronous(true);
                this.mHandler.sendMessageAtTime(msg, nextFrameTime);
            } else if (isRunningOnLooperThreadLocked()) {
                scheduleVsyncLocked();
            } else {
                msg = this.mHandler.obtainMessage(1);
                msg.setAsynchronous(true);
                this.mHandler.sendMessageAtFrontOfQueue(msg);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void doFrame(long r22, int r24) {
        /*
        r21 = this;
        r0 = r21;
        r0 = r0.mLock;
        r16 = r0;
        monitor-enter(r16);
        r0 = r21;
        r11 = r0.mFrameScheduled;	 Catch:{ all -> 0x00ca }
        if (r11 != 0) goto L_0x000f;
    L_0x000d:
        monitor-exit(r16);	 Catch:{ all -> 0x00ca }
    L_0x000e:
        return;
    L_0x000f:
        r4 = r22;
        r14 = java.lang.System.nanoTime();	 Catch:{ all -> 0x00ca }
        r6 = r14 - r22;
        r0 = r21;
        r0 = r0.mFrameIntervalNanos;	 Catch:{ all -> 0x00ca }
        r18 = r0;
        r11 = (r6 > r18 ? 1 : (r6 == r18 ? 0 : -1));
        if (r11 < 0) goto L_0x00ba;
    L_0x0021:
        r0 = r21;
        r0 = r0.mFrameIntervalNanos;	 Catch:{ all -> 0x00ca }
        r18 = r0;
        r12 = r6 / r18;
        r11 = SKIPPED_FRAME_WARNING_LIMIT;	 Catch:{ all -> 0x00ca }
        r0 = (long) r11;	 Catch:{ all -> 0x00ca }
        r18 = r0;
        r11 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1));
        if (r11 < 0) goto L_0x005a;
    L_0x0032:
        r11 = "Choreographer";
        r17 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ca }
        r17.<init>();	 Catch:{ all -> 0x00ca }
        r18 = "Skipped ";
        r17 = r17.append(r18);	 Catch:{ all -> 0x00ca }
        r0 = r17;
        r17 = r0.append(r12);	 Catch:{ all -> 0x00ca }
        r18 = " frames!  ";
        r17 = r17.append(r18);	 Catch:{ all -> 0x00ca }
        r18 = "The application may be doing too much work on its main thread.";
        r17 = r17.append(r18);	 Catch:{ all -> 0x00ca }
        r17 = r17.toString();	 Catch:{ all -> 0x00ca }
        r0 = r17;
        android.util.Log.i(r11, r0);	 Catch:{ all -> 0x00ca }
    L_0x005a:
        r11 = SKIPPED_FRAME_REPORTING_BASE;	 Catch:{ all -> 0x00ca }
        if (r11 < 0) goto L_0x008e;
    L_0x005e:
        r11 = SKIPPED_FRAME_REPORTING_BASE;	 Catch:{ all -> 0x00ca }
        r0 = (long) r11;	 Catch:{ all -> 0x00ca }
        r18 = r0;
        r11 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1));
        if (r11 < 0) goto L_0x008e;
    L_0x0067:
        r11 = 73200; // 0x11df0 float:1.02575E-40 double:3.61656E-319;
        r17 = 2;
        r0 = r17;
        r0 = new java.lang.Object[r0];	 Catch:{ all -> 0x00ca }
        r17 = r0;
        r18 = 0;
        r19 = android.os.Process.myPid();	 Catch:{ all -> 0x00ca }
        r19 = java.lang.Integer.valueOf(r19);	 Catch:{ all -> 0x00ca }
        r17[r18] = r19;	 Catch:{ all -> 0x00ca }
        r18 = 1;
        r0 = (int) r12;	 Catch:{ all -> 0x00ca }
        r19 = r0;
        r19 = java.lang.Integer.valueOf(r19);	 Catch:{ all -> 0x00ca }
        r17[r18] = r19;	 Catch:{ all -> 0x00ca }
        r0 = r17;
        android.util.EventLog.writeEvent(r11, r0);	 Catch:{ all -> 0x00ca }
    L_0x008e:
        r11 = REPORTING_BASE;	 Catch:{ all -> 0x00ca }
        r0 = (long) r11;	 Catch:{ all -> 0x00ca }
        r18 = r0;
        r11 = (r12 > r18 ? 1 : (r12 == r18 ? 0 : -1));
        if (r11 < 0) goto L_0x00b0;
    L_0x0097:
        r0 = r21;
        r11 = r0.mHandler;	 Catch:{ all -> 0x00ca }
        r17 = 3;
        r0 = r17;
        r10 = r11.obtainMessage(r0);	 Catch:{ all -> 0x00ca }
        r11 = java.lang.Long.valueOf(r12);	 Catch:{ all -> 0x00ca }
        r10.obj = r11;	 Catch:{ all -> 0x00ca }
        r0 = r21;
        r11 = r0.mHandler;	 Catch:{ all -> 0x00ca }
        r11.sendMessage(r10);	 Catch:{ all -> 0x00ca }
    L_0x00b0:
        r0 = r21;
        r0 = r0.mFrameIntervalNanos;	 Catch:{ all -> 0x00ca }
        r18 = r0;
        r8 = r6 % r18;
        r22 = r14 - r8;
    L_0x00ba:
        r0 = r21;
        r0 = r0.mLastFrameTimeNanos;	 Catch:{ all -> 0x00ca }
        r18 = r0;
        r11 = (r22 > r18 ? 1 : (r22 == r18 ? 0 : -1));
        if (r11 >= 0) goto L_0x00cd;
    L_0x00c4:
        r21.scheduleVsyncLocked();	 Catch:{ all -> 0x00ca }
        monitor-exit(r16);	 Catch:{ all -> 0x00ca }
        goto L_0x000e;
    L_0x00ca:
        r11 = move-exception;
        monitor-exit(r16);	 Catch:{ all -> 0x00ca }
        throw r11;
    L_0x00cd:
        r0 = r21;
        r11 = r0.mFrameInfo;	 Catch:{ all -> 0x00ca }
        r0 = r22;
        r11.setVsync(r4, r0);	 Catch:{ all -> 0x00ca }
        r11 = 0;
        r0 = r21;
        r0.mFrameScheduled = r11;	 Catch:{ all -> 0x00ca }
        r0 = r22;
        r2 = r21;
        r2.mLastFrameTimeNanos = r0;	 Catch:{ all -> 0x00ca }
        monitor-exit(r16);	 Catch:{ all -> 0x00ca }
        r16 = 8;
        r11 = "Choreographer#doFrame";
        r0 = r16;
        android.os.Trace.traceBegin(r0, r11);	 Catch:{ all -> 0x0127 }
        r0 = r21;
        r11 = r0.mFrameInfo;	 Catch:{ all -> 0x0127 }
        r11.markInputHandlingStart();	 Catch:{ all -> 0x0127 }
        r11 = 0;
        r0 = r21;
        r1 = r22;
        r0.doCallbacks(r11, r1);	 Catch:{ all -> 0x0127 }
        r0 = r21;
        r11 = r0.mFrameInfo;	 Catch:{ all -> 0x0127 }
        r11.markAnimationsStart();	 Catch:{ all -> 0x0127 }
        r11 = 1;
        r0 = r21;
        r1 = r22;
        r0.doCallbacks(r11, r1);	 Catch:{ all -> 0x0127 }
        r0 = r21;
        r11 = r0.mFrameInfo;	 Catch:{ all -> 0x0127 }
        r11.markPerformTraversalsStart();	 Catch:{ all -> 0x0127 }
        r11 = 2;
        r0 = r21;
        r1 = r22;
        r0.doCallbacks(r11, r1);	 Catch:{ all -> 0x0127 }
        r11 = 3;
        r0 = r21;
        r1 = r22;
        r0.doCallbacks(r11, r1);	 Catch:{ all -> 0x0127 }
        r16 = 8;
        android.os.Trace.traceEnd(r16);
        goto L_0x000e;
    L_0x0127:
        r11 = move-exception;
        r16 = 8;
        android.os.Trace.traceEnd(r16);
        throw r11;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.Choreographer.doFrame(long, int):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void doCallbacks(int r21, long r22) {
        /*
        r20 = this;
        r0 = r20;
        r14 = r0.mLock;
        monitor-enter(r14);
        r12 = java.lang.System.nanoTime();	 Catch:{ all -> 0x006f }
        r0 = r20;
        r11 = r0.mCallbackQueues;	 Catch:{ all -> 0x006f }
        r11 = r11[r21];	 Catch:{ all -> 0x006f }
        r16 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r16 = r12 / r16;
        r0 = r16;
        r5 = r11.extractDueCallbacksLocked(r0);	 Catch:{ all -> 0x006f }
        if (r5 != 0) goto L_0x001e;
    L_0x001c:
        monitor-exit(r14);	 Catch:{ all -> 0x006f }
    L_0x001d:
        return;
    L_0x001e:
        r11 = 1;
        r0 = r20;
        r0.mCallbacksRunning = r11;	 Catch:{ all -> 0x006f }
        r11 = 3;
        r0 = r21;
        if (r0 != r11) goto L_0x005a;
    L_0x0028:
        r6 = r12 - r22;
        r16 = 8;
        r11 = "jitterNanos";
        r15 = (int) r6;	 Catch:{ all -> 0x006f }
        r0 = r16;
        android.os.Trace.traceCounter(r0, r11, r15);	 Catch:{ all -> 0x006f }
        r16 = 2;
        r0 = r20;
        r0 = r0.mFrameIntervalNanos;	 Catch:{ all -> 0x006f }
        r18 = r0;
        r16 = r16 * r18;
        r11 = (r6 > r16 ? 1 : (r6 == r16 ? 0 : -1));
        if (r11 < 0) goto L_0x005a;
    L_0x0042:
        r0 = r20;
        r0 = r0.mFrameIntervalNanos;	 Catch:{ all -> 0x006f }
        r16 = r0;
        r16 = r6 % r16;
        r0 = r20;
        r0 = r0.mFrameIntervalNanos;	 Catch:{ all -> 0x006f }
        r18 = r0;
        r8 = r16 + r18;
        r22 = r12 - r8;
        r0 = r22;
        r2 = r20;
        r2.mLastFrameTimeNanos = r0;	 Catch:{ all -> 0x006f }
    L_0x005a:
        monitor-exit(r14);	 Catch:{ all -> 0x006f }
        r14 = 8;
        r11 = CALLBACK_TRACE_TITLES;	 Catch:{ all -> 0x0090 }
        r11 = r11[r21];	 Catch:{ all -> 0x0090 }
        android.os.Trace.traceBegin(r14, r11);	 Catch:{ all -> 0x0090 }
        r4 = r5;
    L_0x0065:
        if (r4 == 0) goto L_0x0072;
    L_0x0067:
        r0 = r22;
        r4.run(r0);	 Catch:{ all -> 0x0090 }
        r4 = r4.next;	 Catch:{ all -> 0x0090 }
        goto L_0x0065;
    L_0x006f:
        r11 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x006f }
        throw r11;
    L_0x0072:
        r0 = r20;
        r14 = r0.mLock;
        monitor-enter(r14);
        r11 = 0;
        r0 = r20;
        r0.mCallbacksRunning = r11;	 Catch:{ all -> 0x008d }
    L_0x007c:
        r10 = r5.next;	 Catch:{ all -> 0x008d }
        r0 = r20;
        r0.recycleCallbackLocked(r5);	 Catch:{ all -> 0x008d }
        r5 = r10;
        if (r5 != 0) goto L_0x007c;
    L_0x0086:
        monitor-exit(r14);	 Catch:{ all -> 0x008d }
        r14 = 8;
        android.os.Trace.traceEnd(r14);
        goto L_0x001d;
    L_0x008d:
        r11 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x008d }
        throw r11;
    L_0x0090:
        r11 = move-exception;
        r0 = r20;
        r14 = r0.mLock;
        monitor-enter(r14);
        r15 = 0;
        r0 = r20;
        r0.mCallbacksRunning = r15;	 Catch:{ all -> 0x00ac }
    L_0x009b:
        r10 = r5.next;	 Catch:{ all -> 0x00ac }
        r0 = r20;
        r0.recycleCallbackLocked(r5);	 Catch:{ all -> 0x00ac }
        r5 = r10;
        if (r5 != 0) goto L_0x009b;
    L_0x00a5:
        monitor-exit(r14);	 Catch:{ all -> 0x00ac }
        r14 = 8;
        android.os.Trace.traceEnd(r14);
        throw r11;
    L_0x00ac:
        r11 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x00ac }
        throw r11;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.Choreographer.doCallbacks(int, long):void");
    }

    void doScheduleVsync() {
        synchronized (this.mLock) {
            if (this.mFrameScheduled) {
                scheduleVsyncLocked();
            }
        }
    }

    void doScheduleCallback(int callbackType) {
        synchronized (this.mLock) {
            if (!this.mFrameScheduled) {
                long now = SystemClock.uptimeMillis();
                if (this.mCallbackQueues[callbackType].hasDueCallbacksLocked(now)) {
                    scheduleFrameLocked(now);
                }
            }
        }
    }

    public void getRootContext(Context context) {
        this.mContext = context;
    }

    public void removeRootContext() {
        this.mContext = null;
    }

    void doReport(long skippedFrames) {
        if (SystemProperties.get("sys.config.bigdata_enable").equals(SmartFaceManager.TRUE) && SystemProperties.get("sys.boot_completed").equals(SmartFaceManager.PAGE_BOTTOM) && !SystemProperties.get("sys.isdumpstaterunning").equals(SmartFaceManager.PAGE_BOTTOM)) {
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.sec.android.intent.action.SG_REPORT");
            broadcastIntent.setFlags(1073741824);
            if (this.mContext != null) {
                broadcastIntent.putExtra("from", EventLogTags.CHOREOGRAPHER_FRAME_SKIP);
                broadcastIntent.putExtra("pid", Process.myPid());
                broadcastIntent.putExtra("skippedFrames", skippedFrames);
                this.mContext.sendBroadcastAsUser(broadcastIntent, UserHandle.getCallingUserHandle());
            }
        }
    }

    private void scheduleVsyncLocked() {
        this.mDisplayEventReceiver.scheduleVsync();
    }

    private boolean isRunningOnLooperThreadLocked() {
        return Looper.myLooper() == this.mLooper;
    }

    private CallbackRecord obtainCallbackLocked(long dueTime, Object action, Object token) {
        CallbackRecord callback = this.mCallbackPool;
        if (callback == null) {
            callback = new CallbackRecord();
        } else {
            this.mCallbackPool = callback.next;
            callback.next = null;
        }
        callback.dueTime = dueTime;
        callback.action = action;
        callback.token = token;
        return callback;
    }

    private void recycleCallbackLocked(CallbackRecord callback) {
        callback.action = null;
        callback.token = null;
        callback.next = this.mCallbackPool;
        this.mCallbackPool = callback;
    }
}
