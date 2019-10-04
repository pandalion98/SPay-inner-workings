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

public class SecP256K1Field {
    static final int[] P = new int[]{-977, -2, -1, -1, -1, -1, -1, -1};
    private static final int P7 = -1;
    static final int[] PExt = new int[]{954529, 1954, 1, 0, 0, 0, 0, 0, -1954, -3, -1, -1, -1, -1, -1, -1};
    private static final int PExt15 = -1;
    private static final int[] PExtInv = new int[]{-954529, -1955, -2, -1, -1, -1, -1, -1, 1953, 2};
    private static final int PInv33 = 977;

    public static void add(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat256.add(arrn, arrn2, arrn3) != 0 || arrn3[7] == -1 && Nat256.gte(arrn3, P)) {
            Nat.add33To(8, 977, arrn3);
        }
    }

    public static void addExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if ((Nat.add(16, arrn, arrn2, arrn3) != 0 || arrn3[15] == -1 && Nat.gte(16, arrn3, PExt)) && Nat.addTo(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.incAt(16, arrn3, PExtInv.length);
        }
    }

    public static void addOne(int[] arrn, int[] arrn2) {
        if (Nat.inc(8, arrn, arrn2) != 0 || arrn2[7] == -1 && Nat256.gte(arrn2, P)) {
            Nat.add33To(8, 977, arrn2);
        }
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
        SecP256K1Field.reduce(arrn4, arrn3);
    }

    public static void multiplyAddToExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if ((Nat256.mulAddTo(arrn, arrn2, arrn3) != 0 || arrn3[15] == -1 && Nat.gte(16, arrn3, PExt)) && Nat.addTo(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.incAt(16, arrn3, PExtInv.length);
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
        if (Nat256.mul33DWordAdd(977, Nat256.mul33Add(977, arrn, 8, arrn, 0, arrn2, 0), arrn2, 0) != 0 || arrn2[7] == -1 && Nat256.gte(arrn2, P)) {
            Nat.add33To(8, 977, arrn2);
        }
    }

    public static void reduce32(int n, int[] arrn) {
        if (n != 0 && Nat256.mul33WordAdd(977, n, arrn, 0) != 0 || arrn[7] == -1 && Nat256.gte(arrn, P)) {
            Nat.add33To(8, 977, arrn);
        }
    }

    public static void square(int[] arrn, int[] arrn2) {
        int[] arrn3 = Nat256.createExt();
        Nat256.square(arrn, arrn3);
        SecP256K1Field.reduce(arrn3, arrn2);
    }

    public static void squareN(int[] arrn, int n, int[] arrn2) {
        int[] arrn3 = Nat256.createExt();
        Nat256.square(arrn, arrn3);
        SecP256K1Field.reduce(arrn3, arrn2);
        while (--n > 0) {
            Nat256.square(arrn2, arrn3);
            SecP256K1Field.reduce(arrn3, arrn2);
        }
    }

    public static void subtract(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat256.sub(arrn, arrn2, arrn3) != 0) {
            Nat.sub33From(8, 977, arrn3);
        }
    }

    public static void subtractExt(int[] arrn, int[] arrn2, int[] arrn3) {
        if (Nat.sub(16, arrn, arrn2, arrn3) != 0 && Nat.subFrom(PExtInv.length, PExtInv, arrn3) != 0) {
            Nat.decAt(16, arrn3, PExtInv.length);
        }
    }

    public static void twice(int[] arrn, int[] arrn2) {
        if (Nat.shiftUpBit(8, arrn, 0, arrn2) != 0 || arrn2[7] == -1 && Nat256.gte(arrn2, P)) {
            Nat.add33To(8, 977, arrn2);
        }
    }
}

