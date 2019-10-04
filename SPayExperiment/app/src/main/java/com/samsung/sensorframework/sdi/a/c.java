/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.location.Location
 *  java.lang.Object
 */
package com.samsung.sensorframework.sdi.a;

import android.location.Location;
import com.samsung.sensorframework.sda.b.a;
import com.samsung.sensorframework.sda.b.a.p;

public class c {
    /*
     * Enabled aggressive block sorting
     */
    private static boolean a(Location location, Location location2) {
        return location != null && location2 != null && location.distanceTo(location2) < 50.0f || location == null && location2 == null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean b(p p2) {
        Location location;
        p p3;
        if (p2 != null) {
            p3 = (p)p2.gT();
            location = p2.getLocation();
        } else {
            p3 = null;
            location = null;
        }
        Location location2 = p3 != null ? p3.getLocation() : null;
        return !c.a(location, location2);
    }
}

