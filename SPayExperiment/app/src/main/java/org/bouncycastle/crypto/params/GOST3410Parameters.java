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
import org.bouncycastle.crypto.params.GOST3410ValidationParameters;

public class GOST3410Parameters
implements CipherParameters {
    private BigInteger a;
    private BigInteger p;
    private BigInteger q;
    private GOST3410ValidationParameters validation;

    public GOST3410Parameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.p = bigInteger;
        this.q = bigInteger2;
        this.a = bigInteger3;
    }

    public GOST3410Parameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, GOST3410ValidationParameters gOST3410ValidationParameters) {
        this.a = bigInteger3;
        this.p = bigInteger;
        this.q = bigInteger2;
        this.validation = gOST3410ValidationParameters;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        GOST3410Parameters gOST3410Parameters;
        return object instanceof GOST3410Parameters && (gOST3410Parameters = (GOST3410Parameters)object).getP().equals((Object)this.p) && gOST3410Parameters.getQ().equals((Object)this.q) && gOST3410Parameters.getA().equals((Object)this.a);
    }

    public BigInteger getA() {
        return this.a;
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getQ() {
        return this.q;
    }

    public GOST3410ValidationParameters getValidationParameters() {
        return this.validation;
    }

    public int hashCode() {
        return this.p.hashCode() ^ this.q.hashCode() ^ this.a.hashCode();
    }
}

