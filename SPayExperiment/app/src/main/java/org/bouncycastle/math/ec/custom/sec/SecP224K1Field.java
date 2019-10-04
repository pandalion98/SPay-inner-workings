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

public class SecP224K1Field {
    static final int[] P = new int[]{-6803, -2, -1, -1, -1, -1, -1};
    private static final int P6 = -1;
    static final int[] PExt = new int[]{46280809, 13606, 1, 0, 0, 0, 0, -13606, -3, -1, -1, -1, -1, -1};
    private static final int PExt13 = -1;
    private static final int[] PExtInv = new int[]{-46280809, -13607, -2, -1, -1, -1, -1, 13605, 2};
    private static final int PInv33 = 6803;

    public static void add(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat224.add(arrn, arrn2, arrn3) != 0 || arrn3[6] == -1 && Nat224.gte(arrn3, P)) {
            Nat.add33To(7, 6803, arrn3);
        }
    }

    public static void addExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if ((Nat.add(14, arrn, arrn2, arrn3) != 0 || arrn3[13] == -1 && Nat.gte(14, arrn3, PExt)) && Nat.addTo(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.incAt(14, arrn3, PExtInv.length);
        }
    }

    public static void addOne(int[] arrn, int[] arrn2) {
        if (Nat.inc(7, arrn, arrn2) != 0 || arrn2[6] == -1 && Nat224.gte(arrn2, P)) {
            Nat.add33To(7, 6803, arrn2);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] arrn = Nat224.fromBigInteger(bigInteger);
        if (arrn[6] == -1 && Nat224.gte(arrn, P)) {
            Nat.add33To(7, 6803, arrn);
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
        SecP224K1Field.reduce(arrn4, arrn3);
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
        if (Nat224.mul33DWordAdd(6803, Nat224.mul33Add(6803, arrn, 7, arrn, 0, arrn2, 0), arrn2, 0) != 0 || arrn2[6] == -1 && Nat224.gte(arrn2, P)) {
            Nat.add33To(7, 6803, arrn2);
        }
    }

    public static void reduce32(int n, int[] arrn) {
        if (n != 0 && Nat224.mul33WordAdd(6803, n, arrn, 0) != 0 || arrn[6] == -1 && Nat224.gte(arrn, P)) {
            Nat.add33To(7, 6803, arrn);
        }
    }

    public static void square(int[] arrn, int[] arrn2) {
        int[] arrn3 = Nat224.createExt();
        Nat224.square(arrn, arrn3);
        SecP224K1Field.reduce(arrn3, arrn2);
    }

    public static void squareN(int[] arrn, int n, int[] arrn2) {
        int[] arrn3 = Nat224.createExt();
        Nat224.square(arrn, arrn3);
        SecP224K1Field.reduce(arrn3, arrn2);
        while (--n > 0) {
            Nat224.square(arrn2, arrn3);
            SecP224K1Field.reduce(arrn3, arrn2);
        }
    }

    public static void subtract(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat224.sub(arrn, arrn2, arrn3) != 0) {
            Nat.sub33From(7, 6803, arrn3);
        }
    }

    public static void subtractExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat.sub(14, arrn, arrn2, arrn3) != 0 && Nat.subFrom(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.decAt(14, arrn3, PExtInv.length);
        }
    }

    public static void twice(int[] arrn, int[] arrn2) {
        if (Nat.shiftUpBit(7, arrn, 0, arrn2) != 0 || arrn2[6] == -1 && Nat224.gte(arrn2, P)) {
            Nat.add33To(7, 6803, arrn2);
        }
    }
}

