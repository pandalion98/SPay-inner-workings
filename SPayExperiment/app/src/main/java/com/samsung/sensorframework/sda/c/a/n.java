/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.Set
 */
package com.samsung.sensorframework.sda.c.a;

import android.content.Context;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.a;
import com.samsung.sensorframework.sda.b.a.b;
import com.samsung.sensorframework.sda.b.a.w;
import com.samsung.sensorframework.sda.b.a.x;
import com.samsung.sensorframework.sda.c.a.h;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class n
extends h {
    public n(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected a b(HashMap<String, String> hashMap) {
        try {
            x x2 = new x();
            Iterator iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
                String string = (String)iterator.next();
                String string2 = (String)hashMap.get((Object)string);
                if (string2 == null || string2.length() == 0) {
                    string2 = "";
                }
                x2.set(string, string2);
                iterator.remove();
            }
            return x2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    protected /* synthetic */ b b(long l2, c c2) {
        return this.e(l2, c2);
    }

    protected w e(long l2, c c2) {
        return new w(l2, c2);
    }
}

