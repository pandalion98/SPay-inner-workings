package org.bouncycastle.jce.spec;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

public class ElGamalParameterSpec implements AlgorithmParameterSpec {
    private BigInteger f308g;
    private BigInteger f309p;

    public ElGamalParameterSpec(BigInteger bigInteger, BigInteger bigInteger2) {
        this.f309p = bigInteger;
        this.f308g = bigInteger2;
    }

    public BigInteger getG() {
        return this.f308g;
    }

    public BigInteger getP() {
        return this.f309p;
    }
}
