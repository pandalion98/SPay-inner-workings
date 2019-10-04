/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.ArrayList
 */
package com.samsung.sensorframework.sda.b.a;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a.a;
import java.util.ArrayList;

public abstract class b
extends com.samsung.sensorframework.sda.b.a {
    protected final ArrayList<a> If = new ArrayList();

    public b(long l2, c c2) {
        super(l2, c2);
    }

    public void a(a a2) {
        this.If.add((Object)a2);
    }

    public ArrayList<a> gU() {
        return this.If;
    }
}

