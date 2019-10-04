/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  java.lang.Long
 *  java.lang.String
 *  java.lang.StringBuilder
 */
package com.samsung.android.spayfw.fraud.a.a;

import android.content.ContentValues;

public class a
extends com.samsung.android.spayfw.fraud.a.a {
    private long id;
    private String oc;
    private String od;
    private long oe;
    private String reason;
    private long time;

    public a(long l2, long l3, String string, String string2, String string3, long l4) {
        super("fdevice_info");
        this.id = l2;
        this.time = l3;
        this.reason = string;
        this.oc = string2;
        this.od = string3;
        this.oe = l4;
    }

    @Override
    public ContentValues bC() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", Long.valueOf((long)this.time));
        contentValues.put("reason", this.reason);
        contentValues.put("external_id", this.oc);
        contentValues.put("extras", this.od);
        contentValues.put("overflow", Long.valueOf((long)this.oe));
        return contentValues;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id=");
        stringBuilder.append(this.id);
        stringBuilder.append(" ");
        stringBuilder.append("time=");
        stringBuilder.append(this.time);
        stringBuilder.append(" ");
        stringBuilder.append("reason=");
        stringBuilder.append(this.reason);
        stringBuilder.append(" ");
        stringBuilder.append("external_id=");
        stringBuilder.append(this.oc);
        stringBuilder.append(" ");
        stringBuilder.append("extras=");
        stringBuilder.append(this.od);
        stringBuilder.append(" ");
        stringBuilder.append("overflow=");
        stringBuilder.append(this.oe);
        return stringBuilder.toString();
    }
}

