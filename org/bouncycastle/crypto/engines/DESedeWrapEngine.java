package org.bouncycastle.crypto.engines;

import java.security.SecureRandom;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;

public class DESedeWrapEngine implements Wrapper {
    private static final byte[] IV2;
    byte[] digest;
    private CBCBlockCipher engine;
    private boolean forWrapping;
    private byte[] iv;
    private KeyParameter param;
    private ParametersWithIV paramPlusIV;
    Digest sha1;

    static {
        IV2 = new byte[]{(byte) 74, (byte) -35, (byte) -94, (byte) 44, (byte) 121, (byte) -24, (byte) 33, (byte) 5};
    }

    public DESedeWrapEngine() {
        this.sha1 = new SHA1Digest();
        this.digest = new byte[20];
    }

    private byte[] calculateCMSKeyChecksum(byte[] bArr) {
        Object obj = new byte[8];
        this.sha1.update(bArr, 0, bArr.length);
        this.sha1.doFinal(this.digest, 0);
        System.arraycopy(this.digest, 0, obj, 0, 8);
        return obj;
    }

    private boolean checkCMSKeyChecksum(byte[] bArr, byte[] bArr2) {
        return Arrays.constantTimeAreEqual(calculateCMSKeyChecksum(bArr), bArr2);
    }

    private static byte[] reverse(byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i] = bArr[bArr.length - (i + 1)];
        }
        return bArr2;
    }

    public String getAlgorithmName() {
        return "DESede";
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        CipherParameters cipherParameters2;
        SecureRandom random;
        this.forWrapping = z;
        this.engine = new CBCBlockCipher(new DESedeEngine());
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            CipherParameters parameters = parametersWithRandom.getParameters();
            cipherParameters2 = parameters;
            random = parametersWithRandom.getRandom();
        } else {
            random = new SecureRandom();
            cipherParameters2 = cipherParameters;
        }
        if (cipherParameters2 instanceof KeyParameter) {
            this.param = (KeyParameter) cipherParameters2;
            if (this.forWrapping) {
                this.iv = new byte[8];
                random.nextBytes(this.iv);
                this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
            }
        } else if (cipherParameters2 instanceof ParametersWithIV) {
            this.paramPlusIV = (ParametersWithIV) cipherParameters2;
            this.iv = this.paramPlusIV.getIV();
            this.param = (KeyParameter) this.paramPlusIV.getParameters();
            if (!this.forWrapping) {
                throw new IllegalArgumentException("You should not supply an IV for unwrapping");
            } else if (this.iv == null || this.iv.length != 8) {
                throw new IllegalArgumentException("IV is not 8 octets");
            }
        }
    }

    public byte[] unwrap(byte[] bArr, int i, int i2) {
        if (this.forWrapping) {
            throw new IllegalStateException("Not set for unwrapping");
        } else if (bArr == null) {
            throw new InvalidCipherTextException("Null pointer as ciphertext");
        } else {
            int blockSize = this.engine.getBlockSize();
            if (i2 % blockSize != 0) {
                throw new InvalidCipherTextException("Ciphertext not multiple of " + blockSize);
            }
            int i3;
            this.engine.init(false, new ParametersWithIV(this.param, IV2));
            byte[] bArr2 = new byte[i2];
            for (i3 = 0; i3 != i2; i3 += blockSize) {
                this.engine.processBlock(bArr, i + i3, bArr2, i3);
            }
            Object reverse = reverse(bArr2);
            this.iv = new byte[8];
            Object obj = new byte[(reverse.length - 8)];
            System.arraycopy(reverse, 0, this.iv, 0, 8);
            System.arraycopy(reverse, 8, obj, 0, reverse.length - 8);
            this.paramPlusIV = new ParametersWithIV(this.param, this.iv);
            this.engine.init(false, this.paramPlusIV);
            Object obj2 = new byte[obj.length];
            for (i3 = 0; i3 != obj2.length; i3 += blockSize) {
                this.engine.processBlock(obj, i3, obj2, i3);
            }
            reverse = new byte[(obj2.length - 8)];
            Object obj3 = new byte[8];
            System.arraycopy(obj2, 0, reverse, 0, obj2.length - 8);
            System.arraycopy(obj2, obj2.length - 8, obj3, 0, 8);
            if (checkCMSKeyChecksum(reverse, obj3)) {
                return reverse;
            }
            throw new InvalidCipherTextException("Checksum inside ciphertext is corrupted");
        }
    }

    public byte[] wrap(byte[] bArr, int i, int i2) {
        int i3 = 0;
        if (this.forWrapping) {
            Object obj = new byte[i2];
            System.arraycopy(bArr, i, obj, 0, i2);
            Object calculateCMSKeyChecksum = calculateCMSKeyChecksum(obj);
            Object obj2 = new byte[(obj.length + calculateCMSKeyChecksum.length)];
            System.arraycopy(obj, 0, obj2, 0, obj.length);
            System.arraycopy(calculateCMSKeyChecksum, 0, obj2, obj.length, calculateCMSKeyChecksum.length);
            int blockSize = this.engine.getBlockSize();
            if (obj2.length % blockSize != 0) {
                throw new IllegalStateException("Not multiple of block length");
            }
            this.engine.init(true, this.paramPlusIV);
            Object obj3 = new byte[obj2.length];
            for (int i4 = 0; i4 != obj2.length; i4 += blockSize) {
                this.engine.processBlock(obj2, i4, obj3, i4);
            }
            obj = new byte[(this.iv.length + obj3.length)];
            System.arraycopy(this.iv, 0, obj, 0, this.iv.length);
            System.arraycopy(obj3, 0, obj, this.iv.length, obj3.length);
            byte[] reverse = reverse(obj);
            this.engine.init(true, new ParametersWithIV(this.param, IV2));
            while (i3 != reverse.length) {
                this.engine.processBlock(reverse, i3, reverse, i3);
                i3 += blockSize;
            }
            return reverse;
        }
        throw new IllegalStateException("Not initialized for wrapping");
    }
}
