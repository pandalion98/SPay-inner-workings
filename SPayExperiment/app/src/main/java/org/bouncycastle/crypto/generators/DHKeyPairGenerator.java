/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.DHKeyGeneratorHelper;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHKeyGenerationParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPrivateKeyParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;

public class DHKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private DHKeyGenerationParameters param;

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        DHKeyGeneratorHelper dHKeyGeneratorHelper = DHKeyGeneratorHelper.INSTANCE;
        DHParameters dHParameters = this.param.getParameters();
        BigInteger bigInteger = dHKeyGeneratorHelper.calculatePrivate(dHParameters, this.param.getRandom());
        return new AsymmetricCipherKeyPair(new DHPublicKeyParameters(dHKeyGeneratorHelper.calculatePublic(dHParameters, bigInteger), dHParameters), new DHPrivateKeyParameters(bigInteger, dHParameters));
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (DHKeyGenerationParameters)keyGenerationParameters;
    }
}

