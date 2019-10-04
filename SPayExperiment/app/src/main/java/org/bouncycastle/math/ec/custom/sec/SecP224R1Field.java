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
import org.bouncycastle.math.raw.Nat224;

public class SecP224R1Field {
    private static final long M = 0xFFFFFFFFL;
    static final int[] P = new int[]{1, 0, 0, -1, -1, -1, -1};
    private static final int P6 = -1;
    static final int[] PExt = new int[]{1, 0, 0, -2, -1, -1, 0, 2, 0, 0, -2, -1, -1, -1};
    private static final int PExt13 = -1;
    private static final int[] PExtInv = new int[]{-1, -1, -1, 1, 0, 0, -1, -3, -1, -1, 1};

    public static void add(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat224.add(arrn, arrn2, arrn3) != 0 || arrn3[6] == -1 && Nat224.gte(arrn3, P)) {
            SecP224R1Field.addPInvTo(arrn3);
        }
    }

    public static void addExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if ((Nat.add(14, arrn, arrn2, arrn3) != 0 || arrn3[13] == -1 && Nat.gte(14, arrn3, PExt)) && Nat.addTo(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.incAt(14, arrn3, PExtInv.length);
        }
    }

    public static void addOne(int[] arrn, int[] arrn2) {
        if (Nat.inc(7, arrn, arrn2) != 0 || arrn2[6] == -1 && Nat224.gte(arrn2, P)) {
            SecP224R1Field.addPInvTo(arrn2);
        }
    }

    private static void addPInvTo(int[] arrn) {
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
        if (l5 >> 32 != 0L) {
            Nat.incAt(7, arrn, 4);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] arrn = Nat224.fromBigInteger(bigInteger);
        if (arrn[6] == -1 && Nat224.gte(arrn, P)) {
            Nat224.subFrom(P, arrn);
        }
        return arrn;
    }

    public static void half(int[] arrn, int[] arrn2) {
        if ((1 & arrn[0]) == 0) {
            Nat.shiftDownBit(7, arrn, 0, arrn2);
            return;
        }
        Nat.shiftDownBit(7, arrn2, Nat224.add(arrn, P, arrn2));
    }

    public static void multiply(int[] arrn, int[] arrn2, int[] arrn3) {
        int[] arrn4 = Nat224.createExt();
        Nat224.mul(arrn, arrn2, arrn4);
        SecP224R1Field.reduce(arrn4, arrn3);
    }

    public static void multiplyAddToExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if ((Nat224.mulAddTo(arrn, arrn2, arrn3) != 0 || arrn3[13] == -1 && Nat.gte(14, arrn3, PExt)) && Nat.addTo(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.incAt(14, arrn3, PExtInv.length);
        }
    }

    public static void negate(int[] arrn, int[] arrn2) {
        if (Nat224.isZero(arrn)) {
            Nat224.zero(arrn2);
            return;
        }
        Nat224.sub(P, arrn, arrn2);
    }

    public static void reduce(int[] arrn, int[] arrn2) {
        long l = 0xFFFFFFFFL & (long)arrn[10];
        long l2 = 0xFFFFFFFFL & (long)arrn[11];
        long l3 = 0xFFFFFFFFL & (long)arrn[12];
        long l4 = 0xFFFFFFFFL & (long)arrn[13];
        long l5 = l2 + (0xFFFFFFFFL & (long)arrn[7]) - 1L;
        long l6 = l3 + (0xFFFFFFFFL & (long)arrn[8]);
        long l7 = l4 + (0xFFFFFFFFL & (long)arrn[9]);
        long l8 = 0L + ((0xFFFFFFFFL & (long)arrn[0]) - l5);
        long l9 = 0xFFFFFFFFL & l8;
        long l10 = (l8 >> 32) + ((0xFFFFFFFFL & (long)arrn[1]) - l6);
        arrn2[1] = (int)l10;
        long l11 = (l10 >> 32) + ((0xFFFFFFFFL & (long)arrn[2]) - l7);
        arrn2[2] = (int)l11;
        long l12 = (l11 >> 32) + (l5 + (0xFFFFFFFFL & (long)arrn[3]) - l);
        long l13 = 0xFFFFFFFFL & l12;
        long l14 = (l12 >> 32) + (l6 + (0xFFFFFFFFL & (long)arrn[4]) - l2);
        arrn2[4] = (int)l14;
        long l15 = (l14 >> 32) + (l7 + (0xFFFFFFFFL & (long)arrn[5]) - l3);
        arrn2[5] = (int)l15;
        long l16 = (l15 >> 32) + (l + (0xFFFFFFFFL & (long)arrn[6]) - l4);
        arrn2[6] = (int)l16;
        long l17 = 1L + (l16 >> 32);
        long l18 = l13 + l17;
        long l19 = l9 - l17;
        arrn2[0] = (int)l19;
        long l20 = l19 >> 32;
        if (l20 != 0L) {
            long l21 = l20 + (0xFFFFFFFFL & (long)arrn2[1]);
            arrn2[1] = (int)l21;
            long l22 = (l21 >> 32) + (0xFFFFFFFFL & (long)arrn2[2]);
            arrn2[2] = (int)l22;
            l18 += l22 >> 32;
        }
        arrn2[3] = (int)l18;
        if (l18 >> 32 != 0L && Nat.incAt(7, arrn2, 4) != 0 || arrn2[6] == -1 && Nat224.gte(arrn2, P)) {
            SecP224R1Field.addPInvTo(arrn2);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void reduce32(int n, int[] arrn) {
        long l;
        if (n != 0) {
            long l2 = 0xFFFFFFFFL & (long)n;
            long l3 = 0L + ((0xFFFFFFFFL & (long)arrn[0]) - l2);
            arrn[0] = (int)l3;
            long l4 = l3 >> 32;
            if (l4 != 0L) {
                long l5 = l4 + (0xFFFFFFFFL & (long)arrn[1]);
                arrn[1] = (int)l5;
                long l6 = (l5 >> 32) + (0xFFFFFFFFL & (long)arrn[2]);
                arrn[2] = (int)l6;
                l4 = l6 >> 32;
            }
            long l7 = l4 + (l2 + (0xFFFFFFFFL & (long)arrn[3]));
            arrn[3] = (int)l7;
            l = l7 >> 32;
        } else {
            l = 0L;
        }
        if (l != 0L && Nat.incAt(7, arrn, 4) != 0 || arrn[6] == -1 && Nat224.gte(arrn, P)) {
            SecP224R1Field.addPInvTo(arrn);
        }
    }

    public static void square(int[] arrn, int[] arrn2) {
        int[] arrn3 = Nat224.createExt();
        Nat224.square(arrn, arrn3);
        SecP224R1Field.reduce(arrn3, arrn2);
    }

    public static void squareN(int[] arrn, int n, int[] arrn2) {
        int[] arrn3 = Nat224.createExt();
        Nat224.square(arrn, arrn3);
        SecP224R1Field.reduce(arrn3, arrn2);
        while (--n > 0) {
            Nat224.square(arrn2, arrn3);
            SecP224R1Field.reduce(arrn3, arrn2);
        }
    }

    private static void subPInvFrom(int[] arrn) {
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
        if (l5 >> 32 != 0L) {
            Nat.decAt(7, arrn, 4);
        }
    }

    public static void subtract(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat224.sub(arrn, arrn2, arrn3) != 0) {
            SecP224R1Field.subPInvFrom(arrn3);
        }
    }

    public static void subtractExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat.sub(14, arrn, arrn2, arrn3) != 0 && Nat.subFrom(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.decAt(14, arrn3, PExtInv.length);
        }
    }

    public static void twice(int[] arrn, int[] arrn2) {
        if (Nat.shiftUpBit(7, arrn, 0, arrn2) != 0 || arrn2[6] == -1 && Nat224.gte(arrn2, P)) {
            SecP224R1Field.addPInvTo(arrn2);
        }
    }
}

