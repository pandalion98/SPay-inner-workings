package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class DHPublicKeyParameters extends DHKeyParameters {
    private BigInteger f218y;

    public DHPublicKeyParameters(BigInteger bigInteger, DHParameters dHParameters) {
        super(false, dHParameters);
        this.f218y = bigInteger;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DHPublicKeyParameters)) {
            return false;
        }
        boolean z = ((DHPublicKeyParameters) obj).getY().equals(this.f218y) && super.equals(obj);
        return z;
    }

    public BigInteger getY() {
        return this.f218y;
    }

    public int hashCode() {
        return this.f218y.hashCode() ^ super.hashCode();
    }
}
