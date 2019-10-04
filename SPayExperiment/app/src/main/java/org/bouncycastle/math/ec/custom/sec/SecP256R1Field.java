/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat256;

public class SecP256R1Field {
    private static final long M = 0xFFFFFFFFL;
    static final int[] P = new int[]{-1, -1, -1, 0, 0, 0, 1, -1};
    private static final int P7 = -1;
    static final int[] PExt = new int[]{1, 0, 0, -2, -1, -1, -2, 1, -2, 1, -2, 1, 1, -2, 2, -2};
    private static final int PExt15 = -1;

    public static void add(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat256.add(arrn, arrn2, arrn3) != 0 || arrn3[7] == -1 && Nat256.gte(arrn3, P)) {
            SecP256R1Field.addPInvTo(arrn3);
        }
    }

    public static void addExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat.add(16, arrn, arrn2, arrn3) != 0 || (-1 & arrn3[15]) == -1 && Nat.gte(16, arrn3, PExt)) {
            Nat.subFrom(16, PExt, arrn3);
        }
    }

    public static void addOne(int[] arrn, int[] arrn2) {
        if (Nat.inc(8, arrn, arrn2) != 0 || arrn2[7] == -1 && Nat256.gte(arrn2, P)) {
            SecP256R1Field.addPInvTo(arrn2);
        }
    }

    private static void addPInvTo(int[] arrn) {
        long l = 1L + (0xFFFFFFFFL & (long)arrn[0]);
        arrn[0] = (int)l;
        long l2 = l >> 32;
        if (l2 != 0L) {
            long l3 = l2 + (0xFFFFFFFFL & (long)arrn[1]);
            arrn[1] = (int)l3;
            long l4 = (l3 >> 32) + (0xFFFFFFFFL & (long)arrn[2]);
            arrn[2] = (int)l4;
            l2 = l4 >> 32;
        }
        long l5 = l2 + ((0xFFFFFFFFL & (long)arrn[3]) - 1L);
        arrn[3] = (int)l5;
        long l6 = l5 >> 32;
        if (l6 != 0L) {
            long l7 = l6 + (0xFFFFFFFFL & (long)arrn[4]);
            arrn[4] = (int)l7;
            long l8 = (l7 >> 32) + (0xFFFFFFFFL & (long)arrn[5]);
            arrn[5] = (int)l8;
            l6 = l8 >> 32;
        }
        long l9 = l6 + ((0xFFFFFFFFL & (long)arrn[6]) - 1L);
        arrn[6] = (int)l9;
        arrn[7] = (int)((l9 >> 32) + (1L + (0xFFFFFFFFL & (long)arrn[7])));
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] arrn = Nat256.fromBigInteger(bigInteger);
        if (arrn[7] == -1 && Nat256.gte(arrn, P)) {
            Nat256.subFrom(P, arrn);
        }
        return arrn;
    }

    public static void half(int[] arrn, int[] arrn2) {
        if ((1 & arrn[0]) == 0) {
            Nat.shiftDownBit(8, arrn, 0, arrn2);
            return;
        }
        Nat.shiftDownBit(8, arrn2, Nat256.add(arrn, P, arrn2));
    }

    public static void multiply(int[] arrn, int[] arrn2, int[] arrn3) {
        int[] arrn4 = Nat256.createExt();
        Nat256.mul(arrn, arrn2, arrn4);
        SecP256R1Field.reduce(arrn4, arrn3);
    }

    public static void multiplyAddToExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat256.mulAddTo(arrn, arrn2, arrn3) != 0 || (-1 & arrn3[15]) == -1 && Nat.gte(16, arrn3, PExt)) {
            Nat.subFrom(16, PExt, arrn3);
        }
    }

    public static void negate(int[] arrn, int[] arrn2) {
        if (Nat256.isZero(arrn)) {
            Nat256.zero(arrn2);
            return;
        }
        Nat256.sub(P, arrn, arrn2);
    }

    public static void reduce(int[] arrn, int[] arrn2) {
        long l = 0xFFFFFFFFL & (long)arrn[8];
        long l2 = 0xFFFFFFFFL & (long)arrn[9];
        long l3 = 0xFFFFFFFFL & (long)arrn[10];
        long l4 = 0xFFFFFFFFL & (long)arrn[11];
        long l5 = 0xFFFFFFFFL & (long)arrn[12];
        long l6 = 0xFFFFFFFFL & (long)arrn[13];
        long l7 = 0xFFFFFFFFL & (long)arrn[14];
        long l8 = 0xFFFFFFFFL & (long)arrn[15];
        long l9 = l - 6L;
        long l10 = l9 + l2;
        long l11 = l2 + l3;
        long l12 = l3 + l4 - l8;
        long l13 = l4 + l5;
        long l14 = l5 + l6;
        long l15 = l6 + l7;
        long l16 = l7 + l8;
        long l17 = 0L + (l10 + (0xFFFFFFFFL & (long)arrn[0]) - l13 - l15);
        arrn2[0] = (int)l17;
        long l18 = (l17 >> 32) + (l11 + (0xFFFFFFFFL & (long)arrn[1]) - l14 - l16);
        arrn2[1] = (int)l18;
        long l19 = (l18 >> 32) + (l12 + (0xFFFFFFFFL & (long)arrn[2]) - l15);
        arrn2[2] = (int)l19;
        long l20 = (l19 >> 32) + (l6 + ((0xFFFFFFFFL & (long)arrn[3]) + (l13 << 1)) - l8 - l10);
        arrn2[3] = (int)l20;
        long l21 = (l20 >> 32) + (l7 + ((0xFFFFFFFFL & (long)arrn[4]) + (l14 << 1)) - l11);
        arrn2[4] = (int)l21;
        long l22 = (l21 >> 32) + ((0xFFFFFFFFL & (long)arrn[5]) + (l15 << 1) - l12);
        arrn2[5] = (int)l22;
        long l23 = (l22 >> 32) + (l15 + ((0xFFFFFFFFL & (long)arrn[6]) + (l16 << 1)) - l10);
        arrn2[6] = (int)l23;
        long l24 = (l23 >> 32) + (l9 + ((0xFFFFFFFFL & (long)arrn[7]) + (l8 << 1)) - l12 - l14);
        arrn2[7] = (int)l24;
        SecP256R1Field.reduce32((int)(6L + (l24 >> 32)), arrn2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void reduce32(int n, int[] arrn) {
        long l;
        if (n != 0) {
            long l2 = 0xFFFFFFFFL & (long)n;
            long l3 = 0L + (l2 + (0xFFFFFFFFL & (long)arrn[0]));
            arrn[0] = (int)l3;
            long l4 = l3 >> 32;
            if (l4 != 0L) {
                long l5 = l4 + (0xFFFFFFFFL & (long)arrn[1]);
                arrn[1] = (int)l5;
                long l6 = (l5 >> 32) + (0xFFFFFFFFL & (long)arrn[2]);
                arrn[2] = (int)l6;
                l4 = l6 >> 32;
            }
            long l7 = l4 + ((0xFFFFFFFFL & (long)arrn[3]) - l2);
            arrn[3] = (int)l7;
            long l8 = l7 >> 32;
            if (l8 != 0L) {
                long l9 = l8 + (0xFFFFFFFFL & (long)arrn[4]);
                arrn[4] = (int)l9;
                long l10 = (l9 >> 32) + (0xFFFFFFFFL & (long)arrn[5]);
                arrn[5] = (int)l10;
                l8 = l10 >> 32;
            }
            long l11 = l8 + ((0xFFFFFFFFL & (long)arrn[6]) - l2);
            arrn[6] = (int)l11;
            long l12 = (l11 >> 32) + (l2 + (0xFFFFFFFFL & (long)arrn[7]));
            arrn[7] = (int)l12;
            l = l12 >> 32;
        } else {
            l = 0L;
        }
        if (l != 0L || arrn[7] == -1 && Nat256.gte(arrn, P)) {
            SecP256R1Field.addPInvTo(arrn);
        }
    }

    public static void square(int[] arrn, int[] arrn2) {
        int[] arrn3 = Nat256.createExt();
        Nat256.square(arrn, arrn3);
        SecP256R1Field.reduce(arrn3, arrn2);
    }

    public static void squareN(int[] arrn, int n, int[] arrn2) {
        int[] arrn3 = Nat256.createExt();
        Nat256.square(arrn, arrn3);
        SecP256R1Field.reduce(arrn3, arrn2);
        while (--n > 0) {
            Nat256.square(arrn2, arrn3);
            SecP256R1Field.reduce(arrn3, arrn2);
        }
    }

    private static void subPInvFrom(int[] arrn) {
        long l = (0xFFFFFFFFL & (long)arrn[0]) - 1L;
        arrn[0] = (int)l;
        long l2 = l >> 32;
        if (l2 != 0L) {
            long l3 = l2 + (0xFFFFFFFFL & (long)arrn[1]);
            arrn[1] = (int)l3;
            long l4 = (l3 >> 32) + (0xFFFFFFFFL & (long)arrn[2]);
            arrn[2] = (int)l4;
            l2 = l4 >> 32;
        }
        long l5 = l2 + (1L + (0xFFFFFFFFL & (long)arrn[3]));
        arrn[3] = (int)l5;
        long l6 = l5 >> 32;
        if (l6 != 0L) {
            long l7 = l6 + (0xFFFFFFFFL & (long)arrn[4]);
            arrn[4] = (int)l7;
            long l8 = (l7 >> 32) + (0xFFFFFFFFL & (long)arrn[5]);
            arrn[5] = (int)l8;
            l6 = l8 >> 32;
        }
        long l9 = l6 + (1L + (0xFFFFFFFFL & (long)arrn[6]));
        arrn[6] = (int)l9;
        arrn[7] = (int)((l9 >> 32) + ((0xFFFFFFFFL & (long)arrn[7]) - 1L));
    }

    public static void subtract(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat256.sub(arrn, arrn2, arrn3) != 0) {
            SecP256R1Field.subPInvFrom(arrn3);
        }
    }

    public static void subtractExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat.sub(16, arrn, arrn2, arrn3) != 0) {
            Nat.addTo(16, PExt, arrn3);
        }
    }

    public static void twice(int[] arrn, int[] arrn2) {
        if (Nat.shiftUpBit(8, arrn, 0, arrn2) != 0 || arrn2[7] == -1 && Nat256.gte(arrn2, P)) {
            SecP256R1Field.addPInvTo(arrn2);
        }
    }
}

