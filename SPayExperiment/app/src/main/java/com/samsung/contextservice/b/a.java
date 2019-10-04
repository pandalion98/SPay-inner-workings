/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.contextservice.b;

import android.content.Context;
import android.content.Intent;
import com.samsung.contextservice.b.b;

public class a {
    public static void a(Intent intent, Context context) {
        if (context == null) {
            b.i("AppInterface", "cannot notify app, ctx is null");
            return;
        }
        context.sendBroadcast(intent, "com.samsung.android.spayfw.permission.UPDATE_NOTIFICATION");
        b.i("AppInterface", "sent broadcast: action: " + intent.getAction() + " type: " + intent.getStringExtra("notiType"));
    }
}

