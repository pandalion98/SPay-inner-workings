package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;

public class ElGamalParameters implements CipherParameters {
    private BigInteger f231g;
    private int f232l;
    private BigInteger f233p;

    public ElGamalParameters(BigInteger bigInteger, BigInteger bigInteger2) {
        this(bigInteger, bigInteger2, 0);
    }

    public ElGamalParameters(BigInteger bigInteger, BigInteger bigInteger2, int i) {
        this.f231g = bigInteger2;
        this.f233p = bigInteger;
        this.f232l = i;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ElGamalParameters)) {
            return false;
        }
        ElGamalParameters elGamalParameters = (ElGamalParameters) obj;
        return elGamalParameters.getP().equals(this.f233p) && elGamalParameters.getG().equals(this.f231g) && elGamalParameters.getL() == this.f232l;
    }

    public BigInteger getG() {
        return this.f231g;
    }

    public int getL() {
        return this.f232l;
    }

    public BigInteger getP() {
        return this.f233p;
    }

    public int hashCode() {
        return (getP().hashCode() ^ getG().hashCode()) + this.f232l;
    }
}
