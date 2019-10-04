/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.samsung.sensorframework.sda.c.b;

import android.content.Context;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.c.a;

public class g
extends a {
    public g(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public com.samsung.sensorframework.sda.b.b.g a(long l2, c c2, float f2, float f3) {
        com.samsung.sensorframework.sda.b.b.g g2 = new com.samsung.sensorframework.sda.b.b.g(l2, c2);
        if (this.Je) {
            g2.setDistance(f2);
            g2.a(f3);
        }
        return g2;
    }
}

