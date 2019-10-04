/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

public class Shacal2Engine
implements BlockCipher {
    private static final int BLOCK_SIZE = 32;
    private static final int[] K = new int[]{1116352408, 1899447441, -1245643825, -373957723, 961987163, 1508970993, -1841331548, -1424204075, -670586216, 310598401, 607225278, 1426881987, 1925078388, -2132889090, -1680079193, -1046744716, -459576895, -272742522, 264347078, 604807628, 770255983, 1249150122, 1555081692, 1996064986, -1740746414, -1473132947, -1341970488, -1084653625, -958395405, -710438585, 113926993, 338241895, 666307205, 773529912, 1294757372, 1396182291, 1695183700, 1986661051, -2117940946, -1838011259, -1564481375, -1474664885, -1035236496, -949202525, -778901479, -694614492, -200395387, 275423344, 430227734, 506948616, 659060556, 883997877, 958139571, 1322822218, 1537002063, 1747873779, 1955562222, 2024104815, -2067236844, -1933114872, -1866530822, -1538233109, -1090935817, -965641998};
    private static final int ROUNDS = 64;
    private boolean forEncryption = false;
    private int[] workingKey = null;

    private void byteBlockToInts(byte[] arrby, int[] arrn, int n2, int n3) {
        while (n3 < 8) {
            int n4 = n2 + 1;
            int n5 = (255 & arrby[n2]) << 24;
            int n6 = n4 + 1;
            int n7 = n5 | (255 & arrby[n4]) << 16;
            int n8 = n6 + 1;
            int n9 = n7 | (255 & arrby[n6]) << 8;
            n2 = n8 + 1;
            arrn[n3] = n9 | 255 & arrby[n8];
            ++n3;
        }
    }

    private void bytes2ints(byte[] arrby, int[] arrn, int n2, int n3) {
        while (n3 < arrby.length / 4) {
            int n4 = n2 + 1;
            int n5 = (255 & arrby[n2]) << 24;
            int n6 = n4 + 1;
            int n7 = n5 | (255 & arrby[n4]) << 16;
            int n8 = n6 + 1;
            int n9 = n7 | (255 & arrby[n6]) << 8;
            n2 = n8 + 1;
            arrn[n3] = n9 | 255 & arrby[n8];
            ++n3;
        }
    }

    private void decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int[] arrn = new int[8];
        this.byteBlockToInts(arrby, arrn, n2, 0);
        for (int i2 = 63; i2 > -1; --i2) {
            int n4 = arrn[0] - ((arrn[1] >>> 2 | arrn[1] << -2) ^ (arrn[1] >>> 13 | arrn[1] << -13) ^ (arrn[1] >>> 22 | arrn[1] << -22)) - (arrn[1] & arrn[2] ^ arrn[1] & arrn[3] ^ arrn[2] & arrn[3]);
            arrn[0] = arrn[1];
            arrn[1] = arrn[2];
            arrn[2] = arrn[3];
            arrn[3] = arrn[4] - n4;
            arrn[4] = arrn[5];
            arrn[5] = arrn[6];
            arrn[6] = arrn[7];
            arrn[7] = n4 - K[i2] - this.workingKey[i2] - ((arrn[4] >>> 6 | arrn[4] << -6) ^ (arrn[4] >>> 11 | arrn[4] << -11) ^ (arrn[4] >>> 25 | arrn[4] << -25)) - (arrn[4] & arrn[5] ^ (-1 ^ arrn[4]) & arrn[6]);
        }
        this.ints2bytes(arrn, arrby2, n3);
    }

    private void encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int[] arrn = new int[8];
        this.byteBlockToInts(arrby, arrn, n2, 0);
        for (int i2 = 0; i2 < 64; ++i2) {
            int n4 = ((arrn[4] >>> 6 | arrn[4] << -6) ^ (arrn[4] >>> 11 | arrn[4] << -11) ^ (arrn[4] >>> 25 | arrn[4] << -25)) + (arrn[4] & arrn[5] ^ (-1 ^ arrn[4]) & arrn[6]) + arrn[7] + K[i2] + this.workingKey[i2];
            arrn[7] = arrn[6];
            arrn[6] = arrn[5];
            arrn[5] = arrn[4];
            arrn[4] = n4 + arrn[3];
            arrn[3] = arrn[2];
            arrn[2] = arrn[1];
            arrn[1] = arrn[0];
            arrn[0] = n4 + ((arrn[0] >>> 2 | arrn[0] << -2) ^ (arrn[0] >>> 13 | arrn[0] << -13) ^ (arrn[0] >>> 22 | arrn[0] << -22)) + (arrn[0] & arrn[2] ^ arrn[0] & arrn[3] ^ arrn[2] & arrn[3]);
        }
        this.ints2bytes(arrn, arrby2, n3);
    }

    private void ints2bytes(int[] arrn, byte[] arrby, int n2) {
        for (int i2 = 0; i2 < arrn.length; ++i2) {
            int n3 = n2 + 1;
            arrby[n2] = (byte)(arrn[i2] >>> 24);
            int n4 = n3 + 1;
            arrby[n3] = (byte)(arrn[i2] >>> 16);
            int n5 = n4 + 1;
            arrby[n4] = (byte)(arrn[i2] >>> 8);
            n2 = n5 + 1;
            arrby[n5] = (byte)arrn[i2];
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Shacal2";
    }

    @Override
    public int getBlockSize() {
        return 32;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof KeyParameter)) {
            throw new IllegalArgumentException("only simple KeyParameter expected.");
        }
        this.forEncryption = bl;
        this.workingKey = new int[64];
        this.setKey(((KeyParameter)cipherParameters).getKey());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.workingKey == null) {
            throw new IllegalStateException("Shacal2 not initialised");
        }
        if (n2 + 32 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 32 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.forEncryption) {
            this.encryptBlock(arrby, n2, arrby2, n3);
            do {
                return 32;
                break;
            } while (true);
        }
        this.decryptBlock(arrby, n2, arrby2, n3);
        return 32;
    }

    @Override
    public void reset() {
    }

    public void setKey(byte[] arrby) {
        int n2;
        if (arrby.length == 0 || arrby.length > 64 || arrby.length < n2 || arrby.length % 8 != 0) {
            throw new IllegalArgumentException("Shacal2-key must be 16 - 64 bytes and multiple of 8");
        }
        this.bytes2ints(arrby, this.workingKey, 0, 0);
        for (n2 = 16; n2 < 64; ++n2) {
            this.workingKey[n2] = ((this.workingKey[n2 - 2] >>> 17 | this.workingKey[n2 - 2] << -17) ^ (this.workingKey[n2 - 2] >>> 19 | this.workingKey[n2 - 2] << -19) ^ this.workingKey[n2 - 2] >>> 10) + this.workingKey[n2 - 7] + ((this.workingKey[n2 - 15] >>> 7 | this.workingKey[n2 - 15] << -7) ^ (this.workingKey[n2 - 15] >>> 18 | this.workingKey[n2 - 15] << -18) ^ this.workingKey[n2 - 15] >>> 3) + this.workingKey[n2 - 16];
        }
    }
}

