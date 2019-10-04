/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.util;

public abstract class Pack {
    public static int bigEndianToInt(byte[] arrby, int n) {
        int n2 = arrby[n] << 24;
        int n3 = n + 1;
        int n4 = n2 | (255 & arrby[n3]) << 16;
        int n5 = n3 + 1;
        return n4 | (255 & arrby[n5]) << 8 | 255 & arrby[n5 + 1];
    }

    public static void bigEndianToInt(byte[] arrby, int n, int[] arrn) {
        for (int i = 0; i < arrn.length; ++i) {
            arrn[i] = Pack.bigEndianToInt(arrby, n);
            n += 4;
        }
    }

    public static long bigEndianToLong(byte[] arrby, int n) {
        int n2 = Pack.bigEndianToInt(arrby, n);
        int n3 = Pack.bigEndianToInt(arrby, n + 4);
        return (0xFFFFFFFFL & (long)n2) << 32 | 0xFFFFFFFFL & (long)n3;
    }

    public static void bigEndianToLong(byte[] arrby, int n, long[] arrl) {
        for (int i = 0; i < arrl.length; ++i) {
            arrl[i] = Pack.bigEndianToLong(arrby, n);
            n += 8;
        }
    }

    public static void intToBigEndian(int n, byte[] arrby, int n2) {
        arrby[n2] = (byte)(n >>> 24);
        int n3 = n2 + 1;
        arrby[n3] = (byte)(n >>> 16);
        int n4 = n3 + 1;
        arrby[n4] = (byte)(n >>> 8);
        arrby[n4 + 1] = (byte)n;
    }

    public static void intToBigEndian(int[] arrn, byte[] arrby, int n) {
        for (int i = 0; i < arrn.length; ++i) {
            Pack.intToBigEndian(arrn[i], arrby, n);
            n += 4;
        }
    }

    public static byte[] intToBigEndian(int n) {
        byte[] arrby = new byte[4];
        Pack.intToBigEndian(n, arrby, 0);
        return arrby;
    }

    public static byte[] intToBigEndian(int[] arrn) {
        byte[] arrby = new byte[4 * arrn.length];
        Pack.intToBigEndian(arrn, arrby, 0);
        return arrby;
    }

    public static void intToLittleEndian(int n, byte[] arrby, int n2) {
        arrby[n2] = (byte)n;
        int n3 = n2 + 1;
        arrby[n3] = (byte)(n >>> 8);
        int n4 = n3 + 1;
        arrby[n4] = (byte)(n >>> 16);
        arrby[n4 + 1] = (byte)(n >>> 24);
    }

    public static void intToLittleEndian(int[] arrn, byte[] arrby, int n) {
        for (int i = 0; i < arrn.length; ++i) {
            Pack.intToLittleEndian(arrn[i], arrby, n);
            n += 4;
        }
    }

    public static byte[] intToLittleEndian(int n) {
        byte[] arrby = new byte[4];
        Pack.intToLittleEndian(n, arrby, 0);
        return arrby;
    }

    public static byte[] intToLittleEndian(int[] arrn) {
        byte[] arrby = new byte[4 * arrn.length];
        Pack.intToLittleEndian(arrn, arrby, 0);
        return arrby;
    }

    public static int littleEndianToInt(byte[] arrby, int n) {
        int n2 = 255 & arrby[n];
        int n3 = n + 1;
        int n4 = n2 | (255 & arrby[n3]) << 8;
        int n5 = n3 + 1;
        return n4 | (255 & arrby[n5]) << 16 | arrby[n5 + 1] << 24;
    }

    public static void littleEndianToInt(byte[] arrby, int n, int[] arrn) {
        for (int i = 0; i < arrn.length; ++i) {
            arrn[i] = Pack.littleEndianToInt(arrby, n);
            n += 4;
        }
    }

    public static void littleEndianToInt(byte[] arrby, int n, int[] arrn, int n2, int n3) {
        for (int i = 0; i < n3; ++i) {
            arrn[n2 + i] = Pack.littleEndianToInt(arrby, n);
            n += 4;
        }
    }

    public static long littleEndianToLong(byte[] arrby, int n) {
        int n2 = Pack.littleEndianToInt(arrby, n);
        return (0xFFFFFFFFL & (long)Pack.littleEndianToInt(arrby, n + 4)) << 32 | 0xFFFFFFFFL & (long)n2;
    }

    public static void littleEndianToLong(byte[] arrby, int n, long[] arrl) {
        for (int i = 0; i < arrl.length; ++i) {
            arrl[i] = Pack.littleEndianToLong(arrby, n);
            n += 8;
        }
    }

    public static void longToBigEndian(long l, byte[] arrby, int n) {
        Pack.intToBigEndian((int)(l >>> 32), arrby, n);
        Pack.intToBigEndian((int)(0xFFFFFFFFL & l), arrby, n + 4);
    }

    public static void longToBigEndian(long[] arrl, byte[] arrby, int n) {
        for (int i = 0; i < arrl.length; ++i) {
            Pack.longToBigEndian(arrl[i], arrby, n);
            n += 8;
        }
    }

    public static byte[] longToBigEndian(long l) {
        byte[] arrby = new byte[8];
        Pack.longToBigEndian(l, arrby, 0);
        return arrby;
    }

    public static byte[] longToBigEndian(long[] arrl) {
        byte[] arrby = new byte[8 * arrl.length];
        Pack.longToBigEndian(arrl, arrby, 0);
        return arrby;
    }

    public static void longToLittleEndian(long l, byte[] arrby, int n) {
        Pack.intToLittleEndian((int)(0xFFFFFFFFL & l), arrby, n);
        Pack.intToLittleEndian((int)(l >>> 32), arrby, n + 4);
    }

    public static void longToLittleEndian(long[] arrl, byte[] arrby, int n) {
        for (int i = 0; i < arrl.length; ++i) {
            Pack.longToLittleEndian(arrl[i], arrby, n);
            n += 8;
        }
    }

    public static byte[] longToLittleEndian(long l) {
        byte[] arrby = new byte[8];
        Pack.longToLittleEndian(l, arrby, 0);
        return arrby;
    }

    public static byte[] longToLittleEndian(long[] arrl) {
        byte[] arrby = new byte[8 * arrl.length];
        Pack.longToLittleEndian(arrl, arrby, 0);
        return arrby;
    }
}

