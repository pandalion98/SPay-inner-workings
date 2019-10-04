/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.util.ArrayList
 */
package com.samsung.sensorframework.sda.b.a;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;
import com.samsung.sensorframework.sda.b.a.r;
import java.util.ArrayList;

public class d
extends a {
    private ArrayList<r> Ii;
    private ArrayList<r> Ij;

    public d(long l2, c c2) {
        super(l2, c2);
    }

    @Override
    public int getSensorType() {
        return 5003;
    }

    public void h(ArrayList<r> arrayList) {
        this.Ii = arrayList;
    }

    public void i(ArrayList<r> arrayList) {
        this.Ij = arrayList;
    }
}

