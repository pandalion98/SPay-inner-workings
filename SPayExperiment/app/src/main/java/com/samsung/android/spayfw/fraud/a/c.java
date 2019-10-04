/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  java.lang.Long
 *  java.lang.String
 */
package com.samsung.android.spayfw.fraud.a;

import android.content.ContentValues;
import com.samsung.android.spayfw.fraud.a.a;

public class c
extends a {
    private String nV;
    private long time;

    public c(String string, long l2) {
        super("fcounter");
        this.nV = string;
        this.time = l2;
    }

    @Override
    public ContentValues bC() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("enroll_id", this.nV);
        contentValues.put("time", Long.valueOf((long)this.time));
        return contentValues;
    }
}

