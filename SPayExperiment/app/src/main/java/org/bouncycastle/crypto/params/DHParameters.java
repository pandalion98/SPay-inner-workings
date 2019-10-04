/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.DHValidationParameters;

public class DHParameters
implements CipherParameters {
    private static final int DEFAULT_MINIMUM_LENGTH = 160;
    private BigInteger g;
    private BigInteger j;
    private int l;
    private int m;
    private BigInteger p;
    private BigInteger q;
    private DHValidationParameters validation;

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2) {
        this(bigInteger, bigInteger2, null, 0);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this(bigInteger, bigInteger2, bigInteger3, 0);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int n2) {
        this(bigInteger, bigInteger2, bigInteger3, DHParameters.getDefaultMParam(n2), n2, null, null);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int n2, int n3) {
        this(bigInteger, bigInteger2, bigInteger3, n2, n3, null, null);
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, int n2, int n3, BigInteger bigInteger4, DHValidationParameters dHValidationParameters) {
        if (n3 != 0) {
            if (n3 > bigInteger.bitLength()) {
                throw new IllegalArgumentException("when l value specified, it must satisfy 2^(l-1) <= p");
            }
            if (n3 < n2) {
                throw new IllegalArgumentException("when l value specified, it may not be less than m value");
            }
        }
        this.g = bigInteger2;
        this.p = bigInteger;
        this.q = bigInteger3;
        this.m = n2;
        this.l = n3;
        this.j = bigInteger4;
        this.validation = dHValidationParameters;
    }

    public DHParameters(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, DHValidationParameters dHValidationParameters) {
        this(bigInteger, bigInteger2, bigInteger3, 160, 0, bigInteger4, dHValidationParameters);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static int getDefaultMParam(int n2) {
        if (n2 == 0) {
            return 160;
        }
        if (n2 >= 160) return 160;
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block7 : {
            block4 : {
                DHParameters dHParameters;
                block6 : {
                    block5 : {
                        if (!(object instanceof DHParameters)) break block4;
                        dHParameters = (DHParameters)object;
                        if (this.getQ() == null) break block5;
                        if (!this.getQ().equals((Object)dHParameters.getQ())) break block4;
                        break block6;
                    }
                    if (dHParameters.getQ() != null) {
                        return false;
                    }
                }
                if (dHParameters.getP().equals((Object)this.p) && dHParameters.getG().equals((Object)this.g)) break block7;
            }
            return false;
        }
        return true;
    }

    public BigInteger getG() {
        return this.g;
    }

    public BigInteger getJ() {
        return this.j;
    }

    public int getL() {
        return this.l;
    }

    public int getM() {
        return this.m;
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getQ() {
        return this.q;
    }

    public DHValidationParameters getValidationParameters() {
        return this.validation;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int hashCode() {
        int n2;
        int n3 = this.getP().hashCode() ^ this.getG().hashCode();
        if (this.getQ() != null) {
            n2 = this.getQ().hashCode();
            do {
                return n2 ^ n3;
                break;
            } while (true);
        }
        n2 = 0;
        return n2 ^ n3;
    }
}

