package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat224;

public class SecP224K1Field {
    static final int[] f352P;
    private static final int P6 = -1;
    static final int[] PExt;
    private static final int PExt13 = -1;
    private static final int[] PExtInv;
    private static final int PInv33 = 6803;

    static {
        f352P = new int[]{-6803, -2, PExt13, PExt13, PExt13, PExt13, PExt13};
        PExt = new int[]{46280809, 13606, 1, 0, 0, 0, 0, -13606, -3, PExt13, PExt13, PExt13, PExt13, PExt13};
        PExtInv = new int[]{-46280809, -13607, -2, PExt13, PExt13, PExt13, PExt13, 13605, 2};
    }

    public static void add(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat224.add(iArr, iArr2, iArr3) != 0 || (iArr3[6] == PExt13 && Nat224.gte(iArr3, f352P))) {
            Nat.add33To(7, PInv33, iArr3);
        }
    }

    public static void addExt(int[] iArr, int[] iArr2, int[] iArr3) {
        if ((Nat.add(14, iArr, iArr2, iArr3) != 0 || (iArr3[13] == PExt13 && Nat.gte(14, iArr3, PExt))) && Nat.addTo(PExtInv.length, PExtInv, iArr3) != 0) {
            Nat.incAt(14, iArr3, PExtInv.length);
        }
    }

    public static void addOne(int[] iArr, int[] iArr2) {
        if (Nat.inc(7, iArr, iArr2) != 0 || (iArr2[6] == PExt13 && Nat224.gte(iArr2, f352P))) {
            Nat.add33To(7, PInv33, iArr2);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] fromBigInteger = Nat224.fromBigInteger(bigInteger);
        if (fromBigInteger[6] == PExt13 && Nat224.gte(fromBigInteger, f352P)) {
            Nat.add33To(7, PInv33, fromBigInteger);
        }
        return fromBigInteger;
    }

    public static void half(int[] iArr, int[] iArr2) {
        if ((iArr[0] & 1) == 0) {
            Nat.shiftDownBit(7, iArr, 0, iArr2);
        } else {
            Nat.shiftDownBit(7, iArr2, Nat224.add(iArr, f352P, iArr2));
        }
    }

    public static void multiply(int[] iArr, int[] iArr2, int[] iArr3) {
        int[] createExt = Nat224.createExt();
        Nat224.mul(iArr, iArr2, createExt);
        reduce(createExt, iArr3);
    }

    public static void multiplyAddToExt(int[] iArr, int[] iArr2, int[] iArr3) {
        if ((Nat224.mulAddTo(iArr, iArr2, iArr3) != 0 || (iArr3[13] == PExt13 && Nat.gte(14, iArr3, PExt))) && Nat.addTo(PExtInv.length, PExtInv, iArr3) != 0) {
            Nat.incAt(14, iArr3, PExtInv.length);
        }
    }

    public static void negate(int[] iArr, int[] iArr2) {
        if (Nat224.isZero(iArr)) {
            Nat224.zero(iArr2);
        } else {
            Nat224.sub(f352P, iArr, iArr2);
        }
    }

    public static void reduce(int[] iArr, int[] iArr2) {
        if (Nat224.mul33DWordAdd(PInv33, Nat224.mul33Add(PInv33, iArr, 7, iArr, 0, iArr2, 0), iArr2, 0) != 0 || (iArr2[6] == PExt13 && Nat224.gte(iArr2, f352P))) {
            Nat.add33To(7, PInv33, iArr2);
        }
    }

    public static void reduce32(int i, int[] iArr) {
        if ((i != 0 && Nat224.mul33WordAdd(PInv33, i, iArr, 0) != 0) || (iArr[6] == PExt13 && Nat224.gte(iArr, f352P))) {
            Nat.add33To(7, PInv33, iArr);
        }
    }

    public static void square(int[] iArr, int[] iArr2) {
        int[] createExt = Nat224.createExt();
        Nat224.square(iArr, createExt);
        reduce(createExt, iArr2);
    }

    public static void squareN(int[] iArr, int i, int[] iArr2) {
        int[] createExt = Nat224.createExt();
        Nat224.square(iArr, createExt);
        reduce(createExt, iArr2);
        while (true) {
            i += PExt13;
            if (i > 0) {
                Nat224.square(iArr2, createExt);
                reduce(createExt, iArr2);
            } else {
                return;
            }
        }
    }

    public static void subtract(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat224.sub(iArr, iArr2, iArr3) != 0) {
            Nat.sub33From(7, PInv33, iArr3);
        }
    }

    public static void subtractExt(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat.sub(14, iArr, iArr2, iArr3) != 0 && Nat.subFrom(PExtInv.length, PExtInv, iArr3) != 0) {
            Nat.decAt(14, iArr3, PExtInv.length);
        }
    }

    public static void twice(int[] iArr, int[] iArr2) {
        if (Nat.shiftUpBit(7, iArr, 0, iArr2) != 0 || (iArr2[6] == PExt13 && Nat224.gte(iArr2, f352P))) {
            Nat.add33To(7, PInv33, iArr2);
        }
    }
}
