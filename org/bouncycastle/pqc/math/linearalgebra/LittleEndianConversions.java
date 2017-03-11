package org.bouncycastle.pqc.math.linearalgebra;

import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public final class LittleEndianConversions {
    private LittleEndianConversions() {
    }

    public static void I2OSP(int i, byte[] bArr, int i2) {
        int i3 = i2 + 1;
        bArr[i2] = (byte) i;
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i >>> 8);
        i3 = i4 + 1;
        bArr[i4] = (byte) (i >>> 16);
        i4 = i3 + 1;
        bArr[i3] = (byte) (i >>> 24);
    }

    public static void I2OSP(int i, byte[] bArr, int i2, int i3) {
        for (int i4 = i3 - 1; i4 >= 0; i4--) {
            bArr[i2 + i4] = (byte) (i >>> (i4 * 8));
        }
    }

    public static void I2OSP(long j, byte[] bArr, int i) {
        int i2 = i + 1;
        bArr[i] = (byte) ((int) j);
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((int) (j >>> 8));
        i2 = i3 + 1;
        bArr[i3] = (byte) ((int) (j >>> 16));
        i3 = i2 + 1;
        bArr[i2] = (byte) ((int) (j >>> 24));
        i2 = i3 + 1;
        bArr[i3] = (byte) ((int) (j >>> 32));
        i3 = i2 + 1;
        bArr[i2] = (byte) ((int) (j >>> 40));
        i2 = i3 + 1;
        bArr[i3] = (byte) ((int) (j >>> 48));
        bArr[i2] = (byte) ((int) (j >>> 56));
    }

    public static byte[] I2OSP(int i) {
        return new byte[]{(byte) i, (byte) (i >>> 8), (byte) (i >>> 16), (byte) (i >>> 24)};
    }

    public static byte[] I2OSP(long j) {
        return new byte[]{(byte) ((int) j), (byte) ((int) (j >>> 8)), (byte) ((int) (j >>> 16)), (byte) ((int) (j >>> 24)), (byte) ((int) (j >>> 32)), (byte) ((int) (j >>> 40)), (byte) ((int) (j >>> 48)), (byte) ((int) (j >>> 56))};
    }

    public static int OS2IP(byte[] bArr) {
        return (((bArr[0] & GF2Field.MASK) | ((bArr[1] & GF2Field.MASK) << 8)) | ((bArr[2] & GF2Field.MASK) << 16)) | ((bArr[3] & GF2Field.MASK) << 24);
    }

    public static int OS2IP(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        return ((((bArr[i2] & GF2Field.MASK) << 8) | (bArr[i] & GF2Field.MASK)) | ((bArr[i3] & GF2Field.MASK) << 16)) | ((bArr[i3 + 1] & GF2Field.MASK) << 24);
    }

    public static int OS2IP(byte[] bArr, int i, int i2) {
        int i3 = 0;
        for (int i4 = i2 - 1; i4 >= 0; i4--) {
            i3 |= (bArr[i + i4] & GF2Field.MASK) << (i4 * 8);
        }
        return i3;
    }

    public static long OS2LIP(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        i2 = i3 + 1;
        i3 = i2 + 1;
        i2 = i3 + 1;
        i3 = i2 + 1;
        i2 = i3 + 1;
        long j = (((((((long) (bArr[i] & GF2Field.MASK)) | ((long) ((bArr[i2] & GF2Field.MASK) << 8))) | ((long) ((bArr[i3] & GF2Field.MASK) << 16))) | ((((long) bArr[i2]) & 255) << 24)) | ((((long) bArr[i3]) & 255) << 32)) | ((((long) bArr[i2]) & 255) << 40)) | ((((long) bArr[i3]) & 255) << 48);
        i3 = i2 + 1;
        return ((((long) bArr[i2]) & 255) << 56) | j;
    }

    public static byte[] toByteArray(int[] iArr, int i) {
        int i2 = 0;
        int length = iArr.length;
        byte[] bArr = new byte[i];
        int i3 = 0;
        while (i2 <= length - 2) {
            I2OSP(iArr[i2], bArr, i3);
            i2++;
            i3 += 4;
        }
        I2OSP(iArr[length - 1], bArr, i3, i - i3);
        return bArr;
    }

    public static int[] toIntArray(byte[] bArr) {
        int i = 0;
        int length = (bArr.length + 3) / 4;
        int length2 = bArr.length & 3;
        int[] iArr = new int[length];
        int i2 = 0;
        while (i <= length - 2) {
            iArr[i] = OS2IP(bArr, i2);
            i++;
            i2 += 4;
        }
        if (length2 != 0) {
            iArr[length - 1] = OS2IP(bArr, i2, length2);
        } else {
            iArr[length - 1] = OS2IP(bArr, i2);
        }
        return iArr;
    }
}
