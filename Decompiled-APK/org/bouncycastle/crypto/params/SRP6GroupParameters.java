package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class SRP6GroupParameters {
    private BigInteger f250N;
    private BigInteger f251g;

    public SRP6GroupParameters(BigInteger bigInteger, BigInteger bigInteger2) {
        this.f250N = bigInteger;
        this.f251g = bigInteger2;
    }

    public BigInteger getG() {
        return this.f251g;
    }

    public BigInteger getN() {
        return this.f250N;
    }
}
