/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.net.NetworkInfo
 *  android.net.NetworkInfo$State
 *  android.os.Parcelable
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.core.retry;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Parcelable;
import com.samsung.android.spayfw.core.a.v;
import com.samsung.android.spayfw.core.retry.c;
import com.samsung.android.spayfw.core.retry.d;

public class b
extends BroadcastReceiver {
    /*
     * Enabled aggressive block sorting
     */
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            com.samsung.android.spayfw.b.c.e("NetworkEventReceiver", "intent is null");
            return;
        } else {
            NetworkInfo networkInfo;
            com.samsung.android.spayfw.b.c.d("NetworkEventReceiver", "intent.action = " + intent.getAction());
            if (!"android.net.conn.CONNECTIVITY_CHANGE".equals((Object)intent.getAction()) || (networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo")) == null || networkInfo.getState() == null) return;
            {
                NetworkInfo.State state = networkInfo.getState();
                com.samsung.android.spayfw.b.c.d("NetworkEventReceiver", "state = " + state.toString());
                if (!state.equals((Object)NetworkInfo.State.CONNECTED)) return;
                {
                    d.e(false);
                    c.bm();
                    v.restart();
                    return;
                }
            }
        }
    }
}
