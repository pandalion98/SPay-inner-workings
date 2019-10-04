/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECPoint;

public interface ECMultiplier {
    public ECPoint multiply(ECPoint var1, BigInteger var2);
}

