package org.bouncycastle.crypto.prng;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Pack;

public class X931SecureRandomBuilder {
    private byte[] dateTimeVector;
    private BlockCipher engine;
    private EntropySourceProvider entropySourceProvider;
    private SecureRandom random;

    public X931SecureRandomBuilder() {
        this(new SecureRandom(), false);
    }

    public X931SecureRandomBuilder(SecureRandom secureRandom, boolean z) {
        this.random = secureRandom;
        this.entropySourceProvider = new BasicEntropySourceProvider(this.random, z);
    }

    public X931SecureRandomBuilder(EntropySourceProvider entropySourceProvider) {
        this.random = null;
        this.entropySourceProvider = entropySourceProvider;
    }

    public X931SecureRandom build(BlockCipher blockCipher, KeyParameter keyParameter, boolean z) {
        this.engine = blockCipher;
        if (this.dateTimeVector == null) {
            if (blockCipher.getBlockSize() == 8) {
                this.dateTimeVector = Pack.longToBigEndian(System.currentTimeMillis());
            } else {
                this.dateTimeVector = new byte[blockCipher.getBlockSize()];
                Object longToBigEndian = Pack.longToBigEndian(System.currentTimeMillis());
                System.arraycopy(longToBigEndian, 0, this.dateTimeVector, 0, longToBigEndian.length);
            }
        }
        blockCipher.init(true, keyParameter);
        return new X931SecureRandom(this.random, new X931RNG(blockCipher, this.dateTimeVector, this.entropySourceProvider.get(blockCipher.getBlockSize() * 8)), z);
    }

    public X931SecureRandomBuilder setDateTimeVector(byte[] bArr) {
        this.dateTimeVector = bArr;
        return this;
    }
}
