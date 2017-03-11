package com.samsung.android.spayfw.payprovider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.samsung.android.spayfw.core.Account;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.p002b.Log;

public class TokenReplenishReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.m286e("TokenReplenishReceiver", "On Receive: intent or tokenId is null");
        } else if ("com.samsung.android.spayfw.ACTION_REPLENISH_ALARM_EXECUTE".equals(intent.getAction())) {
            String stringExtra = intent.getStringExtra("TrTokenId");
            Log.m285d("TokenReplenishReceiver", "On Receive: ACTION_REPLENISH_ALARM_EXECUTE: TrTokenId " + stringExtra);
            Account a = Account.m551a(context, null);
            if (a != null) {
                Card r = a.m559r(stringExtra);
                if (r != null) {
                    r.ad().replenishAlarmExpired();
                    return;
                } else {
                    Log.m286e("TokenReplenishReceiver", "Card not found");
                    return;
                }
            }
            Log.m286e("TokenReplenishReceiver", "Account is NULL");
        }
    }

    public static final void enable() {
        PaymentFrameworkApp.m318b(TokenReplenishReceiver.class);
        Log.m285d("TokenReplenishReceiver", "TokenReplenishReceiver is enabled");
    }
}
