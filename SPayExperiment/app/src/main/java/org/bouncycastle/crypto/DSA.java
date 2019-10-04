/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto;

import java.math.BigInteger;
import org.bouncycastle.crypto.CipherParameters;

public interface DSA {
    public BigInteger[] generateSignature(byte[] var1);

    public void init(boolean var1, CipherParameters var2);

    public boolean verifySignature(byte[] var1, BigInteger var2, BigInteger var3);
}

