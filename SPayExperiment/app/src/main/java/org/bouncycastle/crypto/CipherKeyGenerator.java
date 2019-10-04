/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class CipherKeyGenerator {
    protected SecureRandom random;
    protected int strength;

    public byte[] generateKey() {
        byte[] arrby = new byte[this.strength];
        this.random.nextBytes(arrby);
        return arrby;
    }

    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.random = keyGenerationParameters.getRandom();
        this.strength = (7 + keyGenerationParameters.getStrength()) / 8;
    }
}

