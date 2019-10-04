/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.security.SecureRandom
 */
package org.bouncycastle.pqc.crypto.gmss;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.pqc.crypto.gmss.GMSSParameters;

public class GMSSKeyGenerationParameters
extends KeyGenerationParameters {
    private GMSSParameters params;

    public GMSSKeyGenerationParameters(SecureRandom secureRandom, GMSSParameters gMSSParameters) {
        super(secureRandom, 1);
        this.params = gMSSParameters;
    }

    public GMSSParameters getParameters() {
        return this.params;
    }
}

