/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.modes.gcm;

import org.bouncycastle.util.Pack;

public abstract class GCMUtil {
    private static final int E1 = -520093696;
    private static final long E1L = -2233785415175766016L;
    private static final int[] LOOKUP = GCMUtil.generateLookup();

    public static void asBytes(int[] arrn, byte[] arrby) {
        Pack.intToBigEndian((int[])arrn, (byte[])arrby, (int)0);
    }

    public static void asBytes(long[] arrl, byte[] arrby) {
        Pack.longToBigEndian((long[])arrl, (byte[])arrby, (int)0);
    }

    public static byte[] asBytes(int[] arrn) {
        byte[] arrby = new byte[16];
        Pack.intToBigEndian((int[])arrn, (byte[])arrby, (int)0);
        return arrby;
    }

    public static byte[] asBytes(long[] arrl) {
        byte[] arrby = new byte[16];
        Pack.longToBigEndian((long[])arrl, (byte[])arrby, (int)0);
        return arrby;
    }

    public static void asInts(byte[] arrby, int[] arrn) {
        Pack.bigEndianToInt((byte[])arrby, (int)0, (int[])arrn);
    }

    public static int[] asInts(byte[] arrby) {
        int[] arrn = new int[4];
        Pack.bigEndianToInt((byte[])arrby, (int)0, (int[])arrn);
        return arrn;
    }

    public static void asLongs(byte[] arrby, long[] arrl) {
        Pack.bigEndianToLong((byte[])arrby, (int)0, (long[])arrl);
    }

    public static long[] asLongs(byte[] arrby) {
        long[] arrl = new long[2];
        Pack.bigEndianToLong((byte[])arrby, (int)0, (long[])arrl);
        return arrl;
    }

    private static int[] generateLookup() {
        int[] arrn = new int[256];
        for (int i2 = 0; i2 < 256; ++i2) {
            int n2 = 0;
            for (int i3 = 7; i3 >= 0; --i3) {
                if ((i2 & 1 << i3) == 0) continue;
                n2 ^= -520093696 >>> 7 - i3;
            }
            arrn[i2] = n2;
        }
        return arrn;
    }

    public static void multiply(byte[] arrby, byte[] arrby2) {
        int[] arrn = GCMUtil.asInts(arrby);
        GCMUtil.multiply(arrn, GCMUtil.asInts(arrby2));
        GCMUtil.asBytes(arrn, arrby);
    }

    public static void multiply(int[] arrn, int[] arrn2) {
        int n2 = arrn[0];
        int n3 = arrn[1];
        int n4 = arrn[2];
        int n5 = arrn[3];
        int n6 = 0;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        for (int i2 = 0; i2 < 4; ++i2) {
            int n10 = arrn2[i2];
            for (int i3 = 0; i3 < 32; ++i3) {
                int n11 = n10 >> 31;
                n10 <<= 1;
                n6 ^= n2 & n11;
                n7 ^= n3 & n11;
                n8 ^= n4 & n11;
                n9 ^= n11 & n5;
                int n12 = n5 << 31 >> 8;
                n5 = n5 >>> 1 | n4 << 63;
                n4 = n4 >>> 1 | n3 << 63;
                n3 = n3 >>> 1 | n2 << 63;
                n2 = n2 >>> 1 ^ n12 & -520093696;
            }
        }
        arrn[0] = n6;
        arrn[1] = n7;
        arrn[2] = n8;
        arrn[3] = n9;
    }

    public static void multiply(long[] arrl, long[] arrl2) {
        long l2 = arrl[0];
        long l3 = arrl[1];
        long l4 = 0L;
        long l5 = 0L;
        for (int i2 = 0; i2 < 2; ++i2) {
            long l6 = arrl2[i2];
            for (int i3 = 0; i3 < 64; ++i3) {
                long l7 = l6 >> 63;
                l6 <<= 1;
                l4 ^= l2 & l7;
                l5 ^= l7 & l3;
                long l8 = l3 << 63 >> 8;
                l3 = l3 >>> 1 | l2 << 63;
                l2 = l2 >>> 1 ^ l8 & -2233785415175766016L;
            }
        }
        arrl[0] = l4;
        arrl[1] = l5;
    }

    public static void multiplyP(int[] arrn) {
        int n2 = GCMUtil.shiftRight(arrn) >> 8;
        arrn[0] = arrn[0] ^ n2 & -520093696;
    }

    public static void multiplyP(int[] arrn, int[] arrn2) {
        int n2 = GCMUtil.shiftRight(arrn, arrn2) >> 8;
        arrn2[0] = arrn2[0] ^ n2 & -520093696;
    }

    public static void multiplyP8(int[] arrn) {
        int n2 = GCMUtil.shiftRightN(arrn, 8);
        arrn[0] = arrn[0] ^ LOOKUP[n2 >>> 24];
    }

    public static void multiplyP8(int[] arrn, int[] arrn2) {
        int n2 = GCMUtil.shiftRightN(arrn, 8, arrn2);
        arrn2[0] = arrn2[0] ^ LOOKUP[n2 >>> 24];
    }

    public static byte[] oneAsBytes() {
        byte[] arrby = new byte[16];
        arrby[0] = -128;
        return arrby;
    }

    public static int[] oneAsInts() {
        int[] arrn = new int[4];
        arrn[0] = Integer.MIN_VALUE;
        return arrn;
    }

    public static long[] oneAsLongs() {
        long[] arrl = new long[2];
        arrl[0] = Long.MIN_VALUE;
        return arrl;
    }

    static int shiftRight(int[] arrn) {
        int n2 = arrn[0];
        arrn[0] = n2 >>> 1;
        int n3 = n2 << 31;
        int n4 = arrn[1];
        arrn[1] = n3 | n4 >>> 1;
        int n5 = n4 << 31;
        int n6 = arrn[2];
        arrn[2] = n5 | n6 >>> 1;
        int n7 = n6 << 31;
        int n8 = arrn[3];
        arrn[3] = n7 | n8 >>> 1;
        return n8 << 31;
    }

    static int shiftRight(int[] arrn, int[] arrn2) {
        int n2 = arrn[0];
        arrn2[0] = n2 >>> 1;
        int n3 = n2 << 31;
        int n4 = arrn[1];
        arrn2[1] = n3 | n4 >>> 1;
        int n5 = n4 << 31;
        int n6 = arrn[2];
        arrn2[2] = n5 | n6 >>> 1;
        int n7 = n6 << 31;
        int n8 = arrn[3];
        arrn2[3] = n7 | n8 >>> 1;
        return n8 << 31;
    }

    static long shiftRight(long[] arrl) {
        long l2 = arrl[0];
        arrl[0] = l2 >>> 1;
        long l3 = l2 << 63;
        long l4 = arrl[1];
        arrl[1] = l3 | l4 >>> 1;
        return l4 << 63;
    }

    static long shiftRight(long[] arrl, long[] arrl2) {
        long l2 = arrl[0];
        arrl2[0] = l2 >>> 1;
        long l3 = l2 << 63;
        long l4 = arrl[1];
        arrl2[1] = l3 | l4 >>> 1;
        return l4 << 63;
    }

    static int shiftRightN(int[] arrn, int n2) {
        int n3 = arrn[0];
        int n4 = 32 - n2;
        arrn[0] = n3 >>> n2;
        int n5 = n3 << n4;
        int n6 = arrn[1];
        arrn[1] = n5 | n6 >>> n2;
        int n7 = n6 << n4;
        int n8 = arrn[2];
        arrn[2] = n7 | n8 >>> n2;
        int n9 = n8 << n4;
        int n10 = arrn[3];
        arrn[3] = n9 | n10 >>> n2;
        return n10 << n4;
    }

    static int shiftRightN(int[] arrn, int n2, int[] arrn2) {
        int n3 = arrn[0];
        int n4 = 32 - n2;
        arrn2[0] = n3 >>> n2;
        int n5 = n3 << n4;
        int n6 = arrn[1];
        arrn2[1] = n5 | n6 >>> n2;
        int n7 = n6 << n4;
        int n8 = arrn[2];
        arrn2[2] = n7 | n8 >>> n2;
        int n9 = n8 << n4;
        int n10 = arrn[3];
        arrn2[3] = n9 | n10 >>> n2;
        return n10 << n4;
    }

    public static void xor(byte[] arrby, byte[] arrby2) {
        int n2;
        int n3 = 0;
        do {
            arrby[n3] = (byte)(arrby[n3] ^ arrby2[n3]);
            int n4 = n3 + 1;
            arrby[n4] = (byte)(arrby[n4] ^ arrby2[n4]);
            int n5 = n4 + 1;
            arrby[n5] = (byte)(arrby[n5] ^ arrby2[n5]);
            n2 = n5 + 1;
            arrby[n2] = (byte)(arrby[n2] ^ arrby2[n2]);
        } while ((n3 = n2 + 1) < 16);
    }

    public static void xor(byte[] arrby, byte[] arrby2, int n2, int n3) {
        while (--n3 >= 0) {
            arrby[n3] = (byte)(arrby[n3] ^ arrby2[n2 + n3]);
        }
    }

    public static void xor(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        int n2;
        int n3 = 0;
        do {
            arrby3[n3] = (byte)(arrby[n3] ^ arrby2[n3]);
            int n4 = n3 + 1;
            arrby3[n4] = (byte)(arrby[n4] ^ arrby2[n4]);
            int n5 = n4 + 1;
            arrby3[n5] = (byte)(arrby[n5] ^ arrby2[n5]);
            n2 = n5 + 1;
            arrby3[n2] = (byte)(arrby[n2] ^ arrby2[n2]);
        } while ((n3 = n2 + 1) < 16);
    }

    public static void xor(int[] arrn, int[] arrn2) {
        arrn[0] = arrn[0] ^ arrn2[0];
        arrn[1] = arrn[1] ^ arrn2[1];
        arrn[2] = arrn[2] ^ arrn2[2];
        arrn[3] = arrn[3] ^ arrn2[3];
    }

    public static void xor(int[] arrn, int[] arrn2, int[] arrn3) {
        arrn3[0] = arrn[0] ^ arrn2[0];
        arrn3[1] = arrn[1] ^ arrn2[1];
        arrn3[2] = arrn[2] ^ arrn2[2];
        arrn3[3] = arrn[3] ^ arrn2[3];
    }

    public static void xor(long[] arrl, long[] arrl2) {
        arrl[0] = arrl[0] ^ arrl2[0];
        arrl[1] = arrl[1] ^ arrl2[1];
    }

    public static void xor(long[] arrl, long[] arrl2, long[] arrl3) {
        arrl3[0] = arrl[0] ^ arrl2[0];
        arrl3[1] = arrl[1] ^ arrl2[1];
    }
}

