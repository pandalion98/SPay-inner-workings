/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.location.Location
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.b.a;

import android.location.Location;
import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;

public class p
extends a {
    private int Im;
    private Location In;

    public p(long l2, c c2, int n2) {
        super(l2, c2);
        this.Im = n2;
    }

    public void a(Location location) {
        this.In = location;
    }

    public Location getLocation() {
        return this.In;
    }

    @Override
    public int getSensorType() {
        return this.Im;
    }

    public String toString() {
        if (this.In == null) {
            return "LocationData { null }";
        }
        return "LocationData { " + this.In.toString() + " }";
    }
}

