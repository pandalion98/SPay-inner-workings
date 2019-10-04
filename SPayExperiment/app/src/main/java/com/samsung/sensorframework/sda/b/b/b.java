/*
 * Decompiled with CFR 0.0.
 */
package com.samsung.sensorframework.sda.b.b;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;

public class b
extends a {
    protected int IA;

    public b(long l2, c c2) {
        super(l2, c2);
    }

    public void aj(int n2) {
        this.IA = n2;
    }

    @Override
    public int getSensorType() {
        return 5024;
    }
}

