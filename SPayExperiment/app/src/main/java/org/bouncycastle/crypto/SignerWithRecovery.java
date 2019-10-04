/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.Signer;

public interface SignerWithRecovery
extends Signer {
    public byte[] getRecoveredMessage();

    public boolean hasFullMessage();

    public void updateWithRecoveredMessage(byte[] var1);
}

