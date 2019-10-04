/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.sensorframework.sda.b;

import com.samsung.sensorframework.sda.a.c;

public abstract class a {
    private final long Ia;
    private a Ib;
    protected boolean Ic;
    private c Id;

    public a(long l2, c c2) {
        this.Ia = l2;
        this.Id = c2;
        this.Ic = false;
    }

    public void b(a a2) {
        if (a2 != null) {
            a2.b(null);
        }
        this.Ib = a2;
    }

    public a gT() {
        return this.Ib;
    }

    public abstract int getSensorType();
}

