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
import org.bouncycastle.crypto.params.ElGamalParameters;

public class ElGamalParametersGenerator {
    private int certainty;
    private SecureRandom random;
    private int size;

    public ElGamalParameters generateParameters() {
        BigInteger[] arrbigInteger = DHParametersHelper.generateSafePrimes(this.size, this.certainty, this.random);
        BigInteger bigInteger = arrbigInteger[0];
        return new ElGamalParameters(bigInteger, DHParametersHelper.selectGenerator(bigInteger, arrbigInteger[1], this.random));
    }

    public void init(int n2, int n3, SecureRandom secureRandom) {
        this.size = n2;
        this.certainty = n3;
        this.random = secureRandom;
    }
}

