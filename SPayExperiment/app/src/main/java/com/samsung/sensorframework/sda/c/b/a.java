/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.c.b;

import android.content.Context;
import android.content.Intent;
import com.samsung.sensorframework.sda.a.c;

public class a
extends com.samsung.sensorframework.sda.c.a {
    public a(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public com.samsung.sensorframework.sda.b.b.a a(long l2, c c2, Intent intent) {
        com.samsung.sensorframework.sda.b.b.a a2 = new com.samsung.sensorframework.sda.b.b.a(l2, c2);
        if (this.Je) {
            a2.bT(intent.getAction());
            a2.ad(intent.getIntExtra("level", -1));
            a2.af(intent.getIntExtra("scale", -1));
            a2.ag(intent.getIntExtra("temperature", -1));
            a2.ah(intent.getIntExtra("voltage", -1));
            a2.ae(intent.getIntExtra("plugged", -1));
            a2.setStatus(intent.getIntExtra("status", -1));
            a2.ai(intent.getIntExtra("health", -1));
        }
        return a2;
    }
}

