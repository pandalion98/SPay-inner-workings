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
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;

public class ElGamalKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private ElGamalKeyGenerationParameters param;

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        DHKeyGeneratorHelper dHKeyGeneratorHelper = DHKeyGeneratorHelper.INSTANCE;
        ElGamalParameters elGamalParameters = this.param.getParameters();
        DHParameters dHParameters = new DHParameters(elGamalParameters.getP(), elGamalParameters.getG(), null, elGamalParameters.getL());
        BigInteger bigInteger = dHKeyGeneratorHelper.calculatePrivate(dHParameters, this.param.getRandom());
        return new AsymmetricCipherKeyPair(new ElGamalPublicKeyParameters(dHKeyGeneratorHelper.calculatePublic(dHParameters, bigInteger), elGamalParameters), new ElGamalPrivateKeyParameters(bigInteger, elGamalParameters));
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (ElGamalKeyGenerationParameters)keyGenerationParameters;
    }
}

