/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.Short
 *  java.lang.reflect.Array
 */
package org.bouncycastle.pqc.crypto.rainbow.util;

import java.lang.reflect.Array;

public class RainbowUtil {
    public static byte[] convertArray(short[] arrs) {
        byte[] arrby = new byte[arrs.length];
        for (int i = 0; i < arrs.length; ++i) {
            arrby[i] = (byte)arrs[i];
        }
        return arrby;
    }

    public static short[] convertArray(byte[] arrby) {
        short[] arrs = new short[arrby.length];
        for (int i = 0; i < arrby.length; ++i) {
            arrs[i] = (short)(255 & arrby[i]);
        }
        return arrs;
    }

    public static byte[][] convertArray(short[][] arrs) {
        int[] arrn = new int[]{arrs.length, arrs[0].length};
        byte[][] arrby = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        for (int i = 0; i < arrs.length; ++i) {
            for (int j = 0; j < arrs[0].length; ++j) {
                arrby[i][j] = (byte)arrs[i][j];
            }
        }
        return arrby;
    }

    public static short[][] convertArray(byte[][] arrby) {
        int[] arrn = new int[]{arrby.length, arrby[0].length};
        short[][] arrs = (short[][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        for (int i = 0; i < arrby.length; ++i) {
            for (int j = 0; j < arrby[0].length; ++j) {
                arrs[i][j] = (short)(255 & arrby[i][j]);
            }
        }
        return arrs;
    }

    public static byte[][][] convertArray(short[][][] arrs) {
        int[] arrn = new int[]{arrs.length, arrs[0].length, arrs[0][0].length};
        byte[][][] arrby = (byte[][][])Array.newInstance((Class)Byte.TYPE, (int[])arrn);
        for (int i = 0; i < arrs.length; ++i) {
            for (int j = 0; j < arrs[0].length; ++j) {
                for (int k = 0; k < arrs[0][0].length; ++k) {
                    arrby[i][j][k] = (byte)arrs[i][j][k];
                }
            }
        }
        return arrby;
    }

    public static short[][][] convertArray(byte[][][] arrby) {
        int[] arrn = new int[]{arrby.length, arrby[0].length, arrby[0][0].length};
        short[][][] arrs = (short[][][])Array.newInstance((Class)Short.TYPE, (int[])arrn);
        for (int i = 0; i < arrby.length; ++i) {
            for (int j = 0; j < arrby[0].length; ++j) {
                for (int k = 0; k < arrby[0][0].length; ++k) {
                    arrs[i][j][k] = (short)(255 & arrby[i][j][k]);
                }
            }
        }
        return arrs;
    }

    public static int[] convertArraytoInt(byte[] arrby) {
        int[] arrn = new int[arrby.length];
        for (int i = 0; i < arrby.length; ++i) {
            arrn[i] = 255 & arrby[i];
        }
        return arrn;
    }

    public static byte[] convertIntArray(int[] arrn) {
        byte[] arrby = new byte[arrn.length];
        for (int i = 0; i < arrn.length; ++i) {
            arrby[i] = (byte)arrn[i];
        }
        return arrby;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean equals(short[] arrs, short[] arrs2) {
        if (arrs.length != arrs2.length) {
            return false;
        }
        int n = -1 + arrs.length;
        boolean bl = true;
        while (n >= 0) {
            boolean bl2 = arrs[n] == arrs2[n];
            bl &= bl2;
            --n;
        }
        return bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean equals(short[][] arrs, short[][] arrs2) {
        if (arrs.length != arrs2.length) {
            return false;
        }
        int n = -1 + arrs.length;
        boolean bl = true;
        int n2 = n;
        while (n2 >= 0) {
            boolean bl2 = bl & RainbowUtil.equals(arrs[n2], arrs2[n2]);
            --n2;
            bl = bl2;
        }
        return bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean equals(short[][][] arrs, short[][][] arrs2) {
        if (arrs.length != arrs2.length) {
            return false;
        }
        int n = -1 + arrs.length;
        boolean bl = true;
        int n2 = n;
        while (n2 >= 0) {
            boolean bl2 = bl & RainbowUtil.equals(arrs[n2], arrs2[n2]);
            --n2;
            bl = bl2;
        }
        return bl;
    }
}

