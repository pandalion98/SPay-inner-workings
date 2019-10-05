/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.os.Message
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.samsung.android.spayfw.core.retry;

import android.os.Message;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.i;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class a {
    private static a mL;
    private static List<Message> mM;
    private i jB = PaymentFrameworkApp.az();
    private Timer lH;

    static {
        mM = new ArrayList();
    }

    private a() {
    }

    public static a bj() {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            if (mL == null) {
                mL = new a();
            }
            a a2 = mL;
            // ** MonitorExit[var2] (shouldn't be in output)
            return a2;
        }
    }

    public void b(Message message) {
        a a2 = this;
        synchronized (a2) {
            Log.d("JwtRetryRequester", "addInQueue: adding msg in queue = " + message.toString());
            if (mM != null && !mM.contains((Object)message)) {
                mM.add((Object)message);
                if (this.lH == null) {
                    this.lH = new Timer();
                    this.lH.schedule((TimerTask)new a(), 60000L);
                }
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void bk() {
        a a2 = this;
        synchronized (a2) {
            Log.d("JwtRetryRequester", "flushJwtQueue");
            if (mM != null) {
                int n2 = mM.size();
                for (int i2 = 0; i2 < n2; ++i2) {
                    Message message = (Message)mM.get(0);
                    Log.d("JwtRetryRequester", "flushJwtQueue: msg number = " + i2);
                    this.jB.a(message);
                    mM.remove((Object)message);
                    if (mM.size() < 1) break;
                }
            }
            if (this.lH != null) {
                this.lH.cancel();
                this.lH = null;
            }
            return;
        }
    }

    public class a
    extends TimerTask {
        public void run() {
            Log.d("JwtRetryRequester", "run : JwtRetryTimerTask ");
            a.this.bk();
        }
    }

}

