/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Long
 *  java.util.ArrayList
 */
package com.samsung.sensorframework.sda.b.a;

import com.samsung.sensorframework.sda.b.a;
import java.util.ArrayList;

public class c
extends a {
    private ArrayList<float[]> Ig;
    private ArrayList<Long> Ih;

    public c(long l2, com.samsung.sensorframework.sda.a.c c2) {
        super(l2, c2);
    }

    public void f(ArrayList<float[]> arrayList) {
        this.Ig = arrayList;
    }

    public void g(ArrayList<Long> arrayList) {
        this.Ih = arrayList;
    }

    @Override
    public int getSensorType() {
        return 5001;
    }
}

