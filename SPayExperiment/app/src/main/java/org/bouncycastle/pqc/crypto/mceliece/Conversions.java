/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 */
package org.bouncycastle.pqc.crypto.mceliece;

import java.math.BigInteger;
import org.bouncycastle.pqc.math.linearalgebra.BigIntUtils;
import org.bouncycastle.pqc.math.linearalgebra.GF2Vector;
import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;

final class Conversions {
    private static final BigInteger ONE;
    private static final BigInteger ZERO;

    static {
        ZERO = BigInteger.valueOf((long)0L);
        ONE = BigInteger.valueOf((long)1L);
    }

    private Conversions() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] decode(int n, int n2, GF2Vector gF2Vector) {
        if (gF2Vector.getLength() != n || gF2Vector.getHammingWeight() != n2) {
            throw new IllegalArgumentException("vector has wrong length or hamming weight");
        }
        int[] arrn = gF2Vector.getVecArray();
        BigInteger bigInteger = IntegerFunctions.binomial(n, n2);
        BigInteger bigInteger2 = ZERO;
        int n3 = 0;
        int n4 = n;
        int n5 = n2;
        while (n3 < n) {
            bigInteger = bigInteger.multiply(BigInteger.valueOf((long)(n4 - n5))).divide(BigInteger.valueOf((long)n4));
            --n4;
            if ((arrn[n3 >> 5] & 1 << (n3 & 31)) != 0) {
                bigInteger2 = bigInteger2.add(bigInteger);
                bigInteger = n4 == --n5 ? ONE : bigInteger.multiply(BigInteger.valueOf((long)(n5 + 1))).divide(BigInteger.valueOf((long)(n4 - n5)));
            }
            ++n3;
        }
        return BigIntUtils.toMinimalByteArray(bigInteger2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static GF2Vector encode(int n, int n2, byte[] arrby) {
        if (n < n2) {
            throw new IllegalArgumentException("n < t");
        }
        BigInteger bigInteger = new BigInteger(1, arrby);
        BigInteger bigInteger2 = IntegerFunctions.binomial(n, n2);
        if (bigInteger.compareTo(bigInteger2) >= 0) {
            throw new IllegalArgumentException("Encoded number too large.");
        }
        GF2Vector gF2Vector = new GF2Vector(n);
        int n3 = 0;
        int n4 = n;
        int n5 = n2;
        while (n3 < n) {
            bigInteger2 = bigInteger2.multiply(BigInteger.valueOf((long)(n4 - n5))).divide(BigInteger.valueOf((long)n4));
            --n4;
            if (bigInteger2.compareTo(bigInteger) <= 0) {
                gF2Vector.setBit(n3);
                bigInteger = bigInteger.subtract(bigInteger2);
                bigInteger2 = n4 == --n5 ? ONE : bigInteger2.multiply(BigInteger.valueOf((long)(n5 + 1))).divide(BigInteger.valueOf((long)(n4 - n5)));
            }
            ++n3;
        }
        return gF2Vector;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static byte[] signConversion(int n, int n2, byte[] arrby) {
        int n3;
        int n4;
        byte[] arrby2;
        int n5;
        int n6 = 8;
        if (n < n2) {
            throw new IllegalArgumentException("n < t");
        }
        BigInteger bigInteger = IntegerFunctions.binomial(n, n2);
        int n7 = -1 + bigInteger.bitLength();
        int n8 = n7 >> 3;
        int n9 = n7 & 7;
        if (n9 == 0) {
            int n10 = n8 - 1;
            n3 = n6;
            n4 = n10;
        } else {
            n3 = n9;
            n4 = n8;
        }
        int n11 = n >> 3;
        int n12 = n & 7;
        if (n12 == 0) {
            n5 = n11 - 1;
        } else {
            n6 = n12;
            n5 = n11;
        }
        if (arrby.length < (arrby2 = new byte[n5 + 1]).length) {
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby.length);
            for (int i = arrby.length; i < arrby2.length; ++i) {
                arrby2[i] = 0;
            }
        } else {
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)n5);
            arrby2[n5] = (byte)(-1 + (1 << n6) & arrby[n5]);
        }
        BigInteger bigInteger2 = ZERO;
        int n13 = n2;
        int n14 = n;
        BigInteger bigInteger3 = bigInteger;
        for (int i = 0; i < n; ++i) {
            bigInteger3 = bigInteger3.multiply(new BigInteger(Integer.toString((int)(n14 - n13)))).divide(new BigInteger(Integer.toString((int)n14)));
            --n14;
            int n15 = i >>> 3;
            if ((byte)(1 << (i & 7) & arrby2[n15]) == 0) continue;
            bigInteger2 = bigInteger2.add(bigInteger3);
            bigInteger3 = n14 == --n13 ? ONE : bigInteger3.multiply(new BigInteger(Integer.toString((int)(n13 + 1)))).divide(new BigInteger(Integer.toString((int)(n14 - n13))));
        }
        byte[] arrby3 = new byte[n4 + 1];
        byte[] arrby4 = bigInteger2.toByteArray();
        if (arrby4.length < arrby3.length) {
            System.arraycopy((Object)arrby4, (int)0, (Object)arrby3, (int)0, (int)arrby4.length);
            for (int i = arrby4.length; i < arrby3.length; ++i) {
                arrby3[i] = 0;
            }
            return arrby3;
        } else {
            System.arraycopy((Object)arrby4, (int)0, (Object)arrby3, (int)0, (int)n4);
            arrby3[n4] = (byte)(-1 + (1 << n3) & arrby4[n4]);
        }
        return arrby3;
    }
}

