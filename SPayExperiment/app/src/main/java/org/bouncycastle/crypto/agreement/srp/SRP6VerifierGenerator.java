/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.agreement.srp;

import java.math.BigInteger;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.agreement.srp.SRP6Util;
import org.bouncycastle.crypto.params.SRP6GroupParameters;

public class SRP6VerifierGenerator {
    protected BigInteger N;
    protected Digest digest;
    protected BigInteger g;

    public BigInteger generateVerifier(byte[] arrby, byte[] arrby2, byte[] arrby3) {
        BigInteger bigInteger = SRP6Util.calculateX(this.digest, this.N, arrby, arrby2, arrby3);
        return this.g.modPow(bigInteger, this.N);
    }

    public void init(BigInteger bigInteger, BigInteger bigInteger2, Digest digest) {
        this.N = bigInteger;
        this.g = bigInteger2;
        this.digest = digest;
    }

    public void init(SRP6GroupParameters sRP6GroupParameters, Digest digest) {
        this.N = sRP6GroupParameters.getN();
        this.g = sRP6GroupParameters.getG();
        this.digest = digest;
    }
}

