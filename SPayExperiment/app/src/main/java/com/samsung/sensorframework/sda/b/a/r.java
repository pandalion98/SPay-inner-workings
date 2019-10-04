/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.b.a;

public class r {
    private final String Ir;
    private final String Is;
    private final float It;
    private final long timestamp;

    public r(long l2, String string, String string2, float f2) {
        this.timestamp = l2;
        this.Ir = string;
        this.Is = string2;
        this.It = f2;
    }

    public /* synthetic */ Object clone() {
        return this.gV();
    }

    public r gV() {
        return new r(this.timestamp, this.Ir, this.Is, this.It);
    }
}

