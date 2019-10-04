/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.samsung.android.spayfw.core.hce;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import java.util.Timer;
import java.util.TimerTask;

public class SPayHCEReceiver
extends BroadcastReceiver {
    private static boolean kh = false;
    private static Timer kj = null;

    public static boolean aR() {
        return kh;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void aS() {
        c.d("SPayHCEReceiver", "RF_FIELD_OFF_DETECTED is reset");
        Class<SPayHCEReceiver> class_ = SPayHCEReceiver.class;
        synchronized (SPayHCEReceiver.class) {
            kh = false;
            if (kj != null) {
                kj.cancel();
                kj = null;
            }
            // ** MonitorExit[var1] (shouldn't be in output)
            return;
        }
    }

    public static final void disable() {
        PaymentFrameworkApp.a(SPayHCEReceiver.class);
        c.d("SPayHCEReceiver", "SPayHCEReceiver is disabled");
    }

    public static final void enable() {
        PaymentFrameworkApp.b(SPayHCEReceiver.class);
        c.d("SPayHCEReceiver", "SPayHCEReceiver is enabled");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void onReceive(Context context, Intent intent) {
        String string = null;
        if (intent != null) {
            string = intent.getAction();
        }
        if ("com.android.nfc_extras.action.RF_FIELD_ON_DETECTED".equals(string)) {
            c.d("SPayHCEReceiver", "RF_FIELD_ON_DETECTED is detected");
            TimerTask timerTask = new TimerTask(){

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                public void run() {
                    c.d("SPayHCEReceiver", "reset Rd detector");
                    Class<SPayHCEReceiver> class_ = SPayHCEReceiver.class;
                    synchronized (SPayHCEReceiver.class) {
                        kh = false;
                        // ** MonitorExit[var3_1] (shouldn't be in output)
                        return;
                    }
                }
            };
            Class<SPayHCEReceiver> class_ = SPayHCEReceiver.class;
            synchronized (SPayHCEReceiver.class) {
                kh = true;
                if (kj != null) {
                    kj.cancel();
                    kj = null;
                }
                kj = new Timer();
                kj.schedule(timerTask, 5000L);
                // ** MonitorExit[var6_5] (shouldn't be in output)
                return;
            }
        } else {
            if (!"com.android.nfc_extras.action.RF_FIELD_OFF_DETECTED".equals((Object)string)) return;
            {
                c.d("SPayHCEReceiver", "RF_FIELD_OFF_DETECTED is detected");
                return;
            }
        }
    }

}

