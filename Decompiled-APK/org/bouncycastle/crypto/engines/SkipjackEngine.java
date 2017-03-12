package org.bouncycastle.crypto.engines;

import com.mastercard.mcbp.core.mpplite.states.StatesConstants;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.tls.AlertDescription;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class SkipjackEngine implements BlockCipher {
    static final int BLOCK_SIZE = 8;
    static short[] ftable;
    private boolean encrypting;
    private int[] key0;
    private int[] key1;
    private int[] key2;
    private int[] key3;

    static {
        ftable = new short[]{(short) 163, (short) 215, (short) 9, (short) 131, (short) 248, (short) 72, (short) 246, (short) 244, (short) 179, (short) 33, (short) 21, (short) 120, (short) 153, (short) 177, (short) 175, (short) 249, (short) 231, (short) 45, (short) 77, (short) 138, (short) 206, (short) 76, (short) 202, (short) 46, (short) 82, (short) 149, (short) 217, (short) 30, (short) 78, (short) 56, (short) 68, (short) 40, (short) 10, (short) 223, (short) 2, (short) 160, (short) 23, (short) 241, (short) 96, (short) 104, (short) 18, (short) 183, (short) 122, (short) 195, (short) 233, (short) 250, (short) 61, (short) 83, (short) 150, MCFCITemplate.TAG_DF_NAME, (short) 107, (short) 186, (short) 242, (short) 99, (short) 154, (short) 25, (short) 124, (short) 174, (short) 229, (short) 245, (short) 247, (short) 22, (short) 106, (short) 162, (short) 57, (short) 182, (short) 123, (short) 15, (short) 193, (short) 147, (short) 129, (short) 27, (short) 238, (short) 180, (short) 26, (short) 234, (short) 208, (short) 145, (short) 47, (short) 184, (short) 85, (short) 185, (short) 218, (short) 133, (short) 63, (short) 65, (short) 191, (short) 224, (short) 90, (short) 88, (short) 128, (short) 95, (short) 102, (short) 11, (short) 216, (short) 144, (short) 53, (short) 213, (short) 192, (short) 167, (short) 51, (short) 6, (short) 101, (short) 105, (short) 69, (short) 0, (short) 148, (short) 86, (short) 109, (short) 152, (short) 155, (short) 118, (short) 151, (short) 252, ISO7816.READ_RECORD, (short) 194, (short) 176, (short) 254, (short) 219, (short) 32, (short) 225, (short) 235, (short) 214, (short) 228, (short) 221, (short) 71, (short) 74, (short) 29, (short) 66, (short) 237, (short) 158, AlertDescription.unsupported_extension, (short) 73, (short) 60, (short) 205, (short) 67, (short) 39, (short) 210, (short) 7, (short) 212, (short) 222, (short) 199, (short) 103, (short) 24, (short) 137, (short) 203, (short) 48, (short) 31, (short) 141, (short) 198, (short) 143, (short) 170, (short) 200, (short) 116, (short) 220, (short) 201, (short) 93, (short) 92, (short) 49, ISO7816.SELECT, AlertDescription.unrecognized_name, (short) 136, (short) 97, (short) 44, (short) 159, (short) 13, (short) 43, MCFCITemplate.TAG_APP_PRIORITY_INDICATOR, (short) 80, (short) 130, (short) 84, (short) 100, (short) 38, (short) 125, (short) 3, (short) 64, (short) 52, (short) 75, (short) 28, AlertDescription.unknown_psk_identity, (short) 209, (short) 196, (short) 253, (short) 59, (short) 204, (short) 251, (short) 127, (short) 171, (short) 230, (short) 62, (short) 91, MCFCITemplate.TAG_FCI_PROPRIETARY_TEMPLATE, (short) 173, (short) 4, (short) 35, (short) 156, (short) 20, (short) 81, (short) 34, (short) 240, (short) 41, (short) 121, AlertDescription.bad_certificate_status_response, (short) 126, StatesConstants.LENGTH_MASK, (short) 140, (short) 14, (short) 226, (short) 12, (short) 239, (short) 188, AlertDescription.bad_certificate_hash_value, (short) 117, AlertDescription.certificate_unobtainable, (short) 55, (short) 161, (short) 236, (short) 211, (short) 142, (short) 98, (short) 139, (short) 134, (short) 16, (short) 232, (short) 8, (short) 119, (short) 17, (short) 190, (short) 146, (short) 79, (short) 36, (short) 197, (short) 50, (short) 54, (short) 157, (short) 207, (short) 243, (short) 166, (short) 187, (short) 172, (short) 94, (short) 108, (short) 169, (short) 19, (short) 87, (short) 37, (short) 181, (short) 227, (short) 189, (short) 168, (short) 58, (short) 1, (short) 5, (short) 89, (short) 42, (short) 70};
    }

    private int m1735g(int i, int i2) {
        int i3 = i2 & GF2Field.MASK;
        int i4 = ((i2 >> BLOCK_SIZE) & GF2Field.MASK) ^ ftable[this.key0[i] ^ i3];
        i3 ^= ftable[this.key1[i] ^ i4];
        i4 ^= ftable[this.key2[i] ^ i3];
        return (i4 << BLOCK_SIZE) + (i3 ^ ftable[this.key3[i] ^ i4]);
    }

    private int m1736h(int i, int i2) {
        int i3 = (i2 >> BLOCK_SIZE) & GF2Field.MASK;
        int i4 = (i2 & GF2Field.MASK) ^ ftable[this.key3[i] ^ i3];
        i3 ^= ftable[this.key2[i] ^ i4];
        i4 ^= ftable[this.key1[i] ^ i3];
        return i4 + ((i3 ^ ftable[this.key0[i] ^ i4]) << BLOCK_SIZE);
    }

    public int decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = (bArr[i + 0] << BLOCK_SIZE) + (bArr[i + 1] & GF2Field.MASK);
        int i4 = (bArr[i + 2] << BLOCK_SIZE) + (bArr[i + 3] & GF2Field.MASK);
        int i5 = (bArr[i + 4] << BLOCK_SIZE) + (bArr[i + 5] & GF2Field.MASK);
        int i6 = (bArr[i + 7] & GF2Field.MASK) + (bArr[i + 6] << BLOCK_SIZE);
        int i7 = 31;
        int i8 = 0;
        while (i8 < 2) {
            int i9 = 0;
            while (i9 < BLOCK_SIZE) {
                int h = m1736h(i7, i4);
                i4 = (i7 + 1) ^ (i5 ^ h);
                i9++;
                i7--;
                i5 = i6;
                i6 = i3;
                i3 = h;
            }
            i9 = i4;
            i4 = i5;
            i5 = i6;
            i6 = i7;
            i7 = 0;
            while (i7 < BLOCK_SIZE) {
                i3 = (i3 ^ i9) ^ (i6 + 1);
                i9 = m1736h(i6, i9);
                i6--;
                i7++;
                int i10 = i3;
                i3 = i9;
                i9 = i4;
                i4 = i5;
                i5 = i10;
            }
            i8++;
            i7 = i6;
            i6 = i5;
            i5 = i4;
            i4 = i9;
        }
        bArr2[i2 + 0] = (byte) (i3 >> BLOCK_SIZE);
        bArr2[i2 + 1] = (byte) i3;
        bArr2[i2 + 2] = (byte) (i4 >> BLOCK_SIZE);
        bArr2[i2 + 3] = (byte) i4;
        bArr2[i2 + 4] = (byte) (i5 >> BLOCK_SIZE);
        bArr2[i2 + 5] = (byte) i5;
        bArr2[i2 + 6] = (byte) (i6 >> BLOCK_SIZE);
        bArr2[i2 + 7] = (byte) i6;
        return BLOCK_SIZE;
    }

    public int encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = (bArr[i + 0] << BLOCK_SIZE) + (bArr[i + 1] & GF2Field.MASK);
        int i4 = (bArr[i + 2] << BLOCK_SIZE) + (bArr[i + 3] & GF2Field.MASK);
        int i5 = (bArr[i + 4] << BLOCK_SIZE) + (bArr[i + 5] & GF2Field.MASK);
        int i6 = (bArr[i + 7] & GF2Field.MASK) + (bArr[i + 6] << BLOCK_SIZE);
        int i7 = 0;
        int i8 = 0;
        while (i7 < 2) {
            int i9 = 0;
            while (i9 < BLOCK_SIZE) {
                i3 = m1735g(i8, i3);
                int i10 = (i8 + 1) ^ (i6 ^ i3);
                i9++;
                i8++;
                i6 = i5;
                i5 = i4;
                i4 = i3;
                i3 = i10;
            }
            i9 = i3;
            i3 = i4;
            i4 = i6;
            i6 = i8;
            i8 = 0;
            while (i8 < BLOCK_SIZE) {
                i3 = (i3 ^ i9) ^ (i6 + 1);
                i9 = m1735g(i6, i9);
                i6++;
                i8++;
                int i11 = i5;
                i5 = i3;
                i3 = i9;
                i9 = i4;
                i4 = i11;
            }
            i7++;
            i8 = i6;
            i6 = i4;
            i4 = i3;
            i3 = i9;
        }
        bArr2[i2 + 0] = (byte) (i3 >> BLOCK_SIZE);
        bArr2[i2 + 1] = (byte) i3;
        bArr2[i2 + 2] = (byte) (i4 >> BLOCK_SIZE);
        bArr2[i2 + 3] = (byte) i4;
        bArr2[i2 + 4] = (byte) (i5 >> BLOCK_SIZE);
        bArr2[i2 + 5] = (byte) i5;
        bArr2[i2 + 6] = (byte) (i6 >> BLOCK_SIZE);
        bArr2[i2 + 7] = (byte) i6;
        return BLOCK_SIZE;
    }

    public String getAlgorithmName() {
        return "SKIPJACK";
    }

    public int getBlockSize() {
        return BLOCK_SIZE;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            byte[] key = ((KeyParameter) cipherParameters).getKey();
            this.encrypting = z;
            this.key0 = new int[32];
            this.key1 = new int[32];
            this.key2 = new int[32];
            this.key3 = new int[32];
            for (int i = 0; i < 32; i++) {
                this.key0[i] = key[(i * 4) % 10] & GF2Field.MASK;
                this.key1[i] = key[((i * 4) + 1) % 10] & GF2Field.MASK;
                this.key2[i] = key[((i * 4) + 2) % 10] & GF2Field.MASK;
                this.key3[i] = key[((i * 4) + 3) % 10] & GF2Field.MASK;
            }
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to SKIPJACK init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (this.key1 == null) {
            throw new IllegalStateException("SKIPJACK engine not initialised");
        } else if (i + BLOCK_SIZE > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + BLOCK_SIZE > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else {
            if (this.encrypting) {
                encryptBlock(bArr, i, bArr2, i2);
            } else {
                decryptBlock(bArr, i, bArr2, i2);
            }
            return BLOCK_SIZE;
        }
    }

    public void reset() {
    }
}
