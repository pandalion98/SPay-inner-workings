/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.CipherParameters;

public interface StreamCipher {
    public String getAlgorithmName();

    public void init(boolean var1, CipherParameters var2);

    public int processBytes(byte[] var1, int var2, int var3, byte[] var4, int var5);

    public void reset();

    public byte returnByte(byte var1);
}

