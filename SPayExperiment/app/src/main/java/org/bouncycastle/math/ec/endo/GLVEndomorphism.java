/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec.endo;

import java.math.BigInteger;
import org.bouncycastle.math.ec.endo.ECEndomorphism;

public interface GLVEndomorphism
extends ECEndomorphism {
    public BigInteger[] decomposeScalar(BigInteger var1);
}

