package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.CramerShoupParameters;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.util.BigIntegers;

public class CramerShoupParametersGenerator {
    private static final BigInteger ONE;
    private int certainty;
    private SecureRandom random;
    private int size;

    private static class ParametersHelper {
        private static final BigInteger TWO;

        static {
            TWO = BigInteger.valueOf(2);
        }

        private ParametersHelper() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static java.math.BigInteger[] generateSafePrimes(int r6, int r7, java.security.SecureRandom r8) {
            /*
            r5 = 1;
            r4 = 2;
            r0 = r6 + -1;
        L_0x0004:
            r1 = new java.math.BigInteger;
            r1.<init>(r0, r4, r8);
            r2 = r1.shiftLeft(r5);
            r3 = org.bouncycastle.crypto.generators.CramerShoupParametersGenerator.ONE;
            r2 = r2.add(r3);
            r3 = r2.isProbablePrime(r7);
            if (r3 == 0) goto L_0x0004;
        L_0x001b:
            if (r7 <= r4) goto L_0x0023;
        L_0x001d:
            r3 = r1.isProbablePrime(r7);
            if (r3 == 0) goto L_0x0004;
        L_0x0023:
            r0 = new java.math.BigInteger[r4];
            r3 = 0;
            r0[r3] = r2;
            r0[r5] = r1;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.bouncycastle.crypto.generators.CramerShoupParametersGenerator.ParametersHelper.generateSafePrimes(int, int, java.security.SecureRandom):java.math.BigInteger[]");
        }

        static BigInteger selectGenerator(BigInteger bigInteger, SecureRandom secureRandom) {
            BigInteger modPow;
            BigInteger subtract = bigInteger.subtract(TWO);
            do {
                modPow = BigIntegers.createRandomInRange(TWO, subtract, secureRandom).modPow(TWO, bigInteger);
            } while (modPow.equals(CramerShoupParametersGenerator.ONE));
            return modPow;
        }
    }

    static {
        ONE = BigInteger.valueOf(1);
    }

    public CramerShoupParameters generateParameters() {
        BigInteger bigInteger = ParametersHelper.generateSafePrimes(this.size, this.certainty, this.random)[1];
        BigInteger selectGenerator = ParametersHelper.selectGenerator(bigInteger, this.random);
        BigInteger selectGenerator2 = ParametersHelper.selectGenerator(bigInteger, this.random);
        while (selectGenerator.equals(selectGenerator2)) {
            selectGenerator2 = ParametersHelper.selectGenerator(bigInteger, this.random);
        }
        return new CramerShoupParameters(bigInteger, selectGenerator, selectGenerator2, new SHA256Digest());
    }

    public CramerShoupParameters generateParameters(DHParameters dHParameters) {
        BigInteger p = dHParameters.getP();
        BigInteger g = dHParameters.getG();
        BigInteger selectGenerator = ParametersHelper.selectGenerator(p, this.random);
        while (g.equals(selectGenerator)) {
            selectGenerator = ParametersHelper.selectGenerator(p, this.random);
        }
        return new CramerShoupParameters(p, g, selectGenerator, new SHA256Digest());
    }

    public void init(int i, int i2, SecureRandom secureRandom) {
        this.size = i;
        this.certainty = i2;
        this.random = secureRandom;
    }
}
