/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.Serializable
 *  java.lang.Double
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package ch.hsr.geohash;

import java.io.Serializable;

public class WGS84Point
implements Serializable {
    private static final long serialVersionUID = 7457963026513014856L;
    private final double latitude;
    private final double longitude;

    public WGS84Point(double d2, double d3) {
        this.latitude = d2;
        this.longitude = d3;
        if (Math.abs((double)d2) > 90.0 || Math.abs((double)d3) > 180.0) {
            throw new IllegalArgumentException("The supplied coordinates " + this + " are out of range.");
        }
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof WGS84Point;
        boolean bl2 = false;
        if (bl) {
            WGS84Point wGS84Point = (WGS84Point)object;
            double d2 = this.latitude DCMPL wGS84Point.latitude;
            bl2 = false;
            if (d2 == false) {
                double d3 = this.longitude DCMPL wGS84Point.longitude;
                bl2 = false;
                if (d3 == false) {
                    bl2 = true;
                }
            }
        }
        return bl2;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public int hashCode() {
        long l2 = Double.doubleToLongBits((double)this.latitude);
        long l3 = Double.doubleToLongBits((double)this.longitude);
        return 31 * (1302 + (int)(l2 ^ l2 >>> 32)) + (int)(l3 ^ l3 >>> 32);
    }

    public String toString() {
        return String.format((String)("(" + this.latitude + "," + this.longitude + ")"), (Object[])new Object[0]);
    }
}

