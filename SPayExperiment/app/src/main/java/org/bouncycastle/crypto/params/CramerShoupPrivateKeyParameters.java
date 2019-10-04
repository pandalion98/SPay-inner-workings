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
import org.bouncycastle.crypto.params.CramerShoupPublicKeyParameters;

public class CramerShoupPrivateKeyParameters
extends CramerShoupKeyParameters {
    private CramerShoupPublicKeyParameters pk;
    private BigInteger x1;
    private BigInteger x2;
    private BigInteger y1;
    private BigInteger y2;
    private BigInteger z;

    public CramerShoupPrivateKeyParameters(CramerShoupParameters cramerShoupParameters, BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5) {
        super(true, cramerShoupParameters);
        this.x1 = bigInteger;
        this.x2 = bigInteger2;
        this.y1 = bigInteger3;
        this.y2 = bigInteger4;
        this.z = bigInteger5;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CramerShoupPrivateKeyParameters)) {
            return false;
        }
        CramerShoupPrivateKeyParameters cramerShoupPrivateKeyParameters = (CramerShoupPrivateKeyParameters)object;
        if (!cramerShoupPrivateKeyParameters.getX1().equals((Object)this.x1)) return false;
        if (!cramerShoupPrivateKeyParameters.getX2().equals((Object)this.x2)) return false;
        if (!cramerShoupPrivateKeyParameters.getY1().equals((Object)this.y1)) return false;
        if (!cramerShoupPrivateKeyParameters.getY2().equals((Object)this.y2)) return false;
        if (!cramerShoupPrivateKeyParameters.getZ().equals((Object)this.z)) return false;
        if (!super.equals(object)) return false;
        return true;
    }

    public CramerShoupPublicKeyParameters getPk() {
        return this.pk;
    }

    public BigInteger getX1() {
        return this.x1;
    }

    public BigInteger getX2() {
        return this.x2;
    }

    public BigInteger getY1() {
        return this.y1;
    }

    public BigInteger getY2() {
        return this.y2;
    }

    public BigInteger getZ() {
        return this.z;
    }

    @Override
    public int hashCode() {
        return this.x1.hashCode() ^ this.x2.hashCode() ^ this.y1.hashCode() ^ this.y2.hashCode() ^ this.z.hashCode() ^ super.hashCode();
    }

    public void setPk(CramerShoupPublicKeyParameters cramerShoupPublicKeyParameters) {
        this.pk = cramerShoupPublicKeyParameters;
    }
}

