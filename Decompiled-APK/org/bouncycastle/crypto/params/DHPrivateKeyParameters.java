package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class DHPrivateKeyParameters extends DHKeyParameters {
    private BigInteger f217x;

    public DHPrivateKeyParameters(BigInteger bigInteger, DHParameters dHParameters) {
        super(true, dHParameters);
        this.f217x = bigInteger;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DHPrivateKeyParameters)) {
            return false;
        }
        boolean z = ((DHPrivateKeyParameters) obj).getX().equals(this.f217x) && super.equals(obj);
        return z;
    }

    public BigInteger getX() {
        return this.f217x;
    }

    public int hashCode() {
        return this.f217x.hashCode() ^ super.hashCode();
    }
}
