/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.CipherParameters;

public interface Wrapper {
    public String getAlgorithmName();

    public void init(boolean var1, CipherParameters var2);

    public byte[] unwrap(byte[] var1, int var2, int var3);

    public byte[] wrap(byte[] var1, int var2, int var3);
}

