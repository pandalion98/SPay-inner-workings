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
import org.bouncycastle.crypto.generators.DHParametersHelper;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHValidationParameters;

public class DHParametersGenerator {
    private static final BigInteger TWO = BigInteger.valueOf((long)2L);
    private int certainty;
    private SecureRandom random;
    private int size;

    public DHParameters generateParameters() {
        BigInteger[] arrbigInteger = DHParametersHelper.generateSafePrimes(this.size, this.certainty, this.random);
        BigInteger bigInteger = arrbigInteger[0];
        BigInteger bigInteger2 = arrbigInteger[1];
        return new DHParameters(bigInteger, DHParametersHelper.selectGenerator(bigInteger, bigInteger2, this.random), bigInteger2, TWO, null);
    }

    public void init(int n2, int n3, SecureRandom secureRandom) {
        this.size = n2;
        this.certainty = n3;
        this.random = secureRandom;
    }
}

