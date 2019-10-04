/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.security.AlgorithmParameters
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidKeyException
 *  java.security.InvalidParameterException
 *  java.security.Key
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.CipherSpi
 */
package org.bouncycastle.pqc.jcajce.provider.util;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.CipherSpi;

public abstract class CipherSpiExt
extends CipherSpi {
    public static final int DECRYPT_MODE = 2;
    public static final int ENCRYPT_MODE = 1;
    protected int opMode;

    public abstract int doFinal(byte[] var1, int var2, int var3, byte[] var4, int var5);

    public final byte[] doFinal() {
        return this.doFinal(null, 0, 0);
    }

    public final byte[] doFinal(byte[] arrby) {
        return this.doFinal(arrby, 0, arrby.length);
    }

    public abstract byte[] doFinal(byte[] var1, int var2, int var3);

    protected final int engineDoFinal(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        return this.doFinal(arrby, n, n2, arrby2, n3);
    }

    protected final byte[] engineDoFinal(byte[] arrby, int n, int n2) {
        return this.doFinal(arrby, n, n2);
    }

    protected final int engineGetBlockSize() {
        return this.getBlockSize();
    }

    protected final byte[] engineGetIV() {
        return this.getIV();
    }

    protected final int engineGetKeySize(Key key) {
        if (!(key instanceof Key)) {
            throw new InvalidKeyException("Unsupported key.");
        }
        return this.getKeySize(key);
    }

    protected final int engineGetOutputSize(int n) {
        return this.getOutputSize(n);
    }

    protected final AlgorithmParameters engineGetParameters() {
        return null;
    }

    protected final void engineInit(int n, Key key, AlgorithmParameters algorithmParameters, SecureRandom secureRandom) {
        if (algorithmParameters == null) {
            this.engineInit(n, key, secureRandom);
            return;
        }
        this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
    }

    protected final void engineInit(int n, Key key, SecureRandom secureRandom) {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
            return;
        }
        catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            throw new InvalidParameterException(invalidAlgorithmParameterException.getMessage());
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    protected void engineInit(int n, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        if (algorithmParameterSpec != null && !(algorithmParameterSpec instanceof AlgorithmParameterSpec)) {
            throw new InvalidAlgorithmParameterException();
        }
        if (key == null || !(key instanceof Key)) {
            throw new InvalidKeyException();
        }
        this.opMode = n;
        if (n == 1) {
            this.initEncrypt(key, algorithmParameterSpec, secureRandom);
            return;
        } else {
            if (n != 2) return;
            {
                this.initDecrypt(key, algorithmParameterSpec);
                return;
            }
        }
    }

    protected final void engineSetMode(String string) {
        this.setMode(string);
    }

    protected final void engineSetPadding(String string) {
        this.setPadding(string);
    }

    protected final int engineUpdate(byte[] arrby, int n, int n2, byte[] arrby2, int n3) {
        return this.update(arrby, n, n2, arrby2, n3);
    }

    protected final byte[] engineUpdate(byte[] arrby, int n, int n2) {
        return this.update(arrby, n, n2);
    }

    public abstract int getBlockSize();

    public abstract byte[] getIV();

    public abstract int getKeySize(Key var1);

    public abstract String getName();

    public abstract int getOutputSize(int var1);

    public abstract AlgorithmParameterSpec getParameters();

    public abstract void initDecrypt(Key var1, AlgorithmParameterSpec var2);

    public abstract void initEncrypt(Key var1, AlgorithmParameterSpec var2, SecureRandom var3);

    protected abstract void setMode(String var1);

    protected abstract void setPadding(String var1);

    public abstract int update(byte[] var1, int var2, int var3, byte[] var4, int var5);

    public final byte[] update(byte[] arrby) {
        return this.update(arrby, 0, arrby.length);
    }

    public abstract byte[] update(byte[] var1, int var2, int var3);
}

