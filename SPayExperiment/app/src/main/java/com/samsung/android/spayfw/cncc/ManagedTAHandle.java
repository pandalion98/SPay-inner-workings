/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.samsung.android.spayfw.cncc;

import com.samsung.android.spayfw.b.c;
import com.samsung.android.spaytzsvc.api.TAController;
import java.util.Timer;
import java.util.TimerTask;

public class ManagedTAHandle {
    private static long ONE_MINUTE = 0L;
    private static final String TAG = "ManagedTAHandle";
    private static long UNLOAD_TIMER_EXPIRY_TIME;
    TAController mTAController = null;
    boolean mTALoaded = false;
    TAUnLoadTimerTask mTAUnLoadTimerTask = null;
    Timer mTimer = null;

    static {
        ONE_MINUTE = 60000L;
        UNLOAD_TIMER_EXPIRY_TIME = 5L * ONE_MINUTE;
    }

    ManagedTAHandle(TAController tAController) {
        this.mTAController = tAController;
    }

    private void unloadTA() {
        c.d(TAG, "Inside unloadTA");
        this.mTAController.unloadTA();
        this.mTimer.cancel();
        this.mTimer = null;
        this.mTAUnLoadTimerTask = null;
        this.mTALoaded = false;
    }

    public void loadTA() {
        c.d(TAG, "Inside loadTA");
        try {
            if (!this.mTALoaded) {
                c.d(TAG, "Do Load TA");
                this.mTAController.loadTA();
                this.mTimer = new Timer();
                this.mTAUnLoadTimerTask = new TAUnLoadTimerTask();
                this.mTimer.schedule((TimerTask)this.mTAUnLoadTimerTask, UNLOAD_TIMER_EXPIRY_TIME);
                this.mTALoaded = true;
                return;
            }
            c.d(TAG, "Just reset unload timer. TA is already loaded");
            this.mTimer.cancel();
            this.mTimer = new Timer();
            this.mTAUnLoadTimerTask = new TAUnLoadTimerTask();
            this.mTimer.schedule((TimerTask)this.mTAUnLoadTimerTask, UNLOAD_TIMER_EXPIRY_TIME);
            return;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

    public void setTimeoutForUnload(long l2) {
        UNLOAD_TIMER_EXPIRY_TIME = l2 * ONE_MINUTE;
    }

    class TAUnLoadTimerTask
    extends TimerTask {
        TAUnLoadTimerTask() {
        }

        public void run() {
            c.d(ManagedTAHandle.TAG, "TimerExpired::run: unloading TA");
            ManagedTAHandle.this.unloadTA();
        }
    }

}

