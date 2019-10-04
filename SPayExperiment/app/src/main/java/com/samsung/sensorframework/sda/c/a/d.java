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
import com.samsung.sensorframework.sda.b.a.e;
import com.samsung.sensorframework.sda.b.a.f;
import com.samsung.sensorframework.sda.c.a.h;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class d
extends h {
    public d(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    protected e a(long l2, c c2) {
        return new e(l2, c2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    protected a b(HashMap<String, String> hashMap) {
        try {
            f f2 = new f();
            Iterator iterator = hashMap.keySet().iterator();
            while (iterator.hasNext()) {
                String string = (String)iterator.next();
                String string2 = (String)hashMap.get((Object)string);
                if (string2 == null || string2.length() == 0) {
                    string2 = "";
                }
                if (string.equals((Object)"title") || string.equals((Object)"description")) {
                    string2 = this.ce(string2);
                }
                f2.set(string, string2);
                iterator.remove();
            }
            return f2;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    @Override
    protected /* synthetic */ b b(long l2, c c2) {
        return this.a(l2, c2);
    }
}

