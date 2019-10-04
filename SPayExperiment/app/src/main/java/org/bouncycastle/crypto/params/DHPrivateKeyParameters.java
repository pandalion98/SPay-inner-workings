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

public class DHPrivateKeyParameters
extends DHKeyParameters {
    private BigInteger x;

    public DHPrivateKeyParameters(BigInteger bigInteger, DHParameters dHParameters) {
        super(true, dHParameters);
        this.x = bigInteger;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DHPrivateKeyParameters)) {
            return false;
        }
        if (!((DHPrivateKeyParameters)object).getX().equals((Object)this.x)) return false;
        if (!super.equals(object)) return false;
        return true;
    }

    public BigInteger getX() {
        return this.x;
    }

    @Override
    public int hashCode() {
        return this.x.hashCode() ^ super.hashCode();
    }
}

