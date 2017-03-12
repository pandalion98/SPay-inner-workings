package org.bouncycastle.math.raw;

public abstract class Mont256 {
    private static final long f378M = 4294967295L;

    public static int inverse32(int i) {
        int i2 = (2 - (i * i)) * i;
        i2 *= 2 - (i * i2);
        i2 *= 2 - (i * i2);
        return i2 * (2 - (i * i2));
    }

    public static void multAdd(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i) {
        long j = ((long) iArr2[0]) & f378M;
        int i2 = 0;
        int i3 = 0;
        while (i3 < 8) {
            long j2 = ((long) iArr[i3]) & f378M;
            long j3 = j2 * j;
            long j4 = (((long) iArr3[0]) & f378M) + (f378M & j3);
            long j5 = ((long) (((int) j4) * i)) & f378M;
            long j6 = (((long) iArr4[0]) & f378M) * j5;
            j4 = (((j4 + (f378M & j6)) >>> 32) + (j3 >>> 32)) + (j6 >>> 32);
            for (int i4 = 1; i4 < 8; i4++) {
                j3 = (((long) iArr2[i4]) & f378M) * j2;
                j6 = (((long) iArr4[i4]) & f378M) * j5;
                j4 += ((f378M & j3) + (f378M & j6)) + (((long) iArr3[i4]) & f378M);
                iArr3[i4 - 1] = (int) j4;
                j4 = ((j4 >>> 32) + (j3 >>> 32)) + (j6 >>> 32);
            }
            j4 += ((long) i2) & f378M;
            iArr3[7] = (int) j4;
            i3++;
            i2 = (int) (j4 >>> 32);
        }
        if (i2 != 0 || Nat256.gte(iArr3, iArr4)) {
            Nat256.sub(iArr3, iArr4, iArr3);
        }
    }

    public static void multAddXF(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4) {
        long j = ((long) iArr2[0]) & f378M;
        int i = 0;
        int i2 = 0;
        while (i2 < 8) {
            long j2 = f378M & ((long) iArr[i2]);
            long j3 = (j2 * j) + (((long) iArr3[0]) & f378M);
            long j4 = f378M & j3;
            j3 = (j3 >>> 32) + j4;
            for (int i3 = 1; i3 < 8; i3++) {
                long j5 = (((long) iArr2[i3]) & f378M) * j2;
                long j6 = (((long) iArr4[i3]) & f378M) * j4;
                j3 += ((f378M & j5) + (f378M & j6)) + (((long) iArr3[i3]) & f378M);
                iArr3[i3 - 1] = (int) j3;
                j3 = ((j3 >>> 32) + (j5 >>> 32)) + (j6 >>> 32);
            }
            j3 += ((long) i) & f378M;
            iArr3[7] = (int) j3;
            i2++;
            i = (int) (j3 >>> 32);
        }
        if (i != 0 || Nat256.gte(iArr3, iArr4)) {
            Nat256.sub(iArr3, iArr4, iArr3);
        }
    }

    public static void reduce(int[] iArr, int[] iArr2, int i) {
        for (int i2 = 0; i2 < 8; i2++) {
            int i3 = iArr[0];
            long j = f378M & ((long) (i3 * i));
            long j2 = (((((long) iArr2[0]) & f378M) * j) + (((long) i3) & f378M)) >>> 32;
            for (i3 = 1; i3 < 8; i3++) {
                j2 += ((((long) iArr2[i3]) & f378M) * j) + (((long) iArr[i3]) & f378M);
                iArr[i3 - 1] = (int) j2;
                j2 >>>= 32;
            }
            iArr[7] = (int) j2;
        }
        if (Nat256.gte(iArr, iArr2)) {
            Nat256.sub(iArr, iArr2, iArr);
        }
    }

    public static void reduceXF(int[] iArr, int[] iArr2) {
        for (int i = 0; i < 8; i++) {
            long j = f378M & ((long) iArr[0]);
            long j2 = j;
            for (int i2 = 1; i2 < 8; i2++) {
                j2 += ((((long) iArr2[i2]) & f378M) * j) + (((long) iArr[i2]) & f378M);
                iArr[i2 - 1] = (int) j2;
                j2 >>>= 32;
            }
            iArr[7] = (int) j2;
        }
        if (Nat256.gte(iArr, iArr2)) {
            Nat256.sub(iArr, iArr2, iArr);
        }
    }
}
