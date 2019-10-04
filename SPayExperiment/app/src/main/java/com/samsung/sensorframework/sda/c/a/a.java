/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Long
 *  java.util.ArrayList
 */
package com.samsung.sensorframework.sda.c.a;

import android.content.Context;
import com.samsung.sensorframework.sda.b.a.c;
import java.util.ArrayList;

public class a
extends com.samsung.sensorframework.sda.c.a {
    public a(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public c a(long l2, ArrayList<float[]> arrayList, ArrayList<Long> arrayList2, com.samsung.sensorframework.sda.a.c c2) {
        c c3 = new c(l2, c2);
        if (this.Je) {
            c3.f(arrayList);
            c3.g(arrayList2);
        }
        return c3;
    }
}

