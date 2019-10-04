/*
 * Decompiled with CFR 0.0.
 */
package com.samsung.sensorframework.sda.b.b;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;

public class h
extends a {
    private int IX;

    public h(long l2, c c2) {
        super(l2, c2);
    }

    @Override
    public int getSensorType() {
        return 5008;
    }

    public void setStatus(int n2) {
        this.IX = n2;
    }
}

