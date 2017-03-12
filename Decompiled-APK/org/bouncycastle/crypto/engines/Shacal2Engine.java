package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class Shacal2Engine implements BlockCipher {
    private static final int BLOCK_SIZE = 32;
    private static final int[] f165K;
    private static final int ROUNDS = 64;
    private boolean forEncryption;
    private int[] workingKey;

    static {
        f165K = new int[]{1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998};
    }

    public Shacal2Engine() {
        this.forEncryption = false;
        this.workingKey = null;
    }

    private void byteBlockToInts(byte[] bArr, int[] iArr, int i, int i2) {
        while (i2 < 8) {
            int i3 = i + 1;
            int i4 = i3 + 1;
            i3 = ((bArr[i3] & GF2Field.MASK) << 16) | ((bArr[i] & GF2Field.MASK) << 24);
            int i5 = i4 + 1;
            i = i5 + 1;
            iArr[i2] = (i3 | ((bArr[i4] & GF2Field.MASK) << 8)) | (bArr[i5] & GF2Field.MASK);
            i2++;
        }
    }

    private void bytes2ints(byte[] bArr, int[] iArr, int i, int i2) {
        while (i2 < bArr.length / 4) {
            int i3 = i + 1;
            int i4 = i3 + 1;
            i3 = ((bArr[i3] & GF2Field.MASK) << 16) | ((bArr[i] & GF2Field.MASK) << 24);
            int i5 = i4 + 1;
            i = i5 + 1;
            iArr[i2] = (i3 | ((bArr[i4] & GF2Field.MASK) << 8)) | (bArr[i5] & GF2Field.MASK);
            i2++;
        }
    }

    private void decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int[] iArr = new int[8];
        byteBlockToInts(bArr, iArr, i, 0);
        for (int i3 = 63; i3 > -1; i3--) {
            int i4 = (iArr[0] - ((((iArr[1] >>> 2) | (iArr[1] << -2)) ^ ((iArr[1] >>> 13) | (iArr[1] << -13))) ^ ((iArr[1] >>> 22) | (iArr[1] << -22)))) - (((iArr[1] & iArr[2]) ^ (iArr[1] & iArr[3])) ^ (iArr[2] & iArr[3]));
            iArr[0] = iArr[1];
            iArr[1] = iArr[2];
            iArr[2] = iArr[3];
            iArr[3] = iArr[4] - i4;
            iArr[4] = iArr[5];
            iArr[5] = iArr[6];
            iArr[6] = iArr[7];
            iArr[7] = (((i4 - f165K[i3]) - this.workingKey[i3]) - ((((iArr[4] >>> 6) | (iArr[4] << -6)) ^ ((iArr[4] >>> 11) | (iArr[4] << -11))) ^ ((iArr[4] >>> 25) | (iArr[4] << -25)))) - ((iArr[4] & iArr[5]) ^ ((iArr[4] ^ -1) & iArr[6]));
        }
        ints2bytes(iArr, bArr2, i2);
    }

    private void encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int[] iArr = new int[8];
        byteBlockToInts(bArr, iArr, i, 0);
        for (int i3 = 0; i3 < ROUNDS; i3++) {
            int i4 = (((((((iArr[4] >>> 6) | (iArr[4] << -6)) ^ ((iArr[4] >>> 11) | (iArr[4] << -11))) ^ ((iArr[4] >>> 25) | (iArr[4] << -25))) + ((iArr[4] & iArr[5]) ^ ((iArr[4] ^ -1) & iArr[6]))) + iArr[7]) + f165K[i3]) + this.workingKey[i3];
            iArr[7] = iArr[6];
            iArr[6] = iArr[5];
            iArr[5] = iArr[4];
            iArr[4] = iArr[3] + i4;
            iArr[3] = iArr[2];
            iArr[2] = iArr[1];
            iArr[1] = iArr[0];
            iArr[0] = (i4 + ((((iArr[0] >>> 2) | (iArr[0] << -2)) ^ ((iArr[0] >>> 13) | (iArr[0] << -13))) ^ ((iArr[0] >>> 22) | (iArr[0] << -22)))) + (((iArr[0] & iArr[2]) ^ (iArr[0] & iArr[3])) ^ (iArr[2] & iArr[3]));
        }
        ints2bytes(iArr, bArr2, i2);
    }

    private void ints2bytes(int[] iArr, byte[] bArr, int i) {
        for (int i2 = 0; i2 < iArr.length; i2++) {
            int i3 = i + 1;
            bArr[i] = (byte) (iArr[i2] >>> 24);
            int i4 = i3 + 1;
            bArr[i3] = (byte) (iArr[i2] >>> 16);
            i3 = i4 + 1;
            bArr[i4] = (byte) (iArr[i2] >>> 8);
            i = i3 + 1;
            bArr[i3] = (byte) iArr[i2];
        }
    }

    public String getAlgorithmName() {
        return "Shacal2";
    }

    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.forEncryption = z;
            this.workingKey = new int[ROUNDS];
            setKey(((KeyParameter) cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("only simple KeyParameter expected.");
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.workingKey == null) {
            throw new IllegalStateException("Shacal2 not initialised");
        } else if (i + BLOCK_SIZE > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + BLOCK_SIZE > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            if (this.forEncryption) {
                encryptBlock(bArr, i, bArr2, i2);
            } else {
                decryptBlock(bArr, i, bArr2, i2);
            }
            return BLOCK_SIZE;
        }
    }

    public void reset() {
    }

    public void setKey(byte[] bArr) {
        int i = 16;
        if (bArr.length == 0 || bArr.length > ROUNDS || bArr.length < 16 || bArr.length % 8 != 0) {
            throw new IllegalArgumentException("Shacal2-key must be 16 - 64 bytes and multiple of 8");
        }
        bytes2ints(bArr, this.workingKey, 0, 0);
        while (i < ROUNDS) {
            this.workingKey[i] = ((((((this.workingKey[i - 2] >>> 17) | (this.workingKey[i - 2] << -17)) ^ ((this.workingKey[i - 2] >>> 19) | (this.workingKey[i - 2] << -19))) ^ (this.workingKey[i - 2] >>> 10)) + this.workingKey[i - 7]) + ((((this.workingKey[i - 15] >>> 7) | (this.workingKey[i - 15] << -7)) ^ ((this.workingKey[i - 15] >>> 18) | (this.workingKey[i - 15] << -18))) ^ (this.workingKey[i - 15] >>> 3))) + this.workingKey[i - 16];
            i++;
        }
    }
}
