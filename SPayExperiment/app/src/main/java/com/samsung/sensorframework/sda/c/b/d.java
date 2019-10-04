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
import com.samsung.sensorframework.sda.c.a;

public class d
extends a {
    public d(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public com.samsung.sensorframework.sda.b.b.d b(long l2, c c2, Intent intent) {
        com.samsung.sensorframework.sda.b.b.d d2 = new com.samsung.sensorframework.sda.b.b.d(l2, c2);
        String string = intent.getStringExtra("artist");
        String string2 = intent.getStringExtra("album");
        String string3 = intent.getStringExtra("track");
        long l3 = intent.getLongExtra("id", -1L);
        long l4 = intent.getLongExtra("position", -1L);
        long l5 = intent.getLongExtra("trackLength", -1L);
        int n2 = intent.getIntExtra("listpos", -1);
        int n3 = 2;
        if (intent.getBooleanExtra("playing", false)) {
            n3 = 1;
        }
        d2.setState(n3);
        d2.bU(string);
        d2.bV(string2);
        d2.bW(string3);
        d2.bX("unknown");
        d2.setId(l3);
        d2.D(l4);
        d2.E(l5);
        d2.ak(n2);
        return d2;
    }
}

