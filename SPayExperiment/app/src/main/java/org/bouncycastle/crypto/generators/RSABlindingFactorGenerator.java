/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 */
package org.bouncycastle.crypto.generators;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

public class RSABlindingFactorGenerator {
    private static BigInteger ONE;
    private static BigInteger ZERO;
    private RSAKeyParameters key;
    private SecureRandom random;

    static {
        ZERO = BigInteger.valueOf((long)0L);
        ONE = BigInteger.valueOf((long)1L);
    }

    public BigInteger generateBlindingFactor() {
        BigInteger bigInteger;
        BigInteger bigInteger2;
        if (this.key == null) {
            throw new IllegalStateException("generator not initialised");
        }
        BigInteger bigInteger3 = this.key.getModulus();
        int n2 = -1 + bigInteger3.bitLength();
        do {
            bigInteger2 = new BigInteger(n2, (Random)this.random);
            bigInteger = bigInteger2.gcd(bigInteger3);
        } while (bigInteger2.equals((Object)ZERO) || bigInteger2.equals((Object)ONE) || !bigInteger.equals((Object)ONE));
        return bigInteger2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void init(CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom)cipherParameters;
            this.key = (RSAKeyParameters)parametersWithRandom.getParameters();
            this.random = parametersWithRandom.getRandom();
        } else {
            this.key = (RSAKeyParameters)cipherParameters;
            this.random = new SecureRandom();
        }
        if (this.key instanceof RSAPrivateCrtKeyParameters) {
            throw new IllegalArgumentException("generator requires RSA public key");
        }
    }
}

