/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Random
 */
package com.samsung.android.spayfw.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class UpdateReceiver
extends BroadcastReceiver {
    public static final void enable() {
        PaymentFrameworkApp.b(UpdateReceiver.class);
        com.samsung.android.spayfw.b.c.d("UpdateReceiver", "UpdateReceiver is enabled");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void onReceive(Context context, Intent intent) {
        block7 : {
            Iterator iterator;
            block6 : {
                block4 : {
                    block5 : {
                        String string = intent.getAction();
                        com.samsung.android.spayfw.b.c.d("UpdateReceiver", "onReceive " + string);
                        if (!string.equals((Object)"android.intent.action.MY_PACKAGE_REPLACED")) return;
                        com.samsung.android.spayfw.b.c.d("UpdateReceiver", " Initiate DSRP Check ");
                        a a2 = a.a(context, null);
                        if (a2 == null) break block4;
                        List<c> list = a2.W();
                        if (list == null) break block5;
                        iterator = list.iterator();
                        break block6;
                    }
                    com.samsung.android.spayfw.b.c.i("UpdateReceiver", "Cards are null");
                    break block7;
                }
                com.samsung.android.spayfw.b.c.i("UpdateReceiver", "Account is null");
                break block7;
            }
            while (iterator.hasNext()) {
                c c2 = (c)iterator.next();
                if (c2.ac() != null) {
                    c2.ad().reconstructMissingDsrpBlob();
                    continue;
                }
                com.samsung.android.spayfw.b.c.i("UpdateReceiver", "Token is null");
            }
        }
        try {
            long l2 = new Random().nextInt(86400001);
            SharedPreferences.Editor editor = context.getSharedPreferences("context_settings", 0).edit();
            editor.putLong("randomtime", l2);
            editor.commit();
            com.samsung.android.spayfw.b.c.d("UpdateReceiver", "set random start time to " + l2 / 60000L + " mins");
            return;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.d("UpdateReceiver", "cannot set random value");
            return;
        }
    }
}

