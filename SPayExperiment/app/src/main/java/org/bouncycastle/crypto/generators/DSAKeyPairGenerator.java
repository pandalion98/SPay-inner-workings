/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  org.bouncycastle.math.ec.WNafUtil
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DSAKeyGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPrivateKeyParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.math.ec.WNafUtil;
import org.bouncycastle.util.BigIntegers;

public class DSAKeyPairGenerator
implements AsymmetricCipherKeyPairGenerator {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private DSAKeyGenerationParameters param;

    private static BigInteger calculatePublicKey(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        return bigInteger2.modPow(bigInteger3, bigInteger);
    }

    private static BigInteger generatePrivateKey(BigInteger bigInteger, SecureRandom secureRandom) {
        BigInteger bigInteger2;
        int n2 = bigInteger.bitLength() >>> 2;
        while (WNafUtil.getNafWeight((BigInteger)(bigInteger2 = BigIntegers.createRandomInRange((BigInteger)ONE, (BigInteger)bigInteger.subtract(ONE), (SecureRandom)secureRandom))) < n2) {
        }
        return bigInteger2;
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        DSAParameters dSAParameters = this.param.getParameters();
        BigInteger bigInteger = DSAKeyPairGenerator.generatePrivateKey(dSAParameters.getQ(), this.param.getRandom());
        return new AsymmetricCipherKeyPair(new DSAPublicKeyParameters(DSAKeyPairGenerator.calculatePublicKey(dSAParameters.getP(), dSAParameters.getG(), bigInteger), dSAParameters), new DSAPrivateKeyParameters(bigInteger, dSAParameters));
    }

    @Override
    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.param = (DSAKeyGenerationParameters)keyGenerationParameters;
    }
}

