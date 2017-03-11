package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;

public class DHParameters implements CipherParameters {
    private static final int DEFAULT_MINIMUM_LENGTH = 160;
    private BigInteger f211g;
    private BigInteger f212j;
    private int f213l;
    private int f214m;
    private BigInteger f215p;
    private BigInteger f216q;
    private DHValidationParameters validation;

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2) {
        this(bigInteger, bigInteger2, null, 0);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this(bigInteger, bigInteger2, bigInteger3, 0);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int i) {
        this(bigInteger, bigInteger2, bigInteger3, getDefaultMParam(i), i, null, null);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int i, int i2) {
        this(bigInteger, bigInteger2, bigInteger3, i, i2, null, null);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int i, int i2, BigInteger bigInteger4, DHValidationParameters dHValidationParameters) {
        if (i2 != 0) {
            if (i2 > bigInteger.bitLength()) {
                throw new IllegalArgumentException("when l value specified, it must satisfy 2^(l-1) <= p");
            } else if (i2 < i) {
                throw new IllegalArgumentException("when l value specified, it may not be less than m value");
            }
        }
        this.f211g = bigInteger2;
        this.f215p = bigInteger;
        this.f216q = bigInteger3;
        this.f214m = i;
        this.f213l = i2;
        this.f212j = bigInteger4;
        this.validation = dHValidationParameters;
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, DHValidationParameters dHValidationParameters) {
        this(bigInteger, bigInteger2, bigInteger3, DEFAULT_MINIMUM_LENGTH, 0, bigInteger4, dHValidationParameters);
    }

    private static int getDefaultMParam(int i) {
        if (i == 0) {
            return DEFAULT_MINIMUM_LENGTH;
        }
        if (i >= DEFAULT_MINIMUM_LENGTH) {
            i = DEFAULT_MINIMUM_LENGTH;
        }
        return i;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DHParameters)) {
            return false;
        }
        DHParameters dHParameters = (DHParameters) obj;
        if (getQ() != null) {
            if (!getQ().equals(dHParameters.getQ())) {
                return false;
            }
        } else if (dHParameters.getQ() != null) {
            return false;
        }
        return dHParameters.getP().equals(this.f215p) && dHParameters.getG().equals(this.f211g);
    }

    public BigInteger getG() {
        return this.f211g;
    }

    public BigInteger getJ() {
        return this.f212j;
    }

    public int getL() {
        return this.f213l;
    }

    public int getM() {
        return this.f214m;
    }

    public BigInteger getP() {
        return this.f215p;
    }

    public BigInteger getQ() {
        return this.f216q;
    }

    public DHValidationParameters getValidationParameters() {
        return this.validation;
    }

    public int hashCode() {
        return (getQ() != null ? getQ().hashCode() : 0) ^ (getG().hashCode() ^ getP().hashCode());
    }
}
