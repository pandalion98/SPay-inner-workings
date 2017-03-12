package org.bouncycastle.jce.spec;

import java.math.BigInteger;
import java.security.spec.KeySpec;

public class GOST3410PublicKeySpec implements KeySpec {
    private BigInteger f319a;
    private BigInteger f320p;
    private BigInteger f321q;
    private BigInteger f322y;

    public GOST3410PublicKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        this.f322y = bigInteger;
        this.f320p = bigInteger2;
        this.f321q = bigInteger3;
        this.f319a = bigInteger4;
    }

    public BigInteger getA() {
        return this.f319a;
    }

    public BigInteger getP() {
        return this.f320p;
    }

    public BigInteger getQ() {
        return this.f321q;
    }

    public BigInteger getY() {
        return this.f322y;
    }
}
