/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.util.ArrayList
 */
package com.samsung.sensorframework.sda.c.a;

import android.content.Context;
import com.samsung.sensorframework.sda.b.a.d;
import com.samsung.sensorframework.sda.b.a.r;
import com.samsung.sensorframework.sda.c.a;
import java.util.ArrayList;

public class c
extends a {
    public c(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public d b(long l2, ArrayList<r> arrayList, ArrayList<r> arrayList2, com.samsung.sensorframework.sda.a.c c2) {
        d d2 = new d(l2, c2);
        if (this.Je) {
            d2.h(arrayList);
            d2.i(arrayList2);
        }
        return d2;
    }
}

