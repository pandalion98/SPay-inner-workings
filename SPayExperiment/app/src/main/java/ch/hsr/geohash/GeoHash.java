/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.Serializable
 *  java.lang.Character
 *  java.lang.Comparable
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.util.HashMap
 *  java.util.Map
 */
package ch.hsr.geohash;

import ch.hsr.geohash.BoundingBox;
import ch.hsr.geohash.WGS84Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class GeoHash
implements Serializable,
Comparable<GeoHash> {
    private static final int[] D = new int[]{16, 8, 4, 2, 1};
    private static final char[] J = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    private static final Map<Character, Integer> Z = new HashMap();
    private static final long serialVersionUID = -8553214249630252175L;
    protected long bits = 0L;
    private BoundingBox boundingBox;
    private WGS84Point point;
    protected byte significantBits = 0;

    static {
        int n2 = J.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            Z.put((Object)Character.valueOf((char)J[i2]), (Object)i2);
        }
    }

    protected GeoHash() {
    }

    /*
     * Enabled aggressive block sorting
     */
    private GeoHash(double d2, double d3, int n2) {
        this.point = new WGS84Point(d2, d3);
        int n3 = Math.min((int)n2, (int)64);
        double[] arrd = new double[]{-90.0, 90.0};
        double[] arrd2 = new double[]{-180.0, 180.0};
        boolean bl = true;
        do {
            if (this.significantBits >= n3) {
                GeoHash.a(this, arrd, arrd2);
                this.bits <<= 64 - n3;
                return;
            }
            if (bl) {
                this.a(d3, arrd2);
            } else {
                this.a(d2, arrd);
            }
            if (!bl) {
                bl = true;
                continue;
            }
            bl = false;
        } while (true);
    }

    public static GeoHash a(double d2, double d3, int n2) {
        int n3 = 60;
        if (n2 > 12) {
            throw new IllegalArgumentException("A geohash can only be 12 character long.");
        }
        if (n2 * 5 <= n3) {
            n3 = n2 * 5;
        }
        return new GeoHash(d2, d3, n3);
    }

    private void a(double d2, double[] arrd) {
        double d3 = (arrd[0] + arrd[1]) / 2.0;
        if (d2 >= d3) {
            this.d();
            arrd[0] = d3;
            return;
        }
        this.e();
        arrd[1] = d3;
    }

    private static void a(GeoHash geoHash, double[] arrd, double[] arrd2) {
        geoHash.boundingBox = new BoundingBox(new WGS84Point(arrd[0], arrd2[0]), new WGS84Point(arrd[1], arrd2[1]));
    }

    public int a(GeoHash geoHash) {
        int n2 = Long.compare((long)(Long.MIN_VALUE ^ this.bits), (long)(Long.MIN_VALUE ^ geoHash.bits));
        if (n2 != 0) {
            return n2;
        }
        return Integer.compare((int)this.significantBits, (int)geoHash.significantBits);
    }

    public String c() {
        if (this.significantBits % 5 != 0) {
            throw new IllegalStateException("Cannot convert a geohash to base32 if the precision is not a multiple of 5.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        long l2 = this.bits;
        int n2 = (int)Math.ceil((double)((double)this.significantBits / 5.0));
        for (int i2 = 0; i2 < n2; ++i2) {
            int n3 = (int)((l2 & -576460752303423488L) >>> 59);
            stringBuilder.append(J[n3]);
            l2 <<= 5;
        }
        return stringBuilder.toString();
    }

    public /* synthetic */ int compareTo(Object object) {
        return this.a((GeoHash)object);
    }

    protected final void d() {
        this.significantBits = (byte)(1 + this.significantBits);
        this.bits <<= 1;
        this.bits = 1L | this.bits;
    }

    protected final void e() {
        this.significantBits = (byte)(1 + this.significantBits);
        this.bits <<= 1;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block3 : {
            block2 : {
                if (object == this) break block2;
                if (!(object instanceof GeoHash)) break block3;
                GeoHash geoHash = (GeoHash)object;
                if (geoHash.significantBits != this.significantBits || geoHash.bits != this.bits) break block3;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        return 31 * (527 + (int)(this.bits ^ this.bits >>> 32)) + this.significantBits;
    }

    public String toString() {
        if (this.significantBits % 5 == 0) {
            Object[] arrobject = new Object[]{Long.toBinaryString((long)this.bits), this.boundingBox, this.c()};
            return String.format((String)"%s -> %s -> %s", (Object[])arrobject);
        }
        Object[] arrobject = new Object[]{Long.toBinaryString((long)this.bits), this.boundingBox, this.significantBits};
        return String.format((String)"%s -> %s, bits: %d", (Object[])arrobject);
    }
}

