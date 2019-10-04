/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.params;

import java.security.SecureRandom;
import org.bouncycastle.crypto.KeyGenerationParameters;

public class NaccacheSternKeyGenerationParameters
extends KeyGenerationParameters {
    private int certainty;
    private int cntSmallPrimes;
    private boolean debug = false;

    public NaccacheSternKeyGenerationParameters(SecureRandom secureRandom, int n2, int n3, int n4) {
        this(secureRandom, n2, n3, n4, false);
    }

    public NaccacheSternKeyGenerationParameters(SecureRandom secureRandom, int n2, int n3, int n4, boolean bl) {
        super(secureRandom, n2);
        this.certainty = n3;
        if (n4 % 2 == 1) {
            throw new IllegalArgumentException("cntSmallPrimes must be a multiple of 2");
        }
        if (n4 < 30) {
            throw new IllegalArgumentException("cntSmallPrimes must be >= 30 for security reasons");
        }
        this.cntSmallPrimes = n4;
        this.debug = bl;
    }

    public int getCertainty() {
        return this.certainty;
    }

    public int getCntSmallPrimes() {
        return this.cntSmallPrimes;
    }

    public boolean isDebug() {
        return this.debug;
    }
}

