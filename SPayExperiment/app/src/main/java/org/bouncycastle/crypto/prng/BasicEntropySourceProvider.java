/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.prng;

import java.security.SecureRandom;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;

public class BasicEntropySourceProvider
implements EntropySourceProvider {
    private final boolean _predictionResistant;
    private final SecureRandom _sr;

    public BasicEntropySourceProvider(SecureRandom secureRandom, boolean bl) {
        this._sr = secureRandom;
        this._predictionResistant = bl;
    }

    @Override
    public EntropySource get(final int n2) {
        return new EntropySource(){

            @Override
            public int entropySize() {
                return n2;
            }

            @Override
            public byte[] getEntropy() {
                return BasicEntropySourceProvider.this._sr.generateSeed((7 + n2) / 8);
            }

            @Override
            public boolean isPredictionResistant() {
                return BasicEntropySourceProvider.this._predictionResistant;
            }
        };
    }

}

