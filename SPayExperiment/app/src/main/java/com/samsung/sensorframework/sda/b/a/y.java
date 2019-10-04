/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.samsung.sensorframework.sda.b.a;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;
import com.samsung.sensorframework.sda.b.a.z;
import java.util.ArrayList;

public class y
extends a {
    private ArrayList<z> Iu;

    public y(long l2, c c2) {
        super(l2, c2);
    }

    @Override
    public int getSensorType() {
        return 5010;
    }

    public void k(ArrayList<z> arrayList) {
        this.Iu = arrayList;
    }

    public String toString() {
        if (this.Iu != null) {
            return "WifiData { Size: " + this.Iu.size() + " }";
        }
        return "WifiData { null }";
    }
}

