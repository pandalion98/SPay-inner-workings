/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.params.CramerShoupKeyParameters;
import org.bouncycastle.crypto.params.CramerShoupParameters;

public class CramerShoupPublicKeyParameters
extends CramerShoupKeyParameters {
    private BigInteger c;
    private BigInteger d;
    private BigInteger h;

    public CramerShoupPublicKeyParameters(CramerShoupParameters cramerShoupParameters, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        super(false, cramerShoupParameters);
        this.c = bigInteger;
        this.d = bigInteger2;
        this.h = bigInteger3;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CramerShoupPublicKeyParameters)) {
            return false;
        }
        CramerShoupPublicKeyParameters cramerShoupPublicKeyParameters = (CramerShoupPublicKeyParameters)object;
        if (!cramerShoupPublicKeyParameters.getC().equals((Object)this.c)) return false;
        if (!cramerShoupPublicKeyParameters.getD().equals((Object)this.d)) return false;
        if (!cramerShoupPublicKeyParameters.getH().equals((Object)this.h)) return false;
        if (!super.equals(object)) return false;
        return true;
    }

    public BigInteger getC() {
        return this.c;
    }

    public BigInteger getD() {
        return this.d;
    }

    public BigInteger getH() {
        return this.h;
    }

    @Override
    public int hashCode() {
        return this.c.hashCode() ^ this.d.hashCode() ^ this.h.hashCode() ^ super.hashCode();
    }
}

