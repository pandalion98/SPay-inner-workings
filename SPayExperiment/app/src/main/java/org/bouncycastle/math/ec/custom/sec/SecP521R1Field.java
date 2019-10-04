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
import org.bouncycastle.math.raw.Nat512;

public class SecP521R1Field {
    static final int[] P = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 511};
    private static final int P16 = 511;

    public static void add(int[] arrn, int[] arrn2, int[] arrn3) {
        int n = Nat.add(16, arrn, arrn2, arrn3) + arrn[16] + arrn2[16];
        if (n > 511 || n == 511 && Nat.eq(16, arrn3, P)) {
            n = 511 & n + Nat.inc(16, arrn3);
        }
        arrn3[16] = n;
    }

    public static void addOne(int[] arrn, int[] arrn2) {
        int n = Nat.inc(16, arrn, arrn2) + arrn[16];
        if (n > 511 || n == 511 && Nat.eq(16, arrn2, P)) {
            n = 511 & n + Nat.inc(16, arrn2);
        }
        arrn2[16] = n;
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] arrn = Nat.fromBigInteger(521, bigInteger);
        if (Nat.eq(17, arrn, P)) {
            Nat.zero(17, arrn);
        }
        return arrn;
    }

    public static void half(int[] arrn, int[] arrn2) {
        int n = arrn[16];
        int n2 = Nat.shiftDownBit(16, arrn, n, arrn2);
        arrn2[16] = n >>> 1 | n2 >>> 23;
    }

    protected static void implMultiply(int[] arrn, int[] arrn2, int[] arrn3) {
        Nat512.mul(arrn, arrn2, arrn3);
        int n = arrn[16];
        int n2 = arrn2[16];
        arrn3[32] = Nat.mul31BothAdd(16, n, arrn2, n2, arrn, arrn3, 16) + n * n2;
    }

    protected static void implSquare(int[] arrn, int[] arrn2) {
        Nat512.square(arrn, arrn2);
        int n = arrn[16];
        arrn2[32] = Nat.mulWordAddTo(16, n << 1, arrn, 0, arrn2, 16) + n * n;
    }

    public static void multiply(int[] arrn, int[] arrn2, int[] arrn3) {
        int[] arrn4 = Nat.create(33);
        SecP521R1Field.implMultiply(arrn, arrn2, arrn4);
        SecP521R1Field.reduce(arrn4, arrn3);
    }

    public static void negate(int[] arrn, int[] arrn2) {
        if (Nat.isZero(17, arrn)) {
            Nat.zero(17, arrn2);
            return;
        }
        Nat.sub(17, P, arrn, arrn2);
    }

    public static void reduce(int[] arrn, int[] arrn2) {
        int n = arrn[32];
        int n2 = (Nat.shiftDownBits(16, arrn, 16, 9, n, arrn2, 0) >>> 23) + (n >>> 9) + Nat.addTo(16, arrn, arrn2);
        if (n2 > 511 || n2 == 511 && Nat.eq(16, arrn2, P)) {
            n2 = 511 & n2 + Nat.inc(16, arrn2);
        }
        arrn2[16] = n2;
    }

    public static void reduce23(int[] arrn) {
        int n = arrn[16];
        int n2 = Nat.addWordTo(16, n >>> 9, arrn) + (n & 511);
        if (n2 > 511 || n2 == 511 && Nat.eq(16, arrn, P)) {
            n2 = 511 & n2 + Nat.inc(16, arrn);
        }
        arrn[16] = n2;
    }

    public static void square(int[] arrn, int[] arrn2) {
        int[] arrn3 = Nat.create(33);
        SecP521R1Field.implSquare(arrn, arrn3);
        SecP521R1Field.reduce(arrn3, arrn2);
    }

    public static void squareN(int[] arrn, int n, int[] arrn2) {
        int[] arrn3 = Nat.create(33);
        SecP521R1Field.implSquare(arrn, arrn3);
        SecP521R1Field.reduce(arrn3, arrn2);
        while (--n > 0) {
            SecP521R1Field.implSquare(arrn2, arrn3);
            SecP521R1Field.reduce(arrn3, arrn2);
        }
    }

    public static void subtract(int[] arrn, int[] arrn2, int[] arrn3) {
        int n = Nat.sub(16, arrn, arrn2, arrn3) + arrn[16] - arrn2[16];
        if (n < 0) {
            n = 511 & n + Nat.dec(16, arrn3);
        }
        arrn3[16] = n;
    }

    public static void twice(int[] arrn, int[] arrn2) {
        int n = arrn[16];
        arrn2[16] = 511 & (Nat.shiftUpBit(16, arrn, n << 23, arrn2) | n << 1);
    }
}

