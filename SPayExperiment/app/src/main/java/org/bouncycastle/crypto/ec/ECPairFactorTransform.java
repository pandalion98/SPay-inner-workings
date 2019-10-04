/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.crypto.ec;

import java.math.BigInteger;
import org.bouncycastle.crypto.ec.ECPairTransform;

public interface ECPairFactorTransform
extends ECPairTransform {
    public BigInteger getTransformValue();
}

