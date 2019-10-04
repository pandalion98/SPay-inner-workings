/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  java.security.SecureRandom
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.prng;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.prng.BasicEntropySourceProvider;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;
import org.bouncycastle.crypto.prng.X931RNG;
import org.bouncycastle.crypto.prng.X931SecureRandom;
import org.bouncycastle.util.Pack;

public class X931SecureRandomBuilder {
    private byte[] dateTimeVector;
    private BlockCipher engine;
    private EntropySourceProvider entropySourceProvider;
    private SecureRandom random;

    public X931SecureRandomBuilder() {
        this(new SecureRandom(), false);
    }

    public X931SecureRandomBuilder(SecureRandom secureRandom, boolean bl) {
        this.random = secureRandom;
        this.entropySourceProvider = new BasicEntropySourceProvider(this.random, bl);
    }

    public X931SecureRandomBuilder(EntropySourceProvider entropySourceProvider) {
        this.random = null;
        this.entropySourceProvider = entropySourceProvider;
    }

    /*
     * Enabled aggressive block sorting
     */
    public X931SecureRandom build(BlockCipher blockCipher, KeyParameter keyParameter, boolean bl) {
        this.engine = blockCipher;
        if (this.dateTimeVector == null) {
            if (blockCipher.getBlockSize() == 8) {
                this.dateTimeVector = Pack.longToBigEndian((long)System.currentTimeMillis());
            } else {
                this.dateTimeVector = new byte[blockCipher.getBlockSize()];
                byte[] arrby = Pack.longToBigEndian((long)System.currentTimeMillis());
                System.arraycopy((Object)arrby, (int)0, (Object)this.dateTimeVector, (int)0, (int)arrby.length);
            }
        }
        blockCipher.init(true, keyParameter);
        return new X931SecureRandom(this.random, new X931RNG(blockCipher, this.dateTimeVector, this.entropySourceProvider.get(8 * blockCipher.getBlockSize())), bl);
    }

    public X931SecureRandomBuilder setDateTimeVector(byte[] arrby) {
        this.dateTimeVector = arrby;
        return this;
    }
}

