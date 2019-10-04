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
import org.bouncycastle.math.raw.Nat384;

public class SecP384R1Field {
    private static final long M = 0xFFFFFFFFL;
    static final int[] P = new int[]{-1, 0, 0, -1, -2, -1, -1, -1, -1, -1, -1, -1};
    private static final int P11 = -1;
    static final int[] PExt = new int[]{1, -2, 0, 2, 0, -2, 0, 2, 1, 0, 0, 0, -2, 1, 0, -2, -3, -1, -1, -1, -1, -1, -1, -1};
    private static final int PExt23 = -1;
    private static final int[] PExtInv = new int[]{-1, 1, -1, -3, -1, 1, -1, -3, -2, -1, -1, -1, 1, -2, -1, 1, 2};

    public static void add(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat.add(12, arrn, arrn2, arrn3) != 0 || arrn3[11] == -1 && Nat.gte(12, arrn3, P)) {
            SecP384R1Field.addPInvTo(arrn3);
        }
    }

    public static void addExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if ((Nat.add(24, arrn, arrn2, arrn3) != 0 || arrn3[23] == -1 && Nat.gte(24, arrn3, PExt)) && Nat.addTo(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.incAt(24, arrn3, PExtInv.length);
        }
    }

    public static void addOne(int[] arrn, int[] arrn2) {
        if (Nat.inc(12, arrn, arrn2) != 0 || arrn2[11] == -1 && Nat.gte(12, arrn2, P)) {
            SecP384R1Field.addPInvTo(arrn2);
        }
    }

    private static void addPInvTo(int[] arrn) {
        long l = 1L + (0xFFFFFFFFL & (long)arrn[0]);
        arrn[0] = (int)l;
        long l2 = (l >> 32) + ((0xFFFFFFFFL & (long)arrn[1]) - 1L);
        arrn[1] = (int)l2;
        long l3 = l2 >> 32;
        if (l3 != 0L) {
            long l4 = l3 + (0xFFFFFFFFL & (long)arrn[2]);
            arrn[2] = (int)l4;
            l3 = l4 >> 32;
        }
        long l5 = l3 + (1L + (0xFFFFFFFFL & (long)arrn[3]));
        arrn[3] = (int)l5;
        long l6 = (l5 >> 32) + (1L + (0xFFFFFFFFL & (long)arrn[4]));
        arrn[4] = (int)l6;
        if (l6 >> 32 != 0L) {
            Nat.incAt(12, arrn, 5);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] arrn = Nat.fromBigInteger(384, bigInteger);
        if (arrn[11] == -1 && Nat.gte(12, arrn, P)) {
            Nat.subFrom(12, P, arrn);
        }
        return arrn;
    }

    public static void half(int[] arrn, int[] arrn2) {
        if ((1 & arrn[0]) == 0) {
            Nat.shiftDownBit(12, arrn, 0, arrn2);
            return;
        }
        Nat.shiftDownBit(12, arrn2, Nat.add(12, arrn, P, arrn2));
    }

    public static void multiply(int[] arrn, int[] arrn2, int[] arrn3) {
        int[] arrn4 = Nat.create(24);
        Nat384.mul(arrn, arrn2, arrn4);
        SecP384R1Field.reduce(arrn4, arrn3);
    }

    public static void negate(int[] arrn, int[] arrn2) {
        if (Nat.isZero(12, arrn)) {
            Nat.zero(12, arrn2);
            return;
        }
        Nat.sub(12, P, arrn, arrn2);
    }

    public static void reduce(int[] arrn, int[] arrn2) {
        long l = 0xFFFFFFFFL & (long)arrn[16];
        long l2 = 0xFFFFFFFFL & (long)arrn[17];
        long l3 = 0xFFFFFFFFL & (long)arrn[18];
        long l4 = 0xFFFFFFFFL & (long)arrn[19];
        long l5 = 0xFFFFFFFFL & (long)arrn[20];
        long l6 = 0xFFFFFFFFL & (long)arrn[21];
        long l7 = 0xFFFFFFFFL & (long)arrn[22];
        long l8 = 0xFFFFFFFFL & (long)arrn[23];
        long l9 = l5 + (0xFFFFFFFFL & (long)arrn[12]) - 1L;
        long l10 = l7 + (0xFFFFFFFFL & (long)arrn[13]);
        long l11 = l8 + (l7 + (0xFFFFFFFFL & (long)arrn[14]));
        long l12 = l8 + (0xFFFFFFFFL & (long)arrn[15]);
        long l13 = l2 + l6;
        long l14 = l6 - l8;
        long l15 = l7 - l8;
        long l16 = 0L + (l14 + (l9 + (0xFFFFFFFFL & (long)arrn[0])));
        arrn2[0] = (int)l16;
        long l17 = (l16 >> 32) + (l10 + (l8 + (0xFFFFFFFFL & (long)arrn[1]) - l9));
        arrn2[1] = (int)l17;
        long l18 = (l17 >> 32) + (l11 + ((0xFFFFFFFFL & (long)arrn[2]) - l6 - l10));
        arrn2[2] = (int)l18;
        long l19 = (l18 >> 32) + (l14 + (l12 + (l9 + (0xFFFFFFFFL & (long)arrn[3]) - l11)));
        arrn2[3] = (int)l19;
        long l20 = (l19 >> 32) + (l14 + (l10 + (l9 + (l6 + (l + (0xFFFFFFFFL & (long)arrn[4])))) - l12));
        arrn2[4] = (int)l20;
        long l21 = (l20 >> 32) + (l13 + (l11 + (l10 + ((0xFFFFFFFFL & (long)arrn[5]) - l))));
        arrn2[5] = (int)l21;
        long l22 = (l21 >> 32) + (l12 + (l11 + (l3 + (0xFFFFFFFFL & (long)arrn[6]) - l2)));
        arrn2[6] = (int)l22;
        long l23 = (l22 >> 32) + (l12 + (l4 + (l + (0xFFFFFFFFL & (long)arrn[7])) - l3));
        arrn2[7] = (int)l23;
        long l24 = (l23 >> 32) + (l5 + (l2 + (l + (0xFFFFFFFFL & (long)arrn[8]))) - l4);
        arrn2[8] = (int)l24;
        long l25 = (l24 >> 32) + (l13 + (l3 + (0xFFFFFFFFL & (long)arrn[9]) - l5));
        arrn2[9] = (int)l25;
        long l26 = (l25 >> 32) + (l15 + (l4 + (l3 + (0xFFFFFFFFL & (long)arrn[10])) - l14));
        arrn2[10] = (int)l26;
        long l27 = (l26 >> 32) + (l5 + (l4 + (0xFFFFFFFFL & (long)arrn[11])) - l15);
        arrn2[11] = (int)l27;
        SecP384R1Field.reduce32((int)(1L + (l27 >> 32)), arrn2);
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
            long l4 = (l3 >> 32) + ((0xFFFFFFFFL & (long)arrn[1]) - l2);
            arrn[1] = (int)l4;
            long l5 = l4 >> 32;
            if (l5 != 0L) {
                long l6 = l5 + (0xFFFFFFFFL & (long)arrn[2]);
                arrn[2] = (int)l6;
                l5 = l6 >> 32;
            }
            long l7 = l5 + (l2 + (0xFFFFFFFFL & (long)arrn[3]));
            arrn[3] = (int)l7;
            long l8 = (l7 >> 32) + (l2 + (0xFFFFFFFFL & (long)arrn[4]));
            arrn[4] = (int)l8;
            l = l8 >> 32;
        } else {
            l = 0L;
        }
        if (l != 0L && Nat.incAt(12, arrn, 5) != 0 || arrn[11] == -1 && Nat.gte(12, arrn, P)) {
            SecP384R1Field.addPInvTo(arrn);
        }
    }

    public static void square(int[] arrn, int[] arrn2) {
        int[] arrn3 = Nat.create(24);
        Nat384.square(arrn, arrn3);
        SecP384R1Field.reduce(arrn3, arrn2);
    }

    public static void squareN(int[] arrn, int n, int[] arrn2) {
        int[] arrn3 = Nat.create(24);
        Nat384.square(arrn, arrn3);
        SecP384R1Field.reduce(arrn3, arrn2);
        while (--n > 0) {
            Nat384.square(arrn2, arrn3);
            SecP384R1Field.reduce(arrn3, arrn2);
        }
    }

    private static void subPInvFrom(int[] arrn) {
        long l = (0xFFFFFFFFL & (long)arrn[0]) - 1L;
        arrn[0] = (int)l;
        long l2 = (l >> 32) + (1L + (0xFFFFFFFFL & (long)arrn[1]));
        arrn[1] = (int)l2;
        long l3 = l2 >> 32;
        if (l3 != 0L) {
            long l4 = l3 + (0xFFFFFFFFL & (long)arrn[2]);
            arrn[2] = (int)l4;
            l3 = l4 >> 32;
        }
        long l5 = l3 + ((0xFFFFFFFFL & (long)arrn[3]) - 1L);
        arrn[3] = (int)l5;
        long l6 = (l5 >> 32) + ((0xFFFFFFFFL & (long)arrn[4]) - 1L);
        arrn[4] = (int)l6;
        if (l6 >> 32 != 0L) {
            Nat.decAt(12, arrn, 5);
        }
    }

    public static void subtract(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat.sub(12, arrn, arrn2, arrn3) != 0) {
            SecP384R1Field.subPInvFrom(arrn3);
        }
    }

    public static void subtractExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat.sub(24, arrn, arrn2, arrn3) != 0 && Nat.subFrom(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.decAt(24, arrn3, PExtInv.length);
        }
    }

    public static void twice(int[] arrn, int[] arrn2) {
        if (Nat.shiftUpBit(12, arrn, 0, arrn2) != 0 || arrn2[11] == -1 && Nat.gte(12, arrn2, P)) {
            SecP384R1Field.addPInvTo(arrn2);
        }
    }
}

