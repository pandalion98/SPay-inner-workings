/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.AlarmManager
 *  android.app.PendingIntent
 *  android.app.job.JobInfo
 *  android.app.job.JobInfo$Builder
 *  android.app.job.JobScheduler
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.util.Random
 */
package com.samsung.contextservice.system;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.samsung.contextservice.a;
import com.samsung.contextservice.exception.InitializationException;
import com.samsung.contextservice.server.TaskSchedulerService;
import com.samsung.contextservice.server.e;
import com.samsung.contextservice.server.j;
import com.samsung.contextservice.system.b;
import com.samsung.contextservice.system.c;
import java.util.Random;

public class ManagerHub {
    private static BroadcastReceiver HA;
    private static c Hd;
    private static State Hw;
    private static com.samsung.contextservice.system.a Hx;
    private static e Hy;
    private static Handler Hz;
    public static String TAG;
    private static Context sContext;

    static {
        TAG = "ManagerHub";
        Hw = ManagerHub.a(State.HD);
        Hx = null;
        Hy = null;
        Hd = null;
        Hz = null;
        HA = null;
    }

    public static a Z(int n2) {
        switch (n2) {
            default: {
                return null;
            }
            case 1: {
                if (Hx == null) {
                    Hx = com.samsung.contextservice.system.a.aA(ManagerHub.getContext());
                }
                return Hx;
            }
            case 2: {
                if (Hy == null) {
                    Hy = e.ax(ManagerHub.getContext());
                }
                return Hy;
            }
            case 3: 
        }
        if (Hd == null) {
            Hd = c.aF(ManagerHub.getContext());
        }
        return Hd;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static State a(State state) {
        Class<ManagerHub> class_ = ManagerHub.class;
        synchronized (ManagerHub.class) {
            if (state == null) return Hw;
            Hw = state;
            return Hw;
        }
    }

    public static void aE(Context context) {
        block6 : {
            com.samsung.contextservice.b.b.d(TAG, "onCreate()");
            if (Hw != State.HD) {
                com.samsung.contextservice.b.b.e(TAG, "cannot start context managers, state is " + Hw.toString());
                return;
            }
            if (context == null) {
                throw new InitializationException("context is null");
            }
            com.samsung.contextservice.b.b.i(TAG, "Starting context managers");
            sContext = context.getApplicationContext();
            HandlerThread handlerThread = new HandlerThread("ManagerHub", 0);
            handlerThread.start();
            try {
                Hz = new Handler(handlerThread.getLooper());
                ManagerHub.gK();
                j.az(context);
                Hx = com.samsung.contextservice.system.a.aA(sContext);
                if (Hx == null) {
                    com.samsung.contextservice.b.b.e(TAG, "cannot initialize  client manager");
                    ManagerHub.onDestroy();
                }
                if ((Hy = e.ax(sContext)) == null) {
                    com.samsung.contextservice.b.b.e(TAG, "cannot initialize  server manager");
                    ManagerHub.onDestroy();
                }
                if ((Hd = c.aF(sContext)) != null) break block6;
                com.samsung.contextservice.b.b.e(TAG, "cannot initialize  verdict manager");
                ManagerHub.onDestroy();
            }
            catch (Exception exception) {
                exception.printStackTrace();
                throw new InitializationException("Cannot initialize context service");
            }
        }
        ManagerHub.a(State.HB);
        com.samsung.contextservice.b.b.i(TAG, "Context service created");
        ManagerHub.gH();
    }

    private static void gH() {
        com.samsung.contextservice.b.b.d(TAG, "onStartPending()");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.samsung.android.action.START_SERVICE");
        HA = new BroadcastReceiver(){

            public void onReceive(Context context, Intent intent) {
                ManagerHub.onStart();
            }
        };
        sContext.registerReceiver(HA, intentFilter, "com.samsung.android.spayfw.permission.ACCESS_PF", null);
        Hz.postDelayed(new Runnable(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void run() {
                try {
                    SharedPreferences sharedPreferences = sContext.getSharedPreferences("context_settings", 0);
                    long l2 = sharedPreferences.getLong("randomtime", -1L);
                    if (l2 != 0L) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong("randomtime", 0L);
                        editor.commit();
                        com.samsung.contextservice.b.b.d(ManagerHub.TAG, "Reset random time");
                    }
                    if (l2 < 0L) {
                        l2 = new Random().nextInt(86400001);
                    }
                    Intent intent = new Intent();
                    intent.setAction("com.samsung.android.action.START_SERVICE");
                    PendingIntent pendingIntent = PendingIntent.getBroadcast((Context)sContext, (int)0, (Intent)intent, (int)134217728);
                    AlarmManager alarmManager = (AlarmManager)sContext.getSystemService("alarm");
                    if (!com.samsung.contextservice.b.c.HK) {
                        l2 = 5000L;
                    }
                    alarmManager.set(0, l2 + System.currentTimeMillis(), pendingIntent);
                    com.samsung.contextservice.b.b.d(ManagerHub.TAG, "JWT token is " + com.samsung.contextservice.b.e.aJ(sContext));
                    com.samsung.contextservice.b.b.d(ManagerHub.TAG, "Set context start alarm delay to " + l2 / 1000L + " secs");
                    return;
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                    return;
                }
            }
        }, 30000L);
    }

    public static State gI() {
        return Hw;
    }

    private static void gJ() {
        com.samsung.contextservice.b.b.d(TAG, "initialize schedulers");
        JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(sContext, TaskSchedulerService.class)).setRequiredNetworkType(1).setPeriodic(86400000L).build();
        JobInfo jobInfo2 = new JobInfo.Builder(2, new ComponentName(sContext, TaskSchedulerService.class)).setRequiredNetworkType(1).setPeriodic(86400000L).build();
        JobScheduler jobScheduler = (JobScheduler)sContext.getSystemService("jobscheduler");
        jobScheduler.schedule(jobInfo);
        jobScheduler.schedule(jobInfo2);
    }

    private static void gK() {
        com.samsung.contextservice.b.b.d(TAG, "destroy schedulers");
        JobScheduler jobScheduler = (JobScheduler)sContext.getSystemService("jobscheduler");
        jobScheduler.cancel(1);
        jobScheduler.cancel(2);
    }

    public static Context getContext() {
        return sContext;
    }

    public static void onDestroy() {
        com.samsung.contextservice.b.b.d(TAG, "onDestroy()");
        ManagerHub.a(State.HD);
        if (HA != null) {
            sContext.unregisterReceiver(HA);
            HA = null;
        }
        b.aD(ManagerHub.getContext());
    }

    public static void onStart() {
        com.samsung.contextservice.b.b.d(TAG, "onStart()");
        Hz.post(new Runnable(){

            public void run() {
                try {
                    com.samsung.contextservice.b.b.d(ManagerHub.TAG, "JWT token is " + com.samsung.contextservice.b.e.aJ(sContext));
                    ManagerHub.a(State.HC);
                    ManagerHub.gJ();
                    Thread.sleep((long)30000L);
                    b.aC(ManagerHub.getContext());
                    return;
                }
                catch (Exception exception) {
                    com.samsung.contextservice.b.b.d(ManagerHub.TAG, "cannot start ctx");
                    return;
                }
            }
        });
    }

    public static final class State
    extends Enum<State> {
        public static final /* enum */ State HB = new State();
        public static final /* enum */ State HC = new State();
        public static final /* enum */ State HD = new State();
        private static final /* synthetic */ State[] HE;

        static {
            State[] arrstate = new State[]{HB, HC, HD};
            HE = arrstate;
        }

        public static State valueOf(String string) {
            return (State)Enum.valueOf(State.class, (String)string);
        }

        public static State[] values() {
            return (State[])HE.clone();
        }
    }

}

