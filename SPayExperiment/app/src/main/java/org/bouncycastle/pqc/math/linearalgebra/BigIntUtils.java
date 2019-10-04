/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  java.math.BigInteger
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.math.BigInteger;

public final class BigIntUtils {
    private BigIntUtils() {
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean equals(BigInteger[] arrbigInteger, BigInteger[] arrbigInteger2) {
        block4 : {
            block3 : {
                if (arrbigInteger.length != arrbigInteger2.length) break block3;
                int n = 0;
                for (int i = 0; i < arrbigInteger.length; n |= arrbigInteger[i].compareTo((BigInteger)arrbigInteger2[i]), ++i) {
                }
                if (n == 0) break block4;
            }
            return false;
        }
        return true;
    }

    public static void fill(BigInteger[] arrbigInteger, BigInteger bigInteger) {
        for (int i = -1 + arrbigInteger.length; i >= 0; --i) {
            arrbigInteger[i] = bigInteger;
        }
    }

    public static BigInteger[] subArray(BigInteger[] arrbigInteger, int n, int n2) {
        BigInteger[] arrbigInteger2 = new BigInteger[n2 - n];
        System.arraycopy((Object)arrbigInteger, (int)n, (Object)arrbigInteger2, (int)0, (int)(n2 - n));
        return arrbigInteger2;
    }

    public static int[] toIntArray(BigInteger[] arrbigInteger) {
        int[] arrn = new int[arrbigInteger.length];
        for (int i = 0; i < arrbigInteger.length; ++i) {
            arrn[i] = arrbigInteger[i].intValue();
        }
        return arrn;
    }

    public static int[] toIntArrayModQ(int n, BigInteger[] arrbigInteger) {
        BigInteger bigInteger = BigInteger.valueOf((long)n);
        int[] arrn = new int[arrbigInteger.length];
        for (int i = 0; i < arrbigInteger.length; ++i) {
            arrn[i] = arrbigInteger[i].mod(bigInteger).intValue();
        }
        return arrn;
    }

    public static byte[] toMinimalByteArray(BigInteger bigInteger) {
        byte[] arrby = bigInteger.toByteArray();
        if (arrby.length == 1 || (7 & bigInteger.bitLength()) != 0) {
            return arrby;
        }
        byte[] arrby2 = new byte[bigInteger.bitLength() >> 3];
        System.arraycopy((Object)arrby, (int)1, (Object)arrby2, (int)0, (int)arrby2.length);
        return arrby2;
    }
}

