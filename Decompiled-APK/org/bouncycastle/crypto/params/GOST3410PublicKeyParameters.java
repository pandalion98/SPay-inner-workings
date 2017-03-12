package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class GOST3410PublicKeyParameters extends GOST3410KeyParameters {
    private BigInteger f240y;

    public GOST3410PublicKeyParameters(BigInteger bigInteger, GOST3410Parameters gOST3410Parameters) {
        super(false, gOST3410Parameters);
        this.f240y = bigInteger;
    }

    public BigInteger getY() {
        return this.f240y;
    }
}
