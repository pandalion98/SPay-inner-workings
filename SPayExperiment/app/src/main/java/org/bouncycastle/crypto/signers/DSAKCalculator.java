/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 *  java.security.SecureRandom
 */
package org.bouncycastle.crypto.signers;

import java.math.BigInteger;
import java.security.SecureRandom;

public interface DSAKCalculator {
    public void init(BigInteger var1, BigInteger var2, byte[] var3);

    public void init(BigInteger var1, SecureRandom var2);

    public boolean isDeterministic();

    public BigInteger nextK();
}

