/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.Digest;

public interface DigestDerivationFunction
extends DerivationFunction {
    public Digest getDigest();
}

