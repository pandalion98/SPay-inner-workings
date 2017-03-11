package org.bouncycastle.math.ec.custom.sec;

import java.math.BigInteger;
import org.bouncycastle.math.raw.Nat;
import org.bouncycastle.math.raw.Nat256;

public class SecP256R1Field {
    private static final long f365M = 4294967295L;
    static final int[] f366P;
    private static final int P7 = -1;
    static final int[] PExt;
    private static final int PExt15 = -1;

    static {
        f366P = new int[]{PExt15, PExt15, PExt15, 0, 0, 0, 1, PExt15};
        PExt = new int[]{1, 0, 0, -2, PExt15, PExt15, -2, 1, -2, 1, -2, 1, 1, -2, 2, -2};
    }

    public static void add(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat256.add(iArr, iArr2, iArr3) != 0 || (iArr3[7] == PExt15 && Nat256.gte(iArr3, f366P))) {
            addPInvTo(iArr3);
        }
    }

    public static void addExt(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat.add(16, iArr, iArr2, iArr3) != 0 || ((iArr3[15] & PExt15) == PExt15 && Nat.gte(16, iArr3, PExt))) {
            Nat.subFrom(16, PExt, iArr3);
        }
    }

    public static void addOne(int[] iArr, int[] iArr2) {
        if (Nat.inc(8, iArr, iArr2) != 0 || (iArr2[7] == PExt15 && Nat256.gte(iArr2, f366P))) {
            addPInvTo(iArr2);
        }
    }

    private static void addPInvTo(int[] iArr) {
        long j = (((long) iArr[0]) & f365M) + 1;
        iArr[0] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[1]) & f365M;
            iArr[1] = (int) j;
            j = (j >> 32) + (((long) iArr[2]) & f365M);
            iArr[2] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[3]) & f365M) - 1;
        iArr[3] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[4]) & f365M;
            iArr[4] = (int) j;
            j = (j >> 32) + (((long) iArr[5]) & f365M);
            iArr[5] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[6]) & f365M) - 1;
        iArr[6] = (int) j;
        iArr[7] = (int) ((j >> 32) + ((((long) iArr[7]) & f365M) + 1));
    }

    public static int[] fromBigInteger(BigInteger bigInteger) {
        int[] fromBigInteger = Nat256.fromBigInteger(bigInteger);
        if (fromBigInteger[7] == PExt15 && Nat256.gte(fromBigInteger, f366P)) {
            Nat256.subFrom(f366P, fromBigInteger);
        }
        return fromBigInteger;
    }

    public static void half(int[] iArr, int[] iArr2) {
        if ((iArr[0] & 1) == 0) {
            Nat.shiftDownBit(8, iArr, 0, iArr2);
        } else {
            Nat.shiftDownBit(8, iArr2, Nat256.add(iArr, f366P, iArr2));
        }
    }

    public static void multiply(int[] iArr, int[] iArr2, int[] iArr3) {
        int[] createExt = Nat256.createExt();
        Nat256.mul(iArr, iArr2, createExt);
        reduce(createExt, iArr3);
    }

    public static void multiplyAddToExt(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat256.mulAddTo(iArr, iArr2, iArr3) != 0 || ((iArr3[15] & PExt15) == PExt15 && Nat.gte(16, iArr3, PExt))) {
            Nat.subFrom(16, PExt, iArr3);
        }
    }

    public static void negate(int[] iArr, int[] iArr2) {
        if (Nat256.isZero(iArr)) {
            Nat256.zero(iArr2);
        } else {
            Nat256.sub(f366P, iArr, iArr2);
        }
    }

    public static void reduce(int[] iArr, int[] iArr2) {
        long j = ((long) iArr[8]) & f365M;
        long j2 = ((long) iArr[9]) & f365M;
        long j3 = ((long) iArr[10]) & f365M;
        long j4 = ((long) iArr[11]) & f365M;
        long j5 = ((long) iArr[12]) & f365M;
        long j6 = ((long) iArr[13]) & f365M;
        long j7 = ((long) iArr[14]) & f365M;
        long j8 = ((long) iArr[15]) & f365M;
        j -= 6;
        long j9 = j + j2;
        j2 += j3;
        j3 = (j3 + j4) - j8;
        j4 += j5;
        j5 += j6;
        long j10 = j6 + j7;
        long j11 = j7 + j8;
        long j12 = 0 + ((((((long) iArr[0]) & f365M) + j9) - j4) - j10);
        iArr2[0] = (int) j12;
        j12 = (j12 >> 32) + ((((((long) iArr[1]) & f365M) + j2) - j5) - j11);
        iArr2[1] = (int) j12;
        j12 = (j12 >> 32) + (((((long) iArr[2]) & f365M) + j3) - j10);
        iArr2[2] = (int) j12;
        j4 <<= 1;
        j4 = ((((j4 + (((long) iArr[3]) & f365M)) + j6) - j8) - j9) + (j12 >> 32);
        iArr2[3] = (int) j4;
        j2 = ((((((long) iArr[4]) & f365M) + (j5 << 1)) + j7) - j2) + (j4 >> 32);
        iArr2[4] = (int) j2;
        j2 = (j2 >> 32) + (((((long) iArr[5]) & f365M) + (j10 << 1)) - j3);
        iArr2[5] = (int) j2;
        j2 = (j2 >> 32) + ((((((long) iArr[6]) & f365M) + (j11 << 1)) + j10) - j9);
        iArr2[6] = (int) j2;
        j = (((j + ((((long) iArr[7]) & f365M) + (j8 << 1))) - j3) - j5) + (j2 >> 32);
        iArr2[7] = (int) j;
        reduce32((int) ((j >> 32) + 6), iArr2);
    }

    public static void reduce32(int i, int[] iArr) {
        long j;
        if (i != 0) {
            long j2 = ((long) i) & f365M;
            j = ((((long) iArr[0]) & f365M) + j2) + 0;
            iArr[0] = (int) j;
            j >>= 32;
            if (j != 0) {
                j += ((long) iArr[1]) & f365M;
                iArr[1] = (int) j;
                j = (j >> 32) + (((long) iArr[2]) & f365M);
                iArr[2] = (int) j;
                j >>= 32;
            }
            j += (((long) iArr[3]) & f365M) - j2;
            iArr[3] = (int) j;
            j >>= 32;
            if (j != 0) {
                j += ((long) iArr[4]) & f365M;
                iArr[4] = (int) j;
                j = (j >> 32) + (((long) iArr[5]) & f365M);
                iArr[5] = (int) j;
                j >>= 32;
            }
            j += (((long) iArr[6]) & f365M) - j2;
            iArr[6] = (int) j;
            j = (j >> 32) + (j2 + (((long) iArr[7]) & f365M));
            iArr[7] = (int) j;
            j >>= 32;
        } else {
            j = 0;
        }
        if (j != 0 || (iArr[7] == PExt15 && Nat256.gte(iArr, f366P))) {
            addPInvTo(iArr);
        }
    }

    public static void square(int[] iArr, int[] iArr2) {
        int[] createExt = Nat256.createExt();
        Nat256.square(iArr, createExt);
        reduce(createExt, iArr2);
    }

    public static void squareN(int[] iArr, int i, int[] iArr2) {
        int[] createExt = Nat256.createExt();
        Nat256.square(iArr, createExt);
        reduce(createExt, iArr2);
        while (true) {
            i += PExt15;
            if (i > 0) {
                Nat256.square(iArr2, createExt);
                reduce(createExt, iArr2);
            } else {
                return;
            }
        }
    }

    private static void subPInvFrom(int[] iArr) {
        long j = (((long) iArr[0]) & f365M) - 1;
        iArr[0] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[1]) & f365M;
            iArr[1] = (int) j;
            j = (j >> 32) + (((long) iArr[2]) & f365M);
            iArr[2] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[3]) & f365M) + 1;
        iArr[3] = (int) j;
        j >>= 32;
        if (j != 0) {
            j += ((long) iArr[4]) & f365M;
            iArr[4] = (int) j;
            j = (j >> 32) + (((long) iArr[5]) & f365M);
            iArr[5] = (int) j;
            j >>= 32;
        }
        j += (((long) iArr[6]) & f365M) + 1;
        iArr[6] = (int) j;
        iArr[7] = (int) ((j >> 32) + ((((long) iArr[7]) & f365M) - 1));
    }

    public static void subtract(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat256.sub(iArr, iArr2, iArr3) != 0) {
            subPInvFrom(iArr3);
        }
    }

    public static void subtractExt(int[] iArr, int[] iArr2, int[] iArr3) {
        if (Nat.sub(16, iArr, iArr2, iArr3) != 0) {
            Nat.addTo(16, PExt, iArr3);
        }
    }

    public static void twice(int[] iArr, int[] iArr2) {
        if (Nat.shiftUpBit(8, iArr, 0, iArr2) != 0 || (iArr2[7] == PExt15 && Nat256.gte(iArr2, f366P))) {
            addPInvTo(iArr2);
        }
    }
}
