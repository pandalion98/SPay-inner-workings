package com.samsung.contextservice.p029b;

import android.content.Context;
import android.content.Intent;
import com.samsung.android.spayfw.appinterface.PaymentFramework;

/* renamed from: com.samsung.contextservice.b.a */
public class AppInterface {
    public static void m1399a(Intent intent, Context context) {
        if (context == null) {
            CSlog.m1410i("AppInterface", "cannot notify app, ctx is null");
            return;
        }
        context.sendBroadcast(intent, "com.samsung.android.spayfw.permission.UPDATE_NOTIFICATION");
        CSlog.m1410i("AppInterface", "sent broadcast: action: " + intent.getAction() + " type: " + intent.getStringExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE));
    }
}
