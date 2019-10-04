/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidParameterException
 *  java.security.Key
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.ShortBufferException
 */
package org.bouncycastle.pqc.jcajce.provider.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.ShortBufferException;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;

public abstract class AsymmetricHybridCipher
extends CipherSpiExt {
    protected AlgorithmParameterSpec paramSpec;

    protected abstract int decryptOutputSize(int var1);

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
    public abstract byte[] doFinal(byte[] var1, int var2, int var3);

    protected abstract int encryptOutputSize(int var1);

    @Override
    public final int getBlockSize() {
        return 0;
    }

    @Override
    public final byte[] getIV() {
        return null;
    }

    @Override
    public final int getOutputSize(int n) {
        if (this.opMode == 1) {
            return this.encryptOutputSize(n);
        }
        return this.decryptOutputSize(n);
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

    @Override
    protected final void setMode(String string) {
    }

    @Override
    protected final void setPadding(String string) {
    }

    @Override
    public final int update(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        if (arrby2.length < this.getOutputSize(n2)) {
            throw new ShortBufferException("output");
        }
        byte[] arrby3 = this.update(arrby, n, n2);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)n3, (int)arrby3.length);
        return arrby3.length;
    }

    @Override
    public abstract byte[] update(byte[] var1, int var2, int var3);
}

