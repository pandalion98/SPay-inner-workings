package org.bouncycastle.crypto.params;

import java.math.BigInteger;

public class CramerShoupPublicKeyParameters extends CramerShoupKeyParameters {
    private BigInteger f208c;
    private BigInteger f209d;
    private BigInteger f210h;

    public CramerShoupPublicKeyParameters(CramerShoupParameters cramerShoupParameters, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        super(false, cramerShoupParameters);
        this.f208c = bigInteger;
        this.f209d = bigInteger2;
        this.f210h = bigInteger3;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof CramerShoupPublicKeyParameters)) {
            return false;
        }
        CramerShoupPublicKeyParameters cramerShoupPublicKeyParameters = (CramerShoupPublicKeyParameters) obj;
        boolean z = cramerShoupPublicKeyParameters.getC().equals(this.f208c) && cramerShoupPublicKeyParameters.getD().equals(this.f209d) && cramerShoupPublicKeyParameters.getH().equals(this.f210h) && super.equals(obj);
        return z;
    }

    public BigInteger getC() {
        return this.f208c;
    }

    public BigInteger getD() {
        return this.f209d;
    }

    public BigInteger getH() {
        return this.f210h;
    }

    public int hashCode() {
        return ((this.f208c.hashCode() ^ this.f209d.hashCode()) ^ this.f210h.hashCode()) ^ super.hashCode();
    }
}
