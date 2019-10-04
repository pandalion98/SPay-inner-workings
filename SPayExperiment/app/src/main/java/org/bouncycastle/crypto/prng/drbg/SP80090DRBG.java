/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.prng.drbg;

public interface SP80090DRBG {
    public int generate(byte[] var1, byte[] var2, boolean var3);

    public int getBlockSize();

    public void reseed(byte[] var1);
}

