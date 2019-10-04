/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.prng;

public interface RandomGenerator {
    public void addSeedMaterial(long var1);

    public void addSeedMaterial(byte[] var1);

    public void nextBytes(byte[] var1);

    public void nextBytes(byte[] var1, int var2, int var3);
}

