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
import org.bouncycastle.math.ec.WNafUtil;
import org.bouncycastle.util.BigIntegers;

class DHParametersHelper {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private static final BigInteger TWO = BigInteger.valueOf((long)2L);

    DHParametersHelper() {
    }

    static BigInteger[] generateSafePrimes(int n2, int n3, SecureRandom secureRandom) {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        int n4 = n2 - 1;
        int n5 = n2 >>> 2;
        while (!(bigInteger = (bigInteger2 = new BigInteger(n4, 2, (Random)secureRandom)).shiftLeft(1).add(ONE)).isProbablePrime(n3) || n3 > 2 && !bigInteger2.isProbablePrime(n3 - 2) || WNafUtil.getNafWeight((BigInteger)bigInteger) < n5) {
        }
        return new BigInteger[]{bigInteger, bigInteger2};
    }

    static BigInteger selectGenerator(BigInteger bigInteger, BigInteger bigInteger2, SecureRandom secureRandom) {
        BigInteger bigInteger3;
        BigInteger bigInteger4 = bigInteger.subtract(TWO);
        while ((bigInteger3 = BigIntegers.createRandomInRange((BigInteger)TWO, (BigInteger)bigInteger4, (SecureRandom)secureRandom).modPow(TWO, bigInteger)).equals((Object)ONE)) {
        }
        return bigInteger3;
    }
}

