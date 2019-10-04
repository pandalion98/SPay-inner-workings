/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;

public class TokenReplenishReceiver
extends BroadcastReceiver {
    public static final void enable() {
        PaymentFrameworkApp.b(TokenReplenishReceiver.class);
        c.d("TokenReplenishReceiver", "TokenReplenishReceiver is enabled");
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            c.e("TokenReplenishReceiver", "On Receive: intent or tokenId is null");
            return;
        }
        if (!"com.samsung.android.spayfw.ACTION_REPLENISH_ALARM_EXECUTE".equals((Object)intent.getAction())) return;
        {
            String string = intent.getStringExtra("TrTokenId");
            c.d("TokenReplenishReceiver", "On Receive: ACTION_REPLENISH_ALARM_EXECUTE: TrTokenId " + string);
            a a2 = a.a(context, null);
            if (a2 == null) {
                c.e("TokenReplenishReceiver", "Account is NULL");
                return;
            }
            com.samsung.android.spayfw.core.c c2 = a2.r(string);
            if (c2 != null) {
                c2.ad().replenishAlarmExpired();
                return;
            }
        }
        c.e("TokenReplenishReceiver", "Card not found");
    }
}

