/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 *  java.security.SecureRandom
 *  java.util.Random
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import org.bouncycastle.crypto.signers.DSAKCalculator;

public class RandomDSAKCalculator
implements DSAKCalculator {
    private static final BigInteger ZERO = BigInteger.valueOf((long)0L);
    private BigInteger q;
    private SecureRandom random;

    @Override
    public void init(BigInteger bigInteger, BigInteger bigInteger2, byte[] arrby) {
        throw new IllegalStateException("Operation not supported");
    }

    @Override
    public void init(BigInteger bigInteger, SecureRandom secureRandom) {
        this.q = bigInteger;
        this.random = secureRandom;
    }

    @Override
    public boolean isDeterministic() {
        return false;
    }

    @Override
    public BigInteger nextK() {
        BigInteger bigInteger;
        int n2 = this.q.bitLength();
        while ((bigInteger = new BigInteger(n2, (Random)this.random)).equals((Object)ZERO) || bigInteger.compareTo(this.q) >= 0) {
        }
        return bigInteger;
    }
}

