/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto;

import java.security.SecureRandom;

public class KeyGenerationParameters {
    private SecureRandom random;
    private int strength;

    public KeyGenerationParameters(SecureRandom secureRandom, int n2) {
        this.random = secureRandom;
        this.strength = n2;
    }

    public SecureRandom getRandom() {
        return this.random;
    }

    public int getStrength() {
        return this.strength;
    }
}

