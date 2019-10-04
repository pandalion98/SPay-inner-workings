/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.pqc.math.linearalgebra;

public final class LittleEndianConversions {
    private LittleEndianConversions() {
    }

    public static void I2OSP(int n, byte[] arrby, int n2) {
        int n3 = n2 + 1;
        arrby[n2] = (byte)n;
        int n4 = n3 + 1;
        arrby[n3] = (byte)(n >>> 8);
        int n5 = n4 + 1;
        arrby[n4] = (byte)(n >>> 16);
        n5 + 1;
        arrby[n5] = (byte)(n >>> 24);
    }

    public static void I2OSP(int n, byte[] arrby, int n2, int n3) {
        for (int i = n3 - 1; i >= 0; --i) {
            arrby[n2 + i] = (byte)(n >>> i * 8);
        }
    }

    public static void I2OSP(long l, byte[] arrby, int n) {
        int n2 = n + 1;
        arrby[n] = (byte)l;
        int n3 = n2 + 1;
        arrby[n2] = (byte)(l >>> 8);
        int n4 = n3 + 1;
        arrby[n3] = (byte)(l >>> 16);
        int n5 = n4 + 1;
        arrby[n4] = (byte)(l >>> 24);
        int n6 = n5 + 1;
        arrby[n5] = (byte)(l >>> 32);
        int n7 = n6 + 1;
        arrby[n6] = (byte)(l >>> 40);
        int n8 = n7 + 1;
        arrby[n7] = (byte)(l >>> 48);
        arrby[n8] = (byte)(l >>> 56);
    }

    public static byte[] I2OSP(int n) {
        byte[] arrby = new byte[]{(byte)n, (byte)(n >>> 8), (byte)(n >>> 16), (byte)(n >>> 24)};
        return arrby;
    }

    public static byte[] I2OSP(long l) {
        byte[] arrby = new byte[]{(byte)l, (byte)(l >>> 8), (byte)(l >>> 16), (byte)(l >>> 24), (byte)(l >>> 32), (byte)(l >>> 40), (byte)(l >>> 48), (byte)(l >>> 56)};
        return arrby;
    }

    public static int OS2IP(byte[] arrby) {
        return 255 & arrby[0] | (255 & arrby[1]) << 8 | (255 & arrby[2]) << 16 | (255 & arrby[3]) << 24;
    }

    public static int OS2IP(byte[] arrby, int n) {
        int n2 = n + 1;
        int n3 = 255 & arrby[n];
        int n4 = n2 + 1;
        int n5 = n3 | (255 & arrby[n2]) << 8;
        int n6 = n4 + 1;
        return n5 | (255 & arrby[n4]) << 16 | (255 & arrby[n6]) << 24;
    }

    public static int OS2IP(byte[] arrby, int n, int n2) {
        int n3 = 0;
        for (int i = n2 - 1; i >= 0; --i) {
            n3 |= (255 & arrby[n + i]) << i * 8;
        }
        return n3;
    }

    public static long OS2LIP(byte[] arrby, int n) {
        int n2 = n + 1;
        long l = 255 & arrby[n];
        int n3 = n2 + 1;
        long l2 = l | (long)((255 & arrby[n2]) << 8);
        int n4 = n3 + 1;
        long l3 = l2 | (long)((255 & arrby[n3]) << 16);
        int n5 = n4 + 1;
        long l4 = l3 | (255L & (long)arrby[n4]) << 24;
        int n6 = n5 + 1;
        long l5 = l4 | (255L & (long)arrby[n5]) << 32;
        int n7 = n6 + 1;
        long l6 = l5 | (255L & (long)arrby[n6]) << 40;
        int n8 = n7 + 1;
        long l7 = l6 | (255L & (long)arrby[n7]) << 48;
        n8 + 1;
        return l7 | (255L & (long)arrby[n8]) << 56;
    }

    public static byte[] toByteArray(int[] arrn, int n) {
        int n2 = 0;
        int n3 = arrn.length;
        byte[] arrby = new byte[n];
        int n4 = 0;
        while (n2 <= n3 - 2) {
            LittleEndianConversions.I2OSP(arrn[n2], arrby, n4);
            ++n2;
            n4 += 4;
        }
        LittleEndianConversions.I2OSP(arrn[n3 - 1], arrby, n4, n - n4);
        return arrby;
    }

    public static int[] toIntArray(byte[] arrby) {
        int n = 0;
        int n2 = (3 + arrby.length) / 4;
        int n3 = 3 & arrby.length;
        int[] arrn = new int[n2];
        int n4 = 0;
        while (n <= n2 - 2) {
            arrn[n] = LittleEndianConversions.OS2IP(arrby, n4);
            ++n;
            n4 += 4;
        }
        if (n3 != 0) {
            arrn[n2 - 1] = LittleEndianConversions.OS2IP(arrby, n4, n3);
            return arrn;
        }
        arrn[n2 - 1] = LittleEndianConversions.OS2IP(arrby, n4);
        return arrn;
    }
}

