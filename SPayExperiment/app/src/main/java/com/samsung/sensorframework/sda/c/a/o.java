/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.util.ArrayList
 */
package com.samsung.sensorframework.sda.c.a;

import android.content.Context;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.y;
import com.samsung.sensorframework.sda.b.a.z;
import com.samsung.sensorframework.sda.c.a;
import java.util.ArrayList;

public class o
extends a {
    public o(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public y b(long l2, ArrayList<z> arrayList, c c2) {
        y y2 = new y(l2, c2);
        if (this.Je) {
            y2.k(arrayList);
        }
        return y2;
    }
}

