/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;

public class ElGamalParameters
implements CipherParameters {
    private BigInteger g;
    private int l;
    private BigInteger p;

    public ElGamalParameters(BigInteger bigInteger, BigInteger bigInteger2) {
        this(bigInteger, bigInteger2, 0);
    }

    public ElGamalParameters(BigInteger bigInteger, BigInteger bigInteger2, int n2) {
        this.g = bigInteger2;
        this.p = bigInteger;
        this.l = n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        ElGamalParameters elGamalParameters;
        return object instanceof ElGamalParameters && (elGamalParameters = (ElGamalParameters)object).getP().equals((Object)this.p) && elGamalParameters.getG().equals((Object)this.g) && elGamalParameters.getL() == this.l;
    }

    public BigInteger getG() {
        return this.g;
    }

    public int getL() {
        return this.l;
    }

    public BigInteger getP() {
        return this.p;
    }

    public int hashCode() {
        return (this.getP().hashCode() ^ this.getG().hashCode()) + this.l;
    }
}

