package com.android.internal.os;

import android.os.IBinder;
import android.os.SystemClock;
import android.util.EventLog;
import dalvik.system.VMRuntime;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class BinderInternal {
    private static final int GC_DELAY_MAX_DURATION = 3000;
    private static final int POSTPONED_GC_MAX = 5;
    static Object delayGcMonitorObject = new Object();
    static ExecutorService executor = Executors.newFixedThreadPool(1);
    static FutureTask<Void> futureTaskInstance = null;
    static long lastGcDelayRequestTime = SystemClock.uptimeMillis();
    static int postponedGcCount = 0;
    static WeakReference<GcWatcher> sGcWatcher = new WeakReference(new GcWatcher());
    static ArrayList<Runnable> sGcWatchers = new ArrayList();
    static long sLastGcTime;
    static Runnable[] sTmpWatchers = new Runnable[1];
    static TimerGc timerGcInstance = null;

    static final class GcWatcher {
        GcWatcher() {
        }

        protected void finalize() throws Throwable {
            BinderInternal.handleGc();
            BinderInternal.sLastGcTime = SystemClock.uptimeMillis();
            synchronized (BinderInternal.sGcWatchers) {
                BinderInternal.sTmpWatchers = (Runnable[]) BinderInternal.sGcWatchers.toArray(BinderInternal.sTmpWatchers);
            }
            for (int i = 0; i < BinderInternal.sTmpWatchers.length; i++) {
                if (BinderInternal.sTmpWatchers[i] != null) {
                    BinderInternal.sTmpWatchers[i].run();
                }
            }
            BinderInternal.sGcWatcher = new WeakReference(new GcWatcher());
        }
    }

    public static class TimerGc implements Callable<Void> {
        private long waitTime;

        public TimerGc(long timeInMillis) {
            this.waitTime = timeInMillis;
        }

        public Void call() throws Exception {
            Thread.sleep(this.waitTime);
            BinderInternal.forceGc("Binder");
            BinderInternal.postponedGcCount = 0;
            return null;
        }
    }

    public static final native void disableBackgroundScheduling(boolean z);

    public static final native IBinder getContextObject();

    static final native void handleGc();

    public static final native void joinThreadPool();

    public static void addGcWatcher(Runnable watcher) {
        synchronized (sGcWatchers) {
            sGcWatchers.add(watcher);
        }
    }

    public static long getLastGcTime() {
        return sLastGcTime;
    }

    public static void forceGc(String reason) {
        EventLog.writeEvent(2741, reason);
        VMRuntime.getRuntime().requestConcurrentGC();
    }

    public static void modifyDelayedGcParams() {
        long nowTime = SystemClock.uptimeMillis();
        synchronized (delayGcMonitorObject) {
            if (futureTaskInstance == null || postponedGcCount == 0) {
                lastGcDelayRequestTime = nowTime;
                timerGcInstance = new TimerGc(3000);
                futureTaskInstance = new FutureTask(timerGcInstance);
            } else if (postponedGcCount <= 5) {
                futureTaskInstance.cancel(true);
                if (futureTaskInstance.isCancelled()) {
                    lastGcDelayRequestTime = nowTime;
                    postponedGcCount++;
                    timerGcInstance = new TimerGc(3000);
                    futureTaskInstance = new FutureTask(timerGcInstance);
                    executor.execute(futureTaskInstance);
                }
            }
        }
    }

    static void forceBinderGc() {
        synchronized (delayGcMonitorObject) {
            if (futureTaskInstance != null) {
                long lastGcDelayRequestDuration = SystemClock.uptimeMillis() - lastGcDelayRequestTime;
                if (lastGcDelayRequestDuration < 3000) {
                    if (postponedGcCount != 0) {
                        return;
                    }
                    futureTaskInstance.cancel(true);
                    timerGcInstance = new TimerGc(3000 - lastGcDelayRequestDuration);
                    futureTaskInstance = new FutureTask(timerGcInstance);
                    postponedGcCount = 1;
                    executor.execute(futureTaskInstance);
                    return;
                }
            }
            forceGc("Binder");
        }
    }
}
