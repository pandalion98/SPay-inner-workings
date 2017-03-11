package ch.hsr.geohash;

import java.io.Serializable;

public class WGS84Point implements Serializable {
    private static final long serialVersionUID = 7457963026513014856L;
    private final double latitude;
    private final double longitude;

    public WGS84Point(double d, double d2) {
        this.latitude = d;
        this.longitude = d2;
        if (Math.abs(d) > 90.0d || Math.abs(d2) > 180.0d) {
            throw new IllegalArgumentException("The supplied coordinates " + this + " are out of range.");
        }
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String toString() {
        return String.format("(" + this.latitude + "," + this.longitude + ")", new Object[0]);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WGS84Point)) {
            return false;
        }
        WGS84Point wGS84Point = (WGS84Point) obj;
        if (this.latitude == wGS84Point.latitude && this.longitude == wGS84Point.longitude) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(this.latitude);
        long doubleToLongBits2 = Double.doubleToLongBits(this.longitude);
        return ((((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + 1302) * 31) + ((int) (doubleToLongBits2 ^ (doubleToLongBits2 >>> 32)));
    }
}
