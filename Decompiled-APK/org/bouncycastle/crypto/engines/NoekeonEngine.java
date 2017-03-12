package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class NoekeonEngine implements BlockCipher {
    private static final int genericSize = 16;
    private static final int[] nullVector;
    private static final int[] roundConstants;
    private boolean _forEncryption;
    private boolean _initialised;
    private int[] decryptKeys;
    private int[] state;
    private int[] subKeys;

    static {
        nullVector = new int[]{0, 0, 0, 0};
        roundConstants = new int[]{X509KeyUsage.digitalSignature, 27, 54, CipherSuite.TLS_DH_anon_WITH_AES_128_CBC_SHA256, 216, CipherSuite.TLS_DHE_PSK_WITH_AES_256_GCM_SHA384, 77, CipherSuite.TLS_DHE_RSA_WITH_SEED_CBC_SHA, 47, 94, CipherSuite.TLS_DH_RSA_WITH_CAMELLIA_128_CBC_SHA256, 99, 198, CipherSuite.TLS_DH_DSS_WITH_SEED_CBC_SHA, 53, CipherSuite.TLS_DHE_DSS_WITH_AES_256_CBC_SHA256, 212};
    }

    public NoekeonEngine() {
        this.state = new int[4];
        this.subKeys = new int[4];
        this.decryptKeys = new int[4];
        this._initialised = false;
    }

    private int bytesToIntBig(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        return ((((bArr[i2] & GF2Field.MASK) << genericSize) | (bArr[i] << 24)) | ((bArr[i3] & GF2Field.MASK) << 8)) | (bArr[i3 + 1] & GF2Field.MASK);
    }

    private int decryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int[] iArr;
        this.state[0] = bytesToIntBig(bArr, i);
        this.state[1] = bytesToIntBig(bArr, i + 4);
        this.state[2] = bytesToIntBig(bArr, i + 8);
        this.state[3] = bytesToIntBig(bArr, i + 12);
        System.arraycopy(this.subKeys, 0, this.decryptKeys, 0, this.subKeys.length);
        theta(this.decryptKeys, nullVector);
        int i3 = genericSize;
        while (i3 > 0) {
            theta(this.state, this.decryptKeys);
            iArr = this.state;
            iArr[0] = iArr[0] ^ roundConstants[i3];
            pi1(this.state);
            gamma(this.state);
            pi2(this.state);
            i3--;
        }
        theta(this.state, this.decryptKeys);
        iArr = this.state;
        iArr[0] = roundConstants[i3] ^ iArr[0];
        intToBytesBig(this.state[0], bArr2, i2);
        intToBytesBig(this.state[1], bArr2, i2 + 4);
        intToBytesBig(this.state[2], bArr2, i2 + 8);
        intToBytesBig(this.state[3], bArr2, i2 + 12);
        return genericSize;
    }

    private int encryptBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int[] iArr;
        this.state[0] = bytesToIntBig(bArr, i);
        this.state[1] = bytesToIntBig(bArr, i + 4);
        this.state[2] = bytesToIntBig(bArr, i + 8);
        this.state[3] = bytesToIntBig(bArr, i + 12);
        int i3 = 0;
        while (i3 < genericSize) {
            iArr = this.state;
            iArr[0] = iArr[0] ^ roundConstants[i3];
            theta(this.state, this.subKeys);
            pi1(this.state);
            gamma(this.state);
            pi2(this.state);
            i3++;
        }
        iArr = this.state;
        iArr[0] = roundConstants[i3] ^ iArr[0];
        theta(this.state, this.subKeys);
        intToBytesBig(this.state[0], bArr2, i2);
        intToBytesBig(this.state[1], bArr2, i2 + 4);
        intToBytesBig(this.state[2], bArr2, i2 + 8);
        intToBytesBig(this.state[3], bArr2, i2 + 12);
        return genericSize;
    }

    private void gamma(int[] iArr) {
        iArr[1] = iArr[1] ^ ((iArr[3] ^ -1) & (iArr[2] ^ -1));
        iArr[0] = iArr[0] ^ (iArr[2] & iArr[1]);
        int i = iArr[3];
        iArr[3] = iArr[0];
        iArr[0] = i;
        iArr[2] = iArr[2] ^ ((iArr[0] ^ iArr[1]) ^ iArr[3]);
        iArr[1] = iArr[1] ^ ((iArr[3] ^ -1) & (iArr[2] ^ -1));
        iArr[0] = iArr[0] ^ (iArr[2] & iArr[1]);
    }

    private void intToBytesBig(int i, byte[] bArr, int i2) {
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >>> 24);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i >>> genericSize);
        i3 = i4 + 1;
        bArr[i4] = (byte) (i >>> 8);
        bArr[i3] = (byte) i;
    }

    private void pi1(int[] iArr) {
        iArr[1] = rotl(iArr[1], 1);
        iArr[2] = rotl(iArr[2], 5);
        iArr[3] = rotl(iArr[3], 2);
    }

    private void pi2(int[] iArr) {
        iArr[1] = rotl(iArr[1], 31);
        iArr[2] = rotl(iArr[2], 27);
        iArr[3] = rotl(iArr[3], 30);
    }

    private int rotl(int i, int i2) {
        return (i << i2) | (i >>> (32 - i2));
    }

    private void setKey(byte[] bArr) {
        this.subKeys[0] = bytesToIntBig(bArr, 0);
        this.subKeys[1] = bytesToIntBig(bArr, 4);
        this.subKeys[2] = bytesToIntBig(bArr, 8);
        this.subKeys[3] = bytesToIntBig(bArr, 12);
    }

    private void theta(int[] iArr, int[] iArr2) {
        int i = iArr[0] ^ iArr[2];
        i ^= rotl(i, 8) ^ rotl(i, 24);
        iArr[1] = iArr[1] ^ i;
        iArr[3] = i ^ iArr[3];
        for (i = 0; i < 4; i++) {
            iArr[i] = iArr[i] ^ iArr2[i];
        }
        i = iArr[1] ^ iArr[3];
        i ^= rotl(i, 8) ^ rotl(i, 24);
        iArr[0] = iArr[0] ^ i;
        iArr[2] = i ^ iArr[2];
    }

    public String getAlgorithmName() {
        return "Noekeon";
    }

    public int getBlockSize() {
        return genericSize;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this._forEncryption = z;
            this._initialised = true;
            setKey(((KeyParameter) cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to Noekeon init - " + cipherParameters.getClass().getName());
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        if (!this._initialised) {
            throw new IllegalStateException(getAlgorithmName() + " not initialised");
        } else if (i + genericSize > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i2 + genericSize <= bArr2.length) {
            return this._forEncryption ? encryptBlock(bArr, i, bArr2, i2) : decryptBlock(bArr, i, bArr2, i2);
        } else {
            throw new OutputLengthException("output buffer too short");
        }
    }

    public void reset() {
    }
}
