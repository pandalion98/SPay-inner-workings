package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;

public class GOST3410Parameters implements CipherParameters {
    private BigInteger f236a;
    private BigInteger f237p;
    private BigInteger f238q;
    private GOST3410ValidationParameters validation;

    public GOST3410Parameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f237p = bigInteger;
        this.f238q = bigInteger2;
        this.f236a = bigInteger3;
    }

    public GOST3410Parameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, GOST3410ValidationParameters gOST3410ValidationParameters) {
        this.f236a = bigInteger3;
        this.f237p = bigInteger;
        this.f238q = bigInteger2;
        this.validation = gOST3410ValidationParameters;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GOST3410Parameters)) {
            return false;
        }
        GOST3410Parameters gOST3410Parameters = (GOST3410Parameters) obj;
        return gOST3410Parameters.getP().equals(this.f237p) && gOST3410Parameters.getQ().equals(this.f238q) && gOST3410Parameters.getA().equals(this.f236a);
    }

    public BigInteger getA() {
        return this.f236a;
    }

    public BigInteger getP() {
        return this.f237p;
    }

    public BigInteger getQ() {
        return this.f238q;
    }

    public GOST3410ValidationParameters getValidationParameters() {
        return this.validation;
    }

    public int hashCode() {
        return (this.f237p.hashCode() ^ this.f238q.hashCode()) ^ this.f236a.hashCode();
    }
}
