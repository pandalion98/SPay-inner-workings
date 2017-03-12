package com.samsung.android.spayfw.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import com.samsung.android.spayfw.p002b.Log;
import java.util.List;
import java.util.Random;

public class UpdateReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.m285d("UpdateReceiver", "onReceive " + action);
        if (action.equals("android.intent.action.MY_PACKAGE_REPLACED")) {
            Log.m285d("UpdateReceiver", " Initiate DSRP Check ");
            Account a = Account.m551a(context, null);
            if (a != null) {
                List<Card> W = a.m553W();
                if (W != null) {
                    for (Card card : W) {
                        if (card.ac() != null) {
                            card.ad().reconstructMissingDsrpBlob();
                        } else {
                            Log.m287i("UpdateReceiver", "Token is null");
                        }
                    }
                } else {
                    Log.m287i("UpdateReceiver", "Cards are null");
                }
            } else {
                Log.m287i("UpdateReceiver", "Account is null");
            }
            try {
                long nextInt = (long) new Random().nextInt(86400001);
                Editor edit = context.getSharedPreferences("context_settings", 0).edit();
                edit.putLong("randomtime", nextInt);
                edit.commit();
                Log.m285d("UpdateReceiver", "set random start time to " + (nextInt / 60000) + " mins");
            } catch (Exception e) {
                Log.m285d("UpdateReceiver", "cannot set random value");
            }
        }
    }

    public static final void enable() {
        PaymentFrameworkApp.m318b(UpdateReceiver.class);
        Log.m285d("UpdateReceiver", "UpdateReceiver is enabled");
    }
}
