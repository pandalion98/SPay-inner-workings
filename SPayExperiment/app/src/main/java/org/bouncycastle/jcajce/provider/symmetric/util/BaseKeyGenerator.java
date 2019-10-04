/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  java.security.InvalidAlgorithmParameterException
 *  java.security.InvalidParameterException
 *  java.security.SecureRandom
 *  java.security.spec.AlgorithmParameterSpec
 *  javax.crypto.KeyGeneratorSpi
 *  javax.crypto.SecretKey
 *  javax.crypto.spec.SecretKeySpec
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class BaseKeyGenerator
extends KeyGeneratorSpi {
    protected String algName;
    protected int defaultKeySize;
    protected CipherKeyGenerator engine;
    protected int keySize;
    protected boolean uninitialised = true;

    protected BaseKeyGenerator(String string, int n, CipherKeyGenerator cipherKeyGenerator) {
        this.algName = string;
        this.defaultKeySize = n;
        this.keySize = n;
        this.engine = cipherKeyGenerator;
    }

    protected SecretKey engineGenerateKey() {
        if (this.uninitialised) {
            this.engine.init(new KeyGenerationParameters(new SecureRandom(), this.defaultKeySize));
            this.uninitialised = false;
        }
        return new SecretKeySpec(this.engine.generateKey(), this.algName);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected void engineInit(int var1_1, SecureRandom var2_2) {
        if (var2_2 != null) ** GOTO lbl4
        try {
            var2_2 = new SecureRandom();
lbl4: // 2 sources:
            this.engine.init(new KeyGenerationParameters(var2_2, var1_1));
            this.uninitialised = false;
            return;
        }
        catch (IllegalArgumentException var3_3) {
            throw new InvalidParameterException(var3_3.getMessage());
        }
    }

    protected void engineInit(SecureRandom secureRandom) {
        if (secureRandom != null) {
            this.engine.init(new KeyGenerationParameters(secureRandom, this.defaultKeySize));
            this.uninitialised = false;
        }
    }

    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) {
        throw new InvalidAlgorithmParameterException("Not Implemented");
    }
}

