/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  java.lang.Class
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager;
import java.util.Iterator;
import java.util.List;

public class McTdsTimerUtil {
    private static final String TAG = "McTdsTimerUtil";
    private static final int sDelay = 3600000;
    private static final int sinitDelay = 180000;
    private static Handler tdsHandler;
    private static HandlerThread tdsHandlerThread;
    private static final Runnable tdsRunnable;

    static {
        tdsHandlerThread = new HandlerThread("tdsHandlerThread");
        tdsRunnable = new Runnable(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void run() {
                c.d(McTdsTimerUtil.TAG, "tdsRegistration task started");
                List<Long> list = McTdsManager.getRegList();
                if (list == null || list.isEmpty()) {
                    McTdsTimerUtil.stopTdsTimer();
                    return;
                }
                Iterator iterator = list.iterator();
                do {
                    if (!iterator.hasNext()) {
                        Class<McTdsTimerUtil> class_ = McTdsTimerUtil.class;
                        synchronized (McTdsTimerUtil.class) {
                            tdsHandler.postDelayed((Runnable)this, 3600000L);
                            // ** MonitorExit[var6_4] (shouldn't be in output)
                            return;
                        }
                    }
                    Long l2 = (Long)iterator.next();
                    if (l2 == null) continue;
                    McTdsManager.getInstance(l2).reRegisterIfNeeded(null);
                } while (true);
            }
        };
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void startTdsTimer() {
        Class<McTdsTimerUtil> class_ = McTdsTimerUtil.class;
        synchronized (McTdsTimerUtil.class) {
            if (!tdsHandlerThread.isAlive()) {
                tdsHandlerThread.start();
                c.d(TAG, "tdsHandlerThread start:");
                Looper looper = tdsHandlerThread.getLooper();
                if (looper == null) {
                    c.e(TAG, "failed to obtain tdsthread looper");
                } else {
                    tdsHandler = new Handler(looper);
                    tdsHandler.postDelayed(tdsRunnable, 180000L);
                }
            } else {
                c.d(TAG, "thread already running");
            }
            // ** MonitorExit[var3] (shouldn't be in output)
            return;
        }
    }

    public static void stopTdsTimer() {
        Class<McTdsTimerUtil> class_ = McTdsTimerUtil.class;
        synchronized (McTdsTimerUtil.class) {
            if (tdsHandler != null) {
                c.d(TAG, "tdsTimer clearing task");
                tdsHandler.removeCallbacks(tdsRunnable);
                if (tdsHandlerThread != null && tdsHandlerThread.isAlive()) {
                    c.d(TAG, "tdsHandlerThread end:");
                    tdsHandlerThread.quit();
                }
            }
            // ** MonitorExit[var2] (shouldn't be in output)
            return;
        }
    }

}

