/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.DerivationParameters;

public interface DerivationFunction {
    public int generateBytes(byte[] var1, int var2, int var3);

    public void init(DerivationParameters var1);
}

