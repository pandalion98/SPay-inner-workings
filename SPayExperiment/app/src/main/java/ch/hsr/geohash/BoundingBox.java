/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.Serializable
 *  java.lang.Double
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 */
package ch.hsr.geohash;

import ch.hsr.geohash.WGS84Point;
import java.io.Serializable;

public class BoundingBox
implements Serializable {
    private static final long serialVersionUID = -7145192134410261076L;
    private double maxLat;
    private double maxLon;
    private double minLat;
    private double minLon;

    public BoundingBox(double d2, double d3, double d4, double d5) {
        this.minLon = Math.min((double)d4, (double)d5);
        this.maxLon = Math.max((double)d4, (double)d5);
        this.minLat = Math.min((double)d2, (double)d3);
        this.maxLat = Math.max((double)d2, (double)d3);
    }

    public BoundingBox(WGS84Point wGS84Point, WGS84Point wGS84Point2) {
        this(wGS84Point.getLatitude(), wGS84Point2.getLatitude(), wGS84Point.getLongitude(), wGS84Point2.getLongitude());
    }

    private static int hashCode(double d2) {
        long l2 = Double.doubleToLongBits((double)d2);
        return (int)(l2 ^ l2 >>> 32);
    }

    public WGS84Point a() {
        return new WGS84Point(this.maxLat, this.minLon);
    }

    public WGS84Point b() {
        return new WGS84Point(this.minLat, this.maxLon);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block5 : {
            block4 : {
                if (this == object) break block4;
                if (!(object instanceof BoundingBox)) {
                    return false;
                }
                BoundingBox boundingBox = (BoundingBox)object;
                if (this.minLat != boundingBox.minLat || this.minLon != boundingBox.minLon || this.maxLat != boundingBox.maxLat || this.maxLon != boundingBox.maxLon) break block5;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 37 * (37 * (37 * (629 + BoundingBox.hashCode(this.minLat)) + BoundingBox.hashCode(this.maxLat)) + BoundingBox.hashCode(this.minLon)) + BoundingBox.hashCode(this.maxLon);
    }

    public String toString() {
        return this.a() + " -> " + this.b();
    }
}

