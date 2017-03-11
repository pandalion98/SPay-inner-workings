package com.samsung.android.spayfw.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.core.p005a.ResetNotifier;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spaytui.SpayTuiTAController;

public class PFGenericReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.m285d("PFGenericReceiver", "Intent : " + intent);
        if (intent != null && "android.intent.action.DEVICE_INTEGRITY_VERIFICATION_FAIL".equals(intent.getAction())) {
            Log.m287i("PFGenericReceiver", "ACTION_DEVICE_INTEGRITY_VERIFICATION_FAIL");
            try {
                SpayTuiTAController.getInstance().deletePin();
                ResetNotifier.m500s(context);
                ResetNotifier.m501t(context);
                FactoryResetDetector.ah();
                FactoryResetDetector.disable();
                SharedPreferences sharedPreferences = context.getSharedPreferences(PaymentFramework.CONFIG_RESET_REASON, 0);
                String str = "resetCode=DEVICE_INTEGRITY_COMPROMISED;timestamp=" + System.currentTimeMillis() + ";";
                Log.m285d("PFGenericReceiver", "ResetReason:" + str);
                sharedPreferences.edit().putString(PaymentFramework.CONFIG_RESET_REASON, str).commit();
                Process.killProcess(Process.myPid());
            } catch (Throwable e) {
                Log.m284c("PFGenericReceiver", e.getMessage(), e);
            }
        }
    }

    public static final void enable() {
        PaymentFrameworkApp.m318b(PFGenericReceiver.class);
        Log.m285d("PFGenericReceiver", "PFGenericReceiver is enabled");
    }
}
