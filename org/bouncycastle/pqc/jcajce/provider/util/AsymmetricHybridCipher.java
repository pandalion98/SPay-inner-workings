package org.bouncycastle.pqc.jcajce.provider.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.ShortBufferException;

public abstract class AsymmetricHybridCipher extends CipherSpiExt {
    protected AlgorithmParameterSpec paramSpec;

    protected abstract int decryptOutputSize(int i);

    public final int doFinal(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (bArr2.length < getOutputSize(i2)) {
            throw new ShortBufferException("Output buffer too short.");
        }
        Object doFinal = doFinal(bArr, i, i2);
        System.arraycopy(doFinal, 0, bArr2, i3, doFinal.length);
        return doFinal.length;
    }

    public abstract byte[] doFinal(byte[] bArr, int i, int i2);

    protected abstract int encryptOutputSize(int i);

    public final int getBlockSize() {
        return 0;
    }

    public final byte[] getIV() {
        return null;
    }

    public final int getOutputSize(int i) {
        return this.opMode == 1 ? encryptOutputSize(i) : decryptOutputSize(i);
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

    protected final void setMode(String str) {
    }

    protected final void setPadding(String str) {
    }

    public final int update(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (bArr2.length < getOutputSize(i2)) {
            throw new ShortBufferException("output");
        }
        Object update = update(bArr, i, i2);
        System.arraycopy(update, 0, bArr2, i3, update.length);
        return update.length;
    }

    public abstract byte[] update(byte[] bArr, int i, int i2);
}
