package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat224;

public class SecP224R1Field {
    private static final long f356M = 4294967295L;
    static final int[] f357P;
    private static final int P6 = -1;
    static final int[] PExt;
    private static final int PExt13 = -1;
    private static final int[] PExtInv;

    static {
        f357P = new int[]{1, 0, 0, PExt13, PExt13, PExt13, PExt13};
        PExt = new int[]{1, 0, 0, -2, PExt13, PExt13, 0, 2, 0, 0, -2, PExt13, PExt13, PExt13};
        PExtInv = new int[]{PExt13, PExt13, PExt13, 1, 0, 0, PExt13, -3, PExt13, PExt13, 1};
    }

    public static void add(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat224.add(iArr, iArr2, iArr3) != 0 || (iArr3[6] == PExt13 && Nat224.gte(iArr3, f357P))) {
            addPInvTo(iArr3);
        }
    }

    public static void addExt(int[] iArr, int[] iArr2, int[] iArr3) {
        if ((Nat.add(14, iArr, iArr2, iArr3) != 0 || (iArr3[13] == PExt13 && Nat.gte(14, iArr3, PExt))) && Nat.addTo(PExtInv.length, PExtInv, iArr3) != 0) {
            Nat.incAt(14, iArr3, PExtInv.length);
        }
    }

    public static void addOne(int[] iArr, int[] iArr2) {
        if (Nat.inc(7, iArr, iArr2) != 0 || (iArr2[6] == PExt13 && Nat224.gte(iArr2, f357P))) {
            addPInvTo(iArr2);
        }
    }

    private static void addPInvTo(int[] iArr) {
        long j = (((long) iArr[0]) & f356M) - 1;
        iArr[0] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[1]) & f356M;
            iArr[1] = (int) j;
            j = (j >> 32) + (((long) iArr[2]) & f356M);
            iArr[2] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[3]) & f356M) + 1;
        iArr[3] = (int) j;
        if ((j >> 32) != 0) {
            Nat.incAt(7, iArr, 4);
        }
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] fromBigInteger = Nat224.fromBigInteger(bigInteger);
        if (fromBigInteger[6] == PExt13 && Nat224.gte(fromBigInteger, f357P)) {
            Nat224.subFrom(f357P, fromBigInteger);
        }
        return fromBigInteger;
    }

    public static void half(int[] iArr, int[] iArr2) {
        if ((iArr[0] & 1) == 0) {
            Nat.shiftDownBit(7, iArr, 0, iArr2);
        } else {
            Nat.shiftDownBit(7, iArr2, Nat224.add(iArr, f357P, iArr2));
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
            Nat224.sub(f357P, iArr, iArr2);
        }
    }

    public static void reduce(int[] iArr, int[] iArr2) {
        long j = ((long) iArr[10]) & f356M;
        long j2 = ((long) iArr[11]) & f356M;
        long j3 = ((long) iArr[12]) & f356M;
        long j4 = ((long) iArr[13]) & f356M;
        long j5 = ((((long) iArr[7]) & f356M) + j2) - 1;
        long j6 = (((long) iArr[8]) & f356M) + j3;
        long j7 = (((long) iArr[9]) & f356M) + j4;
        long j8 = 0 + ((((long) iArr[0]) & f356M) - j5);
        long j9 = f356M & j8;
        j8 = (j8 >> 32) + ((((long) iArr[1]) & f356M) - j6);
        iArr2[1] = (int) j8;
        j8 = (j8 >> 32) + ((((long) iArr[2]) & f356M) - j7);
        iArr2[2] = (int) j8;
        j5 = ((j5 + (((long) iArr[3]) & f356M)) - j) + (j8 >> 32);
        j8 = f356M & j5;
        j2 = ((j6 + (((long) iArr[4]) & f356M)) - j2) + (j5 >> 32);
        iArr2[4] = (int) j2;
        j2 = (j2 >> 32) + (((((long) iArr[5]) & f356M) + j7) - j3);
        iArr2[5] = (int) j2;
        j = ((j + (((long) iArr[6]) & f356M)) - j4) + (j2 >> 32);
        iArr2[6] = (int) j;
        j2 = 1 + (j >> 32);
        j = j8 + j2;
        j2 = j9 - j2;
        iArr2[0] = (int) j2;
        j2 >>= 32;
        if (j2 != 0) {
            j2 += ((long) iArr2[1]) & f356M;
            iArr2[1] = (int) j2;
            j2 = (j2 >> 32) + (((long) iArr2[2]) & f356M);
            iArr2[2] = (int) j2;
            j += j2 >> 32;
        }
        iArr2[3] = (int) j;
        if ((j >> 32) == 0 || Nat.incAt(7, iArr2, 4) == 0) {
            if (iArr2[6] == PExt13) {
                if (!Nat224.gte(iArr2, f357P)) {
                    return;
                }
            }
            return;
        }
        addPInvTo(iArr2);
    }

    public static void reduce32(int i, int[] iArr) {
        long j;
        if (i != 0) {
            long j2 = ((long) i) & f356M;
            j = ((((long) iArr[0]) & f356M) - j2) + 0;
            iArr[0] = (int) j;
            j >>= 32;
            if (j != 0) {
                j += ((long) iArr[1]) & f356M;
                iArr[1] = (int) j;
                j = (j >> 32) + (((long) iArr[2]) & f356M);
                iArr[2] = (int) j;
                j >>= 32;
            }
            j += j2 + (((long) iArr[3]) & f356M);
            iArr[3] = (int) j;
            j >>= 32;
        } else {
            j = 0;
        }
        if ((j != 0 && Nat.incAt(7, iArr, 4) != 0) || (iArr[6] == PExt13 && Nat224.gte(iArr, f357P))) {
            addPInvTo(iArr);
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

    private static void subPInvFrom(int[] iArr) {
        long j = (((long) iArr[0]) & f356M) + 1;
        iArr[0] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[1]) & f356M;
            iArr[1] = (int) j;
            j = (j >> 32) + (((long) iArr[2]) & f356M);
            iArr[2] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[3]) & f356M) - 1;
        iArr[3] = (int) j;
        if ((j >> 32) != 0) {
            Nat.decAt(7, iArr, 4);
        }
    }

    public static void subtract(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat224.sub(iArr, iArr2, iArr3) != 0) {
            subPInvFrom(iArr3);
        }
    }

    public static void subtractExt(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat.sub(14, iArr, iArr2, iArr3) != 0 && Nat.subFrom(PExtInv.length, PExtInv, iArr3) != 0) {
            Nat.decAt(14, iArr3, PExtInv.length);
        }
    }

    public static void twice(int[] iArr, int[] iArr2) {
        if (Nat.shiftUpBit(7, iArr, 0, iArr2) != 0 || (iArr2[6] == PExt13 && Nat224.gte(iArr2, f357P))) {
            addPInvTo(iArr2);
        }
    }
}
