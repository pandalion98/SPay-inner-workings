/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.params.ElGamalKeyParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;

public class ElGamalPublicKeyParameters
extends ElGamalKeyParameters {
    private BigInteger y;

    public ElGamalPublicKeyParameters(BigInteger bigInteger, ElGamalParameters elGamalParameters) {
        super(false, elGamalParameters);
        this.y = bigInteger;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ElGamalPublicKeyParameters)) {
            return false;
        }
        if (!((ElGamalPublicKeyParameters)object).getY().equals((Object)this.y)) return false;
        if (!super.equals(object)) return false;
        return true;
    }

    public BigInteger getY() {
        return this.y;
    }

    @Override
    public int hashCode() {
        return this.y.hashCode() ^ super.hashCode();
    }
}

