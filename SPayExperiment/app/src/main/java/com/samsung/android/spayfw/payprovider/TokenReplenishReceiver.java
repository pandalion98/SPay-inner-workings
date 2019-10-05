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

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a;

public class TokenReplenishReceiver
extends BroadcastReceiver {
    public static final void enable() {
        PaymentFrameworkApp.b(TokenReplenishReceiver.class);
        Log.d("TokenReplenishReceiver", "TokenReplenishReceiver is enabled");
    }

    /*
     * Enabled aggressive block sorting
     */
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.e("TokenReplenishReceiver", "On Receive: intent or tokenId is null");
            return;
        }
        if (!"com.samsung.android.spayfw.ACTION_REPLENISH_ALARM_EXECUTE".equals((Object)intent.getAction())) return;
        {
            String string = intent.getStringExtra("TrTokenId");
            Log.d("TokenReplenishReceiver", "On Receive: ACTION_REPLENISH_ALARM_EXECUTE: TrTokenId " + string);
            a a2 = a.a(context, null);
            if (a2 == null) {
                Log.e("TokenReplenishReceiver", "Account is NULL");
                return;
            }
            com.samsung.android.spayfw.core.c c2 = a2.r(string);
            if (c2 != null) {
                c2.ad().replenishAlarmExpired();
                return;
            }
        }
        Log.e("TokenReplenishReceiver", "Card not found");
    }
}

