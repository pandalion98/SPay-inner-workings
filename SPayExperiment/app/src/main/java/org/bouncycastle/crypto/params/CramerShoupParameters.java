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
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.DSAParameters;

public class CramerShoupParameters
implements CipherParameters {
    private Digest H;
    private BigInteger g1;
    private BigInteger g2;
    private BigInteger p;

    public CramerShoupParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, Digest digest) {
        this.p = bigInteger;
        this.g1 = bigInteger2;
        this.g2 = bigInteger3;
        this.H = digest;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        CramerShoupParameters cramerShoupParameters;
        return object instanceof DSAParameters && (cramerShoupParameters = (CramerShoupParameters)object).getP().equals((Object)this.p) && cramerShoupParameters.getG1().equals((Object)this.g1) && cramerShoupParameters.getG2().equals((Object)this.g2);
    }

    public BigInteger getG1() {
        return this.g1;
    }

    public BigInteger getG2() {
        return this.g2;
    }

    public Digest getH() {
        this.H.reset();
        return this.H;
    }

    public BigInteger getP() {
        return this.p;
    }

    public int hashCode() {
        return this.getP().hashCode() ^ this.getG1().hashCode() ^ this.getG2().hashCode();
    }
}

