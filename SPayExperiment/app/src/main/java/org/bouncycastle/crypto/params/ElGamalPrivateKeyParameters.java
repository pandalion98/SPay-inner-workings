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

public class ElGamalPrivateKeyParameters
extends ElGamalKeyParameters {
    private BigInteger x;

    public ElGamalPrivateKeyParameters(BigInteger bigInteger, ElGamalParameters elGamalParameters) {
        super(true, elGamalParameters);
        this.x = bigInteger;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ElGamalPrivateKeyParameters)) {
            return false;
        }
        if (!((ElGamalPrivateKeyParameters)object).getX().equals((Object)this.x)) {
            return false;
        }
        return super.equals(object);
    }

    public BigInteger getX() {
        return this.x;
    }

    @Override
    public int hashCode() {
        return this.getX().hashCode();
    }
}

