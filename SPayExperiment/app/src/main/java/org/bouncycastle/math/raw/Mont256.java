/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.math.raw;

import org.bouncycastle.math.raw.Nat256;

public abstract class Mont256 {
    private static final long M = 0xFFFFFFFFL;

    public static int inverse32(int n) {
        int n2 = n * (2 - n * n);
        int n3 = n2 * (2 - n * n2);
        int n4 = n3 * (2 - n * n3);
        return n4 * (2 - n * n4);
    }

    public static void multAdd(int[] arrn, int[] arrn2, int[] arrn3, int[] arrn4, int n) {
        long l = 0xFFFFFFFFL & (long)arrn2[0];
        int n2 = 0;
        for (int i = 0; i < 8; ++i) {
            long l2 = 0xFFFFFFFFL & (long)arrn3[0];
            long l3 = 0xFFFFFFFFL & (long)arrn[i];
            long l4 = l3 * l;
            long l5 = l2 + (0xFFFFFFFFL & l4);
            long l6 = 0xFFFFFFFFL & (long)(n * (int)l5);
            long l7 = l6 * (0xFFFFFFFFL & (long)arrn4[0]);
            long l8 = (l5 + (0xFFFFFFFFL & l7) >>> 32) + (l4 >>> 32) + (l7 >>> 32);
            for (int j = 1; j < 8; ++j) {
                long l9 = l3 * (0xFFFFFFFFL & (long)arrn2[j]);
                long l10 = l6 * (0xFFFFFFFFL & (long)arrn4[j]);
                long l11 = l8 + ((0xFFFFFFFFL & l9) + (0xFFFFFFFFL & l10) + (0xFFFFFFFFL & (long)arrn3[j]));
                arrn3[j - 1] = (int)l11;
                l8 = (l11 >>> 32) + (l9 >>> 32) + (l10 >>> 32);
            }
            long l12 = l8 + (0xFFFFFFFFL & (long)n2);
            arrn3[7] = (int)l12;
            int n3 = (int)(l12 >>> 32);
            n2 = n3;
        }
        if (n2 != 0 || Nat256.gte(arrn3, arrn4)) {
            Nat256.sub(arrn3, arrn4, arrn3);
        }
    }

    public static void multAddXF(int[] arrn, int[] arrn2, int[] arrn3, int[] arrn4) {
        long l = 0xFFFFFFFFL & (long)arrn2[0];
        int n = 0;
        for (int i = 0; i < 8; ++i) {
            long l2 = 0xFFFFFFFFL & (long)arrn[i];
            long l3 = l2 * l + (0xFFFFFFFFL & (long)arrn3[0]);
            long l4 = 0xFFFFFFFFL & l3;
            long l5 = l4 + (l3 >>> 32);
            for (int j = 1; j < 8; ++j) {
                long l6 = l2 * (0xFFFFFFFFL & (long)arrn2[j]);
                long l7 = l4 * (0xFFFFFFFFL & (long)arrn4[j]);
                long l8 = l5 + ((0xFFFFFFFFL & l6) + (0xFFFFFFFFL & l7) + (0xFFFFFFFFL & (long)arrn3[j]));
                arrn3[j - 1] = (int)l8;
                l5 = (l8 >>> 32) + (l6 >>> 32) + (l7 >>> 32);
            }
            long l9 = l5 + (0xFFFFFFFFL & (long)n);
            arrn3[7] = (int)l9;
            int n2 = (int)(l9 >>> 32);
            n = n2;
        }
        if (n != 0 || Nat256.gte(arrn3, arrn4)) {
            Nat256.sub(arrn3, arrn4, arrn3);
        }
    }

    public static void reduce(int[] arrn, int[] arrn2, int n) {
        for (int i = 0; i < 8; ++i) {
            int n2 = arrn[0];
            long l = 0xFFFFFFFFL & (long)(n2 * n);
            long l2 = l * (0xFFFFFFFFL & (long)arrn2[0]) + (0xFFFFFFFFL & (long)n2) >>> 32;
            for (int j = 1; j < 8; ++j) {
                long l3 = l2 + (l * (0xFFFFFFFFL & (long)arrn2[j]) + (0xFFFFFFFFL & (long)arrn[j]));
                arrn[j - 1] = (int)l3;
                l2 = l3 >>> 32;
            }
            arrn[7] = (int)l2;
        }
        if (Nat256.gte(arrn, arrn2)) {
            Nat256.sub(arrn, arrn2, arrn);
        }
    }

    public static void reduceXF(int[] arrn, int[] arrn2) {
        for (int i = 0; i < 8; ++i) {
            long l = 0xFFFFFFFFL & (long)arrn[0];
            long l2 = l;
            for (int j = 1; j < 8; ++j) {
                long l3 = l2 + (l * (0xFFFFFFFFL & (long)arrn2[j]) + (0xFFFFFFFFL & (long)arrn[j]));
                arrn[j - 1] = (int)l3;
                l2 = l3 >>> 32;
            }
            arrn[7] = (int)l2;
        }
        if (Nat256.gte(arrn, arrn2)) {
            Nat256.sub(arrn, arrn2, arrn);
        }
    }
}

