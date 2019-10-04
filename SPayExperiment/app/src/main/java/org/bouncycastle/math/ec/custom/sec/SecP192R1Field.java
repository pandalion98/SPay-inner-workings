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
import org.bouncycastle.math.raw.Nat192;

public class SecP192R1Field {
    private static final long M = 0xFFFFFFFFL;
    static final int[] P = new int[]{-1, -1, -2, -1, -1, -1};
    private static final int P5 = -1;
    static final int[] PExt = new int[]{1, 0, 2, 0, 1, 0, -2, -1, -3, -1, -1, -1};
    private static final int PExt11 = -1;
    private static final int[] PExtInv = new int[]{-1, -1, -3, -1, -2, -1, 1, 0, 2};

    public static void add(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat192.add(arrn, arrn2, arrn3) != 0 || arrn3[5] == -1 && Nat192.gte(arrn3, P)) {
            SecP192R1Field.addPInvTo(arrn3);
        }
    }

    public static void addExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if ((Nat.add(12, arrn, arrn2, arrn3) != 0 || arrn3[11] == -1 && Nat.gte(12, arrn3, PExt)) && Nat.addTo(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.incAt(12, arrn3, PExtInv.length);
        }
    }

    public static void addOne(int[] arrn, int[] arrn2) {
        if (Nat.inc(6, arrn, arrn2) != 0 || arrn2[5] == -1 && Nat192.gte(arrn2, P)) {
            SecP192R1Field.addPInvTo(arrn2);
        }
    }

    private static void addPInvTo(int[] arrn) {
        long l = 1L + (0xFFFFFFFFL & (long)arrn[0]);
        arrn[0] = (int)l;
        long l2 = l >> 32;
        if (l2 != 0L) {
            long l3 = l2 + (0xFFFFFFFFL & (long)arrn[1]);
            arrn[1] = (int)l3;
            l2 = l3 >> 32;
        }
        long l4 = l2 + (1L + (0xFFFFFFFFL & (long)arrn[2]));
        arrn[2] = (int)l4;
        if (l4 >> 32 != 0L) {
            Nat.incAt(6, arrn, 3);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] arrn = Nat192.fromBigInteger(bigInteger);
        if (arrn[5] == -1 && Nat192.gte(arrn, P)) {
            Nat192.subFrom(P, arrn);
        }
        return arrn;
    }

    public static void half(int[] arrn, int[] arrn2) {
        if ((1 & arrn[0]) == 0) {
            Nat.shiftDownBit(6, arrn, 0, arrn2);
            return;
        }
        Nat.shiftDownBit(6, arrn2, Nat192.add(arrn, P, arrn2));
    }

    public static void multiply(int[] arrn, int[] arrn2, int[] arrn3) {
        int[] arrn4 = Nat192.createExt();
        Nat192.mul(arrn, arrn2, arrn4);
        SecP192R1Field.reduce(arrn4, arrn3);
    }

    public static void multiplyAddToExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if ((Nat192.mulAddTo(arrn, arrn2, arrn3) != 0 || arrn3[11] == -1 && Nat.gte(12, arrn3, PExt)) && Nat.addTo(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.incAt(12, arrn3, PExtInv.length);
        }
    }

    public static void negate(int[] arrn, int[] arrn2) {
        if (Nat192.isZero(arrn)) {
            Nat192.zero(arrn2);
            return;
        }
        Nat192.sub(P, arrn, arrn2);
    }

    public static void reduce(int[] arrn, int[] arrn2) {
        long l = 0xFFFFFFFFL & (long)arrn[6];
        long l2 = 0xFFFFFFFFL & (long)arrn[7];
        long l3 = 0xFFFFFFFFL & (long)arrn[8];
        long l4 = 0xFFFFFFFFL & (long)arrn[9];
        long l5 = 0xFFFFFFFFL & (long)arrn[10];
        long l6 = 0xFFFFFFFFL & (long)arrn[11];
        long l7 = l5 + l;
        long l8 = l6 + l2;
        long l9 = 0L + (l7 + (0xFFFFFFFFL & (long)arrn[0]));
        int n = (int)l9;
        long l10 = (l9 >> 32) + (l8 + (0xFFFFFFFFL & (long)arrn[1]));
        arrn2[1] = (int)l10;
        long l11 = l10 >> 32;
        long l12 = l3 + l7;
        long l13 = l4 + l8;
        long l14 = l11 + (l12 + (0xFFFFFFFFL & (long)arrn[2]));
        long l15 = 0xFFFFFFFFL & l14;
        long l16 = (l14 >> 32) + (l13 + (0xFFFFFFFFL & (long)arrn[3]));
        arrn2[3] = (int)l16;
        long l17 = l16 >> 32;
        long l18 = l12 - l;
        long l19 = l13 - l2;
        long l20 = l17 + (l18 + (0xFFFFFFFFL & (long)arrn[4]));
        arrn2[4] = (int)l20;
        long l21 = (l20 >> 32) + (l19 + (0xFFFFFFFFL & (long)arrn[5]));
        arrn2[5] = (int)l21;
        long l22 = l21 >> 32;
        long l23 = l15 + l22;
        long l24 = l22 + (0xFFFFFFFFL & (long)n);
        arrn2[0] = (int)l24;
        long l25 = l24 >> 32;
        if (l25 != 0L) {
            long l26 = l25 + (0xFFFFFFFFL & (long)arrn2[1]);
            arrn2[1] = (int)l26;
            l23 += l26 >> 32;
        }
        arrn2[2] = (int)l23;
        if (l23 >> 32 != 0L && Nat.incAt(6, arrn2, 3) != 0 || arrn2[5] == -1 && Nat192.gte(arrn2, P)) {
            SecP192R1Field.addPInvTo(arrn2);
        }
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
                l4 = l5 >> 32;
            }
            long l6 = l4 + (l2 + (0xFFFFFFFFL & (long)arrn[2]));
            arrn[2] = (int)l6;
            l = l6 >> 32;
        } else {
            l = 0L;
        }
        if (l != 0L && Nat.incAt(6, arrn, 3) != 0 || arrn[5] == -1 && Nat192.gte(arrn, P)) {
            SecP192R1Field.addPInvTo(arrn);
        }
    }

    public static void square(int[] arrn, int[] arrn2) {
        int[] arrn3 = Nat192.createExt();
        Nat192.square(arrn, arrn3);
        SecP192R1Field.reduce(arrn3, arrn2);
    }

    public static void squareN(int[] arrn, int n, int[] arrn2) {
        int[] arrn3 = Nat192.createExt();
        Nat192.square(arrn, arrn3);
        SecP192R1Field.reduce(arrn3, arrn2);
        while (--n > 0) {
            Nat192.square(arrn2, arrn3);
            SecP192R1Field.reduce(arrn3, arrn2);
        }
    }

    private static void subPInvFrom(int[] arrn) {
        long l = (0xFFFFFFFFL & (long)arrn[0]) - 1L;
        arrn[0] = (int)l;
        long l2 = l >> 32;
        if (l2 != 0L) {
            long l3 = l2 + (0xFFFFFFFFL & (long)arrn[1]);
            arrn[1] = (int)l3;
            l2 = l3 >> 32;
        }
        long l4 = l2 + ((0xFFFFFFFFL & (long)arrn[2]) - 1L);
        arrn[2] = (int)l4;
        if (l4 >> 32 != 0L) {
            Nat.decAt(6, arrn, 3);
        }
    }

    public static void subtract(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat192.sub(arrn, arrn2, arrn3) != 0) {
            SecP192R1Field.subPInvFrom(arrn3);
        }
    }

    public static void subtractExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat.sub(12, arrn, arrn2, arrn3) != 0 && Nat.subFrom(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.decAt(12, arrn3, PExtInv.length);
        }
    }

    public static void twice(int[] arrn, int[] arrn2) {
        if (Nat.shiftUpBit(6, arrn, 0, arrn2) != 0 || arrn2[5] == -1 && Nat192.gte(arrn2, P)) {
            SecP192R1Field.addPInvTo(arrn2);
        }
    }
}

