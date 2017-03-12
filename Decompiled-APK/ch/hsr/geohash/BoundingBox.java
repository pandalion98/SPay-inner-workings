package ch.hsr.geohash;

import java.io.Serializable;

public class BoundingBox implements Serializable {
    private static final long serialVersionUID = -7145192134410261076L;
    private double maxLat;
    private double maxLon;
    private double minLat;
    private double minLon;

    public BoundingBox(WGS84Point wGS84Point, WGS84Point wGS84Point2) {
        this(wGS84Point.getLatitude(), wGS84Point2.getLatitude(), wGS84Point.getLongitude(), wGS84Point2.getLongitude());
    }

    public BoundingBox(double d, double d2, double d3, double d4) {
        this.minLon = Math.min(d3, d4);
        this.maxLon = Math.max(d3, d4);
        this.minLat = Math.min(d, d2);
        this.maxLat = Math.max(d, d2);
    }

    public WGS84Point m0a() {
        return new WGS84Point(this.maxLat, this.minLon);
    }

    public WGS84Point m1b() {
        return new WGS84Point(this.minLat, this.maxLon);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof BoundingBox)) {
            return false;
        }
        BoundingBox boundingBox = (BoundingBox) obj;
        if (this.minLat == boundingBox.minLat && this.minLon == boundingBox.minLon && this.maxLat == boundingBox.maxLat && this.maxLon == boundingBox.maxLon) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((((((hashCode(this.minLat) + 629) * 37) + hashCode(this.maxLat)) * 37) + hashCode(this.minLon)) * 37) + hashCode(this.maxLon);
    }

    private static int hashCode(double d) {
        long doubleToLongBits = Double.doubleToLongBits(d);
        return (int) (doubleToLongBits ^ (doubleToLongBits >>> 32));
    }

    public String toString() {
        return m0a() + " -> " + m1b();
    }
}
