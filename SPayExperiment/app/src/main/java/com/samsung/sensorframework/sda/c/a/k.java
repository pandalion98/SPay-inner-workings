/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.location.Location
 */
package com.samsung.sensorframework.sda.c.a;

import android.content.Context;
import android.location.Location;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.p;
import com.samsung.sensorframework.sda.c.a;

public class k
extends a {
    public k(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public p a(long l2, Location location, c c2, int n2) {
        p p2 = new p(l2, c2, n2);
        if (this.Je) {
            p2.a(location);
        }
        return p2;
    }
}

