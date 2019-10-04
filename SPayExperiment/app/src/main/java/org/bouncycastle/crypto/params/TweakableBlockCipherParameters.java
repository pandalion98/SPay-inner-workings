/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;

public class TweakableBlockCipherParameters
implements CipherParameters {
    private final KeyParameter key;
    private final byte[] tweak;

    public TweakableBlockCipherParameters(KeyParameter keyParameter, byte[] arrby) {
        this.key = keyParameter;
        this.tweak = Arrays.clone((byte[])arrby);
    }

    public KeyParameter getKey() {
        return this.key;
    }

    public byte[] getTweak() {
        return this.tweak;
    }
}

