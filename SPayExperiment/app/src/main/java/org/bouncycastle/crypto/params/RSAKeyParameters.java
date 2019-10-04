/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class RSAKeyParameters
extends AsymmetricKeyParameter {
    private BigInteger exponent;
    private BigInteger modulus;

    public RSAKeyParameters(boolean bl, BigInteger bigInteger, BigInteger bigInteger2) {
        super(bl);
        this.modulus = bigInteger;
        this.exponent = bigInteger2;
    }

    public BigInteger getExponent() {
        return this.exponent;
    }

    public BigInteger getModulus() {
        return this.modulus;
    }
}

