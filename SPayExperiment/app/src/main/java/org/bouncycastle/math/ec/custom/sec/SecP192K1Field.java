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

public class SecP192K1Field {
    static final int[] P = new int[]{-4553, -2, -1, -1, -1, -1};
    private static final int P5 = -1;
    static final int[] PExt = new int[]{20729809, 9106, 1, 0, 0, 0, -9106, -3, -1, -1, -1, -1};
    private static final int PExt11 = -1;
    private static final int[] PExtInv = new int[]{-20729809, -9107, -2, -1, -1, -1, 9105, 2};
    private static final int PInv33 = 4553;

    public static void add(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat192.add(arrn, arrn2, arrn3) != 0 || arrn3[5] == -1 && Nat192.gte(arrn3, P)) {
            Nat.add33To(6, 4553, arrn3);
        }
    }

    public static void addExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if ((Nat.add(12, arrn, arrn2, arrn3) != 0 || arrn3[11] == -1 && Nat.gte(12, arrn3, PExt)) && Nat.addTo(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.incAt(12, arrn3, PExtInv.length);
        }
    }

    public static void addOne(int[] arrn, int[] arrn2) {
        if (Nat.inc(6, arrn, arrn2) != 0 || arrn2[5] == -1 && Nat192.gte(arrn2, P)) {
            Nat.add33To(6, 4553, arrn2);
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
        SecP192K1Field.reduce(arrn4, arrn3);
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
        if (Nat192.mul33DWordAdd(4553, Nat192.mul33Add(4553, arrn, 6, arrn, 0, arrn2, 0), arrn2, 0) != 0 || arrn2[5] == -1 && Nat192.gte(arrn2, P)) {
            Nat.add33To(6, 4553, arrn2);
        }
    }

    public static void reduce32(int n, int[] arrn) {
        if (n != 0 && Nat192.mul33WordAdd(4553, n, arrn, 0) != 0 || arrn[5] == -1 && Nat192.gte(arrn, P)) {
            Nat.add33To(6, 4553, arrn);
        }
    }

    public static void square(int[] arrn, int[] arrn2) {
        int[] arrn3 = Nat192.createExt();
        Nat192.square(arrn, arrn3);
        SecP192K1Field.reduce(arrn3, arrn2);
    }

    public static void squareN(int[] arrn, int n, int[] arrn2) {
        int[] arrn3 = Nat192.createExt();
        Nat192.square(arrn, arrn3);
        SecP192K1Field.reduce(arrn3, arrn2);
        while (--n > 0) {
            Nat192.square(arrn2, arrn3);
            SecP192K1Field.reduce(arrn3, arrn2);
        }
    }

    public static void subtract(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat192.sub(arrn, arrn2, arrn3) != 0) {
            Nat.sub33From(6, 4553, arrn3);
        }
    }

    public static void subtractExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat.sub(12, arrn, arrn2, arrn3) != 0 && Nat.subFrom(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.decAt(12, arrn3, PExtInv.length);
        }
    }

    public static void twice(int[] arrn, int[] arrn2) {
        if (Nat.shiftUpBit(6, arrn, 0, arrn2) != 0 || arrn2[5] == -1 && Nat192.gte(arrn2, P)) {
            Nat.add33To(6, 4553, arrn2);
        }
    }
}

