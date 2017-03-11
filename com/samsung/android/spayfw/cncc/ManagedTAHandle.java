package com.samsung.android.spayfw.cncc;

import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytzsvc.api.TAController;
import java.util.Timer;
import java.util.TimerTask;

public class ManagedTAHandle {
    private static long ONE_MINUTE = 0;
    private static final String TAG = "ManagedTAHandle";
    private static long UNLOAD_TIMER_EXPIRY_TIME;
    TAController mTAController;
    boolean mTALoaded;
    TAUnLoadTimerTask mTAUnLoadTimerTask;
    Timer mTimer;

    class TAUnLoadTimerTask extends TimerTask {
        TAUnLoadTimerTask() {
        }

        public void run() {
            Log.m285d(ManagedTAHandle.TAG, "TimerExpired::run: unloading TA");
            ManagedTAHandle.this.unloadTA();
        }
    }

    static {
        ONE_MINUTE = 60000;
        UNLOAD_TIMER_EXPIRY_TIME = 5 * ONE_MINUTE;
    }

    ManagedTAHandle(TAController tAController) {
        this.mTimer = null;
        this.mTAUnLoadTimerTask = null;
        this.mTALoaded = false;
        this.mTAController = null;
        this.mTAController = tAController;
    }

    public void setTimeoutForUnload(long j) {
        UNLOAD_TIMER_EXPIRY_TIME = ONE_MINUTE * j;
    }

    public void loadTA() {
        Log.m285d(TAG, "Inside loadTA");
        try {
            if (this.mTALoaded) {
                Log.m285d(TAG, "Just reset unload timer. TA is already loaded");
                this.mTimer.cancel();
                this.mTimer = new Timer();
                this.mTAUnLoadTimerTask = new TAUnLoadTimerTask();
                this.mTimer.schedule(this.mTAUnLoadTimerTask, UNLOAD_TIMER_EXPIRY_TIME);
                return;
            }
            Log.m285d(TAG, "Do Load TA");
            this.mTAController.loadTA();
            this.mTimer = new Timer();
            this.mTAUnLoadTimerTask = new TAUnLoadTimerTask();
            this.mTimer.schedule(this.mTAUnLoadTimerTask, UNLOAD_TIMER_EXPIRY_TIME);
            this.mTALoaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unloadTA() {
        Log.m285d(TAG, "Inside unloadTA");
        this.mTAController.unloadTA();
        this.mTimer.cancel();
        this.mTimer = null;
        this.mTAUnLoadTimerTask = null;
        this.mTALoaded = false;
    }
}
