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
import org.bouncycastle.crypto.params.DSAValidationParameters;

public class DSAParameters
implements CipherParameters {
    private BigInteger g;
    private BigInteger p;
    private BigInteger q;
    private DSAValidationParameters validation;

    public DSAParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.g = bigInteger3;
        this.p = bigInteger;
        this.q = bigInteger2;
    }

    public DSAParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, DSAValidationParameters dSAValidationParameters) {
        this.g = bigInteger3;
        this.p = bigInteger;
        this.q = bigInteger2;
        this.validation = dSAValidationParameters;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        DSAParameters dSAParameters;
        return object instanceof DSAParameters && (dSAParameters = (DSAParameters)object).getP().equals((Object)this.p) && dSAParameters.getQ().equals((Object)this.q) && dSAParameters.getG().equals((Object)this.g);
    }

    public BigInteger getG() {
        return this.g;
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getQ() {
        return this.q;
    }

    public DSAValidationParameters getValidationParameters() {
        return this.validation;
    }

    public int hashCode() {
        return this.getP().hashCode() ^ this.getQ().hashCode() ^ this.getG().hashCode();
    }
}

