/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.AlarmManager
 *  android.app.PendingIntent
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.util.Calendar
 */
package com.samsung.sensorframework.sdi.a;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sdi.c.c;
import java.util.Calendar;

public class a
extends BroadcastReceiver {
    private static Calendar a(int n2, int n3, int n4) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(11, n2);
        calendar.set(12, n3);
        calendar.set(13, n4);
        calendar.add(5, 1);
        return calendar;
    }

    public static void a(Context context, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
        Calendar calendar = a.a(com.samsung.sensorframework.sdi.g.a.aA(9), com.samsung.sensorframework.sdi.g.a.aA(60), com.samsung.sensorframework.sdi.g.a.aA(60));
        alarmManager.set(1, calendar.getTimeInMillis(), pendingIntent);
        Log.d("DailySensingAlarm", "Alarm set to: " + com.samsung.sensorframework.sda.f.a.G(calendar.getTimeInMillis()));
    }

    public static void b(Context context, PendingIntent pendingIntent) {
        Log.d("DailySensingAlarm", "cancelDailyAlarm()");
        if (pendingIntent != null) {
            ((AlarmManager)context.getSystemService("alarm")).cancel(pendingIntent);
        }
    }

    public void onReceive(final Context context, final Intent intent) {
        new Thread(){

            public void run() {
                Log.d("DailySensingAlarm", "onReceive() intent action: " + intent.getAction());
                if (c.br(context) != null) {
                    c.br(context).b(intent);
                    return;
                }
                Log.e("DailySensingAlarm", "SensingController.getInstance() returned null");
            }
        }.start();
    }

}

