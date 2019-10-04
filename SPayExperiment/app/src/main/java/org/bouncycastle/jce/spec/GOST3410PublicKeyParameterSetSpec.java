/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.jce.spec;

import java.math.BigInteger;

public class GOST3410PublicKeyParameterSetSpec {
    private BigInteger a;
    private BigInteger p;
    private BigInteger q;

    public GOST3410PublicKeyParameterSetSpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.p = bigInteger;
        this.q = bigInteger2;
        this.a = bigInteger3;
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof GOST3410PublicKeyParameterSetSpec;
        boolean bl2 = false;
        if (bl) {
            GOST3410PublicKeyParameterSetSpec gOST3410PublicKeyParameterSetSpec = (GOST3410PublicKeyParameterSetSpec)object;
            boolean bl3 = this.a.equals((Object)gOST3410PublicKeyParameterSetSpec.a);
            bl2 = false;
            if (bl3) {
                boolean bl4 = this.p.equals((Object)gOST3410PublicKeyParameterSetSpec.p);
                bl2 = false;
                if (bl4) {
                    boolean bl5 = this.q.equals((Object)gOST3410PublicKeyParameterSetSpec.q);
                    bl2 = false;
                    if (bl5) {
                        bl2 = true;
                    }
                }
            }
        }
        return bl2;
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

    public int hashCode() {
        return this.a.hashCode() ^ this.p.hashCode() ^ this.q.hashCode();
    }
}

