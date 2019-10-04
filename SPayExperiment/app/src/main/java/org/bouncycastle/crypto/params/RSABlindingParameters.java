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
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.params.RSAPrivateCrtKeyParameters;

public class RSABlindingParameters
implements CipherParameters {
    private BigInteger blindingFactor;
    private RSAKeyParameters publicKey;

    public RSABlindingParameters(RSAKeyParameters rSAKeyParameters, BigInteger bigInteger) {
        if (rSAKeyParameters instanceof RSAPrivateCrtKeyParameters) {
            throw new IllegalArgumentException("RSA parameters should be for a public key");
        }
        this.publicKey = rSAKeyParameters;
        this.blindingFactor = bigInteger;
    }

    public BigInteger getBlindingFactor() {
        return this.blindingFactor;
    }

    public RSAKeyParameters getPublicKey() {
        return this.publicKey;
    }
}

