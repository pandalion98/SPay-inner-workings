/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.crypto.rainbow;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.rainbow.RainbowParameters;

public class RainbowKeyGenerationParameters
extends KeyGenerationParameters {
    private RainbowParameters params;

    public RainbowKeyGenerationParameters(SecureRandom secureRandom, RainbowParameters rainbowParameters) {
        super(secureRandom, rainbowParameters.getVi()[-1 + rainbowParameters.getVi().length] - rainbowParameters.getVi()[0]);
        this.params = rainbowParameters;
    }

    public RainbowParameters getParameters() {
        return this.params;
    }
}

