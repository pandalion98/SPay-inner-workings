package org.bouncycastle.crypto.encodings;

import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.ParametersWithRandom;

public class OAEPEncoding implements AsymmetricBlockCipher {
    private byte[] defHash;
    private AsymmetricBlockCipher engine;
    private boolean forEncryption;
    private Digest mgf1Hash;
    private SecureRandom random;

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher) {
        this(asymmetricBlockCipher, new SHA1Digest(), null);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest) {
        this(asymmetricBlockCipher, digest, null);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, Digest digest2, byte[] bArr) {
        this.engine = asymmetricBlockCipher;
        this.mgf1Hash = digest2;
        this.defHash = new byte[digest.getDigestSize()];
        digest.reset();
        if (bArr != null) {
            digest.update(bArr, 0, bArr.length);
        }
        digest.doFinal(this.defHash, 0);
    }

    public OAEPEncoding(AsymmetricBlockCipher asymmetricBlockCipher, Digest digest, byte[] bArr) {
        this(asymmetricBlockCipher, digest, digest, bArr);
    }

    private void ItoOSP(int i, byte[] bArr) {
        bArr[0] = (byte) (i >>> 24);
        bArr[1] = (byte) (i >>> 16);
        bArr[2] = (byte) (i >>> 8);
        bArr[3] = (byte) (i >>> 0);
    }

    private byte[] maskGeneratorFunction1(byte[] bArr, int i, int i2, int i3) {
        Object obj = new byte[i3];
        Object obj2 = new byte[this.mgf1Hash.getDigestSize()];
        byte[] bArr2 = new byte[4];
        this.mgf1Hash.reset();
        int i4 = 0;
        while (i4 < i3 / obj2.length) {
            ItoOSP(i4, bArr2);
            this.mgf1Hash.update(bArr, i, i2);
            this.mgf1Hash.update(bArr2, 0, bArr2.length);
            this.mgf1Hash.doFinal(obj2, 0);
            System.arraycopy(obj2, 0, obj, obj2.length * i4, obj2.length);
            i4++;
        }
        if (obj2.length * i4 < i3) {
            ItoOSP(i4, bArr2);
            this.mgf1Hash.update(bArr, i, i2);
            this.mgf1Hash.update(bArr2, 0, bArr2.length);
            this.mgf1Hash.doFinal(obj2, 0);
            System.arraycopy(obj2, 0, obj, obj2.length * i4, obj.length - (i4 * obj2.length));
        }
        return obj;
    }

    public byte[] decodeBlock(byte[] bArr, int i, int i2) {
        Object obj;
        Object processBlock = this.engine.processBlock(bArr, i, i2);
        if (processBlock.length < this.engine.getOutputBlockSize()) {
            obj = new byte[this.engine.getOutputBlockSize()];
            System.arraycopy(processBlock, 0, obj, obj.length - processBlock.length, processBlock.length);
        } else {
            obj = processBlock;
        }
        if (obj.length < (this.defHash.length * 2) + 1) {
            throw new InvalidCipherTextException("data too short");
        }
        int i3;
        byte[] maskGeneratorFunction1 = maskGeneratorFunction1(obj, this.defHash.length, obj.length - this.defHash.length, this.defHash.length);
        for (i3 = 0; i3 != this.defHash.length; i3++) {
            obj[i3] = (byte) (obj[i3] ^ maskGeneratorFunction1[i3]);
        }
        maskGeneratorFunction1 = maskGeneratorFunction1(obj, 0, this.defHash.length, obj.length - this.defHash.length);
        for (i3 = this.defHash.length; i3 != obj.length; i3++) {
            obj[i3] = (byte) (obj[i3] ^ maskGeneratorFunction1[i3 - this.defHash.length]);
        }
        int i4 = 0;
        for (i3 = 0; i3 != this.defHash.length; i3++) {
            if (this.defHash[i3] != obj[this.defHash.length + i3]) {
                i4 = 1;
            }
        }
        if (i4 != 0) {
            throw new InvalidCipherTextException("data hash wrong");
        }
        i3 = this.defHash.length * 2;
        while (i3 != obj.length && obj[i3] == null) {
            i3++;
        }
        if (i3 >= obj.length - 1 || obj[i3] != (byte) 1) {
            throw new InvalidCipherTextException("data start wrong " + i3);
        }
        i3++;
        Object obj2 = new byte[(obj.length - i3)];
        System.arraycopy(obj, i3, obj2, 0, obj2.length);
        return obj2;
    }

    public byte[] encodeBlock(byte[] bArr, int i, int i2) {
        int length;
        Object obj = new byte[((getInputBlockSize() + 1) + (this.defHash.length * 2))];
        System.arraycopy(bArr, i, obj, obj.length - i2, i2);
        obj[(obj.length - i2) - 1] = (byte) 1;
        System.arraycopy(this.defHash, 0, obj, this.defHash.length, this.defHash.length);
        Object obj2 = new byte[this.defHash.length];
        this.random.nextBytes(obj2);
        byte[] maskGeneratorFunction1 = maskGeneratorFunction1(obj2, 0, obj2.length, obj.length - this.defHash.length);
        for (length = this.defHash.length; length != obj.length; length++) {
            obj[length] = (byte) (obj[length] ^ maskGeneratorFunction1[length - this.defHash.length]);
        }
        System.arraycopy(obj2, 0, obj, 0, this.defHash.length);
        byte[] maskGeneratorFunction12 = maskGeneratorFunction1(obj, this.defHash.length, obj.length - this.defHash.length, this.defHash.length);
        for (length = 0; length != this.defHash.length; length++) {
            obj[length] = (byte) (obj[length] ^ maskGeneratorFunction12[length]);
        }
        return this.engine.processBlock(obj, 0, obj.length);
    }

    public int getInputBlockSize() {
        int inputBlockSize = this.engine.getInputBlockSize();
        return this.forEncryption ? (inputBlockSize - 1) - (this.defHash.length * 2) : inputBlockSize;
    }

    public int getOutputBlockSize() {
        int outputBlockSize = this.engine.getOutputBlockSize();
        return this.forEncryption ? outputBlockSize : (outputBlockSize - 1) - (this.defHash.length * 2);
    }

    public AsymmetricBlockCipher getUnderlyingCipher() {
        return this.engine;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithRandom) {
            this.random = ((ParametersWithRandom) cipherParameters).getRandom();
        } else {
            this.random = new SecureRandom();
        }
        this.engine.init(z, cipherParameters);
        this.forEncryption = z;
    }

    public byte[] processBlock(byte[] bArr, int i, int i2) {
        return this.forEncryption ? encodeBlock(bArr, i, i2) : decodeBlock(bArr, i, i2);
    }
}
