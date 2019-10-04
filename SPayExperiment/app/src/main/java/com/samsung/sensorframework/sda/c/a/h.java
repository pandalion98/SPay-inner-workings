/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.Iterator
 */
package com.samsung.sensorframework.sda.c.a;

import android.content.Context;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.a;
import com.samsung.sensorframework.sda.c.b;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class h
extends b {
    public h(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public com.samsung.sensorframework.sda.b.a.b a(long l2, int n2, ArrayList<HashMap<String, String>> arrayList, c c2) {
        com.samsung.sensorframework.sda.b.a.b b2 = this.b(l2, c2);
        if (this.Je) {
            Iterator iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                a a2 = this.b((HashMap<String, String>)((HashMap)iterator.next()));
                if (a2 == null) continue;
                b2.a(a2);
            }
        }
        return b2;
    }

    protected abstract a b(HashMap<String, String> var1);

    protected abstract com.samsung.sensorframework.sda.b.a.b b(long var1, c var3);
}

