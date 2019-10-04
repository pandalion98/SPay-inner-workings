/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.params;

import java.security.SecureRandom;

public class DSAParameterGenerationParameters {
    public static final int DIGITAL_SIGNATURE_USAGE = 1;
    public static final int KEY_ESTABLISHMENT_USAGE = 2;
    private final int certainty;
    private final int l;
    private final int n;
    private final SecureRandom random;
    private final int usageIndex;

    public DSAParameterGenerationParameters(int n2, int n3, int n4, SecureRandom secureRandom) {
        this(n2, n3, n4, secureRandom, -1);
    }

    public DSAParameterGenerationParameters(int n2, int n3, int n4, SecureRandom secureRandom, int n5) {
        this.l = n2;
        this.n = n3;
        this.certainty = n4;
        this.usageIndex = n5;
        this.random = secureRandom;
    }

    public int getCertainty() {
        return this.certainty;
    }

    public int getL() {
        return this.l;
    }

    public int getN() {
        return this.n;
    }

    public SecureRandom getRandom() {
        return this.random;
    }

    public int getUsageIndex() {
        return this.usageIndex;
    }
}

