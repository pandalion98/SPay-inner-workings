package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;

public class DSAParameters implements CipherParameters {
    private BigInteger f221g;
    private BigInteger f222p;
    private BigInteger f223q;
    private DSAValidationParameters validation;

    public DSAParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f221g = bigInteger3;
        this.f222p = bigInteger;
        this.f223q = bigInteger2;
    }

    public DSAParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, DSAValidationParameters dSAValidationParameters) {
        this.f221g = bigInteger3;
        this.f222p = bigInteger;
        this.f223q = bigInteger2;
        this.validation = dSAValidationParameters;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DSAParameters)) {
            return false;
        }
        DSAParameters dSAParameters = (DSAParameters) obj;
        return dSAParameters.getP().equals(this.f222p) && dSAParameters.getQ().equals(this.f223q) && dSAParameters.getG().equals(this.f221g);
    }

    public BigInteger getG() {
        return this.f221g;
    }

    public BigInteger getP() {
        return this.f222p;
    }

    public BigInteger getQ() {
        return this.f223q;
    }

    public DSAValidationParameters getValidationParameters() {
        return this.validation;
    }

    public int hashCode() {
        return (getP().hashCode() ^ getQ().hashCode()) ^ getG().hashCode();
    }
}
