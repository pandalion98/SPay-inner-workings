/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.jcajce.provider.asymmetric.util;

import java.math.BigInteger;

public interface DSAEncoder {
    public BigInteger[] decode(byte[] var1);

    public byte[] encode(BigInteger var1, BigInteger var2);
}

