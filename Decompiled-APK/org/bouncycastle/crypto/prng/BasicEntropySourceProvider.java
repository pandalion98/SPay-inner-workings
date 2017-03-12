package org.bouncycastle.crypto.prng;

import java.security.SecureRandom;

public class BasicEntropySourceProvider implements EntropySourceProvider {
    private final boolean _predictionResistant;
    private final SecureRandom _sr;

    /* renamed from: org.bouncycastle.crypto.prng.BasicEntropySourceProvider.1 */
    class C07471 implements EntropySource {
        final /* synthetic */ int val$bitsRequired;

        C07471(int i) {
            this.val$bitsRequired = i;
        }

        public int entropySize() {
            return this.val$bitsRequired;
        }

        public byte[] getEntropy() {
            return BasicEntropySourceProvider.this._sr.generateSeed((this.val$bitsRequired + 7) / 8);
        }

        public boolean isPredictionResistant() {
            return BasicEntropySourceProvider.this._predictionResistant;
        }
    }

    public BasicEntropySourceProvider(SecureRandom secureRandom, boolean z) {
        this._sr = secureRandom;
        this._predictionResistant = z;
    }

    public EntropySource get(int i) {
        return new C07471(i);
    }
}
