package org.bouncycastle.jce.spec;

import java.math.BigInteger;
import java.security.spec.KeySpec;

public class GOST3410PrivateKeySpec implements KeySpec {
    private BigInteger f312a;
    private BigInteger f313p;
    private BigInteger f314q;
    private BigInteger f315x;

    public GOST3410PrivateKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        this.f315x = bigInteger;
        this.f313p = bigInteger2;
        this.f314q = bigInteger3;
        this.f312a = bigInteger4;
    }

    public BigInteger getA() {
        return this.f312a;
    }

    public BigInteger getP() {
        return this.f313p;
    }

    public BigInteger getQ() {
        return this.f314q;
    }

    public BigInteger getX() {
        return this.f315x;
    }
}
