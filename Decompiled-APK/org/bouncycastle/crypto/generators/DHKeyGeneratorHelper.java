package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.math.ec.WNafUtil;
import org.bouncycastle.util.BigIntegers;

class DHKeyGeneratorHelper {
    static final DHKeyGeneratorHelper INSTANCE;
    private static final BigInteger ONE;
    private static final BigInteger TWO;

    static {
        INSTANCE = new DHKeyGeneratorHelper();
        ONE = BigInteger.valueOf(1);
        TWO = BigInteger.valueOf(2);
    }

    private DHKeyGeneratorHelper() {
    }

    BigInteger calculatePrivate(DHParameters dHParameters, SecureRandom secureRandom) {
        int l = dHParameters.getL();
        BigInteger bit;
        if (l != 0) {
            int i = l >>> 2;
            do {
                bit = new BigInteger(l, secureRandom).setBit(l - 1);
            } while (WNafUtil.getNafWeight(bit) < i);
            return bit;
        }
        bit = TWO;
        l = dHParameters.getM();
        if (l != 0) {
            bit = ONE.shiftLeft(l - 1);
        }
        BigInteger q = dHParameters.getQ();
        if (q == null) {
            q = dHParameters.getP();
        }
        BigInteger subtract = q.subtract(TWO);
        int bitLength = subtract.bitLength() >>> 2;
        do {
            q = BigIntegers.createRandomInRange(bit, subtract, secureRandom);
        } while (WNafUtil.getNafWeight(q) < bitLength);
        return q;
    }

    BigInteger calculatePublic(DHParameters dHParameters, BigInteger bigInteger) {
        return dHParameters.getG().modPow(bigInteger, dHParameters.getP());
    }
}
