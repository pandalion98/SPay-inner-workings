package org.bouncycastle.crypto.modes.gcm;

import com.mastercard.mobile_api.utils.apdu.emv.VerifyPINApdu;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.util.Pack;

public abstract class GCMUtil {
    private static final int E1 = -520093696;
    private static final long E1L = -2233785415175766016L;
    private static final int[] LOOKUP;

    static {
        LOOKUP = generateLookup();
    }

    public static void asBytes(int[] iArr, byte[] bArr) {
        Pack.intToBigEndian(iArr, bArr, 0);
    }

    public static void asBytes(long[] jArr, byte[] bArr) {
        Pack.longToBigEndian(jArr, bArr, 0);
    }

    public static byte[] asBytes(int[] iArr) {
        byte[] bArr = new byte[16];
        Pack.intToBigEndian(iArr, bArr, 0);
        return bArr;
    }

    public static byte[] asBytes(long[] jArr) {
        byte[] bArr = new byte[16];
        Pack.longToBigEndian(jArr, bArr, 0);
        return bArr;
    }

    public static void asInts(byte[] bArr, int[] iArr) {
        Pack.bigEndianToInt(bArr, 0, iArr);
    }

    public static int[] asInts(byte[] bArr) {
        int[] iArr = new int[4];
        Pack.bigEndianToInt(bArr, 0, iArr);
        return iArr;
    }

    public static void asLongs(byte[] bArr, long[] jArr) {
        Pack.bigEndianToLong(bArr, 0, jArr);
    }

    public static long[] asLongs(byte[] bArr) {
        long[] jArr = new long[2];
        Pack.bigEndianToLong(bArr, 0, jArr);
        return jArr;
    }

    private static int[] generateLookup() {
        int[] iArr = new int[SkeinMac.SKEIN_256];
        for (int i = 0; i < SkeinMac.SKEIN_256; i++) {
            int i2 = 0;
            for (int i3 = 7; i3 >= 0; i3--) {
                if (((1 << i3) & i) != 0) {
                    i2 ^= E1 >>> (7 - i3);
                }
            }
            iArr[i] = i2;
        }
        return iArr;
    }

    public static void multiply(byte[] bArr, byte[] bArr2) {
        int[] asInts = asInts(bArr);
        multiply(asInts, asInts(bArr2));
        asBytes(asInts, bArr);
    }

    public static void multiply(int[] iArr, int[] iArr2) {
        int i = iArr[0];
        int i2 = iArr[1];
        int i3 = iArr[2];
        int i4 = iArr[3];
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        for (int i9 = 0; i9 < 4; i9++) {
            int i10 = iArr2[i9];
            for (int i11 = 0; i11 < 32; i11++) {
                int i12 = i10 >> 31;
                i10 <<= 1;
                i5 ^= i & i12;
                i6 ^= i2 & i12;
                i7 ^= i3 & i12;
                i8 ^= i12 & i4;
                i4 = (i4 >>> 1) | (i3 << 63);
                i3 = (i3 >>> 1) | (i2 << 63);
                i2 = (i2 >>> 1) | (i << 63);
                i = (i >>> 1) ^ (((i4 << 31) >> 8) & E1);
            }
        }
        iArr[0] = i5;
        iArr[1] = i6;
        iArr[2] = i7;
        iArr[3] = i8;
    }

    public static void multiply(long[] jArr, long[] jArr2) {
        long j = jArr[0];
        long j2 = jArr[1];
        long j3 = 0;
        long j4 = 0;
        for (int i = 0; i < 2; i++) {
            long j5 = jArr2[i];
            for (int i2 = 0; i2 < 64; i2++) {
                long j6 = j5 >> 63;
                j5 <<= 1;
                j3 ^= j & j6;
                j4 ^= j6 & j2;
                j2 = (j2 >>> 1) | (j << 63);
                j = (j >>> 1) ^ (((j2 << 63) >> 8) & E1L);
            }
        }
        jArr[0] = j3;
        jArr[1] = j4;
    }

    public static void multiplyP(int[] iArr) {
        iArr[0] = ((shiftRight(iArr) >> 8) & E1) ^ iArr[0];
    }

    public static void multiplyP(int[] iArr, int[] iArr2) {
        iArr2[0] = ((shiftRight(iArr, iArr2) >> 8) & E1) ^ iArr2[0];
    }

    public static void multiplyP8(int[] iArr) {
        int shiftRightN = shiftRightN(iArr, 8);
        iArr[0] = LOOKUP[shiftRightN >>> 24] ^ iArr[0];
    }

    public static void multiplyP8(int[] iArr, int[] iArr2) {
        int shiftRightN = shiftRightN(iArr, 8, iArr2);
        iArr2[0] = LOOKUP[shiftRightN >>> 24] ^ iArr2[0];
    }

    public static byte[] oneAsBytes() {
        byte[] bArr = new byte[16];
        bArr[0] = VerifyPINApdu.P2_PLAINTEXT;
        return bArr;
    }

    public static int[] oneAsInts() {
        int[] iArr = new int[4];
        iArr[0] = PKIFailureInfo.systemUnavail;
        return iArr;
    }

    public static long[] oneAsLongs() {
        long[] jArr = new long[2];
        jArr[0] = Long.MIN_VALUE;
        return jArr;
    }

    static int shiftRight(int[] iArr) {
        int i = iArr[0];
        iArr[0] = i >>> 1;
        i <<= 31;
        int i2 = iArr[1];
        iArr[1] = i | (i2 >>> 1);
        i = i2 << 31;
        i2 = iArr[2];
        iArr[2] = i | (i2 >>> 1);
        i = i2 << 31;
        i2 = iArr[3];
        iArr[3] = i | (i2 >>> 1);
        return i2 << 31;
    }

    static int shiftRight(int[] iArr, int[] iArr2) {
        int i = iArr[0];
        iArr2[0] = i >>> 1;
        i <<= 31;
        int i2 = iArr[1];
        iArr2[1] = i | (i2 >>> 1);
        i = i2 << 31;
        i2 = iArr[2];
        iArr2[2] = i | (i2 >>> 1);
        i = i2 << 31;
        i2 = iArr[3];
        iArr2[3] = i | (i2 >>> 1);
        return i2 << 31;
    }

    static long shiftRight(long[] jArr) {
        long j = jArr[0];
        jArr[0] = j >>> 1;
        j <<= 63;
        long j2 = jArr[1];
        jArr[1] = j | (j2 >>> 1);
        return j2 << 63;
    }

    static long shiftRight(long[] jArr, long[] jArr2) {
        long j = jArr[0];
        jArr2[0] = j >>> 1;
        j <<= 63;
        long j2 = jArr[1];
        jArr2[1] = j | (j2 >>> 1);
        return j2 << 63;
    }

    static int shiftRightN(int[] iArr, int i) {
        int i2 = iArr[0];
        int i3 = 32 - i;
        iArr[0] = i2 >>> i;
        i2 <<= i3;
        int i4 = iArr[1];
        iArr[1] = i2 | (i4 >>> i);
        i2 = i4 << i3;
        i4 = iArr[2];
        iArr[2] = i2 | (i4 >>> i);
        i2 = i4 << i3;
        i4 = iArr[3];
        iArr[3] = i2 | (i4 >>> i);
        return i4 << i3;
    }

    static int shiftRightN(int[] iArr, int i, int[] iArr2) {
        int i2 = iArr[0];
        int i3 = 32 - i;
        iArr2[0] = i2 >>> i;
        i2 <<= i3;
        int i4 = iArr[1];
        iArr2[1] = i2 | (i4 >>> i);
        i2 = i4 << i3;
        i4 = iArr[2];
        iArr2[2] = i2 | (i4 >>> i);
        i2 = i4 << i3;
        i4 = iArr[3];
        iArr2[3] = i2 | (i4 >>> i);
        return i4 << i3;
    }

    public static void xor(byte[] bArr, byte[] bArr2) {
        int i = 0;
        do {
            bArr[i] = (byte) (bArr[i] ^ bArr2[i]);
            i++;
            bArr[i] = (byte) (bArr[i] ^ bArr2[i]);
            i++;
            bArr[i] = (byte) (bArr[i] ^ bArr2[i]);
            i++;
            bArr[i] = (byte) (bArr[i] ^ bArr2[i]);
            i++;
        } while (i < 16);
    }

    public static void xor(byte[] bArr, byte[] bArr2, int i, int i2) {
        while (true) {
            i2--;
            if (i2 >= 0) {
                bArr[i2] = (byte) (bArr[i2] ^ bArr2[i + i2]);
            } else {
                return;
            }
        }
    }

    public static void xor(byte[] bArr, byte[] bArr2, byte[] bArr3) {
        int i = 0;
        do {
            bArr3[i] = (byte) (bArr[i] ^ bArr2[i]);
            i++;
            bArr3[i] = (byte) (bArr[i] ^ bArr2[i]);
            i++;
            bArr3[i] = (byte) (bArr[i] ^ bArr2[i]);
            i++;
            bArr3[i] = (byte) (bArr[i] ^ bArr2[i]);
            i++;
        } while (i < 16);
    }

    public static void xor(int[] iArr, int[] iArr2) {
        iArr[0] = iArr[0] ^ iArr2[0];
        iArr[1] = iArr[1] ^ iArr2[1];
        iArr[2] = iArr[2] ^ iArr2[2];
        iArr[3] = iArr[3] ^ iArr2[3];
    }

    public static void xor(int[] iArr, int[] iArr2, int[] iArr3) {
        iArr3[0] = iArr[0] ^ iArr2[0];
        iArr3[1] = iArr[1] ^ iArr2[1];
        iArr3[2] = iArr[2] ^ iArr2[2];
        iArr3[3] = iArr[3] ^ iArr2[3];
    }

    public static void xor(long[] jArr, long[] jArr2) {
        jArr[0] = jArr[0] ^ jArr2[0];
        jArr[1] = jArr[1] ^ jArr2[1];
    }

    public static void xor(long[] jArr, long[] jArr2, long[] jArr3) {
        jArr3[0] = jArr[0] ^ jArr2[0];
        jArr3[1] = jArr[1] ^ jArr2[1];
    }
}
