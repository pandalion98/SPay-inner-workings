/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;

public interface AEADBlockCipher {
    public int doFinal(byte[] var1, int var2);

    public String getAlgorithmName();

    public byte[] getMac();

    public int getOutputSize(int var1);

    public BlockCipher getUnderlyingCipher();

    public int getUpdateOutputSize(int var1);

    public void init(boolean var1, CipherParameters var2);

    public void processAADByte(byte var1);

    public void processAADBytes(byte[] var1, int var2, int var3);

    public int processByte(byte var1, byte[] var2, int var3);

    public int processBytes(byte[] var1, int var2, int var3, byte[] var4, int var5);

    public void reset();
}

