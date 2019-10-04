/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 *  org.bouncycastle.math.ec.WNafUtil
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.math.ec.WNafUtil;
import org.bouncycastle.util.BigIntegers;

class DHKeyGeneratorHelper {
    static final DHKeyGeneratorHelper INSTANCE = new DHKeyGeneratorHelper();
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private static final BigInteger TWO = BigInteger.valueOf((long)2L);

    private DHKeyGeneratorHelper() {
    }

    BigInteger calculatePrivate(DHParameters dHParameters, SecureRandom secureRandom) {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        int n2 = dHParameters.getL();
        if (n2 != 0) {
            BigInteger bigInteger3;
            int n3 = n2 >>> 2;
            while (WNafUtil.getNafWeight((BigInteger)(bigInteger3 = new BigInteger(n2, (Random)secureRandom).setBit(n2 - 1))) < n3) {
            }
            return bigInteger3;
        }
        BigInteger bigInteger4 = TWO;
        int n4 = dHParameters.getM();
        if (n4 != 0) {
            bigInteger4 = ONE.shiftLeft(n4 - 1);
        }
        if ((bigInteger2 = dHParameters.getQ()) == null) {
            bigInteger2 = dHParameters.getP();
        }
        BigInteger bigInteger5 = bigInteger2.subtract(TWO);
        int n5 = bigInteger5.bitLength() >>> 2;
        while (WNafUtil.getNafWeight((BigInteger)(bigInteger = BigIntegers.createRandomInRange((BigInteger)bigInteger4, (BigInteger)bigInteger5, (SecureRandom)secureRandom))) < n5) {
        }
        return bigInteger;
    }

    BigInteger calculatePublic(DHParameters dHParameters, BigInteger bigInteger) {
        return dHParameters.getG().modPow(bigInteger, dHParameters.getP());
    }
}

