package org.bouncycastle.pqc.jcajce.provider.util;

import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public abstract class AsymmetricBlockCipher extends CipherSpiExt {
    protected ByteArrayOutputStream buf;
    protected int cipherTextSize;
    protected int maxPlainTextSize;
    protected AlgorithmParameterSpec paramSpec;

    public AsymmetricBlockCipher() {
        this.buf = new ByteArrayOutputStream();
    }

    protected void checkLength(int i) {
        int size = this.buf.size() + i;
        if (this.opMode == 1) {
            if (size > this.maxPlainTextSize) {
                throw new IllegalBlockSizeException("The length of the plaintext (" + size + " bytes) is not supported by " + "the cipher (max. " + this.maxPlainTextSize + " bytes).");
            }
        } else if (this.opMode == 2 && size != this.cipherTextSize) {
            throw new IllegalBlockSizeException("Illegal ciphertext length (expected " + this.cipherTextSize + " bytes, was " + size + " bytes).");
        }
    }

    public final int doFinal(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (bArr2.length < getOutputSize(i2)) {
            throw new ShortBufferException("Output buffer too short.");
        }
        Object doFinal = doFinal(bArr, i, i2);
        System.arraycopy(doFinal, 0, bArr2, i3, doFinal.length);
        return doFinal.length;
    }

    public final byte[] doFinal(byte[] bArr, int i, int i2) {
        checkLength(i2);
        update(bArr, i, i2);
        byte[] toByteArray = this.buf.toByteArray();
        this.buf.reset();
        switch (this.opMode) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return messageEncrypt(toByteArray);
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return messageDecrypt(toByteArray);
            default:
                return null;
        }
    }

    public final int getBlockSize() {
        return this.opMode == 1 ? this.maxPlainTextSize : this.cipherTextSize;
    }

    public final byte[] getIV() {
        return null;
    }

    public final int getOutputSize(int i) {
        int size = i + this.buf.size();
        int blockSize = getBlockSize();
        return size > blockSize ? 0 : blockSize;
    }

    public final AlgorithmParameterSpec getParameters() {
        return this.paramSpec;
    }

    protected abstract void initCipherDecrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec);

    protected abstract void initCipherEncrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom);

    public final void initDecrypt(Key key) {
        try {
            initDecrypt(key, null);
        } catch (InvalidAlgorithmParameterException e) {
            throw new InvalidParameterException("This cipher needs algorithm parameters for initialization (cannot be null).");
        }
    }

    public final void initDecrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec) {
        this.opMode = 2;
        initCipherDecrypt(key, algorithmParameterSpec);
    }

    public final void initEncrypt(Key key) {
        try {
            initEncrypt(key, null, new SecureRandom());
        } catch (InvalidAlgorithmParameterException e) {
            throw new InvalidParameterException("This cipher needs algorithm parameters for initialization (cannot be null).");
        }
    }

    public final void initEncrypt(Key key, SecureRandom secureRandom) {
        try {
            initEncrypt(key, null, secureRandom);
        } catch (InvalidAlgorithmParameterException e) {
            throw new InvalidParameterException("This cipher needs algorithm parameters for initialization (cannot be null).");
        }
    }

    public final void initEncrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec) {
        initEncrypt(key, algorithmParameterSpec, new SecureRandom());
    }

    public final void initEncrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        this.opMode = 1;
        initCipherEncrypt(key, algorithmParameterSpec, secureRandom);
    }

    protected abstract byte[] messageDecrypt(byte[] bArr);

    protected abstract byte[] messageEncrypt(byte[] bArr);

    protected final void setMode(String str) {
    }

    protected final void setPadding(String str) {
    }

    public final int update(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        update(bArr, i, i2);
        return 0;
    }

    public final byte[] update(byte[] bArr, int i, int i2) {
        if (i2 != 0) {
            this.buf.write(bArr, i, i2);
        }
        return new byte[0];
    }
}
