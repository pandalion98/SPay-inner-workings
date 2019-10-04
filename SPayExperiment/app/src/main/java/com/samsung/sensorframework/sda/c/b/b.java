/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ClipData
 *  android.content.ClipData$Item
 *  android.content.Context
 *  java.lang.CharSequence
 */
package com.samsung.sensorframework.sda.c.b;

import android.content.ClipData;
import android.content.Context;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.c.a;

public class b
extends a {
    public b(Context context, boolean bl, boolean bl2) {
        super(context, bl, bl2);
    }

    public com.samsung.sensorframework.sda.b.b.b a(long l2, c c2, ClipData clipData) {
        int n2;
        com.samsung.sensorframework.sda.b.b.b b2 = new com.samsung.sensorframework.sda.b.b.b(l2, c2);
        if (clipData != null) {
            n2 = 0;
            for (int i2 = 0; i2 < clipData.getItemCount(); ++i2) {
                ClipData.Item item = clipData.getItemAt(i2);
                if (item == null) continue;
                n2 += item.getText().length();
            }
        } else {
            n2 = 0;
        }
        b2.aj(n2);
        return b2;
    }
}

