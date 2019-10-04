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
import org.bouncycastle.crypto.params.DHParameters;

public class DHKeyGenerationParameters
extends KeyGenerationParameters {
    private DHParameters params;

    public DHKeyGenerationParameters(SecureRandom secureRandom, DHParameters dHParameters) {
        super(secureRandom, DHKeyGenerationParameters.getStrength(dHParameters));
        this.params = dHParameters;
    }

    static int getStrength(DHParameters dHParameters) {
        if (dHParameters.getL() != 0) {
            return dHParameters.getL();
        }
        return dHParameters.getP().bitLength();
    }

    public DHParameters getParameters() {
        return this.params;
    }
}

