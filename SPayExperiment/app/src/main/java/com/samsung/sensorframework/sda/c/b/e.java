/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.c.b;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.c.a;

public class e
extends a {
    public e(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public com.samsung.sensorframework.sda.b.b.e c(long l2, c c2, Intent intent) {
        com.samsung.sensorframework.sda.b.b.e e2 = new com.samsung.sensorframework.sda.b.b.e(l2, c2);
        e2.bY(intent.getAction());
        e2.setPackageName(intent.getData().getEncodedSchemeSpecificPart());
        return e2;
    }
}

