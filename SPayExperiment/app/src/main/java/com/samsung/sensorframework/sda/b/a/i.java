/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 */
package com.samsung.sensorframework.sda.b.a;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;
import java.util.ArrayList;
import java.util.HashMap;

public class i
extends a {
    private ArrayList<HashMap<String, Object>> Ik;

    public i(long l2, c c2) {
        super(l2, c2);
    }

    @Override
    public int getSensorType() {
        return 5037;
    }

    public void j(ArrayList<HashMap<String, Object>> arrayList) {
        this.Ik = arrayList;
    }
}

