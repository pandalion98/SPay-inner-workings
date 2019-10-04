/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Long
 *  java.util.ArrayList
 */
package com.samsung.sensorframework.sda.b.a;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;
import java.util.ArrayList;

public class m
extends a {
    private ArrayList<float[]> Ig;
    private ArrayList<Long> Ih;
    private int Im;

    public m(long l2, c c2, int n2) {
        super(l2, c2);
        this.Im = n2;
    }

    public void f(ArrayList<float[]> arrayList) {
        this.Ig = arrayList;
    }

    public void g(ArrayList<Long> arrayList) {
        this.Ih = arrayList;
    }

    @Override
    public int getSensorType() {
        return this.Im;
    }
}

