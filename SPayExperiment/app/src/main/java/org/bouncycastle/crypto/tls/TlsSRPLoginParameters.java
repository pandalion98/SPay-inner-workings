/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.tls;

import java.math.BigInteger;
import org.bouncycastle.crypto.params.SRP6GroupParameters;

public class TlsSRPLoginParameters {
    protected SRP6GroupParameters group;
    protected byte[] salt;
    protected BigInteger verifier;

    public TlsSRPLoginParameters(SRP6GroupParameters sRP6GroupParameters, BigInteger bigInteger, byte[] arrby) {
        this.group = sRP6GroupParameters;
        this.verifier = bigInteger;
        this.salt = arrby;
    }

    public SRP6GroupParameters getGroup() {
        return this.group;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public BigInteger getVerifier() {
        return this.verifier;
    }
}

