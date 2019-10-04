/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.b.a;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;

public class q
extends a {
    private double[] Io;
    private long[] Ip;
    private String Iq;

    public q(long l2, c c2) {
        super(l2, c2);
    }

    public void a(double[] arrd) {
        this.Io = arrd;
    }

    public void a(long[] arrl) {
        this.Ip = arrl;
    }

    public void bS(String string) {
        this.Iq = string;
    }

    @Override
    public int getSensorType() {
        return 5005;
    }
}

