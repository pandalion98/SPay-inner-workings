package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class ElGamalPublicKeyParameters extends ElGamalKeyParameters {
    private BigInteger f235y;

    public ElGamalPublicKeyParameters(BigInteger bigInteger, ElGamalParameters elGamalParameters) {
        super(false, elGamalParameters);
        this.f235y = bigInteger;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ElGamalPublicKeyParameters)) {
            return false;
        }
        boolean z = ((ElGamalPublicKeyParameters) obj).getY().equals(this.f235y) && super.equals(obj);
        return z;
    }

    public BigInteger getY() {
        return this.f235y;
    }

    public int hashCode() {
        return this.f235y.hashCode() ^ super.hashCode();
    }
}
