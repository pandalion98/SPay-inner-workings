/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.CipherParameters;

public interface KeyEncapsulation {
    public CipherParameters decrypt(byte[] var1, int var2, int var3, int var4);

    public CipherParameters encrypt(byte[] var1, int var2, int var3);

    public void init(CipherParameters var1);
}

