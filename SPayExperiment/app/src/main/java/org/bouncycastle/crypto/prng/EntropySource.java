/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.prng;

public interface EntropySource {
    public int entropySize();

    public byte[] getEntropy();

    public boolean isPredictionResistant();
}

