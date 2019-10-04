/*
 * Decompiled with CFR 0.0.
 */
package com.samsung.sensorframework.sda.b.b;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;

public class g
extends a {
    private float IU;
    private float IW;

    public g(long l2, c c2) {
        super(l2, c2);
    }

    public void a(float f2) {
        this.IW = f2;
    }

    @Override
    public int getSensorType() {
        return 5007;
    }

    public void setDistance(float f2) {
        this.IU = f2;
    }
}

