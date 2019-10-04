/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 *  org.bouncycastle.util.BigIntegers
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.CramerShoupParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.util.BigIntegers;

public class CramerShoupParametersGenerator {
    private static final BigInteger ONE = BigInteger.valueOf((long)1L);
    private int certainty;
    private SecureRandom random;
    private int size;

    public CramerShoupParameters generateParameters() {
        BigInteger bigInteger = ParametersHelper.generateSafePrimes(this.size, this.certainty, this.random)[1];
        BigInteger bigInteger2 = ParametersHelper.selectGenerator(bigInteger, this.random);
        BigInteger bigInteger3 = ParametersHelper.selectGenerator(bigInteger, this.random);
        while (bigInteger2.equals((Object)bigInteger3)) {
            bigInteger3 = ParametersHelper.selectGenerator(bigInteger, this.random);
        }
        return new CramerShoupParameters(bigInteger, bigInteger2, bigInteger3, new SHA256Digest());
    }

    public CramerShoupParameters generateParameters(DHParameters dHParameters) {
        BigInteger bigInteger = dHParameters.getP();
        BigInteger bigInteger2 = dHParameters.getG();
        BigInteger bigInteger3 = ParametersHelper.selectGenerator(bigInteger, this.random);
        while (bigInteger2.equals((Object)bigInteger3)) {
            bigInteger3 = ParametersHelper.selectGenerator(bigInteger, this.random);
        }
        return new CramerShoupParameters(bigInteger, bigInteger2, bigInteger3, new SHA256Digest());
    }

    public void init(int n2, int n3, SecureRandom secureRandom) {
        this.size = n2;
        this.certainty = n3;
        this.random = secureRandom;
    }

    private static class ParametersHelper {
        private static final BigInteger TWO = BigInteger.valueOf((long)2L);

        private ParametersHelper() {
        }

        static BigInteger[] generateSafePrimes(int n2, int n3, SecureRandom secureRandom) {
            BigInteger bigInteger;
            BigInteger bigInteger2;
            int n4 = n2 - 1;
            while (!(bigInteger2 = (bigInteger = new BigInteger(n4, 2, (Random)secureRandom)).shiftLeft(1).add(ONE)).isProbablePrime(n3) || n3 > 2 && !bigInteger.isProbablePrime(n3)) {
            }
            return new BigInteger[]{bigInteger2, bigInteger};
        }

        static BigInteger selectGenerator(BigInteger bigInteger, SecureRandom secureRandom) {
            BigInteger bigInteger2;
            BigInteger bigInteger3 = bigInteger.subtract(TWO);
            while ((bigInteger2 = BigIntegers.createRandomInRange((BigInteger)TWO, (BigInteger)bigInteger3, (SecureRandom)secureRandom).modPow(TWO, bigInteger)).equals((Object)ONE)) {
            }
            return bigInteger2;
        }
    }

}

