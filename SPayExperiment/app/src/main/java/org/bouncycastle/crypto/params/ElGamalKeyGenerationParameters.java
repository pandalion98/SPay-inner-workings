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
import org.bouncycastle.crypto.params.ElGamalParameters;

public class ElGamalKeyGenerationParameters
extends KeyGenerationParameters {
    private ElGamalParameters params;

    public ElGamalKeyGenerationParameters(SecureRandom secureRandom, ElGamalParameters elGamalParameters) {
        super(secureRandom, ElGamalKeyGenerationParameters.getStrength(elGamalParameters));
        this.params = elGamalParameters;
    }

    static int getStrength(ElGamalParameters elGamalParameters) {
        if (elGamalParameters.getL() != 0) {
            return elGamalParameters.getL();
        }
        return elGamalParameters.getP().bitLength();
    }

    public ElGamalParameters getParameters() {
        return this.params;
    }
}

