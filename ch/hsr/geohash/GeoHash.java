package ch.hsr.geohash;

import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class GeoHash implements Serializable, Comparable<GeoHash> {
    private static final int[] f1D;
    private static final char[] f2J;
    private static final Map<Character, Integer> f3Z;
    private static final long serialVersionUID = -8553214249630252175L;
    protected long bits;
    private BoundingBox boundingBox;
    private WGS84Point point;
    protected byte significantBits;

    public /* synthetic */ int compareTo(Object obj) {
        return m5a((GeoHash) obj);
    }

    static {
        f1D = new int[]{16, 8, 4, 2, 1};
        f2J = new char[]{LLVARUtil.EMPTY_STRING, LLVARUtil.PLAIN_TEXT, LLVARUtil.HEX_STRING, '3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        f3Z = new HashMap();
        int length = f2J.length;
        for (int i = 0; i < length; i++) {
            f3Z.put(Character.valueOf(f2J[i]), Integer.valueOf(i));
        }
    }

    protected GeoHash() {
        this.bits = 0;
        this.significantBits = (byte) 0;
    }

    public static GeoHash m2a(double d, double d2, int i) {
        int i2 = 60;
        if (i > 12) {
            throw new IllegalArgumentException("A geohash can only be 12 character long.");
        }
        if (i * 5 <= 60) {
            i2 = i * 5;
        }
        return new GeoHash(d, d2, i2);
    }

    private GeoHash(double d, double d2, int i) {
        this.bits = 0;
        this.significantBits = (byte) 0;
        this.point = new WGS84Point(d, d2);
        byte min = Math.min(i, 64);
        double[] dArr = new double[]{-90.0d, 90.0d};
        double[] dArr2 = new double[]{-180.0d, 180.0d};
        Object obj = 1;
        while (this.significantBits < min) {
            if (obj != null) {
                m3a(d2, dArr2);
            } else {
                m3a(d, dArr);
            }
            if (obj == null) {
                obj = 1;
            } else {
                obj = (byte) 0;
            }
        }
        m4a(this, dArr, dArr2);
        this.bits <<= 64 - min;
    }

    private static void m4a(GeoHash geoHash, double[] dArr, double[] dArr2) {
        geoHash.boundingBox = new BoundingBox(new WGS84Point(dArr[0], dArr2[0]), new WGS84Point(dArr[1], dArr2[1]));
    }

    private void m3a(double d, double[] dArr) {
        double d2 = (dArr[0] + dArr[1]) / 2.0d;
        if (d >= d2) {
            m7d();
            dArr[0] = d2;
            return;
        }
        m8e();
        dArr[1] = d2;
    }

    public String m6c() {
        if (this.significantBits % 5 != 0) {
            throw new IllegalStateException("Cannot convert a geohash to base32 if the precision is not a multiple of 5.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        long j = this.bits;
        int ceil = (int) Math.ceil(((double) this.significantBits) / 5.0d);
        for (int i = 0; i < ceil; i++) {
            stringBuilder.append(f2J[(int) ((j & -576460752303423488L) >>> 59)]);
            j <<= 5;
        }
        return stringBuilder.toString();
    }

    protected final void m7d() {
        this.significantBits = (byte) (this.significantBits + 1);
        this.bits <<= 1;
        this.bits |= 1;
    }

    protected final void m8e() {
        this.significantBits = (byte) (this.significantBits + 1);
        this.bits <<= 1;
    }

    public String toString() {
        if (this.significantBits % 5 == 0) {
            return String.format("%s -> %s -> %s", new Object[]{Long.toBinaryString(this.bits), this.boundingBox, m6c()});
        }
        return String.format("%s -> %s, bits: %d", new Object[]{Long.toBinaryString(this.bits), this.boundingBox, Byte.valueOf(this.significantBits)});
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof GeoHash) {
            GeoHash geoHash = (GeoHash) obj;
            if (geoHash.significantBits == this.significantBits && geoHash.bits == this.bits) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return ((((int) (this.bits ^ (this.bits >>> 32))) + 527) * 31) + this.significantBits;
    }

    public int m5a(GeoHash geoHash) {
        int compare = Long.compare(this.bits ^ Long.MIN_VALUE, geoHash.bits ^ Long.MIN_VALUE);
        return compare != 0 ? compare : Integer.compare(this.significantBits, geoHash.significantBits);
    }
}
