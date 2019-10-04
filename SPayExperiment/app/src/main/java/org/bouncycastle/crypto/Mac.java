/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.CipherParameters;

public interface Mac {
    public int doFinal(byte[] var1, int var2);

    public String getAlgorithmName();

    public int getMacSize();

    public void init(CipherParameters var1);

    public void reset();

    public void update(byte var1);

    public void update(byte[] var1, int var2, int var3);
}

