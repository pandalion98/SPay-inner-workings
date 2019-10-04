/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.RemoteException
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.samsung.android.spayfw.core.retry;

import android.os.RemoteException;
import com.samsung.android.spayfw.core.a.z;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class c {
    private static c mO;
    private static ArrayList<z> mP;

    static {
        mP = new ArrayList();
    }

    private c() {
    }

    public static void a(z z2) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            com.samsung.android.spayfw.b.c.d("ReplenishRetryRequester", "addInQueue: adding token in queue = " + z2.getTokenId());
            if (mP != null && !mP.contains((Object)z2)) {
                mP.add((Object)z2);
            }
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return;
        }
    }

    public static void b(z z2) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            com.samsung.android.spayfw.b.c.d("ReplenishRetryRequester", "scheduleTimer: scheduling replenish timer for token = " + z2.getTokenId());
            Timer timer = new Timer();
            c c2 = c.bl();
            c2.getClass();
            timer.schedule((TimerTask)c2.new a(z2), 120000L);
            // ** MonitorExit[var5_1] (shouldn't be in output)
            return;
        }
    }

    public static c bl() {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            if (mO == null) {
                mO = new c();
            }
            c c2 = mO;
            // ** MonitorExit[var2] (shouldn't be in output)
            return c2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void bm() {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            int n2 = mP.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                z z2 = (z)mP.get(0);
                com.samsung.android.spayfw.b.c.d("ReplenishRetryRequester", "flushReplenishQueue: trTokenId = " + z2.getTokenId());
                try {
                    z2.process();
                }
                catch (RemoteException remoteException) {
                    com.samsung.android.spayfw.b.c.c("ReplenishRetryRequester", remoteException.getMessage(), remoteException);
                }
                mP.remove((Object)z2);
                int n3 = mP.size();
                if (n3 < 1) break;
            }
            // ** MonitorExit[var7] (shouldn't be in output)
            return;
        }
    }

    public class a
    extends TimerTask {
        private z mQ;

        public a(z z2) {
            this.mQ = z2;
        }

        public void run() {
            com.samsung.android.spayfw.b.c.d("ReplenishRetryRequester", "run : ReplenishRetryTimerTask - tokenId = " + this.mQ.getTokenId());
            try {
                this.mQ.process();
                return;
            }
            catch (RemoteException remoteException) {
                com.samsung.android.spayfw.b.c.c("ReplenishRetryRequester", remoteException.getMessage(), remoteException);
                return;
            }
        }
    }

}

