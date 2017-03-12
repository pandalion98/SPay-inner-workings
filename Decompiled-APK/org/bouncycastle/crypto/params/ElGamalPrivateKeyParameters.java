package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class ElGamalPrivateKeyParameters extends ElGamalKeyParameters {
    private BigInteger f234x;

    public ElGamalPrivateKeyParameters(BigInteger bigInteger, ElGamalParameters elGamalParameters) {
        super(true, elGamalParameters);
        this.f234x = bigInteger;
    }

    public boolean equals(Object obj) {
        return !(obj instanceof ElGamalPrivateKeyParameters) ? false : !((ElGamalPrivateKeyParameters) obj).getX().equals(this.f234x) ? false : super.equals(obj);
    }

    public BigInteger getX() {
        return this.f234x;
    }

    public int hashCode() {
        return getX().hashCode();
    }
}
