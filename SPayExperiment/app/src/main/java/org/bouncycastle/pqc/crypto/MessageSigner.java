/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.pqc.crypto;

import org.bouncycastle.crypto.CipherParameters;

public interface MessageSigner {
    public byte[] generateSignature(byte[] var1);

    public void init(boolean var1, CipherParameters var2);

    public boolean verifySignature(byte[] var1, byte[] var2);
}

