/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArithmeticException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.pqc.math.linearalgebra;

import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;

public final class BigEndianConversions {
    private BigEndianConversions() {
    }

    public static void I2OSP(int n, byte[] arrby, int n2) {
        int n3 = n2 + 1;
        arrby[n2] = (byte)(n >>> 24);
        int n4 = n3 + 1;
        arrby[n3] = (byte)(n >>> 16);
        int n5 = n4 + 1;
        arrby[n4] = (byte)(n >>> 8);
        arrby[n5] = (byte)n;
    }

    public static void I2OSP(int n, byte[] arrby, int n2, int n3) {
        for (int i = n3 - 1; i >= 0; --i) {
            arrby[n2 + i] = (byte)(n >>> 8 * (n3 - 1 - i));
        }
    }

    public static void I2OSP(long l, byte[] arrby, int n) {
        int n2 = n + 1;
        arrby[n] = (byte)(l >>> 56);
        int n3 = n2 + 1;
        arrby[n2] = (byte)(l >>> 48);
        int n4 = n3 + 1;
        arrby[n3] = (byte)(l >>> 40);
        int n5 = n4 + 1;
        arrby[n4] = (byte)(l >>> 32);
        int n6 = n5 + 1;
        arrby[n5] = (byte)(l >>> 24);
        int n7 = n6 + 1;
        arrby[n6] = (byte)(l >>> 16);
        int n8 = n7 + 1;
        arrby[n7] = (byte)(l >>> 8);
        arrby[n8] = (byte)l;
    }

    public static byte[] I2OSP(int n) {
        byte[] arrby = new byte[]{(byte)(n >>> 24), (byte)(n >>> 16), (byte)(n >>> 8), (byte)n};
        return arrby;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static byte[] I2OSP(int n, int n2) {
        if (n < 0) {
            return null;
        }
        int n3 = IntegerFunctions.ceilLog256(n);
        if (n3 > n2) {
            throw new ArithmeticException("Cannot encode given integer into specified number of octets.");
        }
        byte[] arrby = new byte[n2];
        int n4 = n2 - 1;
        while (n4 >= n2 - n3) {
            arrby[n4] = (byte)(n >>> 8 * (n2 - 1 - n4));
            --n4;
        }
        return arrby;
    }

    public static byte[] I2OSP(long l) {
        byte[] arrby = new byte[]{(byte)(l >>> 56), (byte)(l >>> 48), (byte)(l >>> 40), (byte)(l >>> 32), (byte)(l >>> 24), (byte)(l >>> 16), (byte)(l >>> 8), (byte)l};
        return arrby;
    }

    public static int OS2IP(byte[] arrby) {
        if (arrby.length > 4) {
            throw new ArithmeticException("invalid input length");
        }
        if (arrby.length == 0) {
            return 0;
        }
        int n = 0;
        for (int i = 0; i < arrby.length; ++i) {
            n |= (255 & arrby[i]) << 8 * (-1 + arrby.length - i);
        }
        return n;
    }

    public static int OS2IP(byte[] arrby, int n) {
        int n2 = n + 1;
        int n3 = (255 & arrby[n]) << 24;
        int n4 = n2 + 1;
        int n5 = n3 | (255 & arrby[n2]) << 16;
        int n6 = n4 + 1;
        return n5 | (255 & arrby[n4]) << 8 | 255 & arrby[n6];
    }

    /*
     * Enabled aggressive block sorting
     */
    public static int OS2IP(byte[] arrby, int n, int n2) {
        int n3 = arrby.length;
        int n4 = 0;
        if (n3 != 0) {
            int n5 = arrby.length;
            int n6 = -1 + (n + n2);
            n4 = 0;
            if (n5 >= n6) {
                for (int i = 0; i < n2; ++i) {
                    int n7 = n4 | (255 & arrby[n + i]) << 8 * (-1 + (n2 - i));
                    n4 = n7;
                }
            }
        }
        return n4;
    }

    public static long OS2LIP(byte[] arrby, int n) {
        int n2 = n + 1;
        long l = (255L & (long)arrby[n]) << 56;
        int n3 = n2 + 1;
        long l2 = l | (255L & (long)arrby[n2]) << 48;
        int n4 = n3 + 1;
        long l3 = l2 | (255L & (long)arrby[n3]) << 40;
        int n5 = n4 + 1;
        long l4 = l3 | (255L & (long)arrby[n4]) << 32;
        int n6 = n5 + 1;
        long l5 = l4 | (255L & (long)arrby[n5]) << 24;
        int n7 = n6 + 1;
        long l6 = l5 | (long)((255 & arrby[n6]) << 16);
        int n8 = n7 + 1;
        return l6 | (long)((255 & arrby[n7]) << 8) | (long)(255 & arrby[n8]);
    }

    public static byte[] toByteArray(int[] arrn) {
        byte[] arrby = new byte[arrn.length << 2];
        for (int i = 0; i < arrn.length; ++i) {
            BigEndianConversions.I2OSP(arrn[i], arrby, i << 2);
        }
        return arrby;
    }

    public static byte[] toByteArray(int[] arrn, int n) {
        int n2 = 0;
        int n3 = arrn.length;
        byte[] arrby = new byte[n];
        int n4 = 0;
        while (n2 <= n3 - 2) {
            BigEndianConversions.I2OSP(arrn[n2], arrby, n4);
            ++n2;
            n4 += 4;
        }
        BigEndianConversions.I2OSP(arrn[n3 - 1], arrby, n4, n - n4);
        return arrby;
    }

    public static int[] toIntArray(byte[] arrby) {
        int n = 0;
        int n2 = (3 + arrby.length) / 4;
        int n3 = 3 & arrby.length;
        int[] arrn = new int[n2];
        int n4 = 0;
        while (n <= n2 - 2) {
            arrn[n] = BigEndianConversions.OS2IP(arrby, n4);
            ++n;
            n4 += 4;
        }
        if (n3 != 0) {
            arrn[n2 - 1] = BigEndianConversions.OS2IP(arrby, n4, n3);
            return arrn;
        }
        arrn[n2 - 1] = BigEndianConversions.OS2IP(arrby, n4);
        return arrn;
    }
}

