/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;

public interface AsymmetricCipherKeyPairGenerator {
    public AsymmetricCipherKeyPair generateKeyPair();

    public void init(KeyGenerationParameters var1);
}

