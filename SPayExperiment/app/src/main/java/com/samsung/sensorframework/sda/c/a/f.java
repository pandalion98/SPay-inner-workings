/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 */
package com.samsung.sensorframework.sda.c.a;

import android.content.Context;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.i;
import com.samsung.sensorframework.sda.c.a;
import java.util.ArrayList;
import java.util.HashMap;

public class f
extends a {
    public f(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public i a(long l2, ArrayList<HashMap<String, Object>> arrayList, c c2) {
        i i2 = new i(l2, c2);
        if (this.Je) {
            i2.j(arrayList);
        }
        return i2;
    }
}

