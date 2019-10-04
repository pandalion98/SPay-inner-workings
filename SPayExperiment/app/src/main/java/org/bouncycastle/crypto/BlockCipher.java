/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.CipherParameters;

public interface BlockCipher {
    public String getAlgorithmName();

    public int getBlockSize();

    public void init(boolean var1, CipherParameters var2);

    public int processBlock(byte[] var1, int var2, byte[] var3, int var4);

    public void reset();
}

