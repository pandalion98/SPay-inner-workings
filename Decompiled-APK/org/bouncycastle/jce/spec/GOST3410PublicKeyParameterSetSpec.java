package org.bouncycastle.jce.spec;

import java.math.BigInteger;

public class GOST3410PublicKeyParameterSetSpec {
    private BigInteger f316a;
    private BigInteger f317p;
    private BigInteger f318q;

    public GOST3410PublicKeyParameterSetSpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f317p = bigInteger;
        this.f318q = bigInteger2;
        this.f316a = bigInteger3;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GOST3410PublicKeyParameterSetSpec)) {
            return false;
        }
        GOST3410PublicKeyParameterSetSpec gOST3410PublicKeyParameterSetSpec = (GOST3410PublicKeyParameterSetSpec) obj;
        return this.f316a.equals(gOST3410PublicKeyParameterSetSpec.f316a) && this.f317p.equals(gOST3410PublicKeyParameterSetSpec.f317p) && this.f318q.equals(gOST3410PublicKeyParameterSetSpec.f318q);
    }

    public BigInteger getA() {
        return this.f316a;
    }

    public BigInteger getP() {
        return this.f317p;
    }

    public BigInteger getQ() {
        return this.f318q;
    }

    public int hashCode() {
        return (this.f316a.hashCode() ^ this.f317p.hashCode()) ^ this.f318q.hashCode();
    }
}
