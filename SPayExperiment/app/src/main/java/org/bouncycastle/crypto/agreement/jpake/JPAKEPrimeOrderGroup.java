/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.agreement.jpake;

import java.math.BigInteger;
import org.bouncycastle.crypto.agreement.jpake.JPAKEUtil;

public class JPAKEPrimeOrderGroup {
    private final BigInteger g;
    private final BigInteger p;
    private final BigInteger q;

    public JPAKEPrimeOrderGroup(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this(bigInteger, bigInteger2, bigInteger3, false);
    }

    JPAKEPrimeOrderGroup(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, boolean bl) {
        JPAKEUtil.validateNotNull((Object)bigInteger, "p");
        JPAKEUtil.validateNotNull((Object)bigInteger2, "q");
        JPAKEUtil.validateNotNull((Object)bigInteger3, "g");
        if (!bl) {
            if (!bigInteger.subtract(JPAKEUtil.ONE).mod(bigInteger2).equals((Object)JPAKEUtil.ZERO)) {
                throw new IllegalArgumentException("p-1 must be evenly divisible by q");
            }
            if (bigInteger3.compareTo(BigInteger.valueOf((long)2L)) == -1 || bigInteger3.compareTo(bigInteger.subtract(JPAKEUtil.ONE)) == 1) {
                throw new IllegalArgumentException("g must be in [2, p-1]");
            }
            if (!bigInteger3.modPow(bigInteger2, bigInteger).equals((Object)JPAKEUtil.ONE)) {
                throw new IllegalArgumentException("g^q mod p must equal 1");
            }
            if (!bigInteger.isProbablePrime(20)) {
                throw new IllegalArgumentException("p must be prime");
            }
            if (!bigInteger2.isProbablePrime(20)) {
                throw new IllegalArgumentException("q must be prime");
            }
        }
        this.p = bigInteger;
        this.q = bigInteger2;
        this.g = bigInteger3;
    }

    public BigInteger getG() {
        return this.g;
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getQ() {
        return this.q;
    }
}

