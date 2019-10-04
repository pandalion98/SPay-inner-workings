/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.CramerShoupKeyGenerationParameters;
import org.bouncycastle.crypto.params.CramerShoupParameters;
import org.bouncycastle.crypto.params.CramerShoupPrivateKeyParameters;
import org.bouncycastle.crypto.params.CramerShoupPublicKeyParameters;
import org.bouncycastle.util.BigIntegers;

public class CramerShoupKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private CramerShoupKeyGenerationParameters param;

    private CramerShoupPublicKeyParameters calculatePublicKey(CramerShoupParameters cramerShoupParameters, CramerShoupPrivateKeyParameters cramerShoupPrivateKeyParameters) {
        BigInteger bigInteger = cramerShoupParameters.getG1();
        BigInteger bigInteger2 = cramerShoupParameters.getG2();
        BigInteger bigInteger3 = cramerShoupParameters.getP();
        return new CramerShoupPublicKeyParameters(cramerShoupParameters, bigInteger.modPow(cramerShoupPrivateKeyParameters.getX1(), bigInteger3).multiply(bigInteger2.modPow(cramerShoupPrivateKeyParameters.getX2(), bigInteger3)), bigInteger.modPow(cramerShoupPrivateKeyParameters.getY1(), bigInteger3).multiply(bigInteger2.modPow(cramerShoupPrivateKeyParameters.getY2(), bigInteger3)), bigInteger.modPow(cramerShoupPrivateKeyParameters.getZ(), bigInteger3));
    }

    private CramerShoupPrivateKeyParameters generatePrivateKey(SecureRandom secureRandom, CramerShoupParameters cramerShoupParameters) {
        BigInteger bigInteger = cramerShoupParameters.getP();
        return new CramerShoupPrivateKeyParameters(cramerShoupParameters, this.generateRandomElement(bigInteger, secureRandom), this.generateRandomElement(bigInteger, secureRandom), this.generateRandomElement(bigInteger, secureRandom), this.generateRandomElement(bigInteger, secureRandom), this.generateRandomElement(bigInteger, secureRandom));
    }

    private BigInteger generateRandomElement(BigInteger bigInteger, SecureRandom secureRandom) {
        return BigIntegers.createRandomInRange((BigInteger)ONE, (BigInteger)bigInteger.subtract(ONE), (SecureRandom)secureRandom);
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        CramerShoupParameters cramerShoupParameters = this.param.getParameters();
        CramerShoupPrivateKeyParameters cramerShoupPrivateKeyParameters = this.generatePrivateKey(this.param.getRandom(), cramerShoupParameters);
        CramerShoupPublicKeyParameters cramerShoupPublicKeyParameters = this.calculatePublicKey(cramerShoupParameters, cramerShoupPrivateKeyParameters);
        cramerShoupPrivateKeyParameters.setPk(cramerShoupPublicKeyParameters);
        return new AsymmetricCipherKeyPair(cramerShoupPublicKeyParameters, cramerShoupPrivateKeyParameters);
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (CramerShoupKeyGenerationParameters)keyGenerationParameters;
    }
}

