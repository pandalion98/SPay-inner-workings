/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.ByteArrayOutputStream
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidParameterException
 *  java.security.Key
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.IllegalBlockSizeException
 *  javax.crypto.ShortBufferException
 */
package org.bouncycastle.pqc.jcajce.provider.util;

import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public abstract class AsymmetricBlockCipher
extends CipherSpiExt {
    protected ByteArrayOutputStream buf = new ByteArrayOutputStream();
    protected int cipherTextSize;
    protected int maxPlainTextSize;
    protected AlgorithmParameterSpec paramSpec;

    protected void checkLength(int n) {
        int n2 = n + this.buf.size();
        if (this.opMode == 1) {
            if (n2 > this.maxPlainTextSize) {
                throw new IllegalBlockSizeException("The length of the plaintext (" + n2 + " bytes) is not supported by " + "the cipher (max. " + this.maxPlainTextSize + " bytes).");
            }
        } else if (this.opMode == 2 && n2 != this.cipherTextSize) {
            throw new IllegalBlockSizeException("Illegal ciphertext length (expected " + this.cipherTextSize + " bytes, was " + n2 + " bytes).");
        }
    }

    @Override
    public final int doFinal(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        if (arrby2.length < this.getOutputSize(n2)) {
            throw new ShortBufferException("Output buffer too short.");
        }
        byte[] arrby3 = this.doFinal(arrby, n, n2);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)n3, (int)arrby3.length);
        return arrby3.length;
    }

    @Override
    public final byte[] doFinal(byte[] arrby, int n, int n2) {
        this.checkLength(n2);
        this.update(arrby, n, n2);
        byte[] arrby2 = this.buf.toByteArray();
        this.buf.reset();
        switch (this.opMode) {
            default: {
                return null;
            }
            case 1: {
                return this.messageEncrypt(arrby2);
            }
            case 2: 
        }
        return this.messageDecrypt(arrby2);
    }

    @Override
    public final int getBlockSize() {
        if (this.opMode == 1) {
            return this.maxPlainTextSize;
        }
        return this.cipherTextSize;
    }

    @Override
    public final byte[] getIV() {
        return null;
    }

    @Override
    public final int getOutputSize(int n) {
        int n2;
        int n3 = n + this.buf.size();
        if (n3 > (n2 = this.getBlockSize())) {
            n2 = 0;
        }
        return n2;
    }

    @Override
    public final AlgorithmParameterSpec getParameters() {
        return this.paramSpec;
    }

    protected abstract void initCipherDecrypt(Key var1, AlgorithmParameterSpec var2);

    protected abstract void initCipherEncrypt(Key var1, AlgorithmParameterSpec var2, SecureRandom var3);

    public final void initDecrypt(Key key) {
        try {
            this.initDecrypt(key, null);
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidParameterException("This cipher needs algorithm parameters for initialization (cannot be null).");
        }
    }

    @Override
    public final void initDecrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec) {
        this.opMode = 2;
        this.initCipherDecrypt(key, algorithmParameterSpec);
    }

    public final void initEncrypt(Key key) {
        try {
            this.initEncrypt(key, null, new SecureRandom());
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidParameterException("This cipher needs algorithm parameters for initialization (cannot be null).");
        }
    }

    public final void initEncrypt(Key key, SecureRandom secureRandom) {
        try {
            this.initEncrypt(key, null, secureRandom);
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidParameterException("This cipher needs algorithm parameters for initialization (cannot be null).");
        }
    }

    public final void initEncrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec) {
        this.initEncrypt(key, algorithmParameterSpec, new SecureRandom());
    }

    @Override
    public final void initEncrypt(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        this.opMode = 1;
        this.initCipherEncrypt(key, algorithmParameterSpec, secureRandom);
    }

    protected abstract byte[] messageDecrypt(byte[] var1);

    protected abstract byte[] messageEncrypt(byte[] var1);

    @Override
    protected final void setMode(String string) {
    }

    @Override
    protected final void setPadding(String string) {
    }

    @Override
    public final int update(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        this.update(arrby, n, n2);
        return 0;
    }

    @Override
    public final byte[] update(byte[] arrby, int n, int n2) {
        if (n2 != 0) {
            this.buf.write(arrby, n, n2);
        }
        return new byte[0];
    }
}

