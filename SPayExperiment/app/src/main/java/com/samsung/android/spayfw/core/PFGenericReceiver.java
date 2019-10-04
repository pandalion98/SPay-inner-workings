/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Process
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package com.samsung.android.spayfw.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.core.FactoryResetDetector;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a.q;
import com.samsung.android.spaytui.SpayTuiTAController;

public class PFGenericReceiver
extends BroadcastReceiver {
    public static final void enable() {
        PaymentFrameworkApp.b(PFGenericReceiver.class);
        c.d("PFGenericReceiver", "PFGenericReceiver is enabled");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public void onReceive(Context context, Intent intent) {
        c.d("PFGenericReceiver", "Intent : " + (Object)intent);
        if (intent == null || !"android.intent.action.DEVICE_INTEGRITY_VERIFICATION_FAIL".equals((Object)intent.getAction())) return;
        c.i("PFGenericReceiver", "ACTION_DEVICE_INTEGRITY_VERIFICATION_FAIL");
        try {
            SpayTuiTAController.getInstance().deletePin();
            q.s(context);
            q.t(context);
            FactoryResetDetector.ah();
            FactoryResetDetector.disable();
            SharedPreferences sharedPreferences = context.getSharedPreferences("CONFIG_RESET_REASON", 0);
            String string = "resetCode=DEVICE_INTEGRITY_COMPROMISED;timestamp=" + System.currentTimeMillis() + ";";
            c.d("PFGenericReceiver", "ResetReason:" + string);
            sharedPreferences.edit().putString("CONFIG_RESET_REASON", string).commit();
            Process.killProcess((int)Process.myPid());
            return;
        }
        catch (Exception exception) {
            c.c("PFGenericReceiver", exception.getMessage(), exception);
            return;
        }
    }
}

