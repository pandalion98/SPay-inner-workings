package com.samsung.android.spayfw.core.hce;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.p002b.Log;
import java.util.Timer;
import java.util.TimerTask;

public class SPayHCEReceiver extends BroadcastReceiver {
    private static boolean kh;
    private static Timer kj;

    /* renamed from: com.samsung.android.spayfw.core.hce.SPayHCEReceiver.1 */
    class C04141 extends TimerTask {
        final /* synthetic */ SPayHCEReceiver kk;

        C04141(SPayHCEReceiver sPayHCEReceiver) {
            this.kk = sPayHCEReceiver;
        }

        public void run() {
            Log.m285d("SPayHCEReceiver", "reset Rd detector");
            synchronized (SPayHCEReceiver.class) {
                SPayHCEReceiver.kh = false;
            }
        }
    }

    static {
        kh = false;
        kj = null;
    }

    public void onReceive(Context context, Intent intent) {
        Object obj = null;
        if (intent != null) {
            obj = intent.getAction();
        }
        if ("com.android.nfc_extras.action.RF_FIELD_ON_DETECTED".equals(obj)) {
            Log.m285d("SPayHCEReceiver", "RF_FIELD_ON_DETECTED is detected");
            TimerTask c04141 = new C04141(this);
            synchronized (SPayHCEReceiver.class) {
                kh = true;
                if (kj != null) {
                    kj.cancel();
                    kj = null;
                }
                kj = new Timer();
                kj.schedule(c04141, 5000);
            }
        } else if ("com.android.nfc_extras.action.RF_FIELD_OFF_DETECTED".equals(obj)) {
            Log.m285d("SPayHCEReceiver", "RF_FIELD_OFF_DETECTED is detected");
        }
    }

    public static final void enable() {
        PaymentFrameworkApp.m318b(SPayHCEReceiver.class);
        Log.m285d("SPayHCEReceiver", "SPayHCEReceiver is enabled");
    }

    public static final void disable() {
        PaymentFrameworkApp.m317a(SPayHCEReceiver.class);
        Log.m285d("SPayHCEReceiver", "SPayHCEReceiver is disabled");
    }

    public static boolean aR() {
        return kh;
    }

    public static void aS() {
        Log.m285d("SPayHCEReceiver", "RF_FIELD_OFF_DETECTED is reset");
        synchronized (SPayHCEReceiver.class) {
            kh = false;
            if (kj != null) {
                kj.cancel();
                kj = null;
            }
        }
    }
}
