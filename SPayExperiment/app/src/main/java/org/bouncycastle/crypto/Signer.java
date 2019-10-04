/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.CipherParameters;

public interface Signer {
    public byte[] generateSignature();

    public void init(boolean var1, CipherParameters var2);

    public void reset();

    public void update(byte var1);

    public void update(byte[] var1, int var2, int var3);

    public boolean verifySignature(byte[] var1);
}

