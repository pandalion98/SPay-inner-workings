/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.c.b;

import android.content.Context;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.c.b;

public class f
extends b {
    public f(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public com.samsung.sensorframework.sda.b.b.f a(long l2, c c2, int n2, String string, String string2) {
        com.samsung.sensorframework.sda.b.b.f f2 = new com.samsung.sensorframework.sda.b.b.f(l2, c2);
        if (this.Je) {
            f2.setEventType(n2);
            f2.setData(string);
            if (string2 != null) {
                f2.setNumber(this.cd(string2));
            }
        }
        return f2;
    }
}

