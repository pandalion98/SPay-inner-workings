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
import org.bouncycastle.crypto.params.CramerShoupParameters;

public class CramerShoupKeyGenerationParameters
extends KeyGenerationParameters {
    private CramerShoupParameters params;

    public CramerShoupKeyGenerationParameters(SecureRandom secureRandom, CramerShoupParameters cramerShoupParameters) {
        super(secureRandom, CramerShoupKeyGenerationParameters.getStrength(cramerShoupParameters));
        this.params = cramerShoupParameters;
    }

    static int getStrength(CramerShoupParameters cramerShoupParameters) {
        return cramerShoupParameters.getP().bitLength();
    }

    public CramerShoupParameters getParameters() {
        return this.params;
    }
}

