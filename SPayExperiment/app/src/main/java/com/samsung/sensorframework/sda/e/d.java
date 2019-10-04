/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.sensorframework.sda.e;

import com.samsung.sensorframework.sda.b;
import com.samsung.sensorframework.sda.e.a;

public class d {
    private final a KQ;
    private final b KR;

    public d(a a2, b b2) {
        this.KQ = a2;
        this.KR = b2;
    }

    public boolean a(d d2) {
        return this.KQ == d2.hF() && this.KR == d2.hG();
    }

    public a hF() {
        return this.KQ;
    }

    public b hG() {
        return this.KR;
    }

    public boolean hH() {
        return this.KQ.c(this.KR);
    }

    public void unregister() {
        this.KQ.d(this.KR);
    }
}

