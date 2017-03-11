package com.samsung.android.spayfw.payprovider.mastercard.tds.network;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager;
import java.util.List;

public class McTdsTimerUtil {
    private static final String TAG = "McTdsTimerUtil";
    private static final int sDelay = 3600000;
    private static final int sinitDelay = 180000;
    private static Handler tdsHandler;
    private static HandlerThread tdsHandlerThread;
    private static final Runnable tdsRunnable;

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsTimerUtil.1 */
    static class C05691 implements Runnable {
        C05691() {
        }

        public void run() {
            Log.m285d(McTdsTimerUtil.TAG, "tdsRegistration task started");
            List<Long> regList = McTdsManager.getRegList();
            if (regList == null || regList.isEmpty()) {
                McTdsTimerUtil.stopTdsTimer();
                return;
            }
            for (Long l : regList) {
                if (l != null) {
                    McTdsManager.getInstance(l.longValue()).reRegisterIfNeeded(null);
                }
            }
            synchronized (McTdsTimerUtil.class) {
                McTdsTimerUtil.tdsHandler.postDelayed(this, 3600000);
            }
        }
    }

    static {
        tdsHandlerThread = new HandlerThread("tdsHandlerThread");
        tdsRunnable = new C05691();
    }

    public static synchronized void startTdsTimer() {
        synchronized (McTdsTimerUtil.class) {
            if (tdsHandlerThread.isAlive()) {
                Log.m285d(TAG, "thread already running");
            } else {
                tdsHandlerThread.start();
                Log.m285d(TAG, "tdsHandlerThread start:");
                Looper looper = tdsHandlerThread.getLooper();
                if (looper == null) {
                    Log.m286e(TAG, "failed to obtain tdsthread looper");
                } else {
                    tdsHandler = new Handler(looper);
                    tdsHandler.postDelayed(tdsRunnable, 180000);
                }
            }
        }
    }

    public static synchronized void stopTdsTimer() {
        synchronized (McTdsTimerUtil.class) {
            if (tdsHandler != null) {
                Log.m285d(TAG, "tdsTimer clearing task");
                tdsHandler.removeCallbacks(tdsRunnable);
                if (tdsHandlerThread != null && tdsHandlerThread.isAlive()) {
                    Log.m285d(TAG, "tdsHandlerThread end:");
                    tdsHandlerThread.quit();
                }
            }
        }
    }
}
