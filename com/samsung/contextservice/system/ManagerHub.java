package com.samsung.contextservice.system;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobInfo.Builder;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.HandlerThread;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.contextservice.Manager;
import com.samsung.contextservice.exception.InitializationException;
import com.samsung.contextservice.p029b.CSlog;
import com.samsung.contextservice.p029b.Config;
import com.samsung.contextservice.p029b.Utils;
import com.samsung.contextservice.server.ContextServerManager;
import com.samsung.contextservice.server.ServerConfig;
import com.samsung.contextservice.server.TaskSchedulerService;
import java.util.Random;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public class ManagerHub {
    private static BroadcastReceiver HA;
    private static VerdictManager Hd;
    private static State Hw;
    private static ContextClientManager Hx;
    private static ContextServerManager Hy;
    private static Handler Hz;
    public static String TAG;
    private static Context sContext;

    /* renamed from: com.samsung.contextservice.system.ManagerHub.1 */
    static class C06181 extends BroadcastReceiver {
        C06181() {
        }

        public void onReceive(Context context, Intent intent) {
            ManagerHub.onStart();
        }
    }

    /* renamed from: com.samsung.contextservice.system.ManagerHub.2 */
    static class C06192 implements Runnable {
        C06192() {
        }

        public void run() {
            try {
                SharedPreferences sharedPreferences = ManagerHub.sContext.getSharedPreferences("context_settings", 0);
                long j = sharedPreferences.getLong("randomtime", -1);
                if (j != 0) {
                    Editor edit = sharedPreferences.edit();
                    edit.putLong("randomtime", 0);
                    edit.commit();
                    CSlog.m1408d(ManagerHub.TAG, "Reset random time");
                }
                if (j < 0) {
                    j = (long) new Random().nextInt(86400001);
                }
                Intent intent = new Intent();
                intent.setAction("com.samsung.android.action.START_SERVICE");
                PendingIntent broadcast = PendingIntent.getBroadcast(ManagerHub.sContext, 0, intent, 134217728);
                AlarmManager alarmManager = (AlarmManager) ManagerHub.sContext.getSystemService("alarm");
                if (!Config.HK) {
                    j = 5000;
                }
                alarmManager.set(0, System.currentTimeMillis() + j, broadcast);
                CSlog.m1408d(ManagerHub.TAG, "JWT token is " + Utils.aJ(ManagerHub.sContext));
                CSlog.m1408d(ManagerHub.TAG, "Set context start alarm delay to " + (j / 1000) + " secs");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* renamed from: com.samsung.contextservice.system.ManagerHub.3 */
    static class C06203 implements Runnable {
        C06203() {
        }

        public void run() {
            try {
                CSlog.m1408d(ManagerHub.TAG, "JWT token is " + Utils.aJ(ManagerHub.sContext));
                ManagerHub.m1473a(State.RUNNING);
                ManagerHub.gJ();
                Thread.sleep(PaymentNetworkProvider.NFC_WAIT_TIME);
                ContextSFManager.aC(ManagerHub.getContext());
            } catch (Exception e) {
                CSlog.m1408d(ManagerHub.TAG, "cannot start ctx");
            }
        }
    }

    public enum State {
        CREATED,
        RUNNING,
        STOPPED
    }

    static {
        TAG = "ManagerHub";
        Hw = m1473a(State.STOPPED);
        Hx = null;
        Hy = null;
        Hd = null;
        Hz = null;
        HA = null;
    }

    private static synchronized State m1473a(State state) {
        State state2;
        synchronized (ManagerHub.class) {
            if (state != null) {
                state2 = Hw;
                Hw = state;
            }
            state2 = Hw;
        }
        return state2;
    }

    public static void aE(Context context) {
        CSlog.m1408d(TAG, "onCreate()");
        if (Hw != State.STOPPED) {
            CSlog.m1409e(TAG, "cannot start context managers, state is " + Hw.toString());
        } else if (context == null) {
            throw new InitializationException("context is null");
        } else {
            CSlog.m1410i(TAG, "Starting context managers");
            sContext = context.getApplicationContext();
            HandlerThread handlerThread = new HandlerThread("ManagerHub", 0);
            handlerThread.start();
            try {
                Hz = new Handler(handlerThread.getLooper());
                gK();
                ServerConfig.az(context);
                Hx = ContextClientManager.aA(sContext);
                if (Hx == null) {
                    CSlog.m1409e(TAG, "cannot initialize  client manager");
                    onDestroy();
                }
                Hy = ContextServerManager.ax(sContext);
                if (Hy == null) {
                    CSlog.m1409e(TAG, "cannot initialize  server manager");
                    onDestroy();
                }
                Hd = VerdictManager.aF(sContext);
                if (Hd == null) {
                    CSlog.m1409e(TAG, "cannot initialize  verdict manager");
                    onDestroy();
                }
                m1473a(State.CREATED);
                CSlog.m1410i(TAG, "Context service created");
                gH();
            } catch (Exception e) {
                e.printStackTrace();
                throw new InitializationException("Cannot initialize context service");
            }
        }
    }

    private static void gH() {
        CSlog.m1408d(TAG, "onStartPending()");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.samsung.android.action.START_SERVICE");
        HA = new C06181();
        sContext.registerReceiver(HA, intentFilter, "com.samsung.android.spayfw.permission.ACCESS_PF", null);
        Hz.postDelayed(new C06192(), PaymentNetworkProvider.NFC_WAIT_TIME);
    }

    public static void onStart() {
        CSlog.m1408d(TAG, "onStart()");
        Hz.post(new C06203());
    }

    public static void onDestroy() {
        CSlog.m1408d(TAG, "onDestroy()");
        m1473a(State.STOPPED);
        if (HA != null) {
            sContext.unregisterReceiver(HA);
            HA = null;
        }
        ContextSFManager.aD(getContext());
    }

    public static State gI() {
        return Hw;
    }

    public static Context getContext() {
        return sContext;
    }

    public static Manager m1472Z(int i) {
        switch (i) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                if (Hx == null) {
                    Hx = ContextClientManager.aA(getContext());
                }
                return Hx;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                if (Hy == null) {
                    Hy = ContextServerManager.ax(getContext());
                }
                return Hy;
            case F2m.PPB /*3*/:
                if (Hd == null) {
                    Hd = VerdictManager.aF(getContext());
                }
                return Hd;
            default:
                return null;
        }
    }

    private static void gJ() {
        CSlog.m1408d(TAG, "initialize schedulers");
        JobInfo build = new Builder(1, new ComponentName(sContext, TaskSchedulerService.class)).setRequiredNetworkType(1).setPeriodic(86400000).build();
        JobInfo build2 = new Builder(2, new ComponentName(sContext, TaskSchedulerService.class)).setRequiredNetworkType(1).setPeriodic(86400000).build();
        JobScheduler jobScheduler = (JobScheduler) sContext.getSystemService("jobscheduler");
        jobScheduler.schedule(build);
        jobScheduler.schedule(build2);
    }

    private static void gK() {
        CSlog.m1408d(TAG, "destroy schedulers");
        JobScheduler jobScheduler = (JobScheduler) sContext.getSystemService("jobscheduler");
        jobScheduler.cancel(1);
        jobScheduler.cancel(2);
    }
}
