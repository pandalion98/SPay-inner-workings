package com.samsung.android.spayfw.payprovider;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.utils.Utils;

/* renamed from: com.samsung.android.spayfw.payprovider.h */
public class TokenReplenishAlarm {

    /* renamed from: com.samsung.android.spayfw.payprovider.h.1 */
    static class TokenReplenishAlarm implements Runnable {
        final /* synthetic */ ProviderTokenKey pd;
        final /* synthetic */ long pe;
        final /* synthetic */ Context val$context;

        TokenReplenishAlarm(Context context, ProviderTokenKey providerTokenKey, long j) {
            this.val$context = context;
            this.pd = providerTokenKey;
            this.pe = j;
        }

        public void run() {
            Log.m287i("SPAYFW:TokenReplenishAlarm", "setAlarm");
            AlarmManager alarmManager = (AlarmManager) this.val$context.getSystemService("alarm");
            Intent intent = new Intent(this.val$context, TokenReplenishReceiver.class);
            intent.setAction("com.samsung.android.spayfw.ACTION_REPLENISH_ALARM_EXECUTE");
            intent.setType(this.pd.getTrTokenId());
            intent.putExtra("TrTokenId", this.pd.getTrTokenId());
            PendingIntent broadcast = PendingIntent.getBroadcast(this.val$context, 0, intent, 0);
            long am = this.pe - Utils.am(this.val$context);
            Log.m285d("SPAYFW:TokenReplenishAlarm", "setAlarm : " + am);
            alarmManager.set(2, am + SystemClock.elapsedRealtime(), broadcast);
        }
    }

    public static void m1070a(Context context, long j, ProviderTokenKey providerTokenKey) {
        PaymentFrameworkApp.az().post(new TokenReplenishAlarm(context, providerTokenKey, j));
    }

    public static void m1071a(Context context, ProviderTokenKey providerTokenKey) {
        Log.m287i("SPAYFW:TokenReplenishAlarm", "cancelAlarm");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Intent intent = new Intent(context, TokenReplenishReceiver.class);
        intent.setAction("com.samsung.android.spayfw.ACTION_REPLENISH_ALARM_EXECUTE");
        intent.setType(providerTokenKey.getTrTokenId());
        alarmManager.cancel(PendingIntent.getBroadcast(context, 0, intent, 0));
    }
}
