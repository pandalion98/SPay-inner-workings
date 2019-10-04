/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.AlarmManager
 *  android.app.PendingIntent
 *  android.content.Context
 *  android.content.Intent
 *  android.os.SystemClock
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.payprovider.TokenReplenishReceiver;
import com.samsung.android.spayfw.payprovider.f;

public class h {
    public static void a(final Context context, final long l2, final f f2) {
        PaymentFrameworkApp.az().post(new Runnable(){

            public void run() {
                c.i("SPAYFW:TokenReplenishAlarm", "setAlarm");
                AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
                Intent intent = new Intent(context, TokenReplenishReceiver.class);
                intent.setAction("com.samsung.android.spayfw.ACTION_REPLENISH_ALARM_EXECUTE");
                intent.setType(f2.getTrTokenId());
                intent.putExtra("TrTokenId", f2.getTrTokenId());
                PendingIntent pendingIntent = PendingIntent.getBroadcast((Context)context, (int)0, (Intent)intent, (int)0);
                long l22 = l2 - com.samsung.android.spayfw.utils.h.am(context);
                c.d("SPAYFW:TokenReplenishAlarm", "setAlarm : " + l22);
                alarmManager.set(2, l22 + SystemClock.elapsedRealtime(), pendingIntent);
            }
        });
    }

    public static void a(Context context, f f2) {
        c.i("SPAYFW:TokenReplenishAlarm", "cancelAlarm");
        AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
        Intent intent = new Intent(context, TokenReplenishReceiver.class);
        intent.setAction("com.samsung.android.spayfw.ACTION_REPLENISH_ALARM_EXECUTE");
        intent.setType(f2.getTrTokenId());
        alarmManager.cancel(PendingIntent.getBroadcast((Context)context, (int)0, (Intent)intent, (int)0));
    }

}

