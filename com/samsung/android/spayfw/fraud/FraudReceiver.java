package com.samsung.android.spayfw.fraud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.p002b.Log;

public class FraudReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.m286e("FraudReceiver", "intent is null");
            return;
        }
        Log.m285d("FraudReceiver", "FraudReciever: " + intent.getAction());
        String schemeSpecificPart;
        if ("android.intent.action.PACKAGE_DATA_CLEARED".equals(intent.getAction())) {
            Uri data = intent.getData();
            if (data != null) {
                schemeSpecificPart = data.getSchemeSpecificPart();
                if ("com.samsung.android.spay".equals(schemeSpecificPart)) {
                    Log.m285d("FraudReceiver", "Fraud: add an [" + schemeSpecificPart + "] reset record");
                    FraudDataCollector x = FraudDataCollector.m718x(context);
                    if (x != null) {
                        x.m719W("app_reset");
                    } else {
                        Log.m285d("FraudReceiver", "addFDeviceRecord: cannot get data");
                    }
                    context.sendBroadcast(new Intent("com.samsung.android.spay.action.REQUEST_RESET"));
                    Log.m287i("FraudReceiver", "Sent App Reset Broadcast");
                }
            }
        } else if ("android.intent.action.SIM_STATE_CHANGED".equals(intent.getAction())) {
            schemeSpecificPart = intent.getStringExtra("ss");
            Log.m285d("FraudReceiver", "FraudReceiver: Sim State = " + schemeSpecificPart);
            FraudDataCollector x2 = FraudDataCollector.m718x(context);
            if ("ABSENT".equals(schemeSpecificPart) && x2 != null) {
                x2.m719W("remove_sim");
            } else if ("LOADED".equals(schemeSpecificPart) && x2 != null) {
                x2.m719W("add_sim");
            }
        }
    }

    public static final void enable() {
        PaymentFrameworkApp.m318b(FraudReceiver.class);
    }
}
