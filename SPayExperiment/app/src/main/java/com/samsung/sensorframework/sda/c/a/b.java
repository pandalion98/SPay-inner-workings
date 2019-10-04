/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.c.a;

import android.content.Context;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.q;
import com.samsung.sensorframework.sda.c.a;

public class b
extends a {
    public b(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public q a(long l2, double[] arrd, long[] arrl, String string, c c2) {
        q q2 = new q(l2, c2);
        if (this.Je) {
            q2.a(arrd);
            q2.a(arrl);
            q2.bS(string);
        }
        return q2;
    }
}

