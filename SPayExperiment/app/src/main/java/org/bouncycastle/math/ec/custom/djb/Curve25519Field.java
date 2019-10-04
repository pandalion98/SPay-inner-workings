/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec.custom.djb;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat256;

public class Curve25519Field {
    private static final long M = 0xFFFFFFFFL;
    static final int[] P = new int[]{-19, -1, -1, -1, -1, -1, -1, Integer.MAX_VALUE};
    private static final int P7 = Integer.MAX_VALUE;
    private static final int[] PExt = new int[]{361, 0, 0, 0, 0, 0, 0, 0, -19, -1, -1, -1, -1, -1, -1, 1073741823};
    private static final int PInv = 19;

    public static void add(int[] arrn, int[] arrn2, int[] arrn3) {
        Nat256.add(arrn, arrn2, arrn3);
        if (Nat256.gte(arrn3, P)) {
            Curve25519Field.subPFrom(arrn3);
        }
    }

    public static void addExt(int[] arrn, int[] arrn2, int[] arrn3) {
        Nat.add(16, arrn, arrn2, arrn3);
        if (Nat.gte(16, arrn3, PExt)) {
            Curve25519Field.subPExtFrom(arrn3);
        }
    }

    public static void addOne(int[] arrn, int[] arrn2) {
        Nat.inc(8, arrn, arrn2);
        if (Nat256.gte(arrn2, P)) {
            Curve25519Field.subPFrom(arrn2);
        }
    }

    private static int addPExtTo(int[] arrn) {
        long l = (0xFFFFFFFFL & (long)arrn[0]) + (0xFFFFFFFFL & (long)PExt[0]);
        arrn[0] = (int)l;
        long l2 = l >> 32;
        if (l2 != 0L) {
            l2 = Nat.incAt(8, arrn, 1);
        }
        long l3 = l2 + ((0xFFFFFFFFL & (long)arrn[8]) - 19L);
        arrn[8] = (int)l3;
        long l4 = l3 >> 32;
        if (l4 != 0L) {
            l4 = Nat.decAt(15, arrn, 9);
        }
        long l5 = l4 + ((0xFFFFFFFFL & (long)arrn[15]) + (0xFFFFFFFFL & (long)(1 + PExt[15])));
        arrn[15] = (int)l5;
        return (int)(l5 >> 32);
    }

    private static int addPTo(int[] arrn) {
        long l = (0xFFFFFFFFL & (long)arrn[0]) - 19L;
        arrn[0] = (int)l;
        long l2 = l >> 32;
        if (l2 != 0L) {
            l2 = Nat.decAt(7, arrn, 1);
        }
        long l3 = l2 + (0x80000000L + (0xFFFFFFFFL & (long)arrn[7]));
        arrn[7] = (int)l3;
        return (int)(l3 >> 32);
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] arrn = Nat256.fromBigInteger(bigInteger);
        while (Nat256.gte(arrn, P)) {
            Nat256.subFrom(P, arrn);
        }
        return arrn;
    }

    public static void half(int[] arrn, int[] arrn2) {
        if ((1 & arrn[0]) == 0) {
            Nat.shiftDownBit(8, arrn, 0, arrn2);
            return;
        }
        Nat256.add(arrn, P, arrn2);
        Nat.shiftDownBit(8, arrn2, 0);
    }

    public static void multiply(int[] arrn, int[] arrn2, int[] arrn3) {
        int[] arrn4 = Nat256.createExt();
        Nat256.mul(arrn, arrn2, arrn4);
        Curve25519Field.reduce(arrn4, arrn3);
    }

    public static void multiplyAddToExt(int[] arrn, int[] arrn2, int[] arrn3) {
        Nat256.mulAddTo(arrn, arrn2, arrn3);
        if (Nat.gte(16, arrn3, PExt)) {
            Curve25519Field.subPExtFrom(arrn3);
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
        int n = arrn[7];
        Nat.shiftUpBit(8, arrn, 8, n, arrn2, 0);
        int n2 = Nat256.mulByWordAddTo(19, arrn, arrn2) << 1;
        int n3 = arrn2[7];
        int n4 = n2 + ((n3 >>> 31) - (n >>> 31));
        arrn2[7] = (n3 & Integer.MAX_VALUE) + Nat.addWordTo(7, n4 * 19, arrn2);
        if (Nat256.gte(arrn2, P)) {
            Curve25519Field.subPFrom(arrn2);
        }
    }

    public static void reduce27(int n, int[] arrn) {
        int n2 = arrn[7];
        int n3 = n << 1 | n2 >>> 31;
        arrn[7] = (n2 & Integer.MAX_VALUE) + Nat.addWordTo(7, n3 * 19, arrn);
        if (Nat256.gte(arrn, P)) {
            Curve25519Field.subPFrom(arrn);
        }
    }

    public static void square(int[] arrn, int[] arrn2) {
        int[] arrn3 = Nat256.createExt();
        Nat256.square(arrn, arrn3);
        Curve25519Field.reduce(arrn3, arrn2);
    }

    public static void squareN(int[] arrn, int n, int[] arrn2) {
        int[] arrn3 = Nat256.createExt();
        Nat256.square(arrn, arrn3);
        Curve25519Field.reduce(arrn3, arrn2);
        while (--n > 0) {
            Nat256.square(arrn2, arrn3);
            Curve25519Field.reduce(arrn3, arrn2);
        }
    }

    private static int subPExtFrom(int[] arrn) {
        long l = (0xFFFFFFFFL & (long)arrn[0]) - (0xFFFFFFFFL & (long)PExt[0]);
        arrn[0] = (int)l;
        long l2 = l >> 32;
        if (l2 != 0L) {
            l2 = Nat.decAt(8, arrn, 1);
        }
        long l3 = l2 + (19L + (0xFFFFFFFFL & (long)arrn[8]));
        arrn[8] = (int)l3;
        long l4 = l3 >> 32;
        if (l4 != 0L) {
            l4 = Nat.incAt(15, arrn, 9);
        }
        long l5 = l4 + ((0xFFFFFFFFL & (long)arrn[15]) - (0xFFFFFFFFL & (long)(1 + PExt[15])));
        arrn[15] = (int)l5;
        return (int)(l5 >> 32);
    }

    private static int subPFrom(int[] arrn) {
        long l = 19L + (0xFFFFFFFFL & (long)arrn[0]);
        arrn[0] = (int)l;
        long l2 = l >> 32;
        if (l2 != 0L) {
            l2 = Nat.incAt(7, arrn, 1);
        }
        long l3 = l2 + ((0xFFFFFFFFL & (long)arrn[7]) - 0x80000000L);
        arrn[7] = (int)l3;
        return (int)(l3 >> 32);
    }

    public static void subtract(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat256.sub(arrn, arrn2, arrn3) != 0) {
            Curve25519Field.addPTo(arrn3);
        }
    }

    public static void subtractExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat.sub(16, arrn, arrn2, arrn3) != 0) {
            Curve25519Field.addPExtTo(arrn3);
        }
    }

    public static void twice(int[] arrn, int[] arrn2) {
        Nat.shiftUpBit(8, arrn, 0, arrn2);
        if (Nat256.gte(arrn2, P)) {
            Curve25519Field.subPFrom(arrn2);
        }
    }
}

