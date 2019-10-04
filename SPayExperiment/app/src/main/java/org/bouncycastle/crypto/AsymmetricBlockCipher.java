/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.CipherParameters;

public interface AsymmetricBlockCipher {
    public int getInputBlockSize();

    public int getOutputBlockSize();

    public void init(boolean var1, CipherParameters var2);

    public byte[] processBlock(byte[] var1, int var2, int var3);
}

