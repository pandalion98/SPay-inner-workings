/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.params.DHKeyParameters;
import org.bouncycastle.crypto.params.DHParameters;

public class DHPublicKeyParameters
extends DHKeyParameters {
    private BigInteger y;

    public DHPublicKeyParameters(BigInteger bigInteger, DHParameters dHParameters) {
        super(false, dHParameters);
        this.y = bigInteger;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DHPublicKeyParameters)) {
            return false;
        }
        if (!((DHPublicKeyParameters)object).getY().equals((Object)this.y)) return false;
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

