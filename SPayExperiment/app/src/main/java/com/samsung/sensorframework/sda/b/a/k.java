/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.util.HashMap
 */
package com.samsung.sensorframework.sda.b.a;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.b;
import com.samsung.sensorframework.sda.b.a.j;
import java.util.HashMap;

public class k
extends b {
    protected HashMap<String, j> Il;

    public k(long l2, c c2) {
        super(l2, c2);
    }

    public void a(HashMap<String, j> hashMap) {
        this.Il = hashMap;
    }

    @Override
    public int getSensorType() {
        return 5016;
    }
}

