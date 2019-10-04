/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.fraud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.fraud.a;

public class FraudReceiver
extends BroadcastReceiver {
    public static final void enable() {
        PaymentFrameworkApp.b(FraudReceiver.class);
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            c.e("FraudReceiver", "intent is null");
            return;
        } else {
            c.d("FraudReceiver", "FraudReciever: " + intent.getAction());
            if ("android.intent.action.PACKAGE_DATA_CLEARED".equals((Object)intent.getAction())) {
                String string;
                Uri uri = intent.getData();
                if (uri == null || !"com.samsung.android.spay".equals((Object)(string = uri.getSchemeSpecificPart()))) return;
                {
                    c.d("FraudReceiver", "Fraud: add an [" + string + "] reset record");
                    a a2 = a.x(context);
                    if (a2 != null) {
                        a2.W("app_reset");
                    } else {
                        c.d("FraudReceiver", "addFDeviceRecord: cannot get data");
                    }
                    context.sendBroadcast(new Intent("com.samsung.android.spay.action.REQUEST_RESET"));
                    c.i("FraudReceiver", "Sent App Reset Broadcast");
                    return;
                }
            } else {
                if (!"android.intent.action.SIM_STATE_CHANGED".equals((Object)intent.getAction())) return;
                {
                    String string = intent.getStringExtra("ss");
                    c.d("FraudReceiver", "FraudReceiver: Sim State = " + string);
                    a a3 = a.x(context);
                    if ("ABSENT".equals((Object)string) && a3 != null) {
                        a3.W("remove_sim");
                        return;
                    }
                    if (!"LOADED".equals((Object)string) || a3 == null) return;
                    {
                        a3.W("add_sim");
                        return;
                    }
                }
            }
        }
    }
}

