/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.util;

public abstract class Pack {
    public static int bigEndianToInt(byte[] arrby, int n2) {
        int n3 = arrby[n2] << 24;
        int n4 = n2 + 1;
        int n5 = n3 | (255 & arrby[n4]) << 16;
        int n6 = n4 + 1;
        return n5 | (255 & arrby[n6]) << 8 | 255 & arrby[n6 + 1];
    }

    public static void bigEndianToInt(byte[] arrby, int n2, int[] arrn) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            arrn[i2] = Pack.bigEndianToInt(arrby, n2);
            n2 += 4;
        }
    }

    public static long bigEndianToLong(byte[] arrby, int n2) {
        int n3 = Pack.bigEndianToInt(arrby, n2);
        int n4 = Pack.bigEndianToInt(arrby, n2 + 4);
        return (0xFFFFFFFFL & (long)n3) << 32 | 0xFFFFFFFFL & (long)n4;
    }

    public static void bigEndianToLong(byte[] arrby, int n2, long[] arrl) {
        for (int i2 = 0; i2 < arrl.length; ++i2) {
            arrl[i2] = Pack.bigEndianToLong(arrby, n2);
            n2 += 8;
        }
    }

    public static void intToBigEndian(int n2, byte[] arrby, int n3) {
        arrby[n3] = (byte)(n2 >>> 24);
        int n4 = n3 + 1;
        arrby[n4] = (byte)(n2 >>> 16);
        int n5 = n4 + 1;
        arrby[n5] = (byte)(n2 >>> 8);
        arrby[n5 + 1] = (byte)n2;
    }

    public static void intToBigEndian(int[] arrn, byte[] arrby, int n2) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            Pack.intToBigEndian(arrn[i2], arrby, n2);
            n2 += 4;
        }
    }

    public static byte[] intToBigEndian(int n2) {
        byte[] arrby = new byte[4];
        Pack.intToBigEndian(n2, arrby, 0);
        return arrby;
    }

    public static byte[] intToBigEndian(int[] arrn) {
        byte[] arrby = new byte[4 * arrn.length];
        Pack.intToBigEndian(arrn, arrby, 0);
        return arrby;
    }

    public static void intToLittleEndian(int n2, byte[] arrby, int n3) {
        arrby[n3] = (byte)n2;
        int n4 = n3 + 1;
        arrby[n4] = (byte)(n2 >>> 8);
        int n5 = n4 + 1;
        arrby[n5] = (byte)(n2 >>> 16);
        arrby[n5 + 1] = (byte)(n2 >>> 24);
    }

    public static void intToLittleEndian(int[] arrn, byte[] arrby, int n2) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            Pack.intToLittleEndian(arrn[i2], arrby, n2);
            n2 += 4;
        }
    }

    public static byte[] intToLittleEndian(int n2) {
        byte[] arrby = new byte[4];
        Pack.intToLittleEndian(n2, arrby, 0);
        return arrby;
    }

    public static byte[] intToLittleEndian(int[] arrn) {
        byte[] arrby = new byte[4 * arrn.length];
        Pack.intToLittleEndian(arrn, arrby, 0);
        return arrby;
    }

    public static int littleEndianToInt(byte[] arrby, int n2) {
        int n3 = 255 & arrby[n2];
        int n4 = n2 + 1;
        int n5 = n3 | (255 & arrby[n4]) << 8;
        int n6 = n4 + 1;
        return n5 | (255 & arrby[n6]) << 16 | arrby[n6 + 1] << 24;
    }

    public static void littleEndianToInt(byte[] arrby, int n2, int[] arrn) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            arrn[i2] = Pack.littleEndianToInt(arrby, n2);
            n2 += 4;
        }
    }

    public static void littleEndianToInt(byte[] arrby, int n2, int[] arrn, int n3, int n4) {
        for (int i2 = 0; i2 < n4; ++i2) {
            arrn[n3 + i2] = Pack.littleEndianToInt(arrby, n2);
            n2 += 4;
        }
    }

    public static long littleEndianToLong(byte[] arrby, int n2) {
        int n3 = Pack.littleEndianToInt(arrby, n2);
        return (0xFFFFFFFFL & (long)Pack.littleEndianToInt(arrby, n2 + 4)) << 32 | 0xFFFFFFFFL & (long)n3;
    }

    public static void littleEndianToLong(byte[] arrby, int n2, long[] arrl) {
        for (int i2 = 0; i2 < arrl.length; ++i2) {
            arrl[i2] = Pack.littleEndianToLong(arrby, n2);
            n2 += 8;
        }
    }

    public static void longToBigEndian(long l2, byte[] arrby, int n2) {
        Pack.intToBigEndian((int)(l2 >>> 32), arrby, n2);
        Pack.intToBigEndian((int)(0xFFFFFFFFL & l2), arrby, n2 + 4);
    }

    public static void longToBigEndian(long[] arrl, byte[] arrby, int n2) {
        for (int i2 = 0; i2 < arrl.length; ++i2) {
            Pack.longToBigEndian(arrl[i2], arrby, n2);
            n2 += 8;
        }
    }

    public static byte[] longToBigEndian(long l2) {
        byte[] arrby = new byte[8];
        Pack.longToBigEndian(l2, arrby, 0);
        return arrby;
    }

    public static byte[] longToBigEndian(long[] arrl) {
        byte[] arrby = new byte[8 * arrl.length];
        Pack.longToBigEndian(arrl, arrby, 0);
        return arrby;
    }

    public static void longToLittleEndian(long l2, byte[] arrby, int n2) {
        Pack.intToLittleEndian((int)(0xFFFFFFFFL & l2), arrby, n2);
        Pack.intToLittleEndian((int)(l2 >>> 32), arrby, n2 + 4);
    }

    public static void longToLittleEndian(long[] arrl, byte[] arrby, int n2) {
        for (int i2 = 0; i2 < arrl.length; ++i2) {
            Pack.longToLittleEndian(arrl[i2], arrby, n2);
            n2 += 8;
        }
    }

    public static byte[] longToLittleEndian(long l2) {
        byte[] arrby = new byte[8];
        Pack.longToLittleEndian(l2, arrby, 0);
        return arrby;
    }

    public static byte[] longToLittleEndian(long[] arrl) {
        byte[] arrby = new byte[8 * arrl.length];
        Pack.longToLittleEndian(arrl, arrby, 0);
        return arrby;
    }
}

