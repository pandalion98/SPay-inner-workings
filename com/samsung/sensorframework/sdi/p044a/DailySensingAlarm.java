package com.samsung.sensorframework.sdi.p044a;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.sensorframework.sda.p043f.DataAcquisitionUtils;
import com.samsung.sensorframework.sdi.p046c.SensingController;
import com.samsung.sensorframework.sdi.p050g.SFUtils;
import java.util.Calendar;

/* renamed from: com.samsung.sensorframework.sdi.a.a */
public class DailySensingAlarm extends BroadcastReceiver {

    /* renamed from: com.samsung.sensorframework.sdi.a.a.1 */
    class DailySensingAlarm extends Thread {
        final /* synthetic */ Intent KY;
        final /* synthetic */ DailySensingAlarm KZ;
        final /* synthetic */ Context val$context;

        DailySensingAlarm(DailySensingAlarm dailySensingAlarm, Intent intent, Context context) {
            this.KZ = dailySensingAlarm;
            this.KY = intent;
            this.val$context = context;
        }

        public void run() {
            Log.m285d("DailySensingAlarm", "onReceive() intent action: " + this.KY.getAction());
            if (SensingController.br(this.val$context) != null) {
                SensingController.br(this.val$context).m1689b(this.KY);
            } else {
                Log.m286e("DailySensingAlarm", "SensingController.getInstance() returned null");
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        new DailySensingAlarm(this, intent, context).start();
    }

    public static void m1651a(Context context, PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Calendar a = DailySensingAlarm.m1650a(SFUtils.aA(9), SFUtils.aA(60), SFUtils.aA(60));
        alarmManager.set(1, a.getTimeInMillis(), pendingIntent);
        Log.m285d("DailySensingAlarm", "Alarm set to: " + DataAcquisitionUtils.m1647G(a.getTimeInMillis()));
    }

    public static void m1652b(Context context, PendingIntent pendingIntent) {
        Log.m285d("DailySensingAlarm", "cancelDailyAlarm()");
        if (pendingIntent != null) {
            ((AlarmManager) context.getSystemService("alarm")).cancel(pendingIntent);
        }
    }

    private static Calendar m1650a(int i, int i2, int i3) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        instance.set(11, i);
        instance.set(12, i2);
        instance.set(13, i3);
        instance.add(5, 1);
        return instance;
    }
}
