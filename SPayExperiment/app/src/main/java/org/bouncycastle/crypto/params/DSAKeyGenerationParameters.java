/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.math.BigInteger
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.params;

import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.DSAParameters;

public class DSAKeyGenerationParameters
extends KeyGenerationParameters {
    private DSAParameters params;

    public DSAKeyGenerationParameters(SecureRandom secureRandom, DSAParameters dSAParameters) {
        super(secureRandom, -1 + dSAParameters.getP().bitLength());
        this.params = dSAParameters;
    }

    public DSAParameters getParameters() {
        return this.params;
    }
}

