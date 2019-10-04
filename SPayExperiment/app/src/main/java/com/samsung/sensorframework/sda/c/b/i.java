/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.c.b;

import android.content.Context;
import android.content.Intent;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.b.h;
import com.samsung.sensorframework.sda.c.a;

public class i
extends a {
    public i(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public h d(long l2, c c2, Intent intent) {
        h h2 = new h(l2, c2);
        int n2 = 2;
        if (intent.getAction().equals((Object)"android.intent.action.SCREEN_ON")) {
            n2 = 1;
        } else if (intent.getAction().equals((Object)"android.intent.action.SCREEN_OFF")) {
            n2 = 0;
        }
        h2.setStatus(n2);
        return h2;
    }
}

