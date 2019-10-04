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
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.m;
import com.samsung.sensorframework.sda.c.a;
import java.util.ArrayList;

public class i
extends a {
    public i(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public m a(long l2, ArrayList<float[]> arrayList, ArrayList<Long> arrayList2, c c2, int n2) {
        m m2 = new m(l2, c2, n2);
        if (this.Je) {
            m2.f(arrayList);
            m2.g(arrayList2);
        }
        return m2;
    }
}

