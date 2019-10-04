/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.Commitment;

public interface Committer {
    public Commitment commit(byte[] var1);

    public boolean isRevealed(Commitment var1, byte[] var2);
}

