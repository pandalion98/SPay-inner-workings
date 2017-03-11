package org.bouncycastle.crypto.generators;

import com.samsung.android.spayfw.cncc.CNCCCommands;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class SCrypt {
    private static void BlockMix(int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int i) {
        System.arraycopy(iArr, iArr.length - 16, iArr2, 0, 16);
        int length = iArr.length >>> 1;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = i * 2; i4 > 0; i4--) {
            Xor(iArr2, iArr, i3, iArr3);
            Salsa20Engine.salsaCore(8, iArr3, iArr2);
            System.arraycopy(iArr2, 0, iArr4, i2, 16);
            i2 = (length + i3) - i2;
            i3 += 16;
        }
        System.arraycopy(iArr4, 0, iArr, 0, iArr4.length);
    }

    private static void Clear(byte[] bArr) {
        if (bArr != null) {
            Arrays.fill(bArr, (byte) 0);
        }
    }

    private static void Clear(int[] iArr) {
        if (iArr != null) {
            Arrays.fill(iArr, 0);
        }
    }

    private static void ClearAll(int[][] iArr) {
        for (int[] Clear : iArr) {
            Clear(Clear);
        }
    }

    private static byte[] MFcrypt(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4) {
        int i5 = i2 * X509KeyUsage.digitalSignature;
        byte[] SingleIterationPBKDF2 = SingleIterationPBKDF2(bArr, bArr2, i3 * i5);
        int[] iArr = null;
        try {
            int length = SingleIterationPBKDF2.length >>> 2;
            iArr = new int[length];
            Pack.littleEndianToInt(SingleIterationPBKDF2, 0, iArr);
            i5 >>>= 2;
            for (int i6 = 0; i6 < length; i6 += i5) {
                SMix(iArr, i6, i, i2);
            }
            Pack.intToLittleEndian(iArr, SingleIterationPBKDF2, 0);
            byte[] SingleIterationPBKDF22 = SingleIterationPBKDF2(bArr, SingleIterationPBKDF2, i4);
            return SingleIterationPBKDF22;
        } finally {
            Clear(SingleIterationPBKDF2);
            Clear(iArr);
        }
    }

    private static void SMix(int[] iArr, int i, int i2, int i3) {
        int i4 = i3 * 32;
        int[] iArr2 = new int[16];
        int[] iArr3 = new int[16];
        int[] iArr4 = new int[i4];
        int[] iArr5 = new int[i4];
        int[][] iArr6 = new int[i2][];
        try {
            int i5;
            System.arraycopy(iArr, i, iArr5, 0, i4);
            for (i5 = 0; i5 < i2; i5++) {
                iArr6[i5] = Arrays.clone(iArr5);
                BlockMix(iArr5, iArr2, iArr3, iArr4, i3);
            }
            int i6 = i2 - 1;
            for (i5 = 0; i5 < i2; i5++) {
                Xor(iArr5, iArr6[iArr5[i4 - 16] & i6], 0, iArr5);
                BlockMix(iArr5, iArr2, iArr3, iArr4, i3);
            }
            System.arraycopy(iArr5, 0, iArr, i, i4);
            ClearAll(iArr6);
            ClearAll(new int[][]{iArr5, iArr2, iArr3, iArr4});
        } catch (Throwable th) {
            ClearAll(iArr6);
            ClearAll(new int[][]{iArr5, iArr2, iArr3, iArr4});
        }
    }

    private static byte[] SingleIterationPBKDF2(byte[] bArr, byte[] bArr2, int i) {
        PBEParametersGenerator pKCS5S2ParametersGenerator = new PKCS5S2ParametersGenerator(new SHA256Digest());
        pKCS5S2ParametersGenerator.init(bArr, bArr2, 1);
        return ((KeyParameter) pKCS5S2ParametersGenerator.generateDerivedMacParameters(i * 8)).getKey();
    }

    private static void Xor(int[] iArr, int[] iArr2, int i, int[] iArr3) {
        for (int length = iArr3.length - 1; length >= 0; length--) {
            iArr3[length] = iArr[length] ^ iArr2[i + length];
        }
    }

    public static byte[] generate(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4) {
        if (bArr == null) {
            throw new IllegalArgumentException("Passphrase P must be provided.");
        } else if (bArr2 == null) {
            throw new IllegalArgumentException("Salt S must be provided.");
        } else if (i <= 1) {
            throw new IllegalArgumentException("Cost parameter N must be > 1.");
        } else if (i2 == 1 && i > PKIFailureInfo.notAuthorized) {
            throw new IllegalArgumentException("Cost parameter N must be > 1 and < 65536.");
        } else if (i2 < 1) {
            throw new IllegalArgumentException("Block size r must be >= 1.");
        } else {
            int i5 = CNCCCommands.CMD_CNCC_CMD_UNKNOWN / ((i2 * X509KeyUsage.digitalSignature) * 8);
            if (i3 < 1 || i3 > i5) {
                throw new IllegalArgumentException("Parallelisation parameter p must be >= 1 and <= " + i5 + " (based on block size r of " + i2 + ")");
            } else if (i4 >= 1) {
                return MFcrypt(bArr, bArr2, i, i2, i3, i4);
            } else {
                throw new IllegalArgumentException("Generated key length dkLen must be >= 1.");
            }
        }
    }
}
