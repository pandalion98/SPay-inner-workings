package android.os;

import android.provider.MediaStore;
import android.util.EventLog;
import android.util.Log;
import android.util.Printer;

public final class Looper {
    private static final String TAG = "Looper";
    private static Looper sMainLooper;
    static final ThreadLocal<Looper> sThreadLocal = new ThreadLocal();
    private Printer mLogging;
    private String mPackageName = MediaStore.UNKNOWN_STRING;
    final MessageQueue mQueue;
    private int mSlowLapTimeEvent = -1;
    final Thread mThread;

    public static void prepare() {
        prepare(true);
    }

    private static void prepare(boolean quitAllowed) {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one Looper may be created per thread");
        }
        sThreadLocal.set(new Looper(quitAllowed));
    }

    public static void prepareMainLooper() {
        prepare(false);
        synchronized (Looper.class) {
            if (sMainLooper != null) {
                throw new IllegalStateException("The main Looper has already been prepared.");
            }
            sMainLooper = myLooper();
        }
    }

    public static Looper getMainLooper() {
        Looper looper;
        synchronized (Looper.class) {
            looper = sMainLooper;
        }
        return looper;
    }

    public static void loop() {
        Looper me = myLooper();
        if (me == null) {
            throw new RuntimeException("No Looper; Looper.prepare() wasn't called on this thread.");
        }
        boolean laptime;
        MessageQueue queue = me.mQueue;
        Binder.clearCallingIdentity();
        long ident = Binder.clearCallingIdentity();
        long tm = 0;
        if (me.mSlowLapTimeEvent >= 0) {
            laptime = true;
        } else {
            laptime = false;
        }
        while (true) {
            Message msg = queue.next();
            if (msg != null) {
                if (laptime) {
                    tm = -System.currentTimeMillis();
                }
                Printer logging = me.mLogging;
                if (logging != null) {
                    logging.println(">>>>> Dispatching to " + msg.target + " " + msg.callback + ": " + msg.what);
                }
                msg.target.dispatchMessage(msg);
                if (logging != null) {
                    logging.println("<<<<< Finished to " + msg.target + " " + msg.callback);
                }
                long newIdent = Binder.clearCallingIdentity();
                if (ident != newIdent) {
                    Log.wtf(TAG, "Thread identity changed from 0x" + Long.toHexString(ident) + " to 0x" + Long.toHexString(newIdent) + " while dispatching to " + msg.target.getClass().getName() + " " + msg.callback + " what=" + msg.what);
                }
                msg.recycleUnchecked();
                if (laptime) {
                    tm += System.currentTimeMillis();
                    if (tm >= ((long) me.mSlowLapTimeEvent)) {
                        EventLog.writeEvent(EventLogTags.LOOPER_SLOW_LAP_TIME, new Object[]{me.mPackageName, Integer.valueOf((int) tm)});
                    }
                }
            } else {
                return;
            }
        }
    }

    public static Looper myLooper() {
        return (Looper) sThreadLocal.get();
    }

    public void setSlowLapTimeEvent(int timeOutMs) {
        this.mSlowLapTimeEvent = timeOutMs;
    }

    public void setPackageName(String name) {
        this.mPackageName = name;
    }

    public static MessageQueue myQueue() {
        return myLooper().mQueue;
    }

    private Looper(boolean quitAllowed) {
        this.mQueue = new MessageQueue(quitAllowed);
        this.mThread = Thread.currentThread();
    }

    public boolean isCurrentThread() {
        return Thread.currentThread() == this.mThread;
    }

    public void setMessageLogging(Printer printer) {
        this.mLogging = printer;
    }

    public void quit() {
        this.mQueue.quit(false);
    }

    public void quitSafely() {
        this.mQueue.quit(true);
    }

    public Thread getThread() {
        return this.mThread;
    }

    public MessageQueue getQueue() {
        return this.mQueue;
    }

    public void dump(Printer pw, String prefix) {
        pw.println(prefix + toString());
        this.mQueue.dump(pw, prefix + "  ");
    }

    public String toString() {
        return "Looper (" + this.mThread.getName() + ", tid " + this.mThread.getId() + ") {" + Integer.toHexString(System.identityHashCode(this)) + "}";
    }
}
